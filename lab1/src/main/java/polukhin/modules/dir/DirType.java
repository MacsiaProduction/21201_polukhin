package polukhin.modules.dir;

import polukhin.JduOptions;
import polukhin.PathFactory;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuCompoundType;
import polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirType extends DuFileType implements DuCompoundType {
    private Long size = -1L;
    private List<DuFileType> children = null;
    @Override
    public List<DuFileType> getChildren() throws PathFactoryException {
        if(children == null) {
            children = new ArrayList<>();
            try(var stream = Files.list(path())) {
                stream.forEach(path -> children.add(PathFactory.create(path, options())));
            } catch (PathFactoryException | IOException e) {
                throw new RuntimeException("Failed to init children of "+path()+e);
            }
        }
        return children;
    }
    public DirType(Path dir, JduOptions jduOptions) {
        super(dir, jduOptions);
    }
    @Override
    public Long calculateSize() throws PathFactoryException {
        if (size == -1) {
            size = 0L;
            getChildren().forEach(file -> size += file.calculateSize());
        }
        return size;
    }

    @Override
    public String getPrefix() {
        return "/"+ path().getFileName();
    }
}
