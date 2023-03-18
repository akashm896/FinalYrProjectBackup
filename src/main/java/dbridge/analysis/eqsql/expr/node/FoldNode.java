/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.FoldOp;

/**
 * Created by ek on 24/10/16.
 */
public class FoldNode extends Node {

    /**
     * @param funcExpr A parameterized expression representing the folding function
     * @param initVal Initial value of the variable which is being aggregated
     * @param loopCol Collection over which the loop is iterating (could be a query or a collection variable)
     */
    public FoldNode(Node funcExpr, Node initVal, Node loopCol, Node iterator){
        super(new FoldOp(), funcExpr, initVal, loopCol, iterator);
    }

    private static FuncExprNode makeFuncExprNode(Node funcExpr, VarNode aggVar, VarNode loopCol) {
        FuncExprNode funcExprNode = new FuncExprNode(funcExpr, aggVar, loopCol);
        return funcExprNode;
    }

    /**
     * Convenience constructor
     * @param funcExpr Expression (DIR.dag) representing the folding function
     * @param aggVar The variable which is being aggregated
     * @param loopCol Collection variable over which the loop is iterating
     */
    public FoldNode(Node funcExpr, VarNode aggVar, VarNode loopCol, Node iterator) {
        this(makeFuncExprNode(funcExpr, aggVar, loopCol), (Node)aggVar, (Node)loopCol, iterator);
    }
}
