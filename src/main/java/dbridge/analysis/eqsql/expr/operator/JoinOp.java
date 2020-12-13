//Not part of base DBridge
package dbridge.analysis.eqsql.expr.operator;

public class JoinOp extends Operator {
    public JoinOp() {
        super("Join", OpType.Join, 2);
        /* 2 operands will be: select node, expression (string or list?) representing what to project */
    }
}
