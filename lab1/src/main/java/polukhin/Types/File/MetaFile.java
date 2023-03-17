package polukhin.Types.File;

import polukhin.Types.Printer;
import polukhin.Types.TypeFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MetaFile implements TypeFactory<FileType> {
    @Override
    public Printer<FileType> getPrinter() {
        return new FilePrinter();
    }

    @Override
    public Class<FileType> getFileType() {
        return FileType.class;
    }

    @Override
    public Predicate<Path> getFactoryPredicate() {
        return Files::isRegularFile;
    }
}
