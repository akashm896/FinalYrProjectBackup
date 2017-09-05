package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ZeroOp;

/**
 * Created by ek on 26/10/16.
 */
public class ZeroNode extends LeafNode implements HQLTranslatable {
    public ZeroNode() {
        super(new ZeroOp());
    }

    @Override
    public String toHibQuery() {
        return "0";
    }
}
