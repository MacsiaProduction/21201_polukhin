package polukhin;

 /**
 *  The Converter class provides a static method convert that takes a long value representing a size in bytes
 *  Returns a String representing that size in a human-readable format.
 **/
public class Converter {
    public static String convert(long bytes) {
        if(bytes>1024*1024*1024) return "["+bytes/(1024*1024*1024)+"Gib]";
        if(bytes>1024*1024) return "["+bytes/(1024*1024)+"Mib]";
        if(bytes>1024) return "["+bytes/1024+"Kib]";
        else return "["+bytes+"B]";
    }
}