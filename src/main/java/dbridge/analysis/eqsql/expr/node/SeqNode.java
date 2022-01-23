/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.SeqOp;
import exceptions.HQLTranslationException;

/**
 * Created by venkatesh on 13/7/17.
 */
public class SeqNode extends Node implements HQLTranslatable{
    public SeqNode(Node predecessor, Node follower) {
        super(new SeqOp(), predecessor, follower);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        /* As of now, we are translating it into a union query. But it
         * should be a sequential region. */
        return ((HQLTranslatable)children[0]).toHibQuery()
                + " union " +
                ((HQLTranslatable)children[1]).toHibQuery();
    }
}
