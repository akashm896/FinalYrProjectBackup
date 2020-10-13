package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NullOp;

/**
 * Created by ek on 26/10/16.
 */
public class NullNode extends LeafNode implements HQLTranslatable {
    public NullNode() {
        super(new NullOp());
    }

    @Override
    public String toHibQuery() {
        return "NullNode";
    }
}
