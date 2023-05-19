package ru.nsu.fit.m_polukhin.modules;

import ru.nsu.fit.m_polukhin.JduOptions;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Base class for a Type you want to add for a fdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType{
    private Long size = 0L;
    private final Path path;
    private final JduOptions jduOptions;

    public DuFileType(Path path, JduOptions jduOptions) {
        this.path = path;
        this.jduOptions = jduOptions;
    }

    public void setCalculatedSize(long size) {
        this.size = size;
    }

    public Long getCalculatedSize() {
        if (size == null) throw new IllegalStateException("size wasn't inited");
        return size;
    }

    public abstract String getPrefix();

    public Path path() {
        return path;
    }

    public JduOptions options() {
        return jduOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuFileType that = (DuFileType) o;
        return Objects.equals(size, that.size) && Objects.equals(path, that.path) && Objects.equals(jduOptions, that.jduOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, path, jduOptions);
    }
}