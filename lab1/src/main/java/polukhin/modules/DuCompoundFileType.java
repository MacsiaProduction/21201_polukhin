package polukhin.modules;

import polukhin.JduOptions;

import java.nio.file.Path;
import java.util.stream.Stream;

public abstract class DuCompoundFileType extends DuFileType{
    private Stream<DuFileType> children;
    public DuCompoundFileType(Path path, JduOptions jduOptions) {
        super(path, jduOptions);
    }

    public void setChildren(Stream<DuFileType> children) {
        this.children = children;
    }

    public Stream<DuFileType> getChildren() {
        return this.children;
    }

}
