package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.TupleOp;

public class TupleNode extends Node {
    public TupleNode(Node baseRelExp, Node columnList) {
        super(new TupleOp(), baseRelExp, columnList);
    }
}
