package ru.nsu.fit.m_polukhin.modules.symlink;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class SymlinkType extends DuCompoundFileType {
    public SymlinkType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }

    @Override
    public List<Path> getChildrenAsPaths() throws FileMissingException {
        if (options().followSymLinks()) {
            try {
                return List.of(Files.readSymbolicLink(path()));
            } catch (IOException e) {
                throw new FileMissingException();
            }
        }
        return List.of();
    }

    @Override
    public String getPrefix() {
        return "." + path().getFileName();
    }
}
