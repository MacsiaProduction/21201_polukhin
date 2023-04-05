package polukhin.modules;

import polukhin.JduOptions;
import polukhin.exceptions.PathFactoryException;

import java.nio.file.Path;

/**
 * Base class for a Type you want to add for a fdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType {

    private final Path path;
    private final JduOptions jduOptions;

    /**
     * Constructs a new instance of the abstract dirFile class
     *
     * @param path       the file path to calculate size
     * @param jduOptions the options of calculation
     */
    public DuFileType(Path path, JduOptions jduOptions) {
        this.path = path;
        this.jduOptions = jduOptions;
    }

    /**
     * Calculates and returns the size of the directory or file represented by this instance.
     *
     * @return the size of the directory or file in bytes
     */
    abstract public Long calculateSize() throws PathFactoryException;

    public abstract String getPrefix();

    public Path path() {
        return path;
    }

    public JduOptions options() {
        return jduOptions;
    }

}