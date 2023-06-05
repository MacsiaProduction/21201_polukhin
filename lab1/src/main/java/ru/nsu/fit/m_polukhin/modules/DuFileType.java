package ru.nsu.fit.m_polukhin.modules;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Base class for a Type you want to add for a jdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType {

    private final Path path;
    private Long size = 0L;

    public DuFileType(Path path) {
        this.path = path;
    }

    public Long getCalculatedSize() {
        if (size == null) throw new IllegalStateException("size wasn't initialized");
        return size;
    }

    public void setCalculatedSize(long size) {
        this.size = size;
    }

    public abstract String getPrefix();

    public Path path() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuFileType that = (DuFileType) o;
        return Objects.equals(size, that.size) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, path);
    }
}