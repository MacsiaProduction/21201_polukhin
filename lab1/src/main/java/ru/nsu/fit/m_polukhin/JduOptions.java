package ru.nsu.fit.m_polukhin;

import java.nio.file.Path;

public record JduOptions(Path rootPath, int depth, boolean followSymLinks, int limit) {}
