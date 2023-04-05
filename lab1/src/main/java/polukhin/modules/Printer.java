package polukhin.modules;

import polukhin.JduOptions;
import polukhin.exceptions.PathFactoryException;

import java.util.Comparator;
import java.util.List;

public class Printer {
    private int curDepth = 0;
    private final int limit;
    private final Comparator<DuFileType> comparator;
    public Printer(JduOptions options, Comparator<DuFileType> comparator) {
        this.limit = options.limit();
        this.comparator = comparator;
    }

    public void print(DuFileType duFileType) throws PathFactoryException {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.calculateSize());
        System.out.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundType compoundType) {
            curDepth++;
            List<DuFileType> children = compoundType.getChildren();
            children.sort(comparator);
            var sublist = children.subList(0,limit);
            for (DuFileType child : sublist) {
                print(child);
            }
            curDepth--;
        }
    }
}