package polukhin;
import org.apache.commons.cli.*;
import polukhin.Exceptions.*;

import java.nio.file.Path;

public class Parser {
    // Set default values
    private static final int depth = 10;
    private static final int limit = 10;
    private static final boolean followSymLinks = false;
    private static final Path base_directory = Path.of(".");
    private static final org.apache.commons.cli.Options DU_OPTIONS;
    static {
        DU_OPTIONS = new org.apache.commons.cli.Options();
        DU_OPTIONS.addOption("depth", true, "maximum depth to search");
        DU_OPTIONS.addOption("L", false, "follow symbolic links");
        DU_OPTIONS.addOption("limit", true, "maximum number of files to display");
        DU_OPTIONS.addOption("h", "help", false, "print help message");
    }
    /**
     * Parses the command line arguments and returns an instance of the Options class.
     *
     * @param args The command line arguments.
     * @return An instance of the Options class encapsulating the parsed options.
     */
    public static Options getOptions(String[] args) throws DuParseException {
        int depth = Parser.depth;
        int limit = Parser.limit;
        boolean followSymLinks = Parser.followSymLinks;
        Path base_directory = Parser.base_directory;
        // Parse command line arguments
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(DU_OPTIONS, args);

            if (cmd.hasOption("h")) {
                printHelp();
                throw new DuPrintHelpException("Requested to print help");
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
            if (remainingArgs.length > 1) {
                throw new ParseException("Invalid number of arguments");
            } else if (remainingArgs.length == 1) {
                base_directory = Path.of(remainingArgs[0]);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            printHelp();
            throw new DuParseException("Can't parse input");
        }
        return new Options(base_directory,depth,followSymLinks,limit);
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java Parser [options] <directory>", Parser.DU_OPTIONS);
    }
}