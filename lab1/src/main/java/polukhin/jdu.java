package polukhin;

public class jdu {
    public static void main(String[] args) {
        Options options = Parser.getOptions(args);
        traverse(options);
    }
    private static void traverse(Options options) {
        myDir dir = new myDir(options.base_dir(), options, 0);
        dir.print();
    }
}