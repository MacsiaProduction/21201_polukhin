package polukhin;

public class Converter {
    //maybe should be implemented separately for each type,
    //but they already can write their own print(),
    //even with their own converter if needed.
    public static String convert(long bytes) {
        if(bytes>1024*1024*1024) return "["+bytes/(1024*1024*1024)+"Gib]";
        if(bytes>1024*1024) return "["+bytes/(1024*1024)+"Mib]";
        if(bytes>1024) return "["+bytes/1024+"Kib]";
        else return "["+bytes+"B]";
    }
}
