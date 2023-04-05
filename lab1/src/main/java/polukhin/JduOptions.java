package polukhin;

import java.nio.file.Path;

public record JduOptions(
        // CR: naming
        Path rootPath,
        int depth,
        boolean followSymLinks,
        int limit) {}
