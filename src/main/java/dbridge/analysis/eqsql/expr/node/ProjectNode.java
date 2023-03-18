/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ProjectOp;
import dbridge.analysis.eqsql.expr.operator.SelectOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class ProjectNode extends Node implements HQLTranslatable{
    /**
     * @param relation Any node that represents the result of a query (directly or indirectly)
     * @param projEl The element to be projected
     */
    public ProjectNode(Node relation, Node projEl) {
        super(new ProjectOp(), relation, projEl);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String relationHQL = ((HQLTranslatable) children[0]).toHibQuery();
        String projElHQL = ((HQLTranslatable) children[1]).toHibQuery();

        return "(select " + projElHQL + " " + relationHQL + ")";
        /* Note: "from" keyword is already part of relationHQL */
    }
}
