package polukhin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import static java.lang.Math.min;

public class myDir extends dirFile{
    private Long size = (long)0;
    private final ArrayList<dirFile> Children = new ArrayList<>();
    public myDir(Path dir, Options options, int mine_depth) {
        super(dir,options, mine_depth);
        if(!Files.isDirectory(dir)) {
            System.err.println("not a directory " + dir);
            System.exit(1);
        }
    }
    @Override
    public Long calculateSize() {
        Path directory = file();
        if (size == 0) {
            try {
                Files.list(directory)
                        .forEach(path -> {
                            dirFile tmp = PathFactory.create(path, options(), mine_depth()+1);
                            size+=tmp.calculateSize();
                            Children.add(tmp);
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Children.sort(Comparator.comparingLong(dirFile::calculateSize));
        }
        return size;
    }
    @Override
    public void print() {
        System.out.print(" ".repeat(mine_depth())+"/"+file().getFileName()+"["+calculateSize().toString()+"bytes]\n");
        for(int i = 1; i <= min(options().limit(),Children.size()); i++) {
            Children.get(Children.size()-i).print();
        }
    }
    //@Override
    public static Predicate<Path> getfactoryPredicate() {
        return Files::isDirectory;
    }
}
