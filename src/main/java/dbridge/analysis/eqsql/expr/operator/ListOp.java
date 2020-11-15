package dbridge.analysis.eqsql.expr.operator;

public class ListOp extends Operator {
    public ListOp(int numChild) {
        super("List", OpType.List, numChild);
    }
}
