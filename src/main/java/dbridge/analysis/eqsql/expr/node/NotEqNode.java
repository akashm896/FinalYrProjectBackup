package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NotEqOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class NotEqNode extends Node implements HQLTranslatable {
    public NotEqNode(Node lhs, Node rhs) {
        super(new NotEqOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();

        Node rhs = children[1];
        if(rhs instanceof ValueNode && ((ValueNode)rhs).isNull()){
            return lhsSQL + " is not null";
        }
        else{
            String rhsSQL = ((HQLTranslatable) rhs).toHibQuery();
            return lhsSQL + " <> " + rhsSQL;
        }
    }
}
