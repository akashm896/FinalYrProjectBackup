package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;
import dbridge.analysis.eqsql.expr.operator.SaveOp;

public class SaveNode extends Node{
    public SaveNode(Node fieldExprList) {
        super(new SaveOp(), fieldExprList);
    }
}
