package dbridge.analysis.eqsql.expr.node;
import dbridge.analysis.eqsql.expr.operator.AggSumOp;

public class AggSumNode extends Node {
    public AggSumNode(Node relation, Node column) {
        super(new AggSumOp(), relation, column);
    }
}
