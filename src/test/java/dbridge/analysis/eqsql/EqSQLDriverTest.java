package dbridge.analysis.eqsql;

import config.MyTestRunConfig;
import config.test.FuncSignature;
import config.test.EqSQLRunConfig;
import mytest.debug;
import org.apache.commons.cli.*;
import io.geetam.github.CMDOptions;
/**
 * Created by venkatesh on 5/7/17.
 */
public class EqSQLDriverTest {

    public static String benchDir = null;
    public static String controllerSig = null;

    public static final String CONTROLLERSIG_OPTION_STR = "controllersig";
    public static final String BENCHDIR_OPTION_STR = "benchdir";

    public static void main(String[] args) {
        String javaVersion = System.getProperty("java.version");
        if(javaVersion.startsWith("1.8") == false) {
            System.err.println("Need Java SDK 1.8, have: " + javaVersion);
            System.err.println("Exiting");
            System.exit(1);
        }
        debug d = new debug("EqSQLDriverTest.java", "main()");
        MyTestRunConfig myTestRunConfig = new MyTestRunConfig();
        Options options = new Options();
        options.addOption(BENCHDIR_OPTION_STR, true, "The location of the benchmarks");
        options.addOption(CONTROLLERSIG_OPTION_STR, true, "The controller method signature in soot's format");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        if(cmd != null) {
            if(cmd.hasOption(BENCHDIR_OPTION_STR)) {
                benchDir = cmd.getOptionValue(BENCHDIR_OPTION_STR, "");
                d.dg("Got the bench-dir option value: " + benchDir);
                myTestRunConfig.inputRoot = benchDir;
            }
            if(cmd.hasOption(CONTROLLERSIG_OPTION_STR)) {
                controllerSig = cmd.getOptionValue(CONTROLLERSIG_OPTION_STR);
                d.dg("Got the controllersig option value: " + controllerSig);
            }
        }
        if(benchDir != null && controllerSig != null) {
            CMDOptions.benchDir = benchDir;
            CMDOptions.controllerSig = controllerSig;
            inferSummary();
        } else {
            System.err.println("Need to specifiy options -benchdir and -controllersig");
        }
        //testDoEqSQLRewrite(myTestRunConfig);
    }


    private static void inferSummary() {
        debug d = new debug("EqSQLDriverTest.java", "inferSummary()");
        boolean success = false;
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("starttime, test: " + startTime);
            String[] sigSplit = controllerSig.split(": ");
            assert sigSplit.length == 2 : "Invalid controller signature";
            success = new EqSQLDriver(benchDir, "sootOutput", sigSplit[0], sigSplit[1]).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }


    private static void testDoEqSQLRewrite(EqSQLRunConfig runConfig) {
        debug d = new debug("EqSQLDriverTest.java", "testDoEqSQLRewrite()");
        int caseNum = 28;
        //int caseNum = runConfig.getFuncSignatures().size();
        int index = caseNum - 1;
        FuncSignature fs = runConfig.getFuncSignature(index);


//        String oldClassPath = Scene.v().getSootClassPath();
//        System.out.println("List of functions: ");
//        Scene.v().setSootClassPath(System.getProperty("java.class.path")); //+ ":/home/geetam/projects/DBridge/target/classes/spring-data-jpa-2.2.5.RELEASE.jar:/home/geetam/projects/DBridge/target/classes/spring-data-commons-2.2.5.RELEASE.jar");
//        System.out.println(Scene.v().getSootClassPath());
//      //  Scene.v().setPhantomRefs(true);
//        SootClass sc  = Scene.v().loadClass("com.shakeel.controller.OrdersController", 1);
//
//        for(SootMethod sm : sc.getMethods()) {
//            System.out.println(sm.getSignature());
//        }
//        Scene.v().setSootClassPath(oldClassPath);
//        Scene.v().removeClass(sc);


        boolean success = false;
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("starttime, test: " + startTime);
            success = new EqSQLDriver(runConfig.getInputRoot(), runConfig.getOutputRoot(), fs.classPathRef, fs.funcSign).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }

}
