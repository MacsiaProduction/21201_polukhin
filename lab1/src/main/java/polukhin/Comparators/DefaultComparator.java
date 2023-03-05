package polukhin.Comparators;

import polukhin.dirFile;

import java.util.Comparator;

public class DefaultComparator implements Comparator<dirFile> {
    @Override
    public int compare(dirFile file1, dirFile file2) {
        return file1.calculateSize().compareTo(file2.calculateSize());
    }
}
