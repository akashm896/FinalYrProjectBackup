package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ArithAddOp;
import exceptions.HQLTranslationException;

/**
 * Created by K. Venkatesh Emani on 1/10/2017.
 * Node representing arithmetic addition operation.
 */
public class ArithAddNode extends Node implements HQLTranslatable {
    public ArithAddNode(Node op1, Node op2) {
        super(new ArithAddOp(), op1, op2);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String op1Query = ((HQLTranslatable)children[0]).toHibQuery();
        String op2Query = ((HQLTranslatable)children[1]).toHibQuery();
        return "(" + op1Query + " + " + op2Query + ")";
    }
}
