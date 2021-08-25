package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;
import dbridge.analysis.eqsql.expr.operator.SaveOp;

public class SaveNode extends Node{
    public SaveNode(Node repo, Node tuple) {
        super(new SaveOp(), repo, tuple);
    }
    public Node getRepo() {
        return getChild(0);
    }

    public TupleNode getArgTuple() {
        return (TupleNode) getChild(1);
    }
    public VarNode getArgumentToSave() {
        return getArgTuple().getTuplevn();
    }
}
