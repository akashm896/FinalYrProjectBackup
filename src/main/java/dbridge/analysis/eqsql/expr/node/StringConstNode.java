/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.StringConstOp;

/**
 * Created by ek on 26/10/16.
 */
/*
TODO: Is a StringConstNode necessary? Cant we simply use a String?
 */
public class StringConstNode extends LeafNode implements HQLTranslatable {

    public StringConstNode(String _str) {
        super(new StringConstOp(_str));
    }

    public String getStr() {
        return ((StringConstOp)operator).getStr();
    }

    @Override
    public String toHibQuery() {
        return getStr();
    }
}
