/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.SelectOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class SelectNode extends Node implements HQLTranslatable{

    /**
     * @param relation A node representing a query/relation
     * @param condition The selection condition
     */
    public SelectNode(Node relation, Node condition) {
        super(new SelectOp(), relation, condition);
    }

    private Node getWhereCond(){
        return children[1];
    }

    private Node getRelation(){
        return children[0];
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String hibQuery = ((HQLTranslatable)getRelation()).toHibQuery();
        String whereHQL = "where " +
                ((HQLTranslatable)getWhereCond()).toHibQuery();
        hibQuery = hibQuery + " " + whereHQL;

        return hibQuery;
    }
}
