package polukhin.modules.symlink;

import polukhin.JduOptions;
import polukhin.modules.DuCompoundFileType;

import java.nio.file.Path;

public class SymlinkType extends DuCompoundFileType {
    public SymlinkType(Path file, JduOptions jduOptions) {
        super(file, jduOptions);
    }
    @Override
    public String getPrefix() {
        return "." + path().getFileName();
    }
}
