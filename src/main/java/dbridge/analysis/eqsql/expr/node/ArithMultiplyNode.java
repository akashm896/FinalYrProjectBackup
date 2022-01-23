/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Based on ArithAddNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ArithMultiplyOp;
import exceptions.HQLTranslationException;

public class ArithMultiplyNode extends Node implements HQLTranslatable {
    public ArithMultiplyNode(Node op1, Node op2) {
        super(new ArithMultiplyOp(), op1, op2);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String op1Query = ((HQLTranslatable)children[0]).toHibQuery();
        String op2Query = ((HQLTranslatable)children[1]).toHibQuery();
        return "(" + op1Query + " * " + op2Query + ")";
    }
}
