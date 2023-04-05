package polukhin.types.dir;

import polukhin.types.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaDir implements MetaType<DirType> {
    @Override
    public Class<DirType> getFileType() {
        return DirType.class;
    }

    @Override
    public Predicate<Path> getFactoryPredicate() {
        return Files::isDirectory;
    }
}
