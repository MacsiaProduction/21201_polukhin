package polukhin;

import polukhin.exceptions.ClassLoadException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public interface ConfigIterator {
    static List<Class<? extends MetaType<? extends DuFileType>>> loadFactoryClasses() throws ClassLoadException {
        List<Class<? extends MetaType<? extends DuFileType>>> classes = new ArrayList<>();
        Properties props = new Properties();
        try (InputStream in = PathFactory.class.getResourceAsStream("/factory.config")) {
            props.load(in);
            String[] classNames = props.getProperty("MetaModules").split(",");
            for (String className : classNames) {
                @SuppressWarnings("unchecked")
                Class<? extends MetaType<? extends DuFileType>> clazz = (Class<? extends MetaType<? extends DuFileType>>) Class.forName(className);
                classes.add(clazz);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ClassLoadException();
        }
        return classes;
    }
}
