package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.MethodWontHandleOp;

public class MethodWontHandleNode extends LeafNode implements HQLTranslatable {
    public MethodWontHandleNode() {
        super(new MethodWontHandleOp());
    }

    @Override
    public String toHibQuery() {
        return "MethodWontHandle";
    }
}
