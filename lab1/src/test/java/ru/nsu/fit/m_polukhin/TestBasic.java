package ru.nsu.fit.m_polukhin;

import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.core.DuTreeElement;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestBasic extends DuTest {
    @Test
    public void testOneEmptyFileInDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path barPath = root.resolve("bar.txt");
        Files.createFile(barPath);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", 0, DuTreeElement.file("bar.txt",0)), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testOneFile() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createFile(root);
        var bytes = "testing".getBytes(StandardCharsets.UTF_8);
        var size = bytes.length;
        Files.write(root, bytes);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.file("foo",size), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testOneSymlinkToFile() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path barPath = root.resolve("bar.txt");
        Files.createFile(barPath);
        Files.createSymbolicLink(root.resolve("link"), barPath);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", 8*1024,
                DuTreeElement.file("bar.txt",0),
                DuTreeElement.symlink("link", 8*1024,
                        DuTreeElement.file("bar.txt",0))), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testOneSymlinkToDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path dir = root.resolve("foo1");
        Files.createDirectory(dir);
        Path barPath = dir.resolve("bar.txt");
        Files.createFile(barPath);
        Files.createSymbolicLink(root.resolve("link"), dir);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", 8*1024,
                DuTreeElement.dir("foo1", 0,
                        DuTreeElement.file("bar.txt",0)),
                DuTreeElement.symlink("link", 8*1024,
                        DuTreeElement.dir("foo1", 0,
                            DuTreeElement.file("bar.txt",0)))), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testOneFileInDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path barPath = root.resolve("bar.txt");
        Files.createFile(barPath);
        var bytes = "testing".getBytes(StandardCharsets.UTF_8);
        var size = bytes.length;
        Files.write(barPath, bytes);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", size, DuTreeElement.file("bar.txt",size)), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testSeveralFilesInDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        int size1 = createFile(root, "file1", "asfasdfsdf");
        int size2 = createFile(root, "file2", "asfaetfasfesrd");
        int size3 = createFile(root, "file3", "asfgsdfgdfaetfasfesrd");
        int size4 = createFile(root, "file4", "asfaetfasfsgdfgdfgsdgsesrd");

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", size1+size2+size3+size4,
                DuTreeElement.file("file1",size1),
                DuTreeElement.file("file2",size2),
                DuTreeElement.file("file3",size3),
                DuTreeElement.file("file4",size4)
        ), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testTwoEmptyDirs() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo2");
        Files.createDirectory(fooPath2);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", 0, DuTreeElement.dir("foo2",0)), options(root));
        TestCase.assertEquals(expected, actual);
    }

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
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", size1+size2+size3+size4,
                DuTreeElement.dir("foo2",size1+size2+size3+size4,
                DuTreeElement.file("file1",size1),
                DuTreeElement.file("file2",size2),
                DuTreeElement.file("file3",size3),
                DuTreeElement.file("file4",size4)
                )), options(root));
        TestCase.assertEquals(expected, actual);
    }
}
