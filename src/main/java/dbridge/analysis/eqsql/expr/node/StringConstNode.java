package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.StringConstOp;

/**
 * Created by ek on 26/10/16.
 */
/*
TODO: Is a StringConstNode necessary? Cant we simply use a String?
 */
public class StringConstNode extends LeafNode implements HQLTranslatable {

    public StringConstNode(String _str) {
        super(new StringConstOp(_str));
    }

    public String getStr() {
        return ((StringConstOp)operator).getStr();
    }

    @Override
    public String toHibQuery() {
        return getStr();
    }
}
