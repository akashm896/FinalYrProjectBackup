/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.trans.simplify;

import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.trans.InputTree;
import dbridge.analysis.eqsql.trans.OutputTree;
import dbridge.analysis.eqsql.trans.Rule;

/**
 * Created by K. Venkatesh Emani on 1/9/2017.
 * Simplification rule {@link RuleS7C}: Remove redundant CartesianProd operators
 * Input:
 *      CartesianProd (0)
 *          CartesianProd (1)
 *              Any (2)
 * Output:
 *      CartesianProd
 *          Any (2)
 */
public class RuleS7C extends Rule{
    public RuleS7C() {
        super(makeInputPattern(), makeOutputPattern());
    }

    private static InputTree makeInputPattern() {
        InputTree any2 = new InputTree(OpType.Any, 2);
        InputTree cp1 = new InputTree(OpType.CartesianProd, 1, any2);
        InputTree cp0 = new InputTree(OpType.CartesianProd, 0, cp1);

        return cp0;
    }

    private static OutputTree makeOutputPattern() {
        OutputTree any2 = new OutputTree(2);
        OutputTree cp = new OutputTree(OpType.CartesianProd, any2);
        return cp;
    }
}
