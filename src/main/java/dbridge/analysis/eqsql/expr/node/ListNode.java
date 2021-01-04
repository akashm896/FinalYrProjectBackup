package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ListOp;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends Node {
    //Names of fields whose values are given by children. Not always meaningful.
    public List<VarNode> fieldRefNodeList;
    public ListNode(Node... children) {
        super(new ListOp(children.length), children);
        fieldRefNodeList = new ArrayList<>();
    }
}
