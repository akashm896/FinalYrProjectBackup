/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by venkatesh on 7/7/17.
 */
public class CartesianProdOp extends Operator {

    /**@param arity Number of relations in the cartesian product.
     If arity = 1, then it is a single relation. */
    public CartesianProdOp(int arity) {
        super("Cartesian", OpType.CartesianProd, arity);
    }
}
