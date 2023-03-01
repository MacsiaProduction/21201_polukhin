package polukhin;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class PathFactory {
    private static PathFactory instance;
    private final List<Class<? extends dirFile>> classes;
    private final List<Predicate<Path>> comparators;
    private PathFactory() {
        classes = new ArrayList<>();
        comparators = new ArrayList<>();
        Properties props = new Properties();
        try (InputStream in = PathFactory.class.getResourceAsStream("/F.txt")) {
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

    private static synchronized PathFactory getInstance() {
        if (instance == null) {
            instance = new PathFactory();
        }
        return instance;
    }

    private void register(Class<? extends dirFile> clazz) {
        this.classes.add(clazz);
        try {
            Method method = clazz.getMethod("getfactoryPredicate");
            Predicate<Path> predicate = (Predicate<Path>) method.invoke(null);
            this.comparators.add(predicate);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

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