package dbridge.visitor;

import dbridge.analysis.eqsql.expr.node.Node;

/**
 * Created by ek on 28/10/16.
 */
public interface Visitable {
    Node accept(NodeVisitor v);
}
