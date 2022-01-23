/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

/**
 * Created by ek on 28/10/16.
 * Node that represents a function being invoked.
 * (To be used for construction of InvokeMethodNode)
 */
public class MethodRefNode extends StringConstNode implements MethodRef {

    public MethodRefNode(String _str) {
        super(_str);
    }

    public String getMethodName(){
        return getStr();
    }
}
