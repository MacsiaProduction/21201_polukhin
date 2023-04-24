package polukhin.modules;

import polukhin.JduOptions;
import polukhin.comparators.DuFileTypeComparator;
import polukhin.exceptions.*;

import java.util.List;

public class Printer {
    private int curDepth = 0;
    private final int limit;
    private final DuFileTypeComparator comparator;
    public Printer(JduOptions options, DuFileTypeComparator comparator) {
        this.limit = options.limit();
        this.comparator = comparator;
    }

    public void print(DuFileType duFileType) throws PathFactoryException, FileMissingException {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.calculateSize());
        System.out.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundType compoundType) {
            curDepth++;
            List<DuFileType> children = compoundType.getChildren();
            //children.sort(comparator);
            sort(children,comparator);
            var neededLen = Integer.min(limit, children.size()-1);
            var sublist = children.subList(0,neededLen);
            for (DuFileType child : sublist) {
                print(child);
            }
            curDepth--;
        }
    }

    private void sort(List<DuFileType> list, DuFileTypeComparator comparator) throws PathFactoryException, FileMissingException {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    DuFileType temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}