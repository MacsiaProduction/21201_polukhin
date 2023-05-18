package ru.nsu.fit.m_polukhin;

import junit.framework.TestCase;
import org.junit.Test;

import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Testing extends DuTest{
    @Test
    public void testOneFileInDirectory() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Files.createFile(barPath);

        Path root = Paths.get("foo");
        DuFileType actual = treeFactory().buildTree(fooPath, options(root));
        DuFileType expected = DuTreeElement.tree(fs, DuTreeElement.dir("foo", 0L, DuTreeElement.file("bar.txt",0L)), options(root));
        TestCase.assertEquals(expected, actual);
    }
}
