/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.util;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.MethodRefNode;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.RetVarNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.visitor.NodeVisitor;

import java.util.HashMap;

/**
 * Created by ek on 28/10/16.
 * Resolve reference to function with dag of its return value
 */
public class FuncResolver implements NodeVisitor {

    private HashMap<String, DIR> funcDirMap;

    public FuncResolver(HashMap<String, DIR> funcDirMap) {
        this.funcDirMap = funcDirMap;
    }

    /** Resolve reference to function with dag of its return value */
    @Override
    public Node visit(Node node) {
        if(node instanceof MethodRefNode){
            String methodName = ((MethodRefNode) node).getMethodName();
            assert funcDirMap.containsKey(methodName);
            DIR methodDir = funcDirMap.get(methodName);

            VarNode retVar = RetVarNode.getARetVar();
            assert methodDir.contains(retVar);
            Node methodRetDag = methodDir.find(retVar);

            /* Note that methodRetDag may have references to other methods, hence
            * FuncResolver needs to be */
            return methodRetDag.accept(this);
        }
        return node;
    }
}
