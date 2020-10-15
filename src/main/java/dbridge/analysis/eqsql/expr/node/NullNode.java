package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NullOp;

public class NullNode extends LeafNode implements HQLTranslatable {
    public NullNode() {
        super(new NullOp());
    }

    @Override
    public String toHibQuery() {
        return "NullNode";
    }
}
