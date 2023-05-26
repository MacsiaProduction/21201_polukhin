package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.comparators.DefaultComparator;
import ru.nsu.fit.m_polukhin.exceptions.*;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

public class Main {
    public static void main(String[] args) {
        try {
            JduOptions jduOptions = Parser.getOptions(args);
            if (jduOptions == null) {
                Parser.printHelp();
                return;
            }
            var classes = ClassLoader.loadFactoryClasses(Main.class.getResourceAsStream("/factory.config"));
            TreeFactory treeFactory = new TreeFactory(classes);
            DuFileType root = treeFactory.buildTree(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(System.out, new DefaultComparator(), jduOptions);
            printer.print(root);
        } catch (DuParseException | PathFactoryException | ClassLoadException | FileMissingException e) {
            System.exit(0);
        }
    }
}