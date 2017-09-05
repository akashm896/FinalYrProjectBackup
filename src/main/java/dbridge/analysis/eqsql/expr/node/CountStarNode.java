package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.CountStarOp;

/**
 * Created by K. Venkatesh Emani on 2/16/2017.
 */
public class  CountStarNode extends LeafNode implements HQLTranslatable {
    public CountStarNode(){
        super(new CountStarOp());
    }

    @Override
    public String toHibQuery() {
        return "count(*)";
    }
}
