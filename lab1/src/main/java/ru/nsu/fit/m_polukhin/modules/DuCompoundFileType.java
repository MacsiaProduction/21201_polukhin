package ru.nsu.fit.m_polukhin.modules;

import ru.nsu.fit.m_polukhin.SymlinkOptions;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class DuCompoundFileType extends DuFileType{
    private List<DuFileType> children = new ArrayList<>();
    public DuCompoundFileType(Path path) {
        super(path);
    }

    public void setChildren(List<DuFileType> children) {
        this.children = children;
    }

    public List<DuFileType> getChildrenAsTypes() {
        return this.children;
    }

    abstract public List<Path> getChildrenAsPaths(SymlinkOptions symlinkOptions) throws FileMissingException;

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
