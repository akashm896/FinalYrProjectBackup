//Not part of base DBridge
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NextOp;

/**
 * Created by ek on 26/10/16.
 */
public class NextNode extends LeafNode implements HQLTranslatable {
    public NextNode() {
        super(new NextOp());
    }

    @Override
    public String toHibQuery() {
        return "Iterator";
    }
}
