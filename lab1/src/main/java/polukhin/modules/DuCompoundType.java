package polukhin.modules;

import polukhin.exceptions.FileMissingUncheckedException;

import java.util.List;

public interface DuCompoundType {
    List<DuFileType> getChildren();
}
