package polukhin.modules;

import java.util.List;

public interface DuCompoundType {
    // CR: return paths instead
    List<DuFileType> getChildren();
}
