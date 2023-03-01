package polukhin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class mySymlink extends dirFile {
    private final Long size = (long)8*1024;
    public mySymlink(Path file, Options options, int mine_depth) {
        super(file, options, mine_depth);
        if(!Files.isSymbolicLink(file)) {
            System.err.println("not a symlink " + file);
            System.exit(1);
        }
    }
    @Override
    public Long calculateSize() {
        return size;
    }

    @Override
    public void print() {
        if (options().followSymLinks()) {
            try {
                generator.determine(Files.readSymbolicLink(file()), options(), mine_depth()+1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print(" ".repeat(mine_depth()) + "." + file().getFileName() + "[" + calculateSize().toString() + "bytes]\n");
        }
    }

    //@Override
    public static Predicate<Path> getfactoryPredicate() {
        return Files::isSymbolicLink;
    }
}
