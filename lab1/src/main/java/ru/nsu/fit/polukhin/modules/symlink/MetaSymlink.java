package ru.nsu.fit.polukhin.modules.symlink;

import ru.nsu.fit.polukhin.exceptions.FileMissingException;
import ru.nsu.fit.polukhin.modules.DuFileType;
import ru.nsu.fit.polukhin.modules.MetaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MetaSymlink implements MetaType<SymlinkType> {
    @Override
    public Class<SymlinkType> getFileType() {
        return SymlinkType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isSymbolicLink(path);
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
