/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NotEqOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class NotEqNode extends Node implements HQLTranslatable {
    public NotEqNode(Node lhs, Node rhs) {
        super(new NotEqOp(), lhs, rhs);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String lhsSQL = ((HQLTranslatable) children[0]).toHibQuery();

        Node rhs = children[1];
        if(rhs instanceof ValueNode && ((ValueNode)rhs).isNull()){
            return lhsSQL + " is not null";
        }
        else{
            String rhsSQL = ((HQLTranslatable) rhs).toHibQuery();
            return lhsSQL + " <> " + rhsSQL;
        }
    }
}
