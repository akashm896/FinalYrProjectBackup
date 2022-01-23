/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge;

import config.test.EqSQLRunConfig;
import config.test.FuncSignature;
import dbridge.analysis.eqsql.EqSQLDriver;
import dbridge.analysis.eqsql.expr.DIR;

/**
 * Created by venkatesh on 5/7/17.
 */
public class EqSQLDriverTestStoreResult {
    public static DIR result;
    public static EqSQLRunConfig runConfig = new MyTestRunConfigStoreResult();

    public static void main(String[] args){
        //testDoEqSQLRewrite(new WilosRunConfig());
        testDoEqSQLRewrite(runConfig);
        System.out.println("result = " + result);
    }

    private static void testDoEqSQLRewrite(EqSQLRunConfig runConfig) {
        int caseNum = 17;
        //int caseNum = runConfig.getFuncSignatures().size();
        int index = caseNum - 1;
        FuncSignature fs = runConfig.getFuncSignature(index);


//        String oldClassPath = Scene.v().getSootClassPath();
//        System.out.println("List of functions: ");
//        Scene.v().setSootClassPath(System.getProperty("java.class.path")); //+ ":/home/geetam/projects/DBridge/target/classes/spring-data-jpa-2.2.5.RELEASE.jar:/home/geetam/projects/DBridge/target/classes/spring-data-commons-2.2.5.RELEASE.jar");
//        System.out.println(Scene.v().getSootClassPath());
//      //  Scene.v().setPhantomRefs(true);
//        SootClass sc  = Scene.v().loadClass("mum.edu.cs544.controller.BrandController", 1);
//
//        for(SootMethod sm : sc.getMethods()) {
//            System.out.println(sm.getSignature());
//        }
//        Scene.v().setSootClassPath(oldClassPath);
//        Scene.v().removeClass(sc);


        boolean success = false;
        try {
            success = new EqSQLDriver(runConfig.getInputRoot(), runConfig.getOutputRoot(), fs.classPathRef, fs.funcSign).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }
}
