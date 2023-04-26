package polukhin.comparators;

import polukhin.exceptions.FileMissingUncheckedException;
import polukhin.modules.DuFileType;

import java.util.Comparator;

// CR: dead code
public class InverseComparator implements Comparator<DuFileType> {
    @Override
    public int compare(DuFileType file1, DuFileType file2) throws FileMissingUncheckedException {
        return file2.calculateSize().compareTo(file1.calculateSize());
    }
}
