/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 27/10/16.
 * Operator that represents a formal parameter used inside a function.
 */
public class ParamOp extends Operator {

    int index;

    public ParamOp(int index) {
        super("Param", OpType.Param, 0);
        /* 0 operands. Is a leaf node. The parameter number is stored
        as an attribute*/
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
