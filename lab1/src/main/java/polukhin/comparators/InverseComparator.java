package polukhin.comparators;

import polukhin.types.DuFileType;

import java.util.Comparator;

public class InverseComparator implements Comparator<DuFileType> {
    @Override
    public int compare(DuFileType file1, DuFileType file2) {
        return file2.calculateSize().compareTo(file1.calculateSize());
    }
}
