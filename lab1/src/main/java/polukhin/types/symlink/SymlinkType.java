package polukhin.types.symlink;

import polukhin.Converter;
import polukhin.Options;
import polukhin.PathFactory;
import polukhin.types.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class SymlinkType extends DuFileType {
    public SymlinkType(Path file, Options options, int mine_depth) {
        super(file, options, mine_depth);
    }
    @Override
    public Long calculateSize() {
        return (long) 8 * 1024;
    }

    @Override
    public void print(Comparator<DuFileType> comparator) {
        System.out.print(" ".repeat(mine_depth()) + "." + file().getFileName() +
                Converter.convert(calculateSize()) + "\n");
        if (options().followSymLinks()) {
            try {
                PathFactory.create(Files.readSymbolicLink(file()), options(), mine_depth() + 1).print(comparator);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
