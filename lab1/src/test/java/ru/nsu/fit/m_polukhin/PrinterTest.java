package ru.nsu.fit.m_polukhin;

import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ru.nsu.fit.m_polukhin.core.DuTreeElement.*;

public class PrinterTest extends DuTest {

    /**
     * dir
     */
    @Test
    public void testEmptyDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");

        DuFileType actual = tree(fs, dir("dir", 0));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = "/dir[0B]\r\n";

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     *     file
     */
    @Test
    public void testEmptyFileInDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");

        DuFileType actual = tree(fs, dir("dir", 0, file("file",0)));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[0B]\r
                 file[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     *     dir2
     */
    @Test
    public void testEmptyDirInDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");

        DuFileType actual = tree(fs, dir("dir", 0, dir("dir2",0)));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[0B]\r
                 /dir2[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * file
     */
    @Test
    public void testOneFile() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("file");

        DuFileType actual = tree(fs, file("file",100));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = "file[100B]\r\n";

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * file
     * link -> file
     */
    @Test
    public void testSymlinkToFile() throws IOException {
        FileSystem fs = fileSystem();
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, filePath);

        DuFileType actual = tree(fs, symlink("link", 8*1024, file("file",0)));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, link).print(actual);

        String expected = """
                .link[8Kib]\r
                 file[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     * link -> dir
     */
    @Test
    public void testSymlinkToEmptyDir() throws IOException {
        FileSystem fs = fileSystem();
        Path dir = fs.getPath("dir");
        Files.createDirectory(dir);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, dir);

        DuFileType actual = tree(fs, symlink("link", 8*1024, dir("dir",0)));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, link).print(actual);

        String expected = """
                .link[8Kib]\r
                 /dir[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     *     link -> file
     *     file
     */
    @Test
    public void testSymlinkToDir() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path filePath = root.resolve("file");
        Files.createFile(filePath);
        Files.createSymbolicLink(root.resolve("link"), filePath);

        DuFileType actual = tree(fs, dir("dir", 8*1024,
                file("file",0),
                symlink("link", 8*1024,
                        file("file",0))));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[8Kib]\r
                 file[0B]\r
                 .link[8Kib]\r
                  file[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * file
     * dir
     *     link -> file
     */
    @Test
    public void testSymlinkInDir() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Files.createSymbolicLink(root.resolve("link"), filePath);

        DuFileType actual = tree(fs, dir("dir", 8*1024,
                symlink("link", 8*1024,
                        file("file",0))));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[8Kib]\r
                 .link[8Kib]\r
                  file[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     *     file1
     *     file2
     *     file3
     *     file4
     */
    @Test
    public void testSeveralFilesInDir() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");

        int size1 = 142, size2 = 13243, size3 = 763543, size4 = 9988888;
        DuFileType actual = tree(fs, dir("dir", size1+size2+size3+size4,
                file("file1",size1),
                file("file2",size2),
                file("file3",size3),
                file("file4",size4)
        ));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[10Mib]\r
                 file1[142B]\r
                 file2[12Kib]\r
                 file3[745Kib]\r
                 file4[9Mib]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * foo1
     *     foo2
     *         file1
     *         file2
     *         file3
     *         file4
     *         file5
     *         file6
     */
    @Test
    public void testLimitCase() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");

        int size1 = 123, size2 = 1343, size3 = 1, size4 = 998888, size5 = 13, size6 = 18;
        DuFileType actual = tree(fs, dir("foo1", size1+size2+size3+size4+size5+size6,
                dir("foo2",size1+size2+size3+size4,
                        file("file1",size1),
                        file("file2",size2),
                        file("file3",size3),
                        file("file4",size4),
                        file("file5",size5),
                        file("file6",size6)
                )));
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /foo1[976Kib]\r
                 /foo2[976Kib]\r
                  file3[1B]\r
                  file5[13B]\r
                  file6[18B]\r
                  file1[123B]\r
                  file2[1Kib]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    /**
     * dir
     *     link -> dir
     */
    @Test
    public void testSymlinkToDirWithIt() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("dir");
        Files.createDirectory(root);
        Files.createSymbolicLink(root.resolve("link"), root);

        DuFileType actual = tree(fs,
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
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = """
                /dir[8Kib]\r
                 .link[8Kib]\r
                  /dir[8Kib]\r
                   .link[8Kib]\r
                    /dir[8Kib]\r
                     .link[8Kib]\r
                      /dir[8Kib]\r
                       .link[8Kib]\r
                        /dir[8Kib]\r
                         .link[8Kib]\r
                          /dir[0B]\r
                """;

        TestCase.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testSymlinkWithoutFollowingFlag() throws IOException {
        FileSystem fs = fileSystem();
        Path filePath = fs.getPath("file");
        Files.createFile(filePath);
        Path link = fs.getPath("link");
        Files.createSymbolicLink(link, filePath);
        DuFileType actual = tree(fs, symlink("link", 8*1024));

        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        String expected = """
                .link[8Kib]\r
                """;

        printer(printStream, link).print(actual);
        TestCase.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testExtraModulesNewType() {
        String name = "don't matter";
        String prefix = "ƪ(˘⌣˘)ʃ could be any";
        long size = 1024L;
        FileSystem fs = fileSystem();
        Path root = fs.getPath(name);

        DuFileType actual = new TestType(root, prefix);
        actual.setCalculatedSize(size);
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = prefix+"[1Kib]\r\n";

        TestCase.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testExtraModulesNewCompoundType() throws IOException {
        String name = "( •̀ .̫ •́ )✧ don't matter";
        String prefix = "anything strange .·´¯`(>▂<)´¯`·.";
        long size = 1536;
        FileSystem fs = fileSystem();
        Path root = fs.getPath(name);

        DuFileType tmp = tree(fs, dir("dir", 1536,
                file("file1",1024),
                file("file2",512)
        ));
        DuCompoundFileType actual = new TestCompoundType(root, prefix,((DuCompoundFileType)tmp).getChildrenAsTypes());
        actual.setCalculatedSize(size);
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printer(printStream, root).print(actual);

        String expected = prefix + "[1Kib]\r\n" + " file2[512B]\r\n" + " file1[1Kib]\r\n";

        TestCase.assertEquals(expected, outputStream.toString());
    }
}

class TestCompoundType extends DuCompoundFileType {
    private final String prefix;
    public TestCompoundType(Path path, String prefix, List<DuFileType> children) {
        super(path);
        this.prefix = prefix;
        setChildren(children);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public List<Path> getChildrenAsPaths(SymlinkOptions symlinkOptions) {
        return null;
    }
}

class TestType extends DuFileType {
    private final String prefix;
    public TestType(Path path, String prefix) {
        super(path);
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
