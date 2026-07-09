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

public class SoDModule extends AbstractPrivacyModule {
    private SeparationOfDuty separationOfDuty;
    private List<RoleMapping> roleMappings;  // Role to Transition mapping from privacy spec
    private List<String> violationDetectors = new ArrayList<>();

    public void setSeparationOfDuty(SeparationOfDuty separationOfDuty) {
        this.separationOfDuty = separationOfDuty;
    }

    public void setRoleMappings(List<RoleMapping> roleMappings) {
        this.roleMappings = roleMappings;
    }

    @Override
    public void augmentSpecification(BPMSpecification spec) {
        System.out.println("\n==== AUGMENTING SoD SPECIFICATION ====");

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
            specification.setType("SoDReachability");

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

            boolean hasSoDReachability = spec.getSpecificationTypes().stream()
                    .anyMatch(t -> t.getId().equals("SoDReachability"));

            if (!hasSoDReachability) {
                SpecificationType specType = new SpecificationType();
                specType.setId("SoDReachability");

                Input input = new Input();
                input.setType("and");
                input.setValue("p");
                specType.getInputs().add(input);

                Formula formula = new Formula();
                formula.setLanguage("CTLSPEC");
                formula.setFormula("AG !p");
                specType.getFormulas().add(formula);

                Message message = new Message();
                message.setHold("SOD Violation is unreachable");
                message.setFail("SOD Violation is reachable");
                specType.setMessage(message);

                spec.addSpecificationType(specType);
            }

            System.out.println("  Added AG !p for " + detector);
        }
    }

    @Override
    public void augmentPNML(PlaceTransitionNet net) throws MalformedNetException {
        printStatements();

        if (separationOfDuty == null || separationOfDuty.getConstraints() == null) {
            return;
        }

        for (Constraint constraint : separationOfDuty.getConstraints()) {
            // Get roles from mapping instead of participants
            Set<String> affectedRoles = findRolesWithDuties(constraint.getConflictingDuties());

            if (affectedRoles.isEmpty()) {
                createViolationDetector(net, constraint, "ANY");
            } else {
                for (String roleId : affectedRoles) {
                    createViolationDetector(net, constraint, roleId);
                }
            }
        }
    }

    private Set<String> findRolesWithDuties(List<String> dutyIds) {
        Set<String> affectedRoles = new HashSet<>();

        if (roleMappings == null || roleMappings.isEmpty()) {
            return affectedRoles;
        }

        // Get all transitions that create these duties
        Set<String> relevantTransitions = new HashSet<>();
        for (String dutyId : dutyIds) {
            Duty duty = findDutyById(dutyId);
            if (duty != null && duty.getCreatedBy() != null) {
                relevantTransitions.addAll(duty.getCreatedBy());
            }
        }

        // Find roles that execute ANY of these transitions
        for (RoleMapping mapping : roleMappings) {
            if (mapping.getTransition() != null) {
                for (String transition : mapping.getTransition()) {
                    if (relevantTransitions.contains(transition)) {
                        affectedRoles.add(mapping.getId());
                        break;
                    }
                }
            }
        }

        return affectedRoles;
    }

    private void createViolationDetector(PlaceTransitionNet net, Constraint constraint, String roleId)
            throws MalformedNetException {
        Map<String, Place> dutyPlaces = new HashMap<>();
        Set<String> roleTransitions = getTransitionsForRole(roleId);

        for (String dutyId : constraint.getConflictingDuties()) {
            Duty duty = findDutyById(dutyId);
            if (duty == null) continue;

            boolean roleExecutesDuty = false;
            for (String transitionId : duty.getCreatedBy()) {
                if (roleTransitions.contains(transitionId)) {
                    roleExecutesDuty = true;
                    break;
                }
            }

            if (!roleExecutesDuty) continue;

            String placeName = "P_SOD_DUTY_" + constraint.getId() + "_" + roleId + "_" + dutyId;
            Place dutyPlace = new Place(placeName);
            net.addPlace(dutyPlace);
            dutyPlaces.put(dutyId, dutyPlace);

            for (String transitionId : duty.getCreatedBy()) {
                if (roleTransitions.contains(transitionId)) {
                    Transition transition = net.getTransition(transitionId);
                    if (transition != null) {
                        net.addArc(transition, dutyPlace);
                        System.out.println("  Connected: " + transitionId + " -> " + placeName + " (role: " + roleId + ")");
                    }
                }
            }
        }

        if (dutyPlaces.size() >= 2) {
            String detectorName = "T_SOD_VIOLATION_" + constraint.getId() + "_" + roleId;
            Transition violationDetector = new Transition(detectorName);
            net.addTransition(violationDetector);
            violationDetectors.add(detectorName);

            // Connect all duty places as inputs
            for (Place dutyPlace : dutyPlaces.values()) {
                net.addArc(dutyPlace, violationDetector);
            }

            System.out.println("  Created violation detector for role: " + roleId);
            System.out.println("    Check: AG !" + detectorName);
        }
    }
    private Set<String> getTransitionsForRole(String roleId) {
        Set<String> transitions = new HashSet<>();
        if (roleMappings == null) return transitions;

        for (RoleMapping mapping : roleMappings) {
            if (mapping.getId().equals(roleId) && mapping.getTransition() != null) {
                transitions.addAll(mapping.getTransition());
                break;
            }
        }
        return transitions;
    }

    private Duty findDutyById(String dutyId) {
        if (separationOfDuty == null || separationOfDuty.getDuties() == null) {
            return null;
        }
        for (Duty duty : separationOfDuty.getDuties()) {
            if (duty.getId().equals(dutyId)) {
                return duty;
            }
        }
        return null;
    }

    private void printStatements() {
        System.out.println("\n==== SEPARATION OF DUTY MODULE ====");

        if (separationOfDuty == null) {
            System.out.println("No SoD constraints found.");
            return;
        }

        if (separationOfDuty.getDuties() != null) {
            System.out.println("Duties:");
            for (Duty duty : separationOfDuty.getDuties()) {
                System.out.println("  Duty: " + duty.getId());
                System.out.println("    Created by transitions:");
                for (String transition : duty.getCreatedBy()) {
                    System.out.println("      - " + transition);
                }
            }
        }

        if (separationOfDuty.getConstraints() != null) {
            System.out.println("Constraints:");
            for (Constraint constraint : separationOfDuty.getConstraints()) {
                System.out.println("  Constraint: " + constraint.getId());
                System.out.println("    Conflicting duties:");
                for (String duty : constraint.getConflictingDuties()) {
                    System.out.println("      - " + duty);
                }
            }
        }

        if (roleMappings != null && !roleMappings.isEmpty()) {
            System.out.println("\nRole to Transition Mapping:");
            for (RoleMapping mapping : roleMappings) {
                System.out.println("  Role: " + mapping.getId());
                System.out.println("    Transitions:");
                for (String transition : mapping.getTransition()) {
                    System.out.println("      - " + transition);
                }
            }
        } else {
            System.out.println("\nNo role mappings defined - SoD will apply globally (ANY participant)");
        }
    }
}
