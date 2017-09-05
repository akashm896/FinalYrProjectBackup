package dbridge.analysis.eqsql;

import config.test.FuncSignature;
import config.test.EqSQLRunConfig;
import config.WilosRunConfig;

/**
 * Created by venkatesh on 5/7/17.
 */
public class EqSQLDriverTest {

    public static void main(String[] args){
        testDoEqSQLRewrite(new WilosRunConfig());
    }

    private static void testDoEqSQLRewrite(EqSQLRunConfig runConfig) {
        int caseNum = 1;
        int index = caseNum - 1;
        FuncSignature fs = runConfig.getFuncSignature(index);

        boolean success = false;
        try {
            success = new EqSQLDriver(runConfig.getInputRoot(), runConfig.getOutputRoot(), fs.classPathRef, fs.funcSign).doEqSQLRewrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(success ? "SUCCESS" : "FAILURE");
    }
}
