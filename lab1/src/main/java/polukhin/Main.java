package polukhin;

import polukhin.comparators.*;
import polukhin.exceptions.ClassLoadException;
import polukhin.exceptions.DuParseException;
import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;
import polukhin.modules.Printer;

public class Main {
    public static void main(String[] args) {
        try {
            JduOptions jduOptions = Parser.getOptions(args);
            if (jduOptions == null) {
                Parser.printHelp();
                System.exit(0);
            }
            PathFactory.init(ConfigIterator.loadFactoryClasses());
            DuFileType tmp = PathFactory.create(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(jduOptions, new DefaultComparator());
            printer.print(tmp);
        } catch (DuParseException | PathFactoryException | ClassLoadException | FileMissingException e) {
            System.exit(0);
        }
    }
}