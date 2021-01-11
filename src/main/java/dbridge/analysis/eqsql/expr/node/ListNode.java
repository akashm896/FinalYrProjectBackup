package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ListOp;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends Node {
    //Names of columns whose values are given by children. Not always meaningful.
    public List<FieldRefNode> columns;
    public ListNode(Node... children) {
        super(new ListOp(children.length), children);
        columns = new ArrayList<>();
    }
}
