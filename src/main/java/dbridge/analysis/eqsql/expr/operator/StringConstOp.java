/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 24/10/16.
 */
public class StringConstOp extends Operator {

    private String str;

    public StringConstOp(String _str) {
        super("StringConst", OpType.StringConst, 0);
        /* 0 operands. The corresponding string will be an attribute of StringConst*/
        str = _str;
    }

    public String getStr() {
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StringConstOp that = (StringConstOp) o;

        return str.equals(that.str);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + str.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getName() + "(" + str + ")";
    }
}
