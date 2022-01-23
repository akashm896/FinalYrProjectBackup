/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.MethodWontHandleOp;

import java.util.Objects;

public class MethodWontHandleNode extends LeafNode implements HQLTranslatable {
    public String callSiteStr;

    public MethodWontHandleNode(String callSiteStr) {
        super(new MethodWontHandleOp());
        this.callSiteStr = callSiteStr;
    }

    @Override
    public String toHibQuery() {
        return "MethodWontHandle";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodWontHandleNode that = (MethodWontHandleNode) o;
        return callSiteStr.equals(that.callSiteStr);
    }

    @Override
    public int hashCode() {
        return callSiteStr.hashCode();
    }
}
