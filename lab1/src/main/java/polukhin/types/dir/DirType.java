package polukhin.types.dir;

import polukhin.Converter;
import polukhin.JduOptions;
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
    public DirType(Path dir, JduOptions jduOptions, int mine_depth) {
        super(dir, jduOptions, mine_depth);
    }
    @Override
    public Long calculateSize() {
        Path directory = path();
        if (size == -1) {
            try(var stream = Files.list(directory)) {
                stream.forEach(path -> {
                    // CR: split calculateSize and getChildren
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
        System.out.print(" ".repeat(mine_depth())+"/"+ path().getFileName()+
                Converter.convert(calculateSize()) + "\n");
        for(int i = 1; i <= min(options().limit(), getChildren().size()); i++) {
            getChildren().get(i-1).print(comparator);
        }
    }
}

// CR:
//interface DuCompoundType {
//    List<DuFileType> children();
//}
//
//class Printer {
//
//    int curDepth = 0;
//    int limit;
//    Comparator<DuFileType> comparator;
//
//    void print(DuFileType duFileType) {
//        String header = duFileType.print();
//        if (duFileType instanceof DuCompoundType compoundType) {
//            curDepth++;
//            List<DuFileType> children = compoundType.children();
//            children.sort().subList();
//            for (DuFileType child : children) {
//                print(child);
//            }
//        }
//    }
//}