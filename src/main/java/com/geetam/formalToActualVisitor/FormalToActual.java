package com.geetam.formalToActualVisitor;

import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.visitor.NodeVisitor;

public class FormalToActual implements NodeVisitor {
    public Node formal;
    public Node actual;

    public FormalToActual(Node formal, Node actual) {
        this.formal = formal;
        this.actual = actual;
    }

    @Override
    public Node visit(Node node) {
        if(node instanceof VarNode) {
            VarNode varNode = (VarNode) node;
            if(varNode.toString().equals(formal.toString())) {
                return actual;
            }
        }
        int formalPos = -1;
        for(int i = 0; i < node.getNumChildren(); i++) {
            Node child = node.getChild(i);
            if(child.toString().equals(formal.toString())) {
                formalPos = i;
                break;
            }
        }

        if(formalPos != -1) {
            node.setChild(formalPos, actual);
        }
        return node;
    }
}
