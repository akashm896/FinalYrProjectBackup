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
 * Operator to represent java collection iterator()
 */
public class MethodIteratorOp extends Operator{
    public MethodIteratorOp() {
        super("Iterator()", OpType.MethodIterator, 0);
        /* No children */
    }
}
