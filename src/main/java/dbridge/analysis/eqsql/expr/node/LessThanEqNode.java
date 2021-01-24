//Based on EqNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.EqOp;
import dbridge.analysis.eqsql.expr.operator.LessThanEqOp;
import exceptions.HQLTranslationException;

public class LessThanEqNode extends Node implements HQLTranslatable {
    public LessThanEqNode(Node lhs, Node rhs)  {
        super(new LessThanEqOp(), lhs, rhs);
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
            return lhsSQL + " <= " + rhsSQL;
        }
    }
}
