/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
package io.geetam.github.formalToActualVisitor;

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
