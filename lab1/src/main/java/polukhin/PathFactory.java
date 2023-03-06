package polukhin;

import polukhin.Types.dirFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * Creates an instance of the PathFactory class and registers all available classes that implement
 * the dirFile interface and their corresponding predicates to test if a path can be processed by each class.
 */
public class PathFactory {
    private static PathFactory instance;
    private final List<Class<? extends dirFile>> classes;
    private final List<Predicate<Path>> comparators;
    private PathFactory() {
        classes = new ArrayList<>();
        comparators = new ArrayList<>();
        Properties props = new Properties();
        try (InputStream in = PathFactory.class.getResourceAsStream("/factory.config")) {
            props.load(in);
            String[] classNames = props.getProperty("dirFile.classes").split(",");
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                register(clazz.asSubclass(dirFile.class));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Gets the singleton instance of the PathFactory class.
     * @return the PathFactory instance.
     */
    private static synchronized PathFactory getInstance() {
        if (instance == null) {
            instance = new PathFactory();
        }
        return instance;
    }
    /**
     * Registers a class that implements the dirFile interface and its corresponding predicate to the PathFactory.
     * @param clazz the class to register.
     */
    private void register(Class<? extends dirFile> clazz) {
        this.classes.add(clazz);
        try {
            Method method = clazz.getDeclaredMethod("getFactoryPredicate");
            Predicate<Path> predicate = (Predicate<Path>) method.invoke(null);
            this.comparators.add(predicate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't find a method getFactoryPredicate() for class" + clazz);
        }
    }
    /**
     * Creates an instance of a class that implements the dirFile interface based on the given path and options.
     * @param path the path to create an instance for.
     * @param options the options to pass to the instance constructor.
     * @param mine_depth the depth of the current directory.
     * @return an instance of a class that implements the dirFile interface.
     * @throws RuntimeException if there is an error creating an instance of the class for the given path.
     * @throws IllegalArgumentException if no class is registered that can process the given path.
     */
    public static dirFile create(Path path, Options options, int mine_depth) {
        PathFactory factory = PathFactory.getInstance();
        for (int i = 0; i < factory.classes.size(); i++) {
            if (factory.comparators.get(i).test(path)) {
                try {
                    Class<? extends dirFile> clazz = factory.classes.get(i);
                    Constructor<? extends dirFile> constructor = clazz.getConstructor(Path.class, Options.class, int.class);
                    return constructor.newInstance(path, options, mine_depth);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException("Error creating instance of class " + factory.classes.get(i), e);
                }
            }
        }
        throw new IllegalArgumentException("No matching processor found for path " + path);
    }
}