package polukhin.modules.dir;

import polukhin.JduOptions;
import polukhin.modules.DuCompoundFileType;

import java.nio.file.Path;

public class DirType extends DuCompoundFileType {
    public DirType(Path dir, JduOptions jduOptions) {
        super(dir, jduOptions);
    }

    @Override
    public String getPrefix() {
        return "/"+ path().getFileName();
    }
}
