import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;
import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.petrinet.ptnet.element.Place;
import nl.rug.ds.bpm.petrinet.ptnet.element.Transition;
import nl.rug.ds.bpm.util.exception.MalformedNetException;
import nl.rug.ds.bpm.specification.jaxb.Group;
import nl.rug.ds.bpm.specification.jaxb.Element;
import nl.rug.ds.bpm.specification.jaxb.Specification;
import nl.rug.ds.bpm.specification.jaxb.InputElement;
import nl.rug.ds.bpm.specification.jaxb.Formula;
import nl.rug.ds.bpm.specification.jaxb.Input;
import nl.rug.ds.bpm.specification.jaxb.Message;
import nl.rug.ds.bpm.specification.jaxb.SpecificationSet;
import nl.rug.ds.bpm.specification.jaxb.SpecificationType;
import java.util.*;

public class DoKModule extends AbstractPrivacyModule {
    private DivisionOfKnowledge divisionOfKnowledge;
    private List<RoleMapping> roleMappings;
    private List<String> violationDetectors = new ArrayList<>();

    public void setDivisionOfKnowledge(DivisionOfKnowledge divisionOfKnowledge) {
        this.divisionOfKnowledge = divisionOfKnowledge;
    }
    public void setRoleMappings(List<RoleMapping> roleMappings) {
        this.roleMappings = roleMappings;
    }

    @Override
    public void augmentSpecification(BPMSpecification spec) {
        System.out.println("\n==== AUGMENTING DoK SPECIFICATION ====");

        if (violationDetectors.isEmpty()) {
            System.out.println("  No violation detectors to add.");
            return;
        }

        for (String detector : violationDetectors) {
            Group group = new Group();
            group.setId("group_" + detector);

            Element element = new Element();
            element.setId(detector);
            group.getElements().add(element);

            spec.addGroup(group);

            Specification specification = new Specification();
            specification.setId("check_" + detector);
            specification.setType("DoKReachability");

            InputElement inputElement = new InputElement();
            inputElement.setTarget("p");
            inputElement.setElement(group.getId());
            specification.getInputElements().add(inputElement);

            SpecificationSet specSet;
            if (spec.getSpecificationSets().isEmpty()) {
                specSet = new SpecificationSet();
                spec.addSpecificationSet(specSet);
            } else {
                specSet = spec.getSpecificationSets().get(0);
            }
            specSet.getSpecifications().add(specification);

            boolean hasDoKReachability = spec.getSpecificationTypes().stream()
                    .anyMatch(t -> t.getId().equals("DoKReachability"));

            if (!hasDoKReachability) {
                SpecificationType specType = new SpecificationType();
                specType.setId("DoKReachability");

                Input input = new Input();
                input.setType("and");
                input.setValue("p");
                specType.getInputs().add(input);

                Formula formula = new Formula();
                formula.setLanguage("CTLSPEC");
                formula.setFormula("AG !p");
                specType.getFormulas().add(formula);

                Message message = new Message();
                message.setHold("DoK Violation is unreachable");
                message.setFail("DoK Violation is reachable");
                specType.setMessage(message);

                spec.addSpecificationType(specType);
            }

            System.out.println("  Added AG !p for " + detector);
        }
    }

    @Override
    public void augmentPNML(PlaceTransitionNet net) throws MalformedNetException {
        printStatements();

        if (divisionOfKnowledge == null || divisionOfKnowledge.getConstraints() == null) {
            return;
        }

        for (DoKConstraint constraint : divisionOfKnowledge.getConstraints()) {
            String roleId = constraint.getRole();
            Set<String> roleTransitions = getTransitionsForRole(roleId);

            if (roleTransitions.isEmpty()) {
                System.out.println("  Warning: No transitions found for role: " + roleId);
                continue;
            }

            Set<String> criticalKnowledge = new HashSet<>(constraint.getCriticalSet());
            Set<String> roleAcquiredKnowledge = new HashSet<>();
            Map<String, Place> knowledgePlaces = new HashMap<>();

            for (String knowledgeId : criticalKnowledge) {
                Knowledge knowledge = findKnowledgeById(knowledgeId);
                if (knowledge == null) {
                    System.out.println("  Warning: Knowledge not found: " + knowledgeId);
                    continue;
                }

                boolean roleCanCreate = false;
                if (knowledge.getCreatedBy() != null) {
                    for (String transitionId : knowledge.getCreatedBy()) {
                        if (roleTransitions.contains(transitionId)) {
                            roleCanCreate = true;
                            break;
                        }
                    }
                }

                boolean roleRequires = false;
                if (knowledge.getRequiredBy() != null) {
                    for (String transitionId : knowledge.getRequiredBy()) {
                        if (roleTransitions.contains(transitionId)) {
                            roleRequires = true;
                            break;
                        }
                    }
                }

                if (!roleCanCreate && !roleRequires) {
                    System.out.println("  Role " + roleId + " neither creates or requires knowledge: " + knowledgeId);
                    continue;
                }

                String placeName = "P_KNOWLEDGE_" + constraint.getId() + "_" + roleId + "_" + knowledgeId;
                Place knowledgePlace = new Place(placeName);
                net.addPlace(knowledgePlace);
                knowledgePlaces.put(knowledgeId, knowledgePlace);
                roleAcquiredKnowledge.add(knowledgeId);

                if (knowledge.getCreatedBy() != null) {
                    for (String transitionId : knowledge.getCreatedBy()) {
                        if (roleTransitions.contains(transitionId)) {
                            Transition transition = net.getTransition(transitionId);
                            if (transition != null) {
                                net.addArc(transition, knowledgePlace);
                                System.out.println("  " + transitionId + " CREATES: " + placeName);
                            }
                        }
                    }
                }

                if (knowledge.getRequiredBy() != null) {
                    for (String transitionId : knowledge.getRequiredBy()) {
                        if (roleTransitions.contains(transitionId)) {
                            Transition transition = net.getTransition(transitionId);
                            if (transition != null) {
                                net.addArc(knowledgePlace, transition);
                                System.out.println("  " + transitionId + " REQUIRES: " + placeName);
                            }
                        }
                    }
                }
            }

            if (roleAcquiredKnowledge.containsAll(criticalKnowledge) && knowledgePlaces.size() >= 2) {
                String detectorName = "T_DOK_VIOLATION_" + constraint.getId() + "_" + roleId;
                Transition violationDetector = new Transition(detectorName);
                net.addTransition(violationDetector);
                violationDetectors.add(detectorName);

                for (Place knowledgePlace : knowledgePlaces.values()) {
                    net.addArc(knowledgePlace, violationDetector);
                }

                System.out.println("  Created DoK violation detector for role: " + roleId);
                System.out.println("    Check: AG !" + detectorName);
                System.out.println("    Critical set: " + criticalKnowledge);
            } else {
                System.out.println("  Role " + roleId + " cannot acquire all critical knowledge. Skipping detector.");
            }
        }
    }

    private Set<String> getTransitionsForRole(String roleId) {
        Set<String> transitions = new HashSet<>();
        if (roleMappings == null) return transitions;
        for (RoleMapping mapping : roleMappings) {
            if (mapping.getId().equals(roleId) && mapping.getTransition() != null) {
                transitions.addAll(mapping.getTransition());
            }
        }
        return transitions;
    }

    private Knowledge findKnowledgeById(String id) {
        if (divisionOfKnowledge == null || divisionOfKnowledge.getKnowledgeSet() == null) {
            return null;
        }
        if (divisionOfKnowledge.getKnowledgeSet().getKnowledge() == null) {
            return null;
        }
        for (Knowledge k : divisionOfKnowledge.getKnowledgeSet().getKnowledge()) {
            if (k.getId().equals(id)) {
                return k;
            }
        }
        return null;
    }

    private void printStatements() {
        System.out.println("\n==== DIVISION OF KNOWLEDGE MODULE ====");
        if (divisionOfKnowledge == null) {
            System.out.println("No DoK constraints found.");
            return;
        }

        if (divisionOfKnowledge.getKnowledgeSet() != null &&
                divisionOfKnowledge.getKnowledgeSet().getKnowledge() != null) {
            System.out.println("Knowledge Items:");
            for (Knowledge k : divisionOfKnowledge.getKnowledgeSet().getKnowledge()) {
                System.out.println("  Knowledge: " + k.getId());
                if (k.getCreatedBy() != null) {
                    System.out.println("    Created by: " + k.getCreatedBy());
                }
                if (k.getRequiredBy() != null) {
                    System.out.println("    Required by: " + k.getRequiredBy());
                }
            }
        }

        if (divisionOfKnowledge.getConstraints() != null) {
            System.out.println("Constraints:");
            for (DoKConstraint constraint : divisionOfKnowledge.getConstraints()) {
                System.out.println("  Constraint: " + constraint.getId());
                System.out.println("    Role: " + constraint.getRole());
                System.out.println("    Critical Set: " + constraint.getCriticalSet());
            }
        }

        if (roleMappings != null && !roleMappings.isEmpty()) {
            System.out.println("\nRole to Transition Mapping:");
            for (RoleMapping mapping : roleMappings) {
                System.out.println("  Role: " + mapping.getId());
                System.out.println("    Transitions: " + mapping.getTransition());
            }
        }
    }
}
