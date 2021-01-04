package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.EmptySetOp;

public class EmptySetNode extends LeafNode implements HQLTranslatable {
    public EmptySetNode() {
        super(new EmptySetOp());
    }

    @Override
    public String toHibQuery() {
        return "EmptySet";
    }
}
