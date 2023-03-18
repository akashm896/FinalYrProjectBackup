/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;
import dbridge.analysis.eqsql.expr.operator.SaveOp;

public class SaveNode extends Node{
    public SaveNode(Node repo, Node tuple) {
        super(new SaveOp(), repo, tuple);
    }
    public Node getRepo() {
        return getChild(0);
    }

    public TupleNode getArgTuple() {
        return (TupleNode) getChild(1);
    }
    public VarNode getArgumentToSave() {
        return getArgTuple().getTuplevn();
    }
}
