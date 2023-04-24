package polukhin.modules.dir;

import polukhin.JduOptions;
import polukhin.PathFactory;
import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuCompoundType;
import polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class DirType extends DuFileType implements DuCompoundType {
    private Long size = -1L;
    private List<DuFileType> children = null;
    @Override
    public List<DuFileType> getChildren() throws PathFactoryException, FileMissingException {
        if(children == null) {
            children = new ArrayList<>();
            try(Stream<Path> stream = Files.list(path())) {
                Iterator<Path> iterator = stream.iterator();
                while (iterator.hasNext()) {
                    Path path = iterator.next();
                    children.add(PathFactory.create(path, options()));
                }
            } catch (IOException e) {
                throw new FileMissingException("file not found");
            }
        }
        return children;
    }
    public DirType(Path dir, JduOptions jduOptions) {
        super(dir, jduOptions);
    }
    @Override
    public Long calculateSize() throws PathFactoryException, FileMissingException {
        if (size == -1) {
            size = 0L;
            for (DuFileType file : getChildren()) {
                size += file.calculateSize();
            }
        }
        return size;
    }

    @Override
    public String getPrefix() {
        return "/"+ path().getFileName();
    }
}
