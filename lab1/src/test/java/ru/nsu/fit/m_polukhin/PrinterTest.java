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
    // CR: fragile, pass PrintStream to printer ctor
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
        createFile(fooPath2, "file1", "asfasdfsdf");
        createFile(fooPath2, "file2", "asfaetfasfesrd");
        createFile(fooPath2, "file3", "asfgsdfgdfaetfasfesrd");
        createFile(fooPath2, "file4", "asfaetfasfsgdfgdfgsdgsesrd");

        // CR: you do not need to work with actual files here, it's unit test for printer
        // CR: this means that you can create fake files using DuFileType ctors and pass them to printer
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

    /*
     CR:
     not enough tests. you need to check all possible combinations for the depth 1 and 2.
     e.g:
     - regular file is a root of tree
     - symlink is a root of tree
     - directory with no files is a root
     - directory with 1 regular file is a root
     ...
    */
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
