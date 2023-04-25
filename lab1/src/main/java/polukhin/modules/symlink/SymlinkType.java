package polukhin.modules.symlink;

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

public class SymlinkType extends DuFileType implements DuCompoundType {
    public SymlinkType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }
    @Override
    public Long calculateSize() {
        return (long) 8 * 1024;
    }

    @Override
    public String getPrefix() {
        return "." + path().getFileName();
    }

    @Override
    public List<DuFileType> getChildren() {
        List<DuFileType> list = new ArrayList<>();
        if (options().followSymLinks()) {
            try {
                list.add(PathFactory.create(Files.readSymbolicLink(path()), options()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
