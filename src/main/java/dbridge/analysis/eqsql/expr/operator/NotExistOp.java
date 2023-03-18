/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 23/5/16.
 */
public class NotExistOp extends Operator {
    public NotExistOp() {
        super("Not Exists", OpType.NotExist, 1);
        /* 1 operand will be: query */
    }
}
