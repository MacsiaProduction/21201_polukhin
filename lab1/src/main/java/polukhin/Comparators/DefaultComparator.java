package polukhin.Comparators;

import polukhin.Types.DuFile;

import java.util.Comparator;

public class DefaultComparator implements Comparator<DuFile> {
    @Override
    public int compare(DuFile file1, DuFile file2) {
        return file1.calculateSize().compareTo(file2.calculateSize());
    }
}
