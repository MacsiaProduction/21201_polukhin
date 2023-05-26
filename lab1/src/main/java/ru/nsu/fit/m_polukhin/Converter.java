package ru.nsu.fit.m_polukhin;

public class Converter {
    public static String convert(long bytes) {
        if(bytes>=1024L*1024L*1024L) return "["+bytes/(1024L*1024L*1024L)+"Gib]";
        if(bytes>=1024L*1024L) return "["+bytes/(1024L*1024L)+"Mib]";
        if(bytes>=1024L) return "["+bytes/1024L+"Kib]";
        if (bytes < 0) return "[Invalid]";
        return "["+bytes+"B]";
    }
}