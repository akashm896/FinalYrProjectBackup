/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.GtOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class GtNode extends Node implements HQLTranslatable {
    public GtNode(Node lhs, Node rhs)  {
        super(new GtOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();
        String rhsSQL = ((HQLTranslatable) children[1]).toHibQuery();
        return lhsSQL + " > " + rhsSQL;
    }
}
