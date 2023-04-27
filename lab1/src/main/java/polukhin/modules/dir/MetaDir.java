package polukhin.modules.dir;

import polukhin.exceptions.FileMissingException;
import polukhin.modules.CompoundMetaType;
import polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MetaDir implements CompoundMetaType<DirType> {
    @Override
    public Class<DirType> getFileType() {
        return DirType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isDirectory(path);
    }

    @Override
    public Long calculateSize(DirType instance) {
        return instance.getChildren().mapToLong(DuFileType::getCalculatedSize).sum();
    }

    @Override
    public Stream<Path> getChildren(DirType instance) throws FileMissingException {
        try {
            return Files.list(instance.path());
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }
}
