package ru.nsu.fit.m_polukhin.core;

import ru.nsu.fit.m_polukhin.JduOptions;
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

public record DuTreeElement(Type type, String path, int size, List<DuTreeElement> children) {

    public static DuFileType tree(FileSystem fs, DuTreeElement root, JduOptions options) throws IOException {
        return buildTree(root, fs.getPath(""), options);
    }

    private static DuFileType buildTree(DuTreeElement treeElement, Path parentPath, JduOptions options) throws IOException {
        Path currentPath = parentPath.resolve(treeElement.path);
        if (treeElement.type == Type.FILE) {
            DuFileType file = new FileType(currentPath, options);
            file.setCalculatedSize(treeElement.size());
            return file;
        }
        if (treeElement.type == Type.SYMLINK) {
            SymlinkType fileType = new SymlinkType(currentPath,options);
            fileType.setChildren(List.of(buildTree(treeElement.children.get(0), Files.readSymbolicLink(currentPath).getParent(), options)));
            fileType.setCalculatedSize(treeElement.size());
            return fileType;
        }
        if (treeElement.type == Type.DIR) {
            List<DuFileType> duChildren = new ArrayList<>();
            for (DuTreeElement c : treeElement.children) {
                duChildren.add(buildTree(c, currentPath, options));
            }
            DirType fileType = new DirType(currentPath, options);
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

    public static DuTreeElement file(String name, int size) {
        return new DuTreeElement(Type.FILE, name, size, null);
    }

    private enum Type {
        DIR,
        FILE,
        SYMLINK
    }
}