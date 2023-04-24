package polukhin.comparators;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;

@FunctionalInterface
public interface DuFileTypeComparator {
    int compare(DuFileType file1, DuFileType file2) throws PathFactoryException, FileMissingException;
}