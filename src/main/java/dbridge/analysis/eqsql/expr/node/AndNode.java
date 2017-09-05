package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.AndOp;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class AndNode extends Node implements HQLTranslatable {
    public AndNode(Node cond1, Node cond2)  {
        super(new AndOp(), cond1, cond2);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String cond1SQL = ((HQLTranslatable) children[0]).toHibQuery();
        String cond2SQL = ((HQLTranslatable) children[1]).toHibQuery();
        return cond1SQL + " AND " + cond2SQL;
    }
}
