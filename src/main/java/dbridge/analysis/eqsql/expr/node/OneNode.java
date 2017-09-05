package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.OneOp;

/**
 * Created by ek on 26/10/16.
 */
public class OneNode extends LeafNode implements HQLTranslatable {
    public OneNode() {
        super(new OneOp());
    }

    @Override
    public String toHibQuery() {
        return "1";
    }
}
