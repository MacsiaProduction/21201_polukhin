package polukhin;

import polukhin.exceptions.PathFactoryException;
import polukhin.exceptions.PathFactoryUncheckedException;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Creates an instance of the PathFactory class and registers all available classes that implement
 * the dirFile interface and their corresponding predicates to test if a path can be processed by each class.
 */
public class PathFactory {
    protected static PathFactory instance;
    protected final List<Class<? extends DuFileType>> types;
    protected final List<Predicate<Path>> predicates;

    private PathFactory(List<Class<? extends MetaType<? extends DuFileType>>> metaTypes) throws PathFactoryException {
        types = new ArrayList<>();
        predicates = new ArrayList<>();
        for (Class<? extends MetaType<? extends DuFileType>> aClass : metaTypes) {
            register(aClass);
        }
    }

    public static void init(List<Class<? extends MetaType<? extends DuFileType>>> classes) throws PathFactoryException {
       instance = new PathFactory(classes);
    }

    private void register(Class<? extends MetaType<? extends DuFileType>> metaClass) throws PathFactoryException {
        try {
            Method predicateGetter = metaClass.getMethod("getFactoryPredicate");
            Method classGetter = metaClass.getMethod("getFileType");
            var instance = metaClass.getConstructor().newInstance();
            @SuppressWarnings("unchecked")
            Predicate<Path> factoryPredicate = (Predicate<Path>) predicateGetter.invoke(instance);
            @SuppressWarnings("unchecked")
            Class<? extends DuFileType> clazz = (Class<? extends DuFileType>) classGetter.invoke(instance);
            this.types.add(clazz);
            this.predicates.add(factoryPredicate);
        } catch (Exception e) {
            throw new PathFactoryException("Can't find a method getFactoryPredicate() for class" + metaClass);
        }
    }

    public static DuFileType create(Path path, JduOptions jduOptions) throws PathFactoryUncheckedException {
        PathFactory factory = PathFactory.getInstance();
        for (int i = 0; i < factory.types.size(); i++) {
            if (factory.predicates.get(i).test(path)) {
                try {
                    Class<? extends DuFileType> clazz = factory.types.get(i);
                    Constructor<? extends DuFileType> constructor = clazz.getConstructor(Path.class, JduOptions.class);
                    return constructor.newInstance(path, jduOptions);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw new PathFactoryUncheckedException("Error creating instance of class " + factory.types.get(i));
                }
            }
        }
        throw new PathFactoryUncheckedException("Path" + path + "can't be recognized as any type");
    }

    private static PathFactory getInstance() {
        if(instance == null) throw new IllegalStateException("PathFactory wasn't inited");
        else return instance;
    }
}