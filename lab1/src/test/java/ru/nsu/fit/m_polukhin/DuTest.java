package ru.nsu.fit.m_polukhin;

import org.junit.Rule;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

public abstract class DuTest {

    @Rule
    public final FileSystemRule fileSystemRule = new FileSystemRule();

    protected FileSystem fileSystem() {
        return fileSystemRule.getFileSystem();
    }

    protected static JduOptions options(Path root) {
        return new JduOptions(root, 10, true, 10);
    }

    protected static TreeFactory treeFactory() throws PathFactoryException, ClassLoadException {
        List<Class<? extends MetaType<? extends DuFileType>>> classes;
        classes = ClassLoader.loadFactoryClasses(Main.class.getResourceAsStream("/factory.config"));
        return new TreeFactory(classes);
    }

}