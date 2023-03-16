package polukhin.Comparators;

import polukhin.Types.DuFile;

import java.util.Comparator;

public class InverseComparator implements Comparator<DuFile> {
    @Override
    public int compare(DuFile file1, DuFile file2) {
        return file2.calculateSize().compareTo(file1.calculateSize());
    }
}
