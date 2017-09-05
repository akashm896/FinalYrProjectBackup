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
    protected ProjectNode(Node relation, Node projEl) {
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
