//Based on EqNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.LessThanEqOp;
import dbridge.analysis.eqsql.expr.operator.MoreThanOp;
import exceptions.HQLTranslationException;

public class MoreThanNode extends Node implements HQLTranslatable {
    public MoreThanNode(Node lhs, Node rhs)  {
        super(new MoreThanOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();

        Node rhs = children[1];
        if(rhs instanceof ValueNode && ((ValueNode)rhs).isNull()){
            return lhsSQL + " is null";
        }
        else{
            String rhsSQL = ((HQLTranslatable) rhs).toHibQuery();
            return lhsSQL + " > " + rhsSQL;
        }
    }
}
