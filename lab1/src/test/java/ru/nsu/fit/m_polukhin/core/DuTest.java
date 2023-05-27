package ru.nsu.fit.m_polukhin.core;

import org.junit.Rule;
import ru.nsu.fit.m_polukhin.ClassLoader;
import ru.nsu.fit.m_polukhin.*;
import ru.nsu.fit.m_polukhin.comparators.DefaultComparator;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class DuTest {

    @Rule
    public final FileSystemRule fileSystemRule = new FileSystemRule();

    protected FileSystem fileSystem() {
        return fileSystemRule.getFileSystem();
    }

    protected static Printer printer(PrintStream stream, Path root) {
        return new Printer(stream, new DefaultComparator(), options(root));
    }
    protected static JduOptions options(Path root) {
        return new JduOptions(root, 11, true, 5);
    }

    protected static TreeFactory treeFactory() throws PathFactoryException, ClassLoadException {
        List<Class<? extends MetaType<? extends DuFileType>>> classes;
        classes = ClassLoader.loadFactoryClasses(Main.class.getResourceAsStream("/factory.config"));
        return new TreeFactory(classes);
    }

    // returns size of the created size
    public int createFile(Path dir, String filename, String content) throws IOException {
        Path barPath = dir.resolve(filename);
        Files.createFile(barPath);
        var bytes1 = content.getBytes(StandardCharsets.UTF_8);
        Files.write(barPath, bytes1);
        return bytes1.length;
    }

}