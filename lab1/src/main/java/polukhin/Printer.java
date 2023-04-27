package polukhin;

import polukhin.modules.DuCompoundType;
import polukhin.modules.DuFileType;

import java.util.Comparator;
import java.util.List;

public class Printer {

    private final int limit;

    private final Comparator<DuFileType> comparator;

    public Printer(JduOptions options, Comparator<DuFileType> comparator) {
        this.limit = options.limit();
        this.comparator = comparator;
    }

    public void print(DuFileType duFileType) throws FileMissingUncheckedException {
        doPrint(duFileType, 0);
    }

    private void doPrint(DuFileType duFileType, int curDepth) {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.calculateSize());
        System.out.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundType compoundType) {
            // CR: create in path factory, only until limit reached
            // CR: stream
            List<DuFileType> children = compoundType.getChildren();
            children.sort(comparator);
            var neededLen = Integer.min(limit, children.size()-1);
            var sublist = children.subList(0,neededLen);
            for (DuFileType child : sublist) {
                doPrint(child, curDepth + 1);
            }
        }
    }
}