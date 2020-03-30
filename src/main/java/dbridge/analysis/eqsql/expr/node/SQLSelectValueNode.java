package dbridge.analysis.eqsql.expr.node;

import soot.Value;
import soot.jimple.IntConstant;

public class SQLSelectValueNode extends ValueNode {
    SQLSelectValueNode(Value val) {
        super(val);
    }
    public String attribute;
    public String tableName;

    public SQLSelectValueNode(Value val, String attribute, String tableName) {
        super(val);
        this.attribute = attribute;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "SELECT " + attribute + " FROM " + tableName;
    }
}
