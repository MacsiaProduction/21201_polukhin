package polukhin.Types;

import java.nio.file.Path;
import java.util.function.Predicate;

public interface TypeFactory<Type extends DuFile> {
    Class<Type> getFileType();
    /**
     * Returns a predicate to be used for creating a dirFile object of a specific subclass in PathFactory.
     * @return the predicate
     */
    Predicate<Path> getFactoryPredicate();
}