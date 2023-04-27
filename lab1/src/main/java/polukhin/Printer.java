package polukhin;

import polukhin.modules.DuCompoundFileType;
import polukhin.modules.DuFileType;

import java.util.Comparator;

public class Printer {

    private final int limit;

    private final Comparator<DuFileType> comparator;

    public Printer(JduOptions options, Comparator<DuFileType> comparator) {
        this.limit = options.limit();
        this.comparator = comparator;
    }

    public void print(DuFileType duFileType) {
        doPrint(duFileType, 0);
    }

    private void doPrint(DuFileType duFileType, int curDepth) {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.getCalculatedSize());
        System.out.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundFileType compoundType) {
            // CR: create in path factory, only until limit reached
            var children = compoundType.getChildren();
            children.sorted(comparator).forEachOrdered(child -> doPrint(child, curDepth + 1));
        }
    }
}