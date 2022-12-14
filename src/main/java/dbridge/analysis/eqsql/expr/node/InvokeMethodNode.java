/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.InvokeMethodOp;
import dbridge.analysis.eqsql.util.PrettyPrinter;

/**
 * Created by ek on 17/10/16.
 */
public class InvokeMethodNode extends Node {
    public InvokeMethodNode(Node baseObj, MethodRef methodNode, FuncParamsNode params)  {
        super(new InvokeMethodOp(), baseObj, (Node)methodNode, params);
    }

    /** Convenience constructor */
    public InvokeMethodNode(Node baseObj, String methodName, FuncParamsNode params)  {
        super(new InvokeMethodOp(), baseObj, new StringConstNode(methodName), params);
    }

    /** Convenience constructor */
    public InvokeMethodNode(Node baseObj, Node methodNode)  {
        super(new InvokeMethodOp(), baseObj, methodNode, FuncParamsNode.getEmptyParams());
    }

    /**
     * When there are no parameters, we do not want the toString to return an empty ParamsNode object. So we discard
     * the ParamsNode when it is empty.
     * @return
     */
    @Override
    public String toString(){
        String prettyString = operator.toString();
        for (Node child : children) {
            /* Check if a ParamsNode is empty. */
            if(child instanceof FuncParamsNode
                    && ((FuncParamsNode)child).isEmpty() ) {
                continue;
            }

            String childStr = (child == null) ? "Null" : child.toString();
            childStr = PrettyPrinter.doIndent(childStr);
            prettyString = prettyString + "\n" +
                    childStr;
        }

        return prettyString;
    }
}
