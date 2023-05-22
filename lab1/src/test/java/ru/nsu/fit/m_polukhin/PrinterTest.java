package ru.nsu.fit.m_polukhin;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrinterTest extends DuTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    @Before
    public void setUpOut() {
        System.setOut(new PrintStream(output));
    }

    @After
    public void releaseOut() {
        System.setOut(null);
    }

    @Test
    public void testPrintingTwoDirsWithFiles() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
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
        printer(root).print(actual);
        String expectedString = """
                /foo1[71B]\r
                 /foo2[71B]\r
                  file1[10B]\r
                  file2[14B]\r
                  file3[21B]\r
                  file4[26B]\r
                """;
        TestCase.assertEquals(expectedString, output.toString());
    }

    @Test
    public void testPrintingOneSymlinkToDir() throws IOException, PathFactoryException, FileMissingException, ClassLoadException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo");
        Files.createDirectory(root);
        Path dir = root.resolve("foo1");
        Files.createDirectory(dir);
        Path barPath = dir.resolve("bar.txt");
        Files.createFile(barPath);
        Files.createSymbolicLink(root.resolve("link"), dir);
        DuFileType actual = treeFactory().buildTree(root, options(root));
        printer(root).print(actual);
        String expectedString = """
                /foo[8Kib]\r
                 /foo1[0B]\r
                  bar.txt[0B]\r
                 .link[8Kib]\r
                  /foo1[0B]\r
                   bar.txt[0B]\r
                """;
        TestCase.assertEquals(expectedString, output.toString());
    }
}
