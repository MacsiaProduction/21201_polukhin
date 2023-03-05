package polukhin.Comparators;

import polukhin.dirFile;

import java.util.Comparator;

public class InverseComparator implements Comparator<dirFile> {
    @Override
    public int compare(dirFile file1, dirFile file2) {
        return file2.calculateSize().compareTo(file1.calculateSize());
    }
}
