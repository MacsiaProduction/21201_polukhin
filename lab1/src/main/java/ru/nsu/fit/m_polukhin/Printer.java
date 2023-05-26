package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.stream.Stream;


public class Printer {

    private final Comparator<DuFileType> comparator;
    private final JduOptions options;

    private final PrintStream printStream;

    /**
     * Printer prints a DuFileType object with the given options.
     *
     * @param printStream    The print stream to output the results to
     * @param comparator     The comparator used to sort DuFileTypes
     * @param options        Options for printing out DuFileTypes
     */
    public Printer(PrintStream printStream, Comparator<DuFileType> comparator, JduOptions options) {
        this.printStream = printStream;
        this.comparator = comparator;
        this.options = options;
    }

    public void print(DuFileType duFileType) {
        doPrint(duFileType, 0);
    }

    private void doPrint(DuFileType duFileType, int curDepth) {
        String prefix = duFileType.getPrefix();
        String size = Converter.convert(duFileType.getCalculatedSize());
        printStream.println(" ".repeat(curDepth)+prefix+size);
        if (duFileType instanceof DuCompoundFileType compoundType && curDepth != options.depth()) {
            Stream<DuFileType> children = compoundType.getChildrenAsTypes().stream();
            children.sorted(comparator).limit(options.limit()).forEachOrdered(child -> doPrint(child, curDepth + 1));
        }
    }
}