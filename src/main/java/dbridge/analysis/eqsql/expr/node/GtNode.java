package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.GtOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class GtNode extends Node implements HQLTranslatable {
    public GtNode(Node lhs, Node rhs)  {
        super(new GtOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();
        String rhsSQL = ((HQLTranslatable) children[1]).toHibQuery();
        return lhsSQL + " > " + rhsSQL;
    }
}
