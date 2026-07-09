import nl.rug.ds.bpm.petrinet.interfaces.net.VerifiableNet;
import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;

import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.Net;
import nl.rug.ds.bpm.pnml.ptnet.marshaller.PTNetUnmarshaller;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;
import nl.rug.ds.bpm.verification.checker.CheckerFactory;
import nl.rug.ds.bpm.specification.marshaller.SpecificationMarshaller;

import org.apache.commons.cli.Options;

import java.io.File;
import java.util.Set;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        // initializing BPMVerification helper and CLI options
        BPMVerificationHelper helper = new BPMVerificationHelper();
        Options options = helper.createOptions();
        CLIFlags cliFlags = new CLIFlags(options, args);

        // loading the petri net
        VerifiableNet loadedNet = helper.loadNet(
            cliFlags.getPnmlFilePath(),
            cliFlags.getNetType()
        );

        // loading the specification file
        BPMSpecification bpmSpecification = helper.loadSpecification(cliFlags.getSpecFilePath());

        // casting the net to a more modifiable one
        PlaceTransitionNet net = (PlaceTransitionNet) loadedNet;

        // initializing the privacy modules
        List<IPrivacyModule> modules = PrivacyModuleFactory.loadModules(
            net,
            bpmSpecification,
            cliFlags.getPrivacySpecFilePath()
        );

        // applying the privacy modules
        for (IPrivacyModule module : modules) {
            module.apply(net, bpmSpecification);
        }

        // outputting the augmented PNML file
        new AugmentedPTNetMarshaller(net, new File("augmented.pnml"));

        // outputting the augmented spec file
        new SpecificationMarshaller(bpmSpecification, new File("augmented.spec"));

        // verify
        helper.initiateVerification(cliFlags, net, bpmSpecification);
    }
}
