//Based on ArithAddNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ArithMultiplyOp;
import exceptions.HQLTranslationException;

public class ArithMultiplyNode extends Node implements HQLTranslatable {
    public ArithMultiplyNode(Node op1, Node op2) {
        super(new ArithMultiplyOp(), op1, op2);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String op1Query = ((HQLTranslatable)children[0]).toHibQuery();
        String op2Query = ((HQLTranslatable)children[1]).toHibQuery();
        return "(" + op1Query + " * " + op2Query + ")";
    }
}
