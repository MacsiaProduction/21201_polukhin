package polukhin.Types.Dir;

import polukhin.Converter;
import polukhin.Options;
import polukhin.PathFactory;
import polukhin.Types.DuFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.Math.min;

public class DirType extends DuFile {
    private Long size = (long)0;
    private final List<DuFile> children = new ArrayList<>();
    public List<DuFile> getChildren() {
        return children;
    }
    public DirType(Path dir, Options options, int mine_depth) {
        super(dir,options, mine_depth);
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
}