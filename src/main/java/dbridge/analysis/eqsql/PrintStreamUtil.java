package dbridge.analysis.eqsql;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by K. Venkatesh Emani on 4/30/2017.
 */
public class PrintStreamUtil {
    private static final PrintStream origOut = System.out;
    private static final PrintStream origErr = System.err;

    public static void setDummyStream() {
        PrintStream dummyStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //do nothing;
            }
        });
        System.out.println("Setting dummy stream");
        System.setOut(dummyStream);
        System.setErr(dummyStream);
    }

    public static void setOutStream(){
        System.setOut(origOut);
        System.setErr(origErr);
        System.out.println("Reverted out stream");
    }
}
