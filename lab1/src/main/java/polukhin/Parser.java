package polukhin;
import org.apache.commons.cli.*;
import polukhin.exceptions.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class Parser {
    // Set default values
    private static final int depth = 10;

    private static final int limit = 10;

    private static final boolean followSymLinks = false;

    private static final Path baseDirectory = Path.of(".");

    private static final Options DU_OPTIONS;

    static {
        DU_OPTIONS = new Options();
        DU_OPTIONS.addOption("depth", true, "maximum depth to search");
        DU_OPTIONS.addOption("L", false, "follow symbolic links");
        DU_OPTIONS.addOption("limit", true, "maximum number of files to display");
        DU_OPTIONS.addOption("h", "help", false, "print help message");
    }

    public static JduOptions getOptions(String[] args) throws DuParseException {
        int depth = Parser.depth;
        int limit = Parser.limit;
        boolean followSymLinks = Parser.followSymLinks;
        Path baseDirectory = Parser.baseDirectory;
        // Parse command line arguments
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(DU_OPTIONS, args);

            String[] remainingArgs = cmd.getArgs();

            if (remainingArgs.length > 1) {
                throw new DuParseException("Invalid number of arguments");
            } else if (remainingArgs.length == 1) {
                baseDirectory = parsePath(remainingArgs[0]);
            }

            if (cmd.hasOption("h")) {
                return null;
            }

            if (cmd.hasOption("depth")) {
                depth = parseUnsignedInt(cmd.getOptionValue("depth"));
            }

            if (cmd.hasOption("L")) {
                followSymLinks = true;
            }

            if (cmd.hasOption("limit")) {
                limit = parseUnsignedInt(cmd.getOptionValue("limit"));
            }
        } catch (Exception e) {
            throw new DuParseException("Can't parse input");
        }
        return new JduOptions(baseDirectory,depth,followSymLinks,limit);
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java Parser [options] <directory>", Parser.DU_OPTIONS);
    }

    private static int parseUnsignedInt(String arg) throws DuParseException {
        int val = Integer.parseInt(arg);
        if(val <= 0) throw new DuParseException("invalid argument value");
        return val;
    }

    private static Path parsePath(String arg) throws DuParseException {
        Path path = Path.of(arg);
        if(Files.notExists(path)) {
            throw new DuParseException("Invalid path provided, file: " + path + " don't exist");
        }
        return path;
    }
}