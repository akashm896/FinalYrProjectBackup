package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.*;
import exceptions.UnknownStatementException;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ek on 18/5/16.
 */
public class JInvokeStmtCons implements StmtDIRConstructor {

    private static Set<String> supportedMethods;
    static {
        supportedMethods = new HashSet<>();
        supportedMethods.add("add");
        supportedMethods.add("put");
    }

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        assert (stmt instanceof InvokeStmt);

        InvokeStmt invokeStmt = (InvokeStmt) stmt;
        InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();

        String method = invokeExpr.getMethod().getName();
        if(!supportedMethods.contains(method)){
            return StmtInfo.nullInfo;
            /* Currently, we are only interested in list add method */
        }

        VarNode dest = Utils.fetchBase(invokeExpr);
        Node source = Utils.parseInvokeExpr(invokeExpr);

        return new StmtInfo(dest, source);
        /* Note that the base object is also the target of add operation */
    }
}
