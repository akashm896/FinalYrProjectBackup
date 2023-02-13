/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ArithAddOp;
import dbridge.analysis.eqsql.expr.operator.ArithMinusOp;
import exceptions.HQLTranslationException;

/**
 * Created by K. Venkatesh Emani on 1/10/2017.
 * Node representing arithmetic addition operation.
 */
public class ArithMinusNode extends Node implements HQLTranslatable {
    public Boolean isItr;
    public ArithMinusNode(Node op1, Node op2) {
        super(new ArithMinusOp(), op1, op2);
        isItr = false;
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String op1Query = ((HQLTranslatable)children[0]).toHibQuery();
        String op2Query = ((HQLTranslatable)children[1]).toHibQuery();
        return "(" + op1Query + " + " + op2Query + ")";
    }
//    @Override
//    public String toString() {
//        return "+";
//    }
}
