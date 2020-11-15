package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ListOp;

public class ListNode extends Node {
    public ListNode(Node... children) {
        super(new ListOp(children.length), children);
    }
}
