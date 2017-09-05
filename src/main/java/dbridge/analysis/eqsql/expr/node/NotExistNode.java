package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NotExistOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class NotExistNode extends Node implements HQLTranslatable {
    public NotExistNode(ProjectNode query) {
        super(new NotExistOp(), query);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String querySQL = ((HQLTranslatable) children[0]).toHibQuery();
        return "NOT EXISTS (" + querySQL + ")";
    }
}
