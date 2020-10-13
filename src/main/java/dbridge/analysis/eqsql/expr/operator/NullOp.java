package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 1/11/16.
 */
public class NullOp extends Operator {
    public NullOp() {
        super("NullOp", OpType.Null, 0);
        /* 0 operands. */
    }
}
