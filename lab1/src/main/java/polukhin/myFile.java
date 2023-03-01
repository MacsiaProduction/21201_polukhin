package polukhin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public final class myFile extends dirFile {
    public myFile(Path file, Options options, int mine_depth) {
        super(file,options,mine_depth);
        if(!Files.isRegularFile(file)) {
            System.err.println("not a file " + file);
            System.exit(1);
        }
    }
    @Override
    public Long calculateSize() {
        try {
            return Files.size(super.file());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print() {
        System.out.print(" ".repeat(mine_depth())+file().getFileName()+"["+calculateSize().toString()+"bytes]\n");
    }

    //@Override
    public static Predicate<Path> getfactoryPredicate() {
        return Files::isRegularFile;
    }

}
