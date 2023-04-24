package polukhin.modules;

 /**
 *  The Converter class provides a static method convert that takes a long value representing a size in bytes
 *  Returns a String representing that size in a human-readable format.
 *  The method uses binary prefixes (KiB, MiB, GiB) to represent sizes greater than or equal to 1024 bytes.
 *  If the size is less than 1024 bytes, the method returns the size in bytes.
 */
public class Converter {
    public static String convert(long bytes) {
        if(bytes>1024*1024*1024) return "["+bytes/(1024*1024*1024)+"Gib]";
        if(bytes>1024*1024) return "["+bytes/(1024*1024)+"Mib]";
        if(bytes>1024) return "["+bytes/1024+"Kib]";
        else return "["+bytes+"B]";
    }
}