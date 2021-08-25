package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.TupleOp;

public class TupleNode extends Node {
    VarNode tuplevn;
    public TupleNode(Node baseRelExp, Node columnList, VarNode tuplevn) {
        super(new TupleOp(), baseRelExp, columnList);
        this.tuplevn = tuplevn;
    }
    public VarNode getTuplevn() {
        return tuplevn;
    }

}
