/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.BottomOp;

/**
 * Created by ek on 26/10/16.
 */
public class BottomNode extends LeafNode {
    public static int counter = 0;
    public int thiscount;
    private static BottomNode v;
    /**
     * Constructor does not use super() because it is a special kind of Node,
     * which has no children.
     */
    public BottomNode() {
        super(new BottomOp());
        thiscount = counter++;
    }

    public static boolean isBottom(Object o){
        return (o != null)
            && (o instanceof BottomNode);
    }

    public static BottomNode v(){
        if(v == null){
            v = new BottomNode();
        }
        return v;
    }


    @Override
    public String toString() {
        return "BottomNode";
    }
}
