package polukhin;

import polukhin.comparators.*;
import polukhin.exceptions.*;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // CR: tests
            JduOptions jduOptions = Parser.getOptions(args);
            if (jduOptions == null) {
                Parser.printHelp();
                return;
            }
            List<Class<? extends MetaType<? extends DuFileType>>> classes = ConfigIterator.loadFactoryClasses();
            PathFactory pathFactory = new PathFactory(classes);
            DuFileType tmp = pathFactory.create(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(jduOptions, new DefaultComparator());
            printer.print(tmp);
        } catch (DuParseException | PathFactoryException | ClassLoadException | FileMissingUncheckedException |
                 PathFactoryUncheckedException e) {
            System.exit(0);
        }
    }
}