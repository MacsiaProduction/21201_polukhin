package ru.nsu.fit.m_polukhin.modules.file;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.nio.file.Path;

public final class FileType extends DuFileType {
    public FileType(Path file) {
        super(file);
    }

    @Override
    public String getPrefix() {
        return path().getFileName().toString();
    }
}