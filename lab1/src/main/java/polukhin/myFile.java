package polukhin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

public final class myFile extends dirFile {
    public myFile(Path file, Options options, int mine_depth) {
        super(file,options,mine_depth);
        if(!getFactoryPredicate().test(file)) {
            throw new IllegalArgumentException("try of init myFile with not a file");
        }
    }
    @Override
    public Long calculateSize() {
        try {
            return Files.size(super.file());
        } catch (IOException e) {
            throw new RuntimeException("file not found");
        }
    }

    @Override
    public void print(Comparator<dirFile> comparator) {
        System.out.print(" ".repeat(mine_depth())+file().getFileName() +
                Converter.convert(calculateSize()) + "\n");
    }

    public static Predicate<Path> getFactoryPredicate() {
        return Files::isRegularFile;
    }
}
