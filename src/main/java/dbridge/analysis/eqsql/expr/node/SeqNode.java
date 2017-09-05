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
