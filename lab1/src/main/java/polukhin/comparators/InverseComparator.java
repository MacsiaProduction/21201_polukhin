package polukhin.comparators;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;

public class InverseComparator implements DuFileTypeComparator {
    @Override
    public int compare(DuFileType file1, DuFileType file2) throws PathFactoryException, FileMissingException {
        return file2.calculateSize().compareTo(file1.calculateSize());
    }
}
