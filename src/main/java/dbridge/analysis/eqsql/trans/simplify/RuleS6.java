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
 * Created by K. Venkatesh Emani on 12/20/2016.
 * Simplification Rule {@link RuleS6}: Remove function call for translating a value into boolean. After translating to SQL (HQL), this happens automatically, so we don't care about it here.
 * Input
 *      MethodInv (0)
 *          Any (1)
 *          BooleanValue (2)
 *          Any (3)
 * Note: Any (3) will match an empty FuncParamsNode (because next() does not take arguments)
 * Output
 *      Any (1)
 */
public class RuleS6 extends Rule{
    public RuleS6() {
        super(makeInputPattern(), makeOutputPattern());
    }

    private static InputTree makeInputPattern() {
        InputTree any1 = new InputTree(OpType.Any, 1);
        InputTree booleanValue = new InputTree(OpType.MethodBooleanValue, 2);
        InputTree any3 = new InputTree(OpType.Any, 3);
        InputTree methodInv = new InputTree(OpType.InvokeMethod, 0, any1, booleanValue, any3);

        return methodInv;
    }

    private static OutputTree makeOutputPattern() {
        return new OutputTree(1);
    }


}
