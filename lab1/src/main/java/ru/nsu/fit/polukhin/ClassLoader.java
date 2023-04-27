package ru.nsu.fit.polukhin;

import ru.nsu.fit.polukhin.exceptions.ClassLoadException;
import ru.nsu.fit.polukhin.modules.DuFileType;
import ru.nsu.fit.polukhin.modules.MetaType;

import java.io.*;
import java.util.*;

public final class ClassLoader {
    static List<Class<? extends MetaType<? extends DuFileType>>> loadFactoryClasses(InputStream config) throws ClassLoadException {
        List<Class<? extends MetaType<? extends DuFileType>>> classes = new ArrayList<>();
        Properties props = new Properties();
        try {
            props.load(config);
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