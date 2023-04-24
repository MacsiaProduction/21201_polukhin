package polukhin.comparators;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;

public class DefaultComparator implements DuFileTypeComparator {
    @Override
    public int compare(DuFileType file1, DuFileType file2) throws PathFactoryException, FileMissingException {
        return file1.calculateSize().compareTo(file2.calculateSize());
    }
}
