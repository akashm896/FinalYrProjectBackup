package mytest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class debug {
    private String fileName;
    private String methodName;
    boolean on;
    //Uncomment this filewriter, printwriter thing, if log printing is required.
//    public static FileWriter fileWriter = null;
//    public static PrintWriter printWriter = null;
//    static {
//        try {
//            fileWriter = new FileWriter("outputs/lastrunlog.txt");
//            printWriter = new PrintWriter(fileWriter);
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//    }

    //there are some legacy debug.dbg calls present.
    public static boolean dbgon = false;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static void dbg(String file, String method, String msg) {
        if(dbgon)
            System.out.println(ANSI_BLUE + file + ": " + ANSI_GREEN + method + ": " + ANSI_RESET + msg);
    }

    public static void dbg(String msg) {
        if(dbgon)
            System.out.println(msg);
    }


    public debug(String fileName, String methodName) {
        on = true;
        this.fileName = fileName;
        this.methodName = methodName;
    }

    public void dg(String msg) {
        if(on) {
            System.out.println(ANSI_BLUE + fileName + ": " + ANSI_GREEN + methodName + ": " + ANSI_RESET + msg);
        }
     //   else if(printWriter != null) {
       //     printWriter.println(fileName + ": " +  methodName + ": " +  msg);
        //}
    }
    public void dg(Object msg) {
        dg(msg.toString());
    }
    public void clndg(Object msg) {
        if(on) {
            System.out.println(msg);
        }
        //else if(printWriter != null) {
        //    printWriter.println(msg);
        //}
    }

    public void wrn(String msg) {
        if(on) {
            String warning = ANSI_RED + "WARN: " + fileName + ": " + methodName + ": " + msg + ANSI_RESET;
            System.out.println(warning);
        }
        //else if(printWriter != null) {
         //   printWriter.println("WARN: " + fileName + ": " + methodName + ": " + msg);
      //  }
    }


    public void wrn(Object msg) {
        wrn(msg.toString());
    }

    public void turnOff() {
        on = false;
    }

    public void turnOn() {
        on = true;
    }
}
