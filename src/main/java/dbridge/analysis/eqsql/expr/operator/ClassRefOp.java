/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 17/10/16.
 */
public class ClassRefOp extends Operator {

    /** Name of mapped entity */
    public String className;

    public ClassRefOp(String className) {
        super("ClassRef", OpType.ClassRef, 0);
        /* name of class will be stored as attribute of operator, so no operands */
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String toString(){
        return getName() + "(" + className + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClassRefOp that = (ClassRefOp) o;

        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + className.hashCode();
        return result;
    }
}
