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

public class BoDModule extends AbstractPrivacyModule {
    private BindingOfDuty bindingOfDuty;
    private List<RoleMapping> roleMappings;
    private List<String> violationDetectors = new ArrayList<>();

    public void setBindingOfDuty(BindingOfDuty bindingOfDuty) {
        this.bindingOfDuty = bindingOfDuty;
    }

    public void setRoleMappings(List<RoleMapping> roleMappings) {
        this.roleMappings = roleMappings;
    }

    @Override
    public void augmentSpecification(BPMSpecification spec) {
        System.out.println("\n==== AUGMENTING BoD SPECIFICATION ====");

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
            specification.setType("BoDReachability");

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

            boolean hasBoDReachability = spec.getSpecificationTypes().stream()
                    .anyMatch(t -> t.getId().equals("BoDReachability"));

            if (!hasBoDReachability) {
                SpecificationType specType = new SpecificationType();
                specType.setId("BoDReachability");

                Input input = new Input();
                input.setType("and");
                input.setValue("p");
                specType.getInputs().add(input);

                Formula formula = new Formula();
                formula.setLanguage("CTLSPEC");
                formula.setFormula("EF p");
                specType.getFormulas().add(formula);

                Message message = new Message();
                message.setHold("BoD Detector Transition is reachable (duties are bound)");
                message.setFail("BoD VDetector Transition is not unreachable (duties are not bound)");
                specType.setMessage(message);

                spec.addSpecificationType(specType);
            }

            System.out.println("  Added EF p for " + detector);
        }
    }

    @Override
    public void augmentPNML(PlaceTransitionNet net) throws MalformedNetException {
        printStatements();

        if (bindingOfDuty == null || bindingOfDuty.getConstraints() == null) {
            return;
        }

        for (BoDConstraint constraint : bindingOfDuty.getConstraints()) {
            Set<String> affectedRoles = findRolesWithDuties(constraint.getBoundDuties());

            if (affectedRoles.isEmpty()) {
                createViolationDetector(net, constraint, "ANY");
            } else {
                for (String roleId : affectedRoles) {
                    createViolationDetector(net, constraint, roleId);
                }
            }
        }
    }

    private void createViolationDetector(PlaceTransitionNet net, BoDConstraint constraint, String roleId)
            throws MalformedNetException {
        Map<String, Place> dutyPlaces = new HashMap<>();
        Set<String> roleTransitions = getTransitionsForRole(roleId);

        for (String dutyId : constraint.getBoundDuties()) {
            BoDDuty duty = findDutyById(dutyId);
            if (duty == null) continue;

            boolean roleExecutesDuty = false;
            for (String transitionId : duty.getCreatedBy()) {
                if (roleTransitions.contains(transitionId)) {
                    roleExecutesDuty = true;
                    break;
                }
            }

            if (!roleExecutesDuty) continue;

            String placeName = "P_BOD_DUTY_" + constraint.getId() + "_" + roleId + "_" + dutyId;
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
            // Create the violation detectors
            String detectorName = "T_BOD_VIOLATION_" + constraint.getId() + "_" + roleId;
            Transition violationDetector = new Transition(detectorName);
            net.addTransition(violationDetector);
            violationDetectors.add(detectorName);

            // Connect all duty places as inputs
            for (Place dutyPlace : dutyPlaces.values()) {
                net.addArc(dutyPlace, violationDetector);
            }

            System.out.println("  Created BoD violation detector for role: " + roleId);
            System.out.println("    Check: EF " + detectorName + " (all duties can happen together)");
        }
    }

    private Set<String> findRolesWithDuties(List<String> dutyIds) {
        Set<String> affectedRoles = new HashSet<>();

        if (roleMappings == null || roleMappings.isEmpty()) {
            return affectedRoles;
        }

        Set<String> relevantTransitions = new HashSet<>();
        for (String dutyId : dutyIds) {
            BoDDuty duty = findDutyById(dutyId);
            if (duty != null && duty.getCreatedBy() != null) {
                relevantTransitions.addAll(duty.getCreatedBy());
            }
        }

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

    private BoDDuty findDutyById(String dutyId) {
        if (bindingOfDuty == null || bindingOfDuty.getDuties() == null) {
            return null;
        }
        for (BoDDuty duty : bindingOfDuty.getDuties()) {
            if (duty.getId().equals(dutyId)) {
                return duty;
            }
        }
        return null;
    }

    private void printStatements() {
        System.out.println("\n==== BINDING OF DUTY MODULE ====");

        if (bindingOfDuty == null) {
            System.out.println("No BoD constraints found.");
            return;
        }

        if (bindingOfDuty.getDuties() != null) {
            System.out.println("Duties:");
            for (BoDDuty duty : bindingOfDuty.getDuties()) {
                System.out.println("  Duty: " + duty.getId());
                System.out.println("    Created by transitions:");
                for (String transition : duty.getCreatedBy()) {
                    System.out.println("      - " + transition);
                }
            }
        }

        if (bindingOfDuty.getConstraints() != null) {
            System.out.println("Constraints:");
            for (BoDConstraint constraint : bindingOfDuty.getConstraints()) {
                System.out.println("  Constraint: " + constraint.getId());
                System.out.println("    Duties that should be bound:");
                for (String duty : constraint.getBoundDuties()) {
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
            System.out.println("\nNo role mappings defined - BoD will apply globally (ANY participant)");
        }
    }
}
