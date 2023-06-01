package ru.nsu.fit.m_polukhin.core;

import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.dir.DirType;
import ru.nsu.fit.m_polukhin.modules.file.FileType;
import ru.nsu.fit.m_polukhin.modules.symlink.SymlinkType;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record DuTreeElement(Type type, String path, int size, List<DuTreeElement> children) {

    public static DuFileType tree(FileSystem fs, DuTreeElement root) throws IOException {
        return buildTree(fs, root, fs.getPath(""));
    }

    private static DuFileType buildTree(FileSystem fs, DuTreeElement treeElement, Path parentPath) throws IOException {
        Path currentPath;
        currentPath = Objects.requireNonNullElseGet(parentPath, () -> fs.getPath("")).resolve(treeElement.path);
        if (treeElement.type == Type.FILE) {
            DuFileType file = new FileType(currentPath);
            file.setCalculatedSize(treeElement.size());
            return file;
        }
        if (treeElement.type == Type.SYMLINK) {
            SymlinkType fileType = new SymlinkType(currentPath);
            Path resolved = Files.readSymbolicLink(currentPath);
            if (treeElement.children.size() == 0) {
                fileType.setChildren(List.of());
            } else {
                fileType.setChildren(List.of(buildTree(fs, treeElement.children.get(0), resolved.getParent())));
            }
            fileType.setCalculatedSize(treeElement.size());
            return fileType;
        }
        if (treeElement.type == Type.DIR) {
            List<DuFileType> duChildren = new ArrayList<>();
            for (DuTreeElement c : treeElement.children) {
                duChildren.add(buildTree(fs, c, currentPath));
            }
            DirType fileType = new DirType(currentPath);
            fileType.setChildren(duChildren);
            fileType.setCalculatedSize(treeElement.size());
            return fileType;
        }
        throw new IllegalStateException("Unimplemented type");
    }

    public static DuTreeElement dir(String name, int size, DuTreeElement... files) {
        return new DuTreeElement(Type.DIR, name, size, List.of(files));
    }

    public static DuTreeElement symlink(String name, int size, DuTreeElement child) {
        return new DuTreeElement(Type.SYMLINK, name, size, List.of(child));
    }

    public static DuTreeElement symlink(String name, int size) {
        return new DuTreeElement(Type.SYMLINK, name, size, List.of());
    }

    public static DuTreeElement file(String name, int size) {
        return new DuTreeElement(Type.FILE, name, size, null);
    }

    private enum Type {
        DIR,
        FILE,
        SYMLINK
    }
}