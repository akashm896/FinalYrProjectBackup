package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NullOp;

import java.util.HashMap;

public class NullNode extends Node{

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(toString());
    }

    public NullNode() {
        super(new NullOp());
    }
}
