package dbridge.analysis.eqsql.expr.operator;

public class AddWithFieldExprsOp extends Operator{
    public AddWithFieldExprsOp(int numFields) {
        super("AddWithExprsList", OpType.AddWithFieldExprs, numFields);
    }
}
