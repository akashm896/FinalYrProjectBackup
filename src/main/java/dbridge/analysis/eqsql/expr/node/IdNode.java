package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.IdOp;

public class IdNode extends LeafNode {
    public IdNode() {
        super(new IdOp());
    }
}
