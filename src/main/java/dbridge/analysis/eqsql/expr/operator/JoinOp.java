/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Not part of base DBridge
package dbridge.analysis.eqsql.expr.operator;

public class JoinOp extends Operator {
    public JoinOp() {
        super("Join", OpType.Join, 3);
        /* 2 operands will be: select node, expression (string or list?) representing what to project */
    }
}
