import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import nl.rug.ds.bpm.util.log.LogEvent;
import nl.rug.ds.bpm.util.log.Logger;


/**
 * Class for interacting with the CLI options
 */
class CLIFlags {
    private final String pnmlFilePath;
    private final String specFilePath;
    private final String checkerBinPath;
    private final String netType;
    private final String verifierType;
    private final String outputPath;
    private final String logLevel;
    private final String privacySpecFilePath;

    public CLIFlags(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            pnmlFilePath = cmd.getOptionValue("p");
            specFilePath = cmd.getOptionValue("s");
            checkerBinPath = cmd.getOptionValue("c");
            netType = cmd.getOptionValue("n");
            verifierType = cmd.getOptionValue("v");
            outputPath = cmd.getOptionValue("o");
            logLevel = cmd.getOptionValue("l");
            privacySpecFilePath = cmd.getOptionValue("ps");
        } catch (ParseException e) {
            Logger.log(e.getMessage(), LogEvent.ERROR);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("BPMPrivacy", options);
            throw new RuntimeException(e);
        }
    }

    public String getPrivacySpecFilePath() {
        return privacySpecFilePath;
    }
    public String getPnmlFilePath() {
        return pnmlFilePath;
    }
    public String getNetType() {
        return netType;
    }
    public String getCheckerBinPath() {
        return checkerBinPath;
    }
    public String getOutputPath() {
        return outputPath;
    }
    public String getSpecFilePath() {
        return specFilePath;
    }
    public String getLogLevel() {
        return logLevel;
    }
    public String getVerifierType() {
        return verifierType;
    }
}
