/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import soot.Type;

/**
 * Created by K. Venkatesh Emani on 12/20/2016.
 * Represents a return variable.
 */
public class RetVarNode extends VarNode {
    /* The type of the actual variable (JimpleLocal) that is returned.
    * We need to store the actual variable type along with VarNode to perform any
    * necessary casting during rewrite. Refer <code>BodyRewriter.rewriteBody()<code>
    * for more details. */
    private Type origRetVarType;

    RetVarNode(Type _type) {
        super(VarNode.RETURN_VAR_NAME);
        origRetVarType = _type;
    }

    public Type getOrigRetVarType() {
        return origRetVarType;
    }

    /* Get a new return variable of the given type */
    public static RetVarNode getARetVar(Type _type){
        return new RetVarNode(_type);
    }

    /**
     * Get a new return variable with no type information
     */
    public static RetVarNode getARetVar(){
        return new RetVarNode(null);
    }

    /* Note regarding equals() method:
     * It is important that RetVarNode relies on the same equals method
     * as VarNode. We do not want to compare origRetVarType for checking
     * equality between two RetVarNodes */
    @Override
    public int hashCode() {
        return getVarName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarNode varNode = (VarNode) o;
        return getVarName().equals(varNode.getVarName());
//        return Objects.equals(jimpleVar, varNode.jimpleVar) &&
//                Objects.equals(specialVar, varNode.specialVar);
    }

}
