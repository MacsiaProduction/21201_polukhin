package polukhin;
import org.apache.commons.cli.*;

import java.nio.file.Path;

public class Parser {
    /**
     * Parses the command line arguments and returns an instance of the Options class.
     *
     * @param args The command line arguments.
     * @return An instance of the Options class encapsulating the parsed options.
     */

//    private static final org.apache.commons.cli.Options DU_OPTIONS;

//    static {
//        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
//        options.addOption("depth", true, "maximum depth to search");
//        options.addOption("L", false, "follow symbolic links");
//        options.addOption("limit", true, "maximum number of files to display");
//        options.addOption("h", "help", false, "print help message");
//    }

    public static Options getOptions(String[] args) {
        // Set default values
        // CR: make constants
        int depth = 10;
        boolean followSymLinks = false;
        int limit = 10;
        Path base_directory = Path.of(".");
        // Define command line options
        // CR: static field
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption("depth", true, "maximum depth to search");
        options.addOption("L", false, "follow symbolic links");
        options.addOption("limit", true, "maximum number of files to display");
        options.addOption("h", "help", false, "print help message");

        // Parse command line arguments
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                printHelp(options);
                System.exit(0);
            }

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
            // CR: validate that it is a directory
            base_directory = Path.of(remainingArgs[0]);
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            printHelp(options);
            // CR: move to main
            System.exit(1);
        }
        return new Options(base_directory,depth,followSymLinks,limit);
    }

    // CR: call from main
    private static void printHelp(org.apache.commons.cli.Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java Parser [options] <directory>", options);
    }
}