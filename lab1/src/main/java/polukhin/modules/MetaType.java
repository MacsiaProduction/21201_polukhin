package polukhin.modules;

import polukhin.exceptions.FileMissingException;

import java.nio.file.Path;

public interface MetaType<Type extends DuFileType> {
    Class<Type> getFileType();
    boolean isCompatible(Path path);
    void calculateSize(DuFileType instance) throws FileMissingException;
}