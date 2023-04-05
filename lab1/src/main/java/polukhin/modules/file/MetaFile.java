package polukhin.modules.file;

import polukhin.modules.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaFile implements MetaType<FileType> {
    @Override
    public Class<FileType> getFileType() {
        return FileType.class;
    }

    @Override
    public Predicate<Path> getFactoryPredicate() {
        return Files::isRegularFile;
    }
}
