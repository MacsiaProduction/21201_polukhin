package polukhin.modules.symlink;

import polukhin.PathFactory;
import polukhin.exceptions.FileMissingException;
import polukhin.modules.CompoundMetaType;
import polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MetaSymlink implements CompoundMetaType<SymlinkType> {
    @Override
    public Class<SymlinkType> getFileType() {
        return SymlinkType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isSymbolicLink(path);
    }

    @Override
    public Long calculateSize(SymlinkType instance) throws FileMissingException {
        try {
            return Files.size(instance.path());
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }

    @Override
    public Stream<Path> getChildren(SymlinkType instance) throws FileMissingException {
        if (instance.options().followSymLinks()) {
            try {
                return Stream.of(Files.readSymbolicLink(instance.path()));
            } catch (IOException e) {
                throw new FileMissingException();
            }
        }
        return Stream.of();
    }
}
