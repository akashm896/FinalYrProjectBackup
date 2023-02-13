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
 * Created by ek on 2/11/16.
 * Simplification Rule {@link RuleS3}: Remove next()
 * Input
 *      MethodInv (0)
 *          Any (1)
 *          Next (2)
 *          Any (3)
 * Note: Any (3) will match an empty FuncParamsNode (because next() does not take arguments)
 * Output
 *      Any (1)
 */
public class RuleS3 extends Rule {

    private static InputTree makeInputPattern() {
        InputTree any1 = new InputTree(OpType.Any, 1);
        InputTree next = new InputTree(OpType.MethodNext, 2);
        InputTree any3 = new InputTree(OpType.Any, 3);
        InputTree methodInv = new InputTree(OpType.InvokeMethod, 0, any1, next, any3);

        return methodInv;
    }

    private static OutputTree makeOutputPattern() {
        return new OutputTree(1);
    }

    public RuleS3() {
        super(makeInputPattern(), makeOutputPattern());
    }
}
