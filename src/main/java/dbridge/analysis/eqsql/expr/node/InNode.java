package dbridge.analysis.eqsql.expr.node;
import dbridge.analysis.eqsql.expr.operator.InOp;

public class InNode extends Node {
    public InNode(Node left, Node right) {
        super(new InOp(), left, right);
    }
}
