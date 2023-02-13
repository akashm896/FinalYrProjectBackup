/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

/**
 * Created by ek on 24/10/16.
 * Node to represent unalgebrizable expressions.
 */
public class UnAlgNode extends LeafNode {
    private UnAlgNode() {
        super(null);
    }

    public static UnAlgNode v() {
        return new UnAlgNode();
    }

    public static boolean isUnAlgNode(Object other){
        return other instanceof UnAlgNode;
    }

    @Override
    public String toString() {
        return "UnAlg";
    }
}
