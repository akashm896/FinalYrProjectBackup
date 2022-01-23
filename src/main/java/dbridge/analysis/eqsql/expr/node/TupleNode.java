/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;


import dbridge.analysis.eqsql.expr.operator.TupleOp;

public class TupleNode extends Node {
    VarNode tuplevn;
    public TupleNode(Node baseRelExp, Node columnList, VarNode tuplevn) {
        super(new TupleOp(), baseRelExp, columnList);
        this.tuplevn = tuplevn;
    }
    public VarNode getTuplevn() {
        return tuplevn;
    }

}
