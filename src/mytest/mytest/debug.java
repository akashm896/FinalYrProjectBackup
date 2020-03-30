package mytest;

public class debug {
    public static void dbg(String file, String method, String msg) {
        System.out.println(file + ": " + method + ": " + msg);
    }

    public static void dbg(String msg) {
        System.out.println(msg);
    }
}
