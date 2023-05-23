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

import static ru.nsu.fit.m_polukhin.core.DuTreeElement.*;

public class TestBasic extends DuTest {
    @Test
    public void testOneEmptyFileInDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path barPath = root.resolve("bar.txt");
        Files.createFile(barPath);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo", 0, file("bar.txt",0)), options(root));
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
        DuFileType expected = tree(fs, file("foo",size), options(root));
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
        DuFileType expected = tree(fs, dir("foo", 8*1024,
                file("bar.txt",0),
                symlink("link", 8*1024,
                        file("bar.txt",0))), options(root));
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
        DuFileType expected = tree(fs, dir("foo", 8*1024,
                dir("foo1", 0,
                        file("bar.txt",0)),
                symlink("link", 8*1024,
                        dir("foo1", 0,
                            file("bar.txt",0)))), options(root));
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
        DuFileType expected = tree(fs, dir("foo", size, file("bar.txt",size)), options(root));
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
        DuFileType expected = tree(fs, dir("foo", size1+size2+size3+size4,
                file("file1",size1),
                file("file2",size2),
                file("file3",size3),
                file("file4",size4)
        ), options(root));
        TestCase.assertEquals(expected, actual);
    }

    /*
    CR:
    tests should contain all simple combinations of hierarchies and corner cases.
    so, for factory I would've expected at least:
    - file as root
    - empty dir as root
    - symlink as root
    - dir with one file as root
    - dir with empty dir as root
    - dir with symlink as root

    tests for symlinks:

    1)
    foo
      slink1 -> foo

    2)

    foo
      slink1 -> bar
    bar
      slink2 -> foo

    also maybe smth additional for negative size, wrong files and so on.
    please check if your current tests fit this criteria
     */
    @Test
    public void testTwoEmptyDirs() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo2");
        Files.createDirectory(fooPath2);

        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", 0, dir("foo2",0)), options(root));
        TestCase.assertEquals(expected, actual);
    }

    /*
    CR: please write comments on top of tests so it would be easier to understand what they check.
    e.g. for this test:

    foo1
      foo2
        file1
        file2
        file3
        file4
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
                )), options(root));
        TestCase.assertEquals(expected, actual);
    }
}
