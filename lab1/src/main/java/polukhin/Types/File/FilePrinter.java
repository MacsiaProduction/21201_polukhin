package polukhin.Types.File;

import polukhin.Converter;
import polukhin.Types.DuFile;
import polukhin.Types.Printer;

import java.util.Comparator;

public class FilePrinter implements Printer<FileType> {
    @Override
    public void print(FileType toPrint, Comparator<DuFile> comparator) {
        System.out.print(" ".repeat(toPrint.mine_depth())+toPrint.file().getFileName() +
                Converter.convert(toPrint.calculateSize()) + "\n");
    }
}
