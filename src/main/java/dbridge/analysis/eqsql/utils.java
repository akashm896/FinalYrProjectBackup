/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql;

import dbridge.analysis.eqsql.expr.node.NodeFactory;
import dbridge.analysis.eqsql.expr.node.SQLSelectValueNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import jas.Var;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Value;
import soot.jimple.internal.JInstanceFieldRef;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class utils {
    public static Map<String, VarNode> fieldAccessStringExprToVarNode = new HashMap<>();

    public static List<VarNode> getVarNodeFieldAccessListOfBaseVar(Value baseVar) {
        List<VarNode> ret = new LinkedList<>();
        SootClass baseVarClass = Scene.v().loadClass(baseVar.getType().toString(), 1);
        for (SootField sootField : baseVarClass.getFields()) {
            ret.add(getFieldAccessVarNode(baseVar, sootField));
        }
        return ret;
    }

    public static VarNode getFieldAccessVarNode(Value baseVar, SootField sootField) {
        JInstanceFieldRef fieldRef = new JInstanceFieldRef(baseVar, sootField.makeRef());
        String fieldAcc = baseVar.toString() + "." + fieldRef.toString();
        if(!fieldAccessStringExprToVarNode.containsKey(fieldAcc)) {
            VarNode varNode = (VarNode) NodeFactory.constructFromValue2(fieldRef);
            fieldAccessStringExprToVarNode.put(fieldAcc, varNode);
        }
        return fieldAccessStringExprToVarNode.get(fieldAcc);
    }


}
