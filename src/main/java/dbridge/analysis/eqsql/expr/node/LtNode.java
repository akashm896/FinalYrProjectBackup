package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.LtOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class LtNode extends Node implements HQLTranslatable {
    public LtNode(Node lhs, Node rhs)  {
        super(new LtOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();
        String rhsSQL = ((HQLTranslatable) children[1]).toHibQuery();
        return lhsSQL + " < " + rhsSQL;
    }
}
