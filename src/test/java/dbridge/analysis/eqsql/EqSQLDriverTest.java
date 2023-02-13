package dbridge.analysis.eqsql;

import config.MyTestRunConfig;
import config.test.FuncSignature;
import config.test.EqSQLRunConfig;
import mytest.debug;
import org.apache.commons.cli.*;
import io.geetam.github.CMDOptions;

import static io.geetam.github.Utils.getAbsBenchDir;

/**
 * Created by venkatesh on 5/7/17.
 */
public class EqSQLDriverTest {


    public static final String CONTROLLERSIG_OPTION_STR = "controllersig";
    public static final String BENCHDIR_OPTION_STR = "benchdir";
    public static final String REPO_OPTION_STR = "repo";
    public static final String OUTFILE_OPTION_STR = "outfile";

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
        options.addOption(REPO_OPTION_STR, true, "This option should be specified if a repo's translation" +
                                                                  "is required, value is the repo itself");
        options.addOption(OUTFILE_OPTION_STR, true, "Alloy outfile name");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        if(cmd != null) {
            if(cmd.hasOption(BENCHDIR_OPTION_STR)) {
                CMDOptions.benchDir = cmd.getOptionValue(BENCHDIR_OPTION_STR, "");
                d.dg("Got the bench-dir option value: " + CMDOptions.benchDir);
                myTestRunConfig.inputRoot = CMDOptions.benchDir;
            }
            if(cmd.hasOption(CONTROLLERSIG_OPTION_STR)) {
                CMDOptions.controllerSig = cmd.getOptionValue(CONTROLLERSIG_OPTION_STR);
                d.dg("Got the controllersig option value: " + CMDOptions.controllerSig);
            }
            if(cmd.hasOption(REPO_OPTION_STR)) {
                CMDOptions.repo = cmd.getOptionValue(REPO_OPTION_STR);
                d.dg("Got the repo option value: " + CMDOptions.repo);
            }
            if(cmd.hasOption(OUTFILE_OPTION_STR)) {
                CMDOptions.outfile = cmd.getOptionValue(OUTFILE_OPTION_STR);
                d.dg("Got the outfile option value: " + CMDOptions.outfile);
            }
        }
        if(CMDOptions.benchDir != null && CMDOptions.controllerSig != null) {
            String classPath =  System.getProperty("java.class.path");
            //Need to do this in order for bcel to work properly
            System.setProperty("java.class.path", classPath +":" + getAbsBenchDir());

            inferSummary();
        } else {
            System.err.println("Need to specifiy options -benchdir and -controllersig");
        }
        testDoEqSQLRewrite(myTestRunConfig);
    }


    private static void inferSummary() {
        debug d = new debug("EqSQLDriverTest.java", "inferSummary()");
        boolean success = false;
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("starttime, test: " + startTime);
            String[] sigSplit = CMDOptions.controllerSig.split(": ");
            assert sigSplit.length == 2 : "Invalid controller signature";
            success = new EqSQLDriver(CMDOptions.benchDir, "sootOutput", sigSplit[0], sigSplit[1]).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }


    private static void testDoEqSQLRewrite(EqSQLRunConfig runConfig) {
        debug d = new debug("EqSQLDriverTest.java", "testDoEqSQLRewrite()");
        int caseNum = 73;
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
