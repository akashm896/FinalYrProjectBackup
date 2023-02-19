/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

import soot.Value;

/**
 * Created by ek on 26/10/16.
 * Operator which holds a soot value
 */
public class ValueOp extends Operator {
    private Value value;

    public ValueOp(Value value) {
        super("SootValue", OpType.Value, 0);
        /* 0 operands. The soot value will be contained as an attribute */
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}
