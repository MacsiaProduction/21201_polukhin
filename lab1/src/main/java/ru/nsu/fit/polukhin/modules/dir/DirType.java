package ru.nsu.fit.polukhin.modules.dir;

import ru.nsu.fit.polukhin.JduOptions;
import ru.nsu.fit.polukhin.exceptions.FileMissingException;
import ru.nsu.fit.polukhin.modules.DuCompoundFileType;

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
