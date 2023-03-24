package polukhin.types.file;

import polukhin.Converter;
import polukhin.JduOptions;
import polukhin.types.DuFileType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class FileType extends DuFileType {

    public FileType(Path file, JduOptions jduOptions, int mine_depth) {
        super(file, jduOptions, mine_depth);
    }

    @Override
    public Long calculateSize() {
        try {
            return Files.size(path());
        } catch (IOException e) {
            // CR: custom exception
            throw new RuntimeException("file not found");
        }
    }

    // CR: return String
    // CR: returns only path().getFileName(), similar to other classes
    @Override
    public void print(Comparator<DuFileType> comparator) {
        System.out.print(" ".repeat(mine_depth()) + path().getFileName() +
                Converter.convert(calculateSize()) + "\n");
    }
}