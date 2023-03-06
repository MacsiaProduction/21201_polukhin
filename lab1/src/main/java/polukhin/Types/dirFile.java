package polukhin.Types;

import polukhin.Options;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

public abstract class dirFile {
    private final Path file;
    private final Options options;
    private final int mine_depth;
    public dirFile(Path file, Options options, int mine_depth) {
        this.file = file;
        this.options = options;
        this.mine_depth = mine_depth;
    }
    abstract public Long calculateSize();
    public Path file() {
        return file;
    }
    public Options options() {
        return options;
    }
    public int mine_depth() {
        return mine_depth;
    }
    abstract public void print(Comparator<dirFile> comparator);

    public static Predicate<Path> getFactoryPredicate() {
        throw new UnsupportedOperationException("getFactoryPredicate() is not implemented in myDir subclass");
    }
}