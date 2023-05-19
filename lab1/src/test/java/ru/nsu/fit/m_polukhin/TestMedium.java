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
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", size1+size2+size3+size4,
                DuTreeElement.dir("foo",size1+size2+size3+size4,
                        DuTreeElement.dir("foo",size1+size2+size3+size4,
                                DuTreeElement.dir("foo",size1+size2+size3+size4,
                                        DuTreeElement.dir("foo",size1+size2+size3+size4,
                                                DuTreeElement.dir("foo",size1+size2+size3+size4,
                                                        DuTreeElement.dir("foo",size1+size2+size3+size4,
                                                                DuTreeElement.dir("foo",size1+size2+size3+size4,
                DuTreeElement.dir("foo2",size1+size2+size3+size4,
                        DuTreeElement.file("file1",size1),
                        DuTreeElement.file("file2",size2),
                        DuTreeElement.file("file3",size3),
                        DuTreeElement.file("file4",size4)
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
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", size1+size2+size3+size4+size5,
                DuTreeElement.file("file1",size1),DuTreeElement.dir("foo",size2+size3+size4+size5,
                        DuTreeElement.file("file2",size2), DuTreeElement.dir("foo",size3+size4+size5,
                                DuTreeElement.file("file3",size3),DuTreeElement.dir("foo",size4+size5,
                                        DuTreeElement.file("file4",size4),DuTreeElement.dir("foo2",size5,
                                                DuTreeElement.file("file5",size5)
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
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", size1+size2+size3+2*size4+2*size5,
                DuTreeElement.file("file1",size1),DuTreeElement.dir("foo",size2+size3+2*size4+2*size5,
                        DuTreeElement.file("file2",size2), DuTreeElement.dir("foo",size3+2*size4+2*size5,
                                DuTreeElement.file("file3",size3),
                                DuTreeElement.dir("foo",size4+size5,
                                        DuTreeElement.file("file4",size4),DuTreeElement.dir("foo2",size5,
                                                DuTreeElement.file("file5",size5))),
                                DuTreeElement.dir("foo1",size4+size5,
                                        DuTreeElement.file("file4",size4),DuTreeElement.dir("foo2",size5,
                                                DuTreeElement.file("file5",size5)))
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
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo1", 0,
                DuTreeElement.dir("foo",0,
                        DuTreeElement.dir("foo",0,
                                DuTreeElement.dir("foo",0,
                                        DuTreeElement.dir("foo",0,
                                                DuTreeElement.dir("foo",0,
                                                        DuTreeElement.dir("foo",0,
                                                                DuTreeElement.dir("foo",0,
                                                                        DuTreeElement.dir("foo",0,
                                                                            DuTreeElement.dir("foo2",0)))))))))), options(root));
        TestCase.assertEquals(expected, actual);
    }


}
