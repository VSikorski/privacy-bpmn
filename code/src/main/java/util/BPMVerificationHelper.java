import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import nl.rug.ds.bpm.CommandlineVerifier;
import nl.rug.ds.bpm.petrinet.interfaces.net.VerifiableNet;
import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.verification.checker.CheckerFactory;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for interacting with the BPMVerification package
 */
public class BPMVerificationHelper extends CommandlineVerifier {
    public BPMVerificationHelper() {super();}
    public BPMVerificationHelper(String[] args) {super(args);}

    @Override
    protected Options createOptions() {
        // adding the extra option for privacy-spec file location
        Options options = super.createOptions();
        Option privacySpecOption = new Option("ps", "privacy-spec", true, "privacy specification file path");
        privacySpecOption.setRequired(true);
        options.addOption(privacySpecOption);
        return options;
    }

    protected void initiateVerification(CLIFlags cliFlags, PlaceTransitionNet net, BPMSpecification spec) {
        // Set the log level
        setLogLevel(cliFlags.getLogLevel());

        // Initialize the formal checker
        CheckerFactory checkerFactory = loadModelChecker(
            cliFlags.getCheckerBinPath(),
            cliFlags.getOutputPath()
        );

        // Verify
        verify((VerifiableNet) net, spec, checkerFactory, cliFlags.getVerifierType());
    }
}
