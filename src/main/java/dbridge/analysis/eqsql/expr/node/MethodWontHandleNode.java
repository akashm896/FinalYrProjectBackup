package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.MethodWontHandleOp;

import java.util.Objects;

public class MethodWontHandleNode extends LeafNode implements HQLTranslatable {
    public String callSiteStr;

    public MethodWontHandleNode(String callSiteStr) {
        super(new MethodWontHandleOp());
        this.callSiteStr = callSiteStr;
    }

    @Override
    public String toHibQuery() {
        return "MethodWontHandle";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodWontHandleNode that = (MethodWontHandleNode) o;
        return callSiteStr.equals(that.callSiteStr);
    }

    @Override
    public int hashCode() {
        return callSiteStr.hashCode();
    }
}
