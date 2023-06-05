package ru.nsu.fit.m_polukhin.modules.dir;

import ru.nsu.fit.m_polukhin.SymlinkOptions;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public final class DirType extends DuCompoundFileType {
    public DirType(Path dir) {
        super(dir);
    }

    @Override
    public List<Path> getChildrenAsPaths(SymlinkOptions symlinkOptions) throws FileMissingException {
        try (Stream<Path> paths = Files.list(path())){
            return paths.toList();
        } catch (IOException e) {
            throw new FileMissingException();
        }
    }

    @Override
    public String getPrefix() {
        return "/"+ path().getFileName();
    }
}
