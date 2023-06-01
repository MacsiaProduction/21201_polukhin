package ru.nsu.fit.m_polukhin;

import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeFactoryTest extends DuTest {

    private Path createTestDir() throws IOException {
        FileSystem fs = fileSystem();
        Path root = fs.getPath("foo1");
        Files.createDirectory(root);
        Path fooPath2 = fs.getPath("foo1", "foo","foo","foo","foo","foo","foo","foo","foo2");
        Files.createDirectories(fooPath2);
        createFile(fooPath2, "file1", "1".repeat(10));
        createFile(fooPath2, "file2", "2".repeat(20));
        createFile(fooPath2, "file3", "3".repeat(30));
        createFile(fooPath2, "file4", "4".repeat(40));
        return root;
    }
    @Test
    public void testBuildTree() throws PathFactoryException, FileMissingException, IOException, ClassLoadException {
        Path root = createTestDir();
        DuFileType duFileType = treeFactory().buildTree(root, options(root));

        assertNotNull(duFileType);
        assertTrue(duFileType instanceof DuCompoundFileType);
        assertTrue(((DuCompoundFileType) duFileType).getChildrenAsTypes().stream().findAny().isPresent());
        assertTrue(((DuCompoundFileType) duFileType).getChildrenAsPaths(() -> true).stream().findAny().isPresent());
    }
}