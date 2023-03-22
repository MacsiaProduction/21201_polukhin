package polukhin.types.file;
import polukhin.Converter;
import polukhin.Options;
import polukhin.types.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class FileType extends DuFileType {
    public FileType(Path file, Options options, int mine_depth) {
        super(file,options,mine_depth);
    }
    @Override
    public Long calculateSize() {
        try {
            return Files.size(super.file());
        } catch (IOException e) {
            throw new RuntimeException("file not found");
        }
    }
    @Override
    public void print(Comparator<DuFileType> comparator) {
        System.out.print(" ".repeat(mine_depth())+file().getFileName() +
                Converter.convert(calculateSize()) + "\n");
    }
}
