package polukhin.types;

import polukhin.JduOptions;

import java.nio.file.Path;
import java.util.Comparator;

/**
 * Base class for a Type you want to add for a fdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType {

    private final Path path;
    private final JduOptions jduOptions;
    private final int mine_depth;

    /**
     * Constructs a new instance of the abstract dirFile class
     *
     * @param path       the file path to calculate size
     * @param jduOptions the options of calculation
     * @param mine_depth the depth of printing
     */
    public DuFileType(Path path, JduOptions jduOptions, int mine_depth) {
        this.path = path;
        this.jduOptions = jduOptions;
        this.mine_depth = mine_depth;
    }

    /**
     * Calculates and returns the size of the directory or file represented by this instance.
     *
     * @return the size of the directory or file in bytes
     */
    abstract public Long calculateSize();

    public abstract void print(Comparator<DuFileType> comparator);

    public Path path() {
        return path;
    }

    public JduOptions options() {
        return jduOptions;
    }

    public int mine_depth() {
        return mine_depth;
    }
}