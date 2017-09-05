package dbridge.analysis.eqsql.hibernate.construct;

import exceptions.UnknownStatementException;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.internal.*;

public class StmtDIRConstructionHandler {

    public static StmtInfo constructDagSS(Unit stmt) throws
            UnknownStatementException {
        if (!(stmt instanceof Stmt)) {
            throw new UnknownStatementException("Unknown statement: " + stmt.toString());
        }
        if (isUnaffectingStmt(stmt)) {
            return StmtInfo.nullInfo;
        }

        StmtInfo stmtInfo = StmtInfo.nullInfo;
        StmtDIRConstructor sdc = fetchStmtDagConstructor(stmt);
        if(sdc != null){
            stmtInfo = sdc.construct(stmt);
        }
        return stmtInfo;
    }

    /**
     * @param stmt
     * @return create and return an appropriate StmtDagConstructor object depending on the type of stmt
     */
    public static StmtDIRConstructor fetchStmtDagConstructor(Unit stmt) {
        StmtDIRConstructor sdc = null;
        if (stmt instanceof JIdentityStmt) {
            sdc = new JIdentityStmtCons();
        }
        else if (stmt instanceof JReturnStmt) {
            sdc = new JReturnStmtCons();
        }
        else if (stmt instanceof soot.jimple.IfStmt) {
            sdc = new IfStmtCons();
        }
        else if (stmt instanceof JInvokeStmt) {
            sdc = new JInvokeStmtCons();
        }
        else if (stmt instanceof JAssignStmt) {
            sdc = new JAssignStmtCons();
        }
        return sdc;
    }

    private static boolean isUnaffectingStmt(Unit stmt) {
        return (stmt instanceof JNopStmt) ||
                (stmt instanceof JGotoStmt);
    }
}