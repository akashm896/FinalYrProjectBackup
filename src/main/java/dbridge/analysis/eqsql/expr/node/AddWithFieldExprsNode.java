package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;

public class AddWithFieldExprsNode extends Node{
    public AddWithFieldExprsNode(Node coll, Node tuple) {
        super(new AddWithFieldExprsOp(), coll, tuple);
    }
}
