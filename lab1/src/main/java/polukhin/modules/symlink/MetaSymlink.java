package polukhin.modules.symlink;

import polukhin.modules.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaSymlink implements MetaType<SymlinkType> {
    @Override
    public Class<SymlinkType> getFileType() {
        return SymlinkType.class;
    }
    @Override
    public Predicate<Path> getFactoryPredicate() {
        return Files::isSymbolicLink;
    }
}
