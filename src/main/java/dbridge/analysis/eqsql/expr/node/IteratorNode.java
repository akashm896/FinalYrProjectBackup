//Not part of base DBridge
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.IteratorOp;

/**
 * Created by ek on 26/10/16.
 */
public class IteratorNode extends LeafNode implements HQLTranslatable {
    public IteratorNode() {
        super(new IteratorOp());
    }

    @Override
    public String toHibQuery() {
        return "Iterator";
    }
}
