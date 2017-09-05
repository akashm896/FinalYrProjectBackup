package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.NodeFactory;
import dbridge.analysis.eqsql.expr.node.RetVarNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import exceptions.UnknownStatementException;
import soot.Unit;
import soot.jimple.internal.JReturnStmt;

/**
 * Created by ek on 18/5/16.
 */
public class JReturnStmtCons implements StmtDIRConstructor {

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        assert (stmt instanceof JReturnStmt);
        JReturnStmt returnStmt = (JReturnStmt)stmt;

        VarNode dest = RetVarNode.getARetVar(returnStmt.getOp().getType());
        Node source = NodeFactory.constructFromValue(returnStmt.getOp());

        return new StmtInfo(dest, source);
    }
}
