package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.FieldRefOp;
import dbridge.common.ClassToAliasMapper;
import exceptions.HQLTranslationException;

/**
 * Created by ek on 17/10/16.
 */
public class FieldRefNode extends LeafNode implements HQLTranslatable {

    public FieldRefNode(String baseClass, String fieldName, String typeClass){
        super(new FieldRefOp(baseClass, fieldName, typeClass));
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String baseClass = ((FieldRefOp)operator).getBaseClass();
        String fieldName = ((FieldRefOp)operator).getFieldName();
        String classAlias = ClassToAliasMapper.getAlias(baseClass);

        return classAlias + "." + fieldName;
    }

    public ClassRefNode getTypeClassRef() throws HQLTranslationException {
        return new ClassRefNode(((FieldRefOp) operator).getTypeClass());
    }
}
