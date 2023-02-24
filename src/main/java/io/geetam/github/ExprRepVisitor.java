package io.geetam.github;

import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.visitor.NodeVisitor;

public class ExprRepVisitor implements NodeVisitor {
    public Node toReplace;
    public Node replacement;

    public ExprRepVisitor(Node toReplace, Node replacement) {
        this.toReplace = toReplace;
        this.replacement = replacement;
    }

    @Override
    public Node visit(Node node) {
        if (node.equals(toReplace)) {
            return replacement;
        }
        return node;
    }
}
