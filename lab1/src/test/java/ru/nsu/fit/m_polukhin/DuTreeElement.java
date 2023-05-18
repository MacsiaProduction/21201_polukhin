package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.dir.DirType;
import ru.nsu.fit.m_polukhin.modules.file.FileType;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record DuTreeElement(Type type, String path, Long size, List<DuTreeElement> children) {

    public static DuFileType tree(FileSystem fs, DuTreeElement root, JduOptions options) throws ClassLoadException, PathFactoryException, FileMissingException, IOException {
        return buildTree(root, fs.getPath(""), options);
    }

    private static DuFileType buildTree(DuTreeElement treeElement, Path parentPath, JduOptions options) {
        Path currentPath = parentPath.resolve(treeElement.path);
        if (treeElement.type == Type.FILE) {
            var file = new FileType(currentPath, options);
            file.setCalculatedSize(treeElement.size());
            return file;
        }
        List<DuFileType> duChildren = new ArrayList<>();
        for (DuTreeElement c : treeElement.children) {
            DuFileType duFileType = buildTree(c, currentPath, options);
            duChildren.add(duFileType);
        }
        var dir = new DirType(currentPath, options);
        dir.setChildren(duChildren);
        dir.setCalculatedSize(treeElement.size());
        return dir;
    }

    public static DuTreeElement dir(String name, Long size, DuTreeElement... files) {
        return new DuTreeElement(Type.DIR, name, size, List.of(files));
    }

    public static DuTreeElement file(String name, Long size) {
        return new DuTreeElement(Type.FILE, name, size, null);
    }

    private enum Type {
        DIR,
        FILE
    }
}