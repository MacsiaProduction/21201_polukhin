package polukhin.modules;

import polukhin.JduOptions;

import java.nio.file.Path;

/**
 * Base class for a Type you want to add for a fdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType {

    private final Path path;
    private final JduOptions jduOptions;

    public DuFileType(Path path, JduOptions jduOptions) {
        this.path = path;
        this.jduOptions = jduOptions;
    }

    abstract public Long calculateSize();

    public abstract String getPrefix();

    public Path path() {
        return path;
    }

    public JduOptions options() {
        return jduOptions;
    }

}