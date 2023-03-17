package polukhin.Types.File;
import polukhin.Converter;
import polukhin.Options;
import polukhin.Types.DuFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileType extends DuFile {
    public FileType(Path file, Options options, int mine_depth) {
        super(file,options,mine_depth);
    }
    @Override
    public Long calculateSize() {
        try {
            return Files.size(super.file());
        } catch (IOException e) {
            throw new RuntimeException("file not found");
        }
    }
}
