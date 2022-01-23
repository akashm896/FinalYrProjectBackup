/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 1/11/16.
 * Special operator that can represent any operator. For use only within transformation rules.
 */
public class AnyOp extends Operator {
    public AnyOp() {
        super("Any", OpType.Any, 0);
    }

    public static boolean isAnyOp(Operator op){
        return op.equals(new AnyOp());
    }
}
