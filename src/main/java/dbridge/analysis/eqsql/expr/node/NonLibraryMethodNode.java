//Not part of base DBridge

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NonLibraryMethodOp;

public class NonLibraryMethodNode extends LeafNode implements HQLTranslatable {
    public NonLibraryMethodNode() {
        super(new NonLibraryMethodOp());
    }

    @Override
    public String toHibQuery() {
        return "NonLibraryMethodNode";
    }
}
