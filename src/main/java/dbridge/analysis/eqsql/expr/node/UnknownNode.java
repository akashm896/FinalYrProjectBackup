/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//based on BottomNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.UnknownOp;

public class UnknownNode extends LeafNode {

    private static UnknownNode v;
    /**
     * Constructor does not use super() because it is a special kind of Node,
     * which has no children.
     */
    public UnknownNode() {
        super(new UnknownOp());
    }

    public static boolean isBottom(Object o){
        return (o != null)
            && (o instanceof UnknownNode);
    }

    public static UnknownNode v(){
        if(v == null){
            v = new UnknownNode();
        }
        return v;
    }

    @Override
    public String toString() {
        return "UnknownNode";
    }
}
