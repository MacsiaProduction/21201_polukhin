package polukhin.modules;

import polukhin.exceptions.FileMissingException;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface CompoundMetaType<Type extends DuCompoundFileType> extends MetaType<Type>{
    Stream<Path> getChildren(Type instance) throws FileMissingException;
}
