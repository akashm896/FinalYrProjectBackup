package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.RelMinusOp;

public class RelMinusNode extends Node {
    public RelMinusNode(Node left, Node right) {
        super(new RelMinusOp(), left, right);
    }
}
