package polukhin.types.dir;

import polukhin.Converter;
import polukhin.Options;
import polukhin.PathFactory;
import polukhin.types.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;

public class DirType extends DuFileType {
    private Long size = (long)-1;
    private final List<DuFileType> children = new ArrayList<>();
    public List<DuFileType> getChildren() {
        return children;
    }
    public DirType(Path dir, Options options, int mine_depth) {
        super(dir,options, mine_depth);
    }
    @Override
    public Long calculateSize() {
        Path directory = file();
        if (size == -1) {
            try(var stream = Files.list(directory)) {
                stream.forEach(path -> {
                    DuFileType tmp = PathFactory.create(path, options(), mine_depth()+1);
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
    public void print(Comparator<DuFileType> comparator) {
        getChildren().sort(comparator);
        System.out.print(" ".repeat(mine_depth())+"/"+file().getFileName()+
                Converter.convert(calculateSize()) + "\n");
        for(int i = 1; i <= min(options().limit(), getChildren().size()); i++) {
            getChildren().get(i-1).print(comparator);
        }
    }
}