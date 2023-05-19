package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.comparators.*;
import ru.nsu.fit.m_polukhin.exceptions.*;
import ru.nsu.fit.m_polukhin.modules.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            JduOptions jduOptions = Parser.getOptions(args);
            if (jduOptions == null) {
                Parser.printHelp();
                return;
            }
            List<Class<? extends MetaType<? extends DuFileType>>> classes;
            classes = ClassLoader.loadFactoryClasses(Main.class.getResourceAsStream("/factory.config"));
            TreeFactory treeFactory = new TreeFactory(classes);
            DuFileType root = treeFactory.buildTree(jduOptions.rootPath(), jduOptions);
            Printer printer = new Printer(new DefaultComparator(), jduOptions);
            printer.print(root);
        } catch (DuParseException | PathFactoryException | ClassLoadException | FileMissingException e) {
            System.exit(0);
        }
    }
}