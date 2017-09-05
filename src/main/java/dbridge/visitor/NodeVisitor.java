package dbridge.visitor;

import dbridge.analysis.eqsql.expr.node.Node;

/**
 * Created by ek on 28/10/16.
 */
public interface NodeVisitor {
    /** Process a node and return a new node. No guarantee is given whether the original node is modified or not. */
    Node visit(Node node);
}
