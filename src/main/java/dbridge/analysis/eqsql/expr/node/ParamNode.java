/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ParamOp;

/**
 * Created by ek on 27/10/16.
 * Node representing a formal parameter
 */
public class ParamNode extends LeafNode implements HQLTranslatable {

    public ParamNode(int _paramNumber){
        super(new ParamOp(_paramNumber));
    }

    @Override
    public String toString() {
        return "Param" + ((ParamOp)operator).getIndex();
    }

    @Override
    public String toHibQuery() {
        return ":" + toString();
        /* */
    }
}
