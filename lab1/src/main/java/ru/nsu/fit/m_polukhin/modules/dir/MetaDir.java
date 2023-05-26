package ru.nsu.fit.m_polukhin.modules.dir;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;

public class MetaDir implements MetaType<DirType> {
    @Override
    public DirType createFileType(Path path, JduOptions options) {
        return new DirType(path, options);
    }

    @Override
    public Class<DirType> getFileType() {
        return DirType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isDirectory(path);
    }

    @Override
    public void calculateSize(DuFileType instance) {
        instance.setCalculatedSize(((DirType)instance).getChildrenAsTypes().mapToLong(DuFileType::getCalculatedSize).sum());
    }
}
