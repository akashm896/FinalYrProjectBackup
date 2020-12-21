package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NullOp;

public class NullNode extends Node{
    public NullNode() {
        super(new NullOp());
    }
}
