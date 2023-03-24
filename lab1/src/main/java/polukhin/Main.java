package polukhin;

import polukhin.comparators.*;
import polukhin.exceptions.DuParseException;
import polukhin.types.DuFileType;

public class Main {
    public static void main(String[] args) {
        try {
            JduOptions jduOptions = Parser.getOptions(args);
            // CR: check Files.exists(...)
            // CR: call overload without curDepth
            // CR: ConfigIterator, returns classes
            DuFileType tmp = PathFactory.create(jduOptions.rootPath(), jduOptions, 0);
            tmp.print(new InverseComparator());
        } catch (DuParseException e) {
            System.exit(0);
        }
    }
}