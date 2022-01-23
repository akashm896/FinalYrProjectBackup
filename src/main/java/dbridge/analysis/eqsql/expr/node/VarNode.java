/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.VarOp;
import soot.Type;
import soot.Value;
import soot.jimple.internal.JimpleLocal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ek on 18/10/16.
 * Representation of a variable in the DIR.
 */
public class VarNode extends LeafNode implements Comparable<VarNode>, HQLTranslatable {
    protected static final String COND_VAR_NAME = "condition";
    protected static final String RETURN_VAR_NAME = "return";

    public Value jimpleVar;
    /** A custom variable for special purposes such as "condition", "return" etc. */
    private String specialVar;
    public Type repoType;

    /* Intentionally package local. This constructor can be accessed indirectly through NodeFactory.v() */
    public VarNode(JimpleLocal _var) {
        super(new VarOp());
        jimpleVar = _var;
        specialVar = null;
        repoType = null;
    }
    public VarNode(Value _var) {
        super(new VarOp());
        jimpleVar = _var;
        specialVar = null;
        repoType = null;
    }



    /**
     * Constructor to be used for special purpose variables, such as
     * "condition" and "return". Do not use this if you do not know
     * what it means.
     */
    public VarNode(String varStr) {
        super(new VarOp());
        jimpleVar = null;
        specialVar = varStr;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        VarNode varNode = (VarNode) o;
//
//        if (jimpleVar != null ? !jimpleVar.equals(varNode.jimpleVar) : varNode.jimpleVar != null) return false;
//        return specialVar != null ? specialVar.equals(varNode.specialVar) : varNode.specialVar == null;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        VarNode varNode = (VarNode) o;
//
//        if (jimpleVar != null) {
//            return jimpleVar.equals(varNode.jimpleVar);
//        }
//        if(specialVar != null) {
//            return specialVar.equals(varNode.specialVar);
//        }
//        return false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarNode varNode = (VarNode) o;
        return getVarName().equals(varNode.getVarName());
//        return Objects.equals(jimpleVar, varNode.jimpleVar) &&
//                Objects.equals(specialVar, varNode.specialVar);
    }

    @Override
    public int hashCode() {
        return getVarName().hashCode();
    }
//    }

//    @Override
//    public int hashCode() {
//        int result = jimpleVar != null ? jimpleVar.hashCode() : 0;
//        result = 31 * result + (specialVar != null ? specialVar.hashCode() : 0);
//        return result;
//    }

    @Override
    public String toString() {
        if(jimpleVar != null){
            return jimpleVar.toString();
        }
        return specialVar;
    }

    /**
     * Note that a new VarNode is created and returned in each invocation.
     * //TODO Can we use a single instance of a condition VarNode?
     */
    public static VarNode getACondVar() {
        return new VarNode(COND_VAR_NAME);
    }

    public boolean isJimpleVar(){
        return jimpleVar != null;
    }

    public String getVarName(){
        if(this.isJimpleVar()){
            return jimpleVar.toString();
        }
        return specialVar;
    }

    /**
     * Base case for readSet()
     */
    @Override
    public Set<VarNode> readSet() {
        Set<VarNode> rs = new HashSet<>();
        if(this.isJimpleVar()){
            rs.add(this);
        }
        return rs;
    }

    /**
     * This method is only so that the VarNode keys of DIR can be sorted and printed
     * in an order. No other significance.
     */
    @Override
    public int compareTo(VarNode varNode) {
        return this.getVarName().compareTo(varNode.getVarName());
    }

    @Override
    public String toHibQuery() {
        return toString();
    }

    public Boolean isCondVar() {
        return toString().equals(COND_VAR_NAME) && jimpleVar == null;
    }
}
