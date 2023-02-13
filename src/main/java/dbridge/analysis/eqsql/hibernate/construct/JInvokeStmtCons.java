/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.*;
import exceptions.UnknownStatementException;
import mytest.debug;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ek on 18/5/16.
 */
public class JInvokeStmtCons implements StmtDIRConstructor {
    public static boolean isMethodSupported(String method) {
        Set <String> mustEqualOneOf = new HashSet<>();
        Set <String> mustStartWithOneOf = new HashSet<>();
        mustEqualOneOf.add("add");
        mustEqualOneOf.add("put");
        mustStartWithOneOf.add("get");
        mustStartWithOneOf.add("set");
        mustStartWithOneOf.add("add");

        for(String eq : mustEqualOneOf) {
            if(method.equals(eq)) {
                return true;
            }
        }

        for(String sw : mustStartWithOneOf) {
            if(method.startsWith(sw)) {
                return true;
            }
        }
        return false;

    }
    private static Set<String> supportedMethods;
    static {
        supportedMethods = new HashSet<>();
        supportedMethods.add("add");
        supportedMethods.add("put");
    }

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        debug d = new debug("JInvokeStmtCons.java", "construct()");
        d.dg("JInvokeStmtCons.java: invoke statement: " + stmt);
        assert (stmt instanceof InvokeStmt);

        InvokeStmt invokeStmt = (InvokeStmt) stmt;
        InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();

        String method = invokeExpr.getMethod().getName();
        if(!isMethodSupported(method)){
            d.dg("JInvokeStmtCons.java: returning nullInfo");
            return StmtInfo.nullInfo;
            /* Currently, we are only interested in list add method */
        }


        VarNode baseObj = Utils.fetchBase(invokeExpr);
        VarNode dest = baseObj;
        if(method.startsWith("add") && !method.equals("add")) {
            String attName = method.substring(3).toLowerCase();
            d.dg("baseObj: " + baseObj);
            String keyStr = baseObj.toString() + "." + attName;
            dest = new VarNode(keyStr);
        }
        Node source = Utils.parseInvokeExpr(invokeExpr, stmt.getJavaSourceStartLineNumber());

        return new StmtInfo(dest, source);
        /* Note that the base object is also the target of add operation */
    }
}
