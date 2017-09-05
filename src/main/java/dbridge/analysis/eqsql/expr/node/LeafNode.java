package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.Operator;

import java.util.HashSet;

/**
 * Created by ek on 28/10/16.
 * Common constructor statements for all Leaf derivatives
 * (Note: Leaf derivatives do not use the constructor from Node)
 */
public class LeafNode extends Node implements Leaf {
    public LeafNode(Operator op) {
        children = null;
        operator = op;
    }
}
