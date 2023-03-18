/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.CountStarOp;

/**
 * Created by K. Venkatesh Emani on 2/16/2017.
 */
public class  CountStarNode extends LeafNode implements HQLTranslatable {
    public CountStarNode(){
        super(new CountStarOp());
    }

    @Override
    public String toHibQuery() {
        return "count(*)";
    }
}
