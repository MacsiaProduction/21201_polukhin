package polukhin.modules;

import polukhin.JduOptions;
import polukhin.exceptions.FileMissingException;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public abstract class DuCompoundFileType extends DuFileType{
    private List<DuFileType> children;
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
}
