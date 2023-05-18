package ru.nsu.fit.m_polukhin.modules.file;

import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    public void calculateSize(DuFileType instance) throws FileMissingException {
        try {
            instance.setCalculatedSize(Files.size(instance.path()));
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }
}