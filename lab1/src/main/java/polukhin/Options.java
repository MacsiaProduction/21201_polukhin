package polukhin;

import java.nio.file.Path;

public record Options(
        Path base_dir,
        int depth,
        boolean followSymLinks,
        int limit) {}
