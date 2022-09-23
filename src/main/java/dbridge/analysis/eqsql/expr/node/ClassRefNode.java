/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ClassRefOp;
import dbridge.common.ClassToAliasMapper;
import exceptions.HQLTranslationException;
import soot.jimple.ClassConstant;

/**
 * Created by ek on 17/10/16.
 */
public class ClassRefNode extends LeafNode implements HQLTranslatable {
    /**
     * Constructor
     */
    public ClassRefNode(String className) {
        super(new ClassRefOp(className));
    }

    /**
     * Constructor to create a ClassRefNode from soot.ClassConstant
     */
    public ClassRefNode(ClassConstant classConstant){
        this(getClassName(classConstant));
    }

    /**
     * The class name is contained within ClassConstant as a String of the form
     * x/y/z/MyClass for the class MyClass in package x.y.z
     */

    public static String getClassName(ClassConstant classConstant) {
        String[] split = classConstant.getValue().split("/");
        String className = split[split.length - 1];
        return className;
    }


    @Override
    public String toHibQuery() throws HQLTranslationException {
        return ((ClassRefOp)operator).getClassName();
    }

    public String getAlias(){
        String className =  ((ClassRefOp)operator).getClassName();
        return ClassToAliasMapper.getAlias(className);
    }
}
