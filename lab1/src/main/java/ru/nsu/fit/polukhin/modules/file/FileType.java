package ru.nsu.fit.polukhin.modules.file;

import ru.nsu.fit.polukhin.JduOptions;
import ru.nsu.fit.polukhin.modules.DuFileType;

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