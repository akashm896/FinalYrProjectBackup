package mytest;

public class debug {
    private String fileName;
    private String methodName;
    public static void dbg(String file, String method, String msg) {
        System.out.println(file + ": " + method + ": " + msg);
    }

    public static void dbg(String msg) {
        System.out.println(msg);
    }

    public debug(String fileName, String methodName) {
        this.fileName = fileName;
        this.methodName = methodName;
    }

    public void dg(String msg) {
        System.out.println(fileName + ": " + methodName + ": " + msg);
    }
    public void dg(Object msg) {
        dg(msg.toString());
    }
}
