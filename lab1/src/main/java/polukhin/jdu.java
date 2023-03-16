package polukhin;

import polukhin.Comparators.*;
import polukhin.Exceptions.DuParseException;
import polukhin.Types.DuFile;

public class jdu {
    public static void main(String[] args) {
        try {
            Options options = Parser.getOptions(args);
            DuFile tmp = PathFactory.create(options.base_dir(), options, 0);
            tmp.print(new InverseComparator());
        } catch (DuParseException e) {
            System.exit(0);
        }
    }
}