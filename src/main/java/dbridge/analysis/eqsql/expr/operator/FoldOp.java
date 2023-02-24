/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 24/10/16.
 */
public class FoldOp extends Operator {
    public FoldOp() {
        super("Fold", OpType.Fold, 4);
        /* 3 operands will be: expression representing the folding function, initial value of variable,
         * and the collection on which the corresponding loop iterates */
        // The fourth operator is the iterator node.
    }
}
