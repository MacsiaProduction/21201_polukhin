package ru.nsu.fit.m_polukhin;

public class Converter {
    public static String convert(long bytes) {
        if(bytes>1024*1024*1024) return "["+bytes/(1024*1024*1024)+"Gib]";
        if(bytes>1024*1024) return "["+bytes/(1024*1024)+"Mib]";
        if(bytes>1024) return "["+bytes/1024+"Kib]";
        else return "["+bytes+"B]";
    }
}