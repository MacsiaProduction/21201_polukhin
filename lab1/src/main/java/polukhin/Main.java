package polukhin;

import polukhin.comparators.*;
import polukhin.exceptions.DuParseException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;
import polukhin.modules.Printer;

public class Main {
    public static void main(String[] args) {
        try {
            JduOptions jduOptions = Parser.getOptions(args);
            // CR: check Files.exists(...)
            // CR: ConfigIterator, returns classes
            DuFileType tmp = PathFactory.create(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(jduOptions, new DefaultComparator());
            printer.print(tmp);
        } catch (DuParseException | PathFactoryException e) {
            System.exit(0);
        }
    }
}