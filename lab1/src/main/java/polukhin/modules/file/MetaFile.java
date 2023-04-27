package polukhin.modules.file;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.FileMissingUncheckedException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.MetaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaFile implements MetaType<FileType> {
    @Override
    public Class<FileType> getFileType() {
        return FileType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    public Long calculateSize(FileType instance) throws FileMissingException {
        try {
            return Files.size(instance.path());
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }
}
