package ru.nsu.fit.m_polukhin;

import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;
import ru.nsu.fit.m_polukhin.modules.dir.MetaDir;
import ru.nsu.fit.m_polukhin.modules.file.MetaFile;
import ru.nsu.fit.m_polukhin.modules.symlink.MetaSymlink;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(((DuCompoundFileType) duFileType).getChildrenAsTypes().findAny().isPresent());
        assertTrue(((DuCompoundFileType) duFileType).getChildrenAsPaths().findAny().isPresent());
    }

    @Test
    public void testCreate() throws PathFactoryException, IOException {
        Path root = createTestDir();
        List<Class<? extends MetaType<? extends DuFileType>>> classList =
                Arrays.asList(MetaSymlink.class, MetaFile.class, MetaDir.class);
        TreeFactory treeFactory = new TreeFactory(classList);
        DuFileType fileType = treeFactory.create(root, options(root));

        assertNotNull(fileType);
        assertTrue(fileType instanceof DuCompoundFileType);
    }

    @Test
    public void testGetMetaOf() throws PathFactoryException, IOException {
        Path root = createTestDir();
        List<Class<? extends MetaType<? extends DuFileType>>> classList =
                Arrays.asList(MetaSymlink.class, MetaFile.class, MetaDir.class);
        TreeFactory treeFactory = new TreeFactory(classList);
        MetaType<? extends DuFileType> metaType = treeFactory.getMetaOf(root);

        assertNotNull(metaType);
        assertEquals(MetaDir.class, metaType.getClass());
    }
}