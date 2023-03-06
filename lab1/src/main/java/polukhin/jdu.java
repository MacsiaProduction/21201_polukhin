package polukhin;

import polukhin.Comparators.*;
import polukhin.Types.myDir;

public class jdu {
    public static void main(String[] args) {
        Options options = Parser.getOptions(args);
        myDir dir = new myDir(options.base_dir(), options, 0);
        dir.print(new InverseComparator());
    }
}