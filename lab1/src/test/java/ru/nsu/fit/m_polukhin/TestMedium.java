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
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.nsu.fit.m_polukhin.core.DuTreeElement.*;

// CR: don't understand why we need this ('TestMedium'  does not tell me anything)
// CR: if the idea was just to test deep hierarchies, then it is not really needed, because we've tested base cases
public class TestMedium extends DuTest {
    @Test
    public void testNestedDirs() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo","foo","foo","foo","foo","foo","foo","foo2");
        Files.createDirectories(fooPath2);
        int size1 = createFile(fooPath2, "file1", "1".repeat(10));
        int size2 = createFile(fooPath2, "file2", "2".repeat(20));
        int size3 = createFile(fooPath2, "file3", "3".repeat(30));
        int size4 = createFile(fooPath2, "file4", "4".repeat(40));
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", size1+size2+size3+size4,
                dir("foo",size1+size2+size3+size4,
                        dir("foo",size1+size2+size3+size4,
                                dir("foo",size1+size2+size3+size4,
                                        dir("foo",size1+size2+size3+size4,
                                                dir("foo",size1+size2+size3+size4,
                                                        dir("foo",size1+size2+size3+size4,
                                                                dir("foo",size1+size2+size3+size4,
                dir("foo2",size1+size2+size3+size4,
                        file("file1",size1),
                        file("file2",size2),
                        file("file3",size3),
                        file("file4",size4)
                ))))))))), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testNestedDirsWithFiles() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo","foo","foo","foo2");
        Files.createDirectories(fooPath2);
        int size1 = createFile(fs.getPath("foo1"), "file1", "1".repeat(10));
        int size2 = createFile(fs.getPath("foo1", "foo"), "file2", "2".repeat(20));
        int size3 = createFile(fs.getPath("foo1", "foo","foo"), "file3", "3".repeat(30));
        int size4 = createFile(fs.getPath("foo1", "foo","foo","foo"), "file4", "4".repeat(40));
        int size5 = createFile(fs.getPath("foo1", "foo","foo","foo","foo2"), "file5", "5".repeat(50));
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", size1+size2+size3+size4+size5,
                file("file1",size1), dir("foo",size2+size3+size4+size5,
                        file("file2",size2), dir("foo",size3+size4+size5,
                                file("file3",size3), dir("foo",size4+size5,
                                        file("file4",size4), dir("foo2",size5,
                                                file("file5",size5)
                                        ))))), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testForkNestedDirsWithFiles() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo","foo","foo","foo2");
        Path fooPath3 = fs.getPath("foo1", "foo","foo","foo1","foo2");
        Files.createDirectories(fooPath2);
        Files.createDirectories(fooPath3);
        int size1 = createFile(fs.getPath("foo1"), "file1", "1".repeat(10));
        int size2 = createFile(fs.getPath("foo1", "foo"), "file2", "2".repeat(20));
        int size3 = createFile(fs.getPath("foo1", "foo","foo"), "file3", "3".repeat(30));
        int size4 = createFile(fs.getPath("foo1", "foo","foo","foo"), "file4", "4".repeat(40));
        int size5 = createFile(fs.getPath("foo1", "foo","foo","foo","foo2"), "file5", "5".repeat(50));
        assert size4 == createFile(fs.getPath("foo1", "foo","foo","foo1"), "file4", "4".repeat(40));
        assert size5 == createFile(fs.getPath("foo1", "foo","foo","foo1","foo2"), "file5", "5".repeat(50));
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", size1+size2+size3+2*size4+2*size5,
                file("file1",size1), dir("foo",size2+size3+2*size4+2*size5,
                        file("file2",size2), dir("foo",size3+2*size4+2*size5,
                                file("file3",size3),
                                dir("foo",size4+size5,
                                        file("file4",size4), dir("foo2",size5,
                                                file("file5",size5))),
                                dir("foo1",size4+size5,
                                        file("file4",size4), dir("foo2",size5,
                                                file("file5",size5)))
                                ))), options(root));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testEdgeCaseDepth() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo","foo","foo","foo","foo","foo","foo","foo","foo2");
        Files.createDirectories(fooPath2);
        createFile(fooPath2, "file1", "1".repeat(10));
        createFile(fooPath2, "file2", "2".repeat(20));
        createFile(fooPath2, "file3", "3".repeat(30));
        createFile(fooPath2, "file4", "4".repeat(40));
        DuFileType actual = treeFactory().buildTree(root, options(root));
        DuFileType expected = tree(fs, dir("foo1", 0,
                dir("foo",0,
                        dir("foo",0,
                                dir("foo",0,
                                        dir("foo",0,
                                                dir("foo",0,
                                                        dir("foo",0,
                                                                dir("foo",0,
                                                                        dir("foo",0,
                                                                            dir("foo2",0)))))))))), options(root));
        TestCase.assertEquals(expected, actual);
    }
}
