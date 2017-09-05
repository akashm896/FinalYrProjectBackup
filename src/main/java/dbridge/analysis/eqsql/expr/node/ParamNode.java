package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ParamOp;

/**
 * Created by ek on 27/10/16.
 * Node representing a formal parameter
 */
public class ParamNode extends LeafNode implements HQLTranslatable {

    public ParamNode(int _paramNumber){
        super(new ParamOp(_paramNumber));
    }

    @Override
    public String toString() {
        return "Param" + ((ParamOp)operator).getIndex();
    }

    @Override
    public String toHibQuery() {
        return ":" + toString();
        /* */
    }
}
