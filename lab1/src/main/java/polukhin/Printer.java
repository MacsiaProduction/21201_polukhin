package polukhin;

import polukhin.modules.DuCompoundFileType;
import polukhin.modules.DuFileType;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Printer {

    private final Comparator<DuFileType> comparator;
    private final JduOptions options;

    public Printer(Comparator<DuFileType> comparator, JduOptions options) {
        this.comparator = comparator;
        this.options = options;
    }

    public void print(DuFileType duFileType) {
        doPrint(duFileType, 0);
    }

    private void doPrint(DuFileType duFileType, int curDepth) {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.getCalculatedSize());
        System.out.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundFileType compoundType) {
            Stream<DuFileType> children = compoundType.getChildrenAsTypes();
            children.sorted(comparator).limit(options.limit()).forEachOrdered(child -> doPrint(child, curDepth + 1));
        }
    }
}