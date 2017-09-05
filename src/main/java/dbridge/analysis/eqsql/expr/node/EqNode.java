package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.EqOp;
import exceptions.HQLTranslationException;
import soot.jimple.NullConstant;

/**
 * Created by ek on 17/10/16.
 */
public class EqNode extends Node implements HQLTranslatable {
    public EqNode(Node lhs, Node rhs)  {
        super(new EqOp(), lhs, rhs);
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
            return lhsSQL + " = " + rhsSQL;
        }
    }
}
