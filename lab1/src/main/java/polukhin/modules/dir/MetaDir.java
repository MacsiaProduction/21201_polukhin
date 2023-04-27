package polukhin.modules.dir;

import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.nio.file.Files;
import java.nio.file.Path;

public class MetaDir implements MetaType<DirType> {
    @Override
    public Class<DirType> getFileType() {
        return DirType.class;
    }

    @Override
    public boolean isCompatible(Path path) {
        return Files.isDirectory(path);
    }

    @Override
    public void calculateSize(DuFileType instance) {
        instance.setCalculatedSize(((DirType)instance).getChildrenAsTypes().mapToLong(DuFileType::getCalculatedSize).sum());
    }
}
