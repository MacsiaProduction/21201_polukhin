package ru.nsu.fit.m_polukhin.modules;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;

import java.nio.file.Path;

public interface MetaType<Type extends DuFileType> {

    Type createFileType(Path path);

    boolean isCompatible(Path path);

    void calculateSize(DuFileType instance) throws FileMissingException;
}