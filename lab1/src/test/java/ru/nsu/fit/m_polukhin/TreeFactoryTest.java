package ru.nsu.fit.m_polukhin;

import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.nsu.fit.m_polukhin.core.DuTreeElement.*;

public class TreeFactoryTest extends DuTest {
    /**
     * dir
     */
    @Test
    public void testEmptyDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", 0));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *     file
     */
    @Test
    public void testEmptyFileInDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path filePath = root.resolve("file");
        Files.createFile(filePath);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", 0, file("file",0)));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *     dir2
     */
    @Test
    public void testEmptyDirInDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path dir2 = root.resolve("dir2");
        Files.createDirectory(dir2);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", 0, dir("dir2",0)));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * file
     */
    @Test
    public void testOneFile() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("file");
        Files.createFile(root);
        var bytes = "testing".getBytes(StandardCharsets.UTF_8);
        var size = bytes.length;
        Files.write(root, bytes);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, file("file",size));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * file
     * link -> file
     */
    @Test
    public void testSymlinkToFile() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, filePath);
        DuFileType actual = treeFactory().buildTree(link, options(link));
        DuFileType expected = tree(fs, symlink("link", 8*1024, file("file",0)));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     * link -> dir
     */
    @Test
    public void testSymlinkToEmptyDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path dir = fs.getPath("dir");
        Files.createDirectory(dir);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, dir);
        DuFileType actual = treeFactory().buildTree(link, options(link));
        DuFileType expected = tree(fs, symlink("link", 8*1024, dir("dir",0)));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *     link -> file
     *     file
     */
    @Test
    public void testSymlinkToDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path filePath = root.resolve("file");
        Files.createFile(filePath);
        Files.createSymbolicLink(root.resolve("link"), filePath);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", 8*1024,
                file("file",0),
                symlink("link", 8*1024,
                        file("file",0))));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * file
     * dir
     *     link -> file
     */
    @Test
    public void testSymlinkInDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Files.createSymbolicLink(root.resolve("link"), filePath);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", 8*1024,
                symlink("link", 8*1024,
                        file("file",0))));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *     file1
     *     file2
     *     file3
     *     file4
     */
    @Test
    public void testSeveralFilesInDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        int size1 = createFile(root, "file1", "asfasdfsdf");
        int size2 = createFile(root, "file2", "asfaetfasfesrd");
        int size3 = createFile(root, "file3", "asfgsdfgdfaetfasfesrd");
        int size4 = createFile(root, "file4", "asfaetfasfsgdfgdfgsdgsesrd");

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("dir", size1+size2+size3+size4,
                file("file1",size1),
                file("file2",size2),
                file("file3",size3),
                file("file4",size4)
        ));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * foo1
     *     foo2
     *         file1
     *         file2
     *         file3
     *         file4
     */
    @Test
    public void testTwoDirsWithFiles() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo2");
        Files.createDirectory(fooPath2);
        int size1 = createFile(fooPath2, "file1", "asfasdfsdf");
        int size2 = createFile(fooPath2, "file2", "asfaetfasfesrd");
        int size3 = createFile(fooPath2, "file3", "asfgsdfgdfaetfasfesrd");
        int size4 = createFile(fooPath2, "file4", "asfaetfasfsgdfgdfgsdgsesrd");

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", size1+size2+size3+size4,
                dir("foo2",size1+size2+size3+size4,
                file("file1",size1),
                file("file2",size2),
                file("file3",size3),
                file("file4",size4)
                )));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *     link -> dir
     */
    @Test
    public void testSymlinkToDirWithIt() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Files.createSymbolicLink(root.resolve("link"), root);
        
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs,
                dir("dir",8*1024,
                    symlink("link",8*1024,
                            dir("dir",8*1024,
                                    symlink("link",8*1024,
                                            dir("dir",8*1024,
                                                    symlink("link",8*1024,
                                                            dir("dir",8*1024,
                                                                    symlink("link",8*1024,
                                                                            dir("dir",8*1024,
                                                                                    symlink("link",8*1024,
                                                                                            dir("dir",0))))))))))));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * dir
     *  dir
     *   dir
     *    dir
     *     dir
     *      dir
     *       dir
     *        dir
     *         dir
     *          dir
     *           dir
     *            file1
     */
    @Test
    public void testEdgeCaseDepth() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "dir","dir","dir","dir","dir","dir","dir","dir","dir","foo2");
        Files.createDirectories(fooPath2);
        createFile(fooPath2, "file1", "1".repeat(10));
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", 0,
                dir("dir",0,
                        dir("dir",0,
                                dir("dir",0,
                                        dir("dir",0,
                                                dir("dir",0,
                                                        dir("dir",0,
                                                                dir("dir",0,
                                                                        dir("dir",0,
                                                                                dir("dir",0,
                                                                                    dir("foo2",0))))))))))));
        TestCase.assertEquals(expected, actual);
    }

    /**
     * file was removed
     * link -> file
     */
    @Test
    public void testBadSymlink() throws IOException{
        FileSystem fs = fileSystem();
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, filePath);
        Files.delete(filePath);
        assertThrows(PathFactoryException.class, () -> treeFactory().buildTree(link, options(link)));
    }

    @Test
    public void testWrongPath() {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("file");
        assertThrows(PathFactoryException.class, () -> treeFactory().buildTree(root, options(root)));
    }

    /**
     * file
     * link -/> file
     */
    @Test
    public void testSymlinkWithoutFollowingFlag() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, filePath);
        var options = new JduOptions(link, 5, false, 5);
        DuFileType actual = treeFactory().buildTree(link, options);
        DuFileType expected = tree(fs, symlink("link", 8*1024));
        TestCase.assertEquals(expected, actual);
    }

}
