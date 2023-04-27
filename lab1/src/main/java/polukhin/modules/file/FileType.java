package polukhin.modules.file;

import polukhin.JduOptions;
import polukhin.modules.DuFileType;

import java.nio.file.Path;

public final class FileType extends DuFileType {
    public FileType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }

    @Override
    public String getPrefix() {
        return path().getFileName().toString();
    }
}