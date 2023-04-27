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
            List<Class<? extends MetaType<? extends DuFileType>>> classes;
            classes = ClassLoader.loadFactoryClasses(Main.class.getResourceAsStream("/factory.config"));
            PathFactory pathFactory = new PathFactory(classes);
            DuFileType root = pathFactory.buildTree(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(new DefaultComparator(), jduOptions);
            printer.print(root);
        } catch (DuParseException | PathFactoryException | ClassLoadException | FileMissingException e) {
            System.exit(0);
        }
    }
}