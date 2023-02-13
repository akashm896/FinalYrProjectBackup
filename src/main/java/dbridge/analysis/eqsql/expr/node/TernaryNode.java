/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.TernaryOp;

/**
 * Created by ek on 17/10/16.
 */
public class TernaryNode extends Node {
    public Boolean isBooleanTyped;
    public TernaryNode(Node condition, Node trueDag, Node falseDag) {
        super(new TernaryOp(), condition, trueDag, falseDag);
        this.isBooleanTyped = false;
    }
    public TernaryNode(Node condition, Node trueDag, Node falseDag, Boolean isBooleanTyped) {
        super(new TernaryOp(), condition, trueDag, falseDag);
        this.isBooleanTyped = isBooleanTyped;
    }

}
