package polukhin.modules;

import polukhin.exceptions.FileMissingException;

import java.nio.file.Path;

public interface MetaType<Type extends DuFileType> {
    Class<Type> getFileType();
    boolean isCompatible(Path path);
    Long calculateSize(Type instance) throws FileMissingException;
}