package ru.nsu.fit.m_polukhin.comparators;

import ru.nsu.fit.m_polukhin.modules.DuFileType;

import java.util.Comparator;

public class DefaultComparator implements Comparator<DuFileType> {
    @Override
    public int compare(DuFileType file1, DuFileType file2) {
        return file1.getCalculatedSize().compareTo(file2.getCalculatedSize());
    }
}
