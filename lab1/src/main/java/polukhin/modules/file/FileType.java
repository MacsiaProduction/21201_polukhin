package polukhin.modules.file;

import polukhin.JduOptions;
import polukhin.exceptions.FileMissingUncheckedException;
import polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileType extends DuFileType {

    public FileType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }

    @Override
    public Long calculateSize() throws FileMissingUncheckedException {
        try {
            return Files.size(path());
        } catch (IOException e) {
            throw new FileMissingUncheckedException("file not found");
        }
    }

    @Override
    public String getPrefix() {
        return path().getFileName().toString();
    }
}