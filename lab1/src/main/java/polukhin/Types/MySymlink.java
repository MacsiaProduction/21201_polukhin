package polukhin.Types;

import polukhin.Converter;
import polukhin.Options;
import polukhin.PathFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

public class MySymlink extends DuFile {
    public MySymlink(Path file, Options options, int mine_depth) {
        super(file, options, mine_depth);
        if(!getFactoryPredicate().test(file)) {
            throw new IllegalArgumentException("try of init mySymlink with not a symbolic link");
        }
    }
    @Override
    public Long calculateSize() {
        return (long) 8 * 1024;
    }

    @Override
    public void print(Comparator<DuFile> comparator) {
        System.out.print(" ".repeat(mine_depth()) + "." + file().getFileName() +
                Converter.convert(calculateSize()) + "\n");
        if (options().followSymLinks()) {
            try {
                PathFactory.create(Files.readSymbolicLink(file()), options(), mine_depth() + 1).print(comparator);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Predicate<Path> getFactoryPredicate() {
        return Files::isSymbolicLink;
    }
}
