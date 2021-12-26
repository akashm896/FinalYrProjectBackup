package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.TernaryOp;

/**
 * Created by ek on 17/10/16.
 */
public class TernaryNode extends Node {
    public Boolean isBooleanTyped;
    public TernaryNode(Node condition, Node trueDag, Node falseDag) {
        super(new TernaryOp(), condition, trueDag, falseDag);
        this.isBooleanTyped = false;
    }
    public TernaryNode(Node condition, Node trueDag, Node falseDag, Boolean isBooleanTyped) {
        super(new TernaryOp(), condition, trueDag, falseDag);
        this.isBooleanTyped = isBooleanTyped;
    }

}
