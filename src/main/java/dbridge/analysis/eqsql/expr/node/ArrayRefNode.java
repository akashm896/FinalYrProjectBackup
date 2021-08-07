package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ArrayRefOp;

public class ArrayRefNode extends Node {
    public ArrayRefNode(Node array, Node index) {
        super(new ArrayRefOp(), array, index);
    }
}
