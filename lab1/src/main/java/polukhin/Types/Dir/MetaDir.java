package polukhin.Types.Dir;

import polukhin.Types.Printer;
import polukhin.Types.TypeFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaDir implements TypeFactory<DirType> {
    @Override
    public Printer<DirType> getPrinter() {
        return new DirPrinter();
    }

    @Override
    public Class<DirType> getFileType() {
        return DirType.class;
    }

    @Override
    public Predicate<Path> getFactoryPredicate() {
        return Files::isDirectory;
    }
}
