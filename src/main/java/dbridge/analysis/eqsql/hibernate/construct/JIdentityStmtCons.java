package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.SelfRefNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import exceptions.UnknownStatementException;
import soot.Unit;
import soot.jimple.internal.JIdentityStmt;

/**
 * Created by ek on 18/5/16.
 */
public class JIdentityStmtCons implements StmtDIRConstructor {

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        assert (stmt instanceof JIdentityStmt);
        JIdentityStmt identityStmt = (JIdentityStmt) stmt;

        /*
        interprocedural analysis for functions with parameters is not considered
        right now, so we do not do anything when stmt is a parameter assignment
        such as:
        _act = @parameter0 : ...

        Only "this" assignments are considered.
         */

        if (stmt.toString().contains("@this")){
            VarNode dest = Utils.getVarNode(identityStmt.leftBox);
            SelfRefNode source = new SelfRefNode();

            return new StmtInfo(dest, source);
        }
        return StmtInfo.nullInfo;
    }


}
