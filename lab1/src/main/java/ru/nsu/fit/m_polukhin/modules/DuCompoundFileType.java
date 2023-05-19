package ru.nsu.fit.m_polukhin.modules;

import ru.nsu.fit.m_polukhin.JduOptions;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class DuCompoundFileType extends DuFileType{
    private List<DuFileType> children = new ArrayList<>();
    public DuCompoundFileType(Path path, JduOptions jduOptions) {
        super(path, jduOptions);
    }

    public void setChildren(List<DuFileType> children) {
        this.children = children;
    }

    public Stream<DuFileType> getChildrenAsTypes() {
        return this.children.stream();
    }

    abstract public Stream<Path> getChildrenAsPaths() throws FileMissingException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DuCompoundFileType that = (DuCompoundFileType) o;
        return Arrays.equals(children.toArray(), that.children.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Arrays.hashCode(children.toArray()));
    }
}
