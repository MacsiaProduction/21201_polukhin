package polukhin;

import polukhin.exceptions.ClassLoadException;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.io.*;
import java.util.*;

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
