package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.MethodGetHibernateTemplateOp;

/**
 * Created by K. Venkatesh Emani on 1/6/2017.
 */
public class MethodGetHibernateTemplateNode extends LeafNode implements MethodRef {
    public MethodGetHibernateTemplateNode() {
        super(new MethodGetHibernateTemplateOp());
    }
}
