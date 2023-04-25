package polukhin.comparators;

import polukhin.exceptions.FileMissingUncheckedException;
import polukhin.modules.DuFileType;

import java.util.Comparator;

public class DefaultComparator implements Comparator<DuFileType> {
    @Override
    public int compare(DuFileType file1, DuFileType file2) throws FileMissingUncheckedException {
        return file1.calculateSize().compareTo(file2.calculateSize());
    }
}
