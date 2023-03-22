package polukhin.types;

import java.nio.file.Path;
import java.util.function.Predicate;

/**

 An interface representing a metadata type for a file type.
 You should create one if you want to register your type in PathFactory.
 @param <Type> the type of the file type that this metadata type represents.
 */
public interface MetaType<Type extends DuFileType> {

    /**
     Returns the Class object representing the file type that this metadata type represents.
     @return the Class object representing the file type.
     */
    Class<Type> getFileType();
    /**
     Returns the Predicate object representing the factory predicate that is used to determine
     which file type class to return when a specific Path is given.
     @return the Predicate object representing the factory predicate.
     */
    Predicate<Path> getFactoryPredicate();
}