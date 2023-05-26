package ru.nsu.fit.m_polukhin.modules.symlink;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;

public class MetaSymlink implements MetaType<SymlinkType> {
    @Override
    public SymlinkType createFileType(Path path, JduOptions options) {
        return new SymlinkType(path, options);
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isSymbolicLink(path);
    }

    @Override
    public void calculateSize(DuFileType instance) {
        instance.setCalculatedSize(8*1024);
    }
}
