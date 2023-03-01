package polukhin;
import org.apache.commons.cli.*;

import java.nio.file.Path;

public class Parser {
    public static Options getOptions(String[] args) {
        // Set default values
        int depth = 10;
        boolean followSymLinks = false;
        int limit = 10;
        Path base_directory = Path.of(".");
        // Define command line options
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption("depth", true, "maximum depth to search");
        options.addOption("L", false, "follow symbolic links");
        options.addOption("limit", true, "maximum number of files to display");

        // Parse command line arguments
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("depth")) {
                depth = Integer.parseInt(cmd.getOptionValue("depth"));
            }

            if (cmd.hasOption("L")) {
                followSymLinks = true;
            }

            if (cmd.hasOption("limit")) {
                limit = Integer.parseInt(cmd.getOptionValue("limit"));
            }

            String[] remainingArgs = cmd.getArgs();
            if (remainingArgs.length != 1) {
                throw new ParseException("Invalid number of arguments");
            }
            base_directory = Path.of(remainingArgs[0]);
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            System.exit(1);
        }
        return new Options(base_directory,depth,followSymLinks,limit);
    }
}
