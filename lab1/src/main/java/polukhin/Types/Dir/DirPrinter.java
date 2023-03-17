package polukhin.Types.Dir;

import polukhin.Converter;
import polukhin.Types.DuFile;
import polukhin.Types.Printer;

import java.util.Comparator;

import static java.lang.Math.min;


public class DirPrinter implements Printer<DirType> {
    @Override
    public void print(DirType toPrint, Comparator<DuFile> comparator) {
        toPrint.getChildren().sort(comparator);
        System.out.print(" ".repeat(toPrint.mine_depth())+"/"+toPrint.file().getFileName()+
                Converter.convert(toPrint.calculateSize()) + "\n");
        for(int i = 1; i <= min(toPrint.options().limit(), toPrint.getChildren().size()); i++) {
    //todo factory.getPrinter(toPrint.getChildren().get(i-1).path()).print(toPrint.getChildren().get(i-1), comparator);
            toPrint.getChildren().get(i-1).print(comparator);
        }
    }
}
