package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ExistOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class ExistNode extends Node implements HQLTranslatable {
    public ExistNode(ProjectNode query) {
        super(new ExistOp(), query);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String querySQL = ((HQLTranslatable) children[0]).toHibQuery();
        return "EXISTS (" + querySQL + ")";
    }
}
