package polukhin.Types;

import polukhin.Converter;
import polukhin.Options;
import polukhin.PathFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.Math.min;

public class MyDir extends DuFile {
    private Long size = (long)0;
    private final List<DuFile> children = new ArrayList<>();
    public MyDir(Path dir, Options options, int mine_depth) {
        super(dir,options, mine_depth);
        if(!getFactoryPredicate().test(dir)) {
            throw new IllegalArgumentException("try of init myDir with not a directory");
        }
    }
    @Override
    public Long calculateSize() {
        Path directory = file();
        if (size == 0) {
            try(var stream = Files.list(directory)) {
                stream.forEach(path -> {
                            DuFile tmp = PathFactory.create(path, options(), mine_depth()+1);
                            size+=tmp.calculateSize();
                            children.add(tmp);
                        });
            } catch (IOException e) {
                throw new RuntimeException("error while calculating size of dir");
            }
        }
        return size;
    }
    @Override
    public void print(Comparator<DuFile> comparator) {
        children.sort(comparator);
        System.out.print(" ".repeat(mine_depth())+"/"+file().getFileName()+
                Converter.convert(calculateSize()) + "\n");
        for(int i = 1; i <= min(options().limit(), children.size()); i++) {
            children.get(i-1).print(comparator);
        }
    }

    public static Predicate<Path> getFactoryPredicate() {
        return Files::isDirectory;
    }
}