import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.toolspecific.Process;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.ToolSpecific;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.Net;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.node.transition.Transition;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.toolspecific.process.Role;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PrivacyModuleFactory {
    public static List<IPrivacyModule> loadModules(
        PlaceTransitionNet net,
        BPMSpecification specification,
        String privacySpecPath
    ) throws Exception {
        // maintaining a privacy modules list
        List<IPrivacyModule> modules = new ArrayList<>();

        // unmarshalling the privacy specification file
        JAXBContext context = JAXBContext.newInstance(BPMNPrivacySpec.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        BPMNPrivacySpec privacySpec = (BPMNPrivacySpec) unmarshaller.unmarshal(
            new File(privacySpecPath)
        );

        // adding the Separation Of Duty module to the list
        if (privacySpec.getSeparationOfDuty() != null) {
            SoDModule sodModule = new SoDModule();
            sodModule.setSeparationOfDuty(privacySpec.getSeparationOfDuty());
            sodModule.setRoleMappings(privacySpec.getRoleMappings());
            modules.add(sodModule);
        }

        // adding the Binding Of Duty module to the list
        if (privacySpec.getBindingOfDuty() != null) {
            BoDModule bodModule = new BoDModule();
            bodModule.setBindingOfDuty(privacySpec.getBindingOfDuty());
            bodModule.setRoleMappings(privacySpec.getRoleMappings());
            modules.add(bodModule);
        }

        // adding the Division Of Knowledge module to the list
        if (privacySpec.getDivisionOfKnowledge() != null) {
            DoKModule dokModule = new DoKModule();
            dokModule.setDivisionOfKnowledge(privacySpec.getDivisionOfKnowledge());
            dokModule.setRoleMappings(privacySpec.getRoleMappings());
            modules.add(dokModule);
        }

        return modules;
    }
}
