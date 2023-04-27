package polukhin.modules;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;

import java.nio.file.Path;
import java.util.function.Predicate;

public interface MetaType<Type extends DuFileType> {
    Class<Type> getFileType();
    boolean isCompatible(Path path);
    Long calculateSize(Type instance) throws FileMissingException;
}