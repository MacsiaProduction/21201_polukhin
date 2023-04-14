package polukhin;
import org.apache.commons.cli.*;
import polukhin.exceptions.*;

import java.nio.file.Path;

public class Parser {
    // Set default values
    private static final int DEPTH = 10;
    // CR: naming
    private static final int limit = 10;
    private static final boolean followSymLinks = false;
    private static final Path base_directory = Path.of(".");

    private static final Options DU_OPTIONS;

    static {
        DU_OPTIONS = new Options();
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
    public static JduOptions getOptions(String[] args) throws DuParseException {
        int depth = Parser.DEPTH;
        int limit = Parser.limit;
        boolean followSymLinks = Parser.followSymLinks;
        // CR: naming
        Path baseDirectory = Parser.base_directory;
        // Parse command line arguments
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(DU_OPTIONS, args);

            if (cmd.hasOption("h")) {
                printHelp();
                // CR: return null
                //throw new DuPrintHelpException("Requested to print help");
            }

            if (cmd.hasOption("depth")) {
                // CR: parseUnsignedInt(String)
                depth = Integer.parseInt(cmd.getOptionValue("depth"));
            }

            if (cmd.hasOption("L")) {
                followSymLinks = true;
            }

            if (cmd.hasOption("limit")) {
                limit = Integer.parseInt(cmd.getOptionValue("limit"));
            }

            // CR: move before if's
            String[] remainingArgs = cmd.getArgs();
            if (remainingArgs.length > 1) {
                throw new ParseException("Invalid number of arguments");
            } else if (remainingArgs.length == 1) {
                baseDirectory = Path.of(remainingArgs[0]);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            // CR: catch custom exception in main, print help
            printHelp();
            throw new DuParseException("Can't parse input");
        }
        return new JduOptions(baseDirectory,depth,followSymLinks,limit);
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java Parser [options] <directory>", Parser.DU_OPTIONS);
    }
}