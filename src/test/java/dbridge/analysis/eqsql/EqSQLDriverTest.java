package dbridge.analysis.eqsql;

import config.MyTestRunConfig;
import config.test.FuncSignature;
import config.test.EqSQLRunConfig;
import config.WilosRunConfig;
import mytest.Owner;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;

/**
 * Created by venkatesh on 5/7/17.
 */
public class EqSQLDriverTest {

    public static void main(String[] args){
        //testDoEqSQLRewrite(new WilosRunConfig());
        testDoEqSQLRewrite(new MyTestRunConfig());
    }

    private static void testDoEqSQLRewrite(EqSQLRunConfig runConfig) {
        int caseNum = 34;
        int index = caseNum - 1;
        FuncSignature fs = runConfig.getFuncSignature(index);

        System.out.println("List of functions: ");
//        Scene.v().setSootClassPath(System.getProperty("java.class.path") + ":/home/geetam/projects/DBridge/target/classes/spring-data-jpa-2.2.5.RELEASE.jar:/home/geetam/projects/DBridge/target/classes/spring-data-commons-2.2.5.RELEASE.jar");
//        System.out.println(Scene.v().getSootClassPath());
//        Scene.v().setPhantomRefs(true);
//        SootClass sc  = Scene.v().loadClass("com.spring.bioMedical.Controller.AdminController", 1);
//        for(SootMethod sm : sc.getMethods()) {
//            System.out.println(sm.getSignature());
//        }
        boolean success = false;
        try {
            success = new EqSQLDriver(runConfig.getInputRoot(), runConfig.getOutputRoot(), fs.classPathRef, fs.funcSign).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }
}
