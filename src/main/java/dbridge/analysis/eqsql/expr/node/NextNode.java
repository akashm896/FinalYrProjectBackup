/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Not part of base DBridge
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NextOp;

/**
 * Created by ek on 26/10/16.
 */
public class NextNode extends LeafNode implements HQLTranslatable {
    public NextNode() {
        super(new NextOp());
    }

    @Override
    public String toHibQuery() {
        return "Iterator";
    }
}
