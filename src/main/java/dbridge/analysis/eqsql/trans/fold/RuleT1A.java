/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.trans.fold;

import dbridge.analysis.eqsql.expr.operator.*;
import dbridge.analysis.eqsql.trans.InputTree;
import dbridge.analysis.eqsql.trans.OutputTree;
import dbridge.analysis.eqsql.trans.Rule;

/**
 * Created by ek on 6/11/16.
 * Transformation rule T1: Fold removal
 * Input:
 *      Fold (0)
 *          FuncExpr (1)
 *            MethodInv (2)
 *              PlaceholderVar (3)
 *              MethodInsert (4)
 *              FuncParams (5)
 *                  Cartesian (6)
*           Bottom (7)
 *          Any (8)
 * Output:
 *      Any (8)
 * Precondition: The tuple added, Cartesian(6), should come from the query, Any(8).
 * Since we use the query itself to represent the tuple, the precondition will
 * be that Cartesian(6) = Any(8) (i.e, Any(8) should match a Cartesaian that is the
 * same as Cartesian(6)). This precondition is currently assumed to be true always.
 *
 * //TODO: modify the trans rule where the output will be:
 * (The remaining comment may be outdated)
 *      Project
 *          Any (8)
 *          Any(6)
 *  This will ensure correctness in the case when only a particular column is added using
 *  MethodInsert. To account for the case of SELECT *, whereby the entire row (object)
 *  is added, we need to add a simplification rule as follows:
 *  Input:
 *      Project
 *          Select (1)
 *          Select (2)
 *  Output
 *      Select (1)
 */
public class RuleT1A extends Rule {
    private static InputTree makeInPattern() {
        InputTree phVar = new InputTree(OpType.PlaceholderVar, 3);
        InputTree insert = new InputTree(OpType.MethodInsert, 4);

        InputTree cart6 = new InputTree(OpType.CartesianProd, 6);
        InputTree funcParams = new InputTree(OpType.FuncParams, 5, cart6);

        InputTree methodInv = new InputTree(OpType.InvokeMethod, 2,
                phVar, insert, funcParams);


        InputTree funcExpr = new InputTree(OpType.FuncExpr, 1, methodInv);
        InputTree bottom = new InputTree(OpType.Bottom, 7);
        InputTree any8 = new InputTree(OpType.Any, 8);

        InputTree fold = new InputTree(OpType.Fold, 0, funcExpr, bottom, any8);

        return fold;
    }

    private static OutputTree makeOutPattern() {
        return new OutputTree(8);
    }

    public RuleT1A() {
        super(makeInPattern(), makeOutPattern());
    }
}
