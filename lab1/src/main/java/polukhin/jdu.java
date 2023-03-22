package polukhin;

import polukhin.comparators.*;
import polukhin.exceptions.DuParseException;
import polukhin.types.DuFileType;

public class jdu {
    public static void main(String[] args) {
        try {
            Options options = Parser.getOptions(args);
            DuFileType tmp = PathFactory.create(options.base_dir(), options, 0);
            tmp.print(new InverseComparator());
        } catch (DuParseException e) {
            System.exit(0);
        }
    }
}