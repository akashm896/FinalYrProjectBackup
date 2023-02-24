/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

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
