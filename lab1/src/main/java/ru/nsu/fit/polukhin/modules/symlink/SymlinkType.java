package ru.nsu.fit.polukhin.modules.symlink;

import ru.nsu.fit.polukhin.JduOptions;
import ru.nsu.fit.polukhin.exceptions.FileMissingException;
import ru.nsu.fit.polukhin.modules.DuCompoundFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class SymlinkType extends DuCompoundFileType {
    public SymlinkType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }

    @Override
    public Stream<Path> getChildrenAsPaths() throws FileMissingException {
        if (options().followSymLinks()) {
            try {
                return Stream.of(Files.readSymbolicLink(path()));
            } catch (IOException e) {
                throw new FileMissingException();
            }
        }
        return Stream.of();
    }

    @Override
    public String getPrefix() {
        return "." + path().getFileName();
    }
}
