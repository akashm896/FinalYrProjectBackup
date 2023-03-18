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
 * Created by venkatesh on 8/7/17.
 * Simplification rule {@link RuleS8B}: Simplify InvokeMethod(CartesianProd,LazyFetch) to LazyFetch
 * This is similar to {@link RuleS8A}. Refer {@link RuleS8A} for details.
 *
 * Input:
 *      InvokeMethod (0)
 *          CartesianProd (1)
 *          LazyFetch (2)
 *          Any (3)
 * Any(3) matches an empty FuncParams node.
 * Output:
 *      LazyFetch (2)
 */
public class RuleS8B extends Rule{
    public RuleS8B() {
        super(makeInputPattern(), makeOutputPattern());
    }

    private static InputTree makeInputPattern() {
        InputTree cp1 = new InputTree(OpType.CartesianProd, 1);
        InputTree lf2 = new InputTree(OpType.LazyFetch, 2);
        InputTree any3 = new InputTree(OpType.Any, 3);

        InputTree invMethod0 = new InputTree(OpType.InvokeMethod, 0, cp1, lf2, any3);
        return invMethod0;
    }

    private static OutputTree makeOutputPattern() {
        return new OutputTree(2);
    }
}
