package polukhin;

import java.nio.file.Path;

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
    abstract public void print();
    //abstract public Predicate<Path> getfactoryPredicate();
}