package ru.nsu.fit.m_polukhin;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.m_polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;
import ru.nsu.fit.m_polukhin.modules.dir.MetaDir;
import ru.nsu.fit.m_polukhin.modules.file.MetaFile;
import ru.nsu.fit.m_polukhin.modules.symlink.MetaSymlink;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassLoaderTest {

    @Test
    void testLoadFactoryClasses() throws ClassLoadException {
        String configString = "MetaModules=ru.nsu.fit.m_polukhin.modules.symlink.MetaSymlink," +
                "ru.nsu.fit.m_polukhin.modules.file.MetaFile," +
                "ru.nsu.fit.m_polukhin.modules.dir.MetaDir";
        ByteArrayInputStream config = new ByteArrayInputStream(configString.getBytes());
        List<Class<? extends MetaType<? extends DuFileType>>> expectedClasses = new ArrayList<>();
        expectedClasses.add(MetaSymlink.class);
        expectedClasses.add(MetaFile.class);
        expectedClasses.add(MetaDir.class);

        List<Class<? extends MetaType<? extends DuFileType>>> actualClasses = ClassLoader.loadFactoryClasses(config);

        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    void testLoadFactoryClasses_InvalidConfig() {
        String configString = "MetaModules=invalidClassName";
        ByteArrayInputStream config = new ByteArrayInputStream(configString.getBytes());

        assertThrows(ClassLoadException.class, () -> ClassLoader.loadFactoryClasses(config));
    }
}