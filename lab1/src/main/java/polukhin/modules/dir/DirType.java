package polukhin.modules.dir;

import polukhin.JduOptions;
import polukhin.exceptions.FileMissingException;
import polukhin.modules.DuCompoundFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DirType extends DuCompoundFileType {
    public DirType(Path dir, JduOptions jduOptions) {
        super(dir, jduOptions);
    }

    @Override
    public Stream<Path> getChildrenAsPaths() throws FileMissingException {
        try {
            return Files.list(path());
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }

    @Override
    public String getPrefix() {
        return "/"+ path().getFileName();
    }

}
