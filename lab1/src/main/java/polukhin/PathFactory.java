package polukhin;

import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * Creates an instance of the PathFactory class and registers all available classes that implement
 * the dirFile interface and their corresponding predicates to test if a path can be processed by each class.
 */
public class PathFactory {
    private static PathFactory instance;
    private final List<Class<? extends DuFileType>> classes;
    // CR: naming
    private final List<Predicate<Path>> comparators;

    private PathFactory() throws PathFactoryException {
        classes = new ArrayList<>();
        comparators = new ArrayList<>();
        Properties props = new Properties();
        try (InputStream in = PathFactory.class.getResourceAsStream("/factory.config")) {
            props.load(in);
            String[] classNames = props.getProperty("MetaModules").split(",");
            for (String className : classNames) {
                @SuppressWarnings("unchecked")
                Class<? extends MetaType<? extends DuFileType>> clazz = (Class<? extends MetaType<? extends DuFileType>>) Class.forName(className);
                register(clazz);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new PathFactoryException("Failed to register classes from factory.config");
        }
    }

    /**
     * Gets the singleton instance of the PathFactory class.
     *
     * @return the PathFactory instance.
     */
    private static PathFactory getInstance() throws PathFactoryException {
        if (instance == null) {
            instance = new PathFactory();
        }
        return instance;
    }

    /**
     * Registers a class that implements the MetaClass interface and its corresponding predicate to the PathFactory.
     *
     * @param metaClass the class to register.
     */
    private void register(Class<? extends MetaType<? extends DuFileType>> metaClass) throws PathFactoryException {
        try {
            Method predicateGetter = metaClass.getMethod("getFactoryPredicate");
            Method classGetter = metaClass.getMethod("getFileType");
            var instance = metaClass.getConstructor().newInstance();
            @SuppressWarnings("unchecked")
            Predicate<Path> factoryPredicate = (Predicate<Path>) predicateGetter.invoke(instance);
            @SuppressWarnings("unchecked")
            Class<? extends DuFileType> clazz = (Class<? extends DuFileType>) classGetter.invoke(instance);
            this.classes.add(clazz);
            this.comparators.add(factoryPredicate);
        } catch (Exception e) {
            throw new PathFactoryException("Can't find a method getFactoryPredicate() for class" + metaClass);
        }
    }

    /**
     * Creates an instance of a class that implements the dirFile interface based on the given path and options.
     *
     * @param path       the path to create an instance for.
     * @param jduOptions the options to pass to the instance constructor.
     * @return an instance of a class that implements the dirFile interface.
     * @throws PathFactoryException if no class is registered that can process the given path or
     * if there is an error creating an instance of the class for the given path..
     */
    public static DuFileType create(Path path, JduOptions jduOptions) throws PathFactoryException {
        PathFactory factory = PathFactory.getInstance();
        for (int i = 0; i < factory.classes.size(); i++) {
            if (factory.comparators.get(i).test(path)) {
                try {
                    Class<? extends DuFileType> clazz = factory.classes.get(i);
                    Constructor<? extends DuFileType> constructor = clazz.getConstructor(Path.class, JduOptions.class);
                    return constructor.newInstance(path, jduOptions);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw new PathFactoryException("Error creating instance of class " + factory.classes.get(i));
                }
            }
        }
        throw new PathFactoryException("Path" + path + "can't be recognized as any type");
    }
}