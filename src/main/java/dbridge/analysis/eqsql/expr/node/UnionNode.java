package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.UnionOp;

public class UnionNode extends Node {
    public UnionNode(Node left, Node right) {
        super(new UnionOp(), left, right);
    }
}
