/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import soot.Value;
import soot.jimple.IntConstant;

public class SQLSelectValueNode extends ValueNode {
    SQLSelectValueNode(Value val) {
        super(val);
    }
    public String attribute;
    public String tableName;

    public SQLSelectValueNode(Value val, String attribute, String tableName) {
        super(val);
        this.attribute = attribute;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "SELECT " + attribute + " FROM " + tableName;
    }
}
