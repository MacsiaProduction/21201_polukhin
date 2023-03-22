package polukhin.types;

import polukhin.Options;

import java.nio.file.Path;
import java.util.Comparator;
/**
 * Base class for a Type you want to add for a fdu implementation,
 * it should be able to print itself and calculate it's size.
 */
public abstract class DuFileType {
    private final Path file;
    private final Options options;
    private final int mine_depth;
    /**
     * Constructs a new instance of the abstract dirFile class
     *
     * @param file the file path to calculate size
     * @param options the options of calculation
     * @param mine_depth the depth of printing
     */
    public DuFileType(Path file, Options options, int mine_depth) {
        this.file = file;
        this.options = options;
        this.mine_depth = mine_depth;
    }
    /**
     * Calculates and returns the size of the directory or file represented by this instance.
     *
     * @return the size of the directory or file in bytes
     */
    abstract public Long calculateSize();
    public abstract void print(Comparator<DuFileType> comparator);
    public Path file() {
        return file;
    }
    public Options options() {
        return options;
    }
    public int mine_depth() {
        return mine_depth;
    }
}