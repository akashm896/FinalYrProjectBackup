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
