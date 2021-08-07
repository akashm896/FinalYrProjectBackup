package dbridge.analysis.eqsql.expr.operator;

public class ArrayRefOp extends Operator{
    public ArrayRefOp() {
        super("ArrayRef", OpType.ArrayRef, 2);
    }
}
