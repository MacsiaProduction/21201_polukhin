package polukhin.modules;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;

import java.util.List;

public interface DuCompoundType {
    List<DuFileType> getChildren() throws PathFactoryException, FileMissingException;
}
