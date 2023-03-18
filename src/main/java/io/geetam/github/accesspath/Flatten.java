/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
package io.geetam.github.accesspath;

import dbridge.analysis.eqsql.hibernate.construct.Utils;
import io.geetam.github.OptionalTypeInfo;
import mytest.debug;
import soot.*;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.AnnotationTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.isAStarToOneField;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.isStarToManyField;

public class Flatten {
    //max number of dots in accp = BOUND + 1
    public static int MAX_LEN_ACCP = 3;
    public static int BOUND = MAX_LEN_ACCP - 1;

    public static List<AccessPath> flatten(Value var, Type varType, int depth) {
        debug d = new debug("Flatten.java", "flatten()");
        if(depth > BOUND) {
            d.wrn("depth > BOUND");
            return new LinkedList<>();
        }

        d.turnOff();
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        d.dg("LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            d.dg("OptionalTypeInfo.typeMap: " + OptionalTypeInfo.typeMap);
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            d.dg("optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        List<SootField> allFields = getAllFields(typeClass);
        for(SootField sf : allFields) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            if(isAStarToOneField(sf)) {
                AccessPath ap = new AccessPath();
                prependBaseToAccp(var, ap);
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
            if(AccessPath.isTerminalType(sf.getType()) == false) {
                JimpleLocal localForField = new JimpleLocal(sf.getName(), sf.getType());
                List <AccessPath> accessPathsFromSF = flatten(localForField, sf.getType(), depth + 1);
                for(AccessPath ap : accessPathsFromSF) {
                    prependBaseToAccp(var, ap);
                }
            ret.addAll(accessPathsFromSF);
            }
            else {
                AccessPath ap = new AccessPath();
                prependBaseToAccp(var, ap);
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
        }
        d.dg("returning: " + ret);
        return ret;
    }

    public static void prependBaseToAccp(Value base, AccessPath ap) {
        debug d = new debug("Flatten.java", "prependBaseToAccp");
        if (base instanceof JInstanceFieldRef) {
            d.dg("var instance of fieldref");
            JInstanceFieldRef fieldRef = (JInstanceFieldRef) base;
            d.dg("fieldtostring: " + fieldRef.getField().toString());
            ap.getPath().addFirst(fieldRef.getBase().toString() + "." + fieldRef.getField().getName());
        } else {
            ap.getPath().addFirst(base.toString());
        }
    }

    //Only returns accesspaths of length 1, ending at a primitive
    public static List <AccessPath> flattenEntity(Value var, Type varType) {
        debug d = new debug("Flatten.java", "flattenEntity()");
        d.turnOff();
        d.dg("var: " + var);
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

       d.dg( "LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            d.dg("optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        //Comment shows old behaviour, we should actually get all fields.
       // Collection <SootField> fields  = typeClass.getFields();
        for(SootField sf : getAllFields(typeClass)) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isPrimitiveType(sf.getType())) {
                AccessPath ap = new AccessPath();
                ap.getPath().add(var.toString());
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
        }
        d.dg("returning: " + ret);
        return ret;

    }

    public static List <String> flattenEntityClass(SootClass typeClass) {
        debug d = new debug("Flatten.java", "flattenEntityClass()");
        d.turnOff();
        List <String> ret = new LinkedList<>();
        List <SootField> allFields = getAllFields(typeClass);
        d.dg("typeClass.getFields() = " + allFields);
        for(SootField sf : allFields) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isPrimitiveType(sf.getType())) {
                ret.add(sf.getName());
            }
        }
        d.dg("returning: " + ret);
        return ret;
    }

    public static List <String> attributes(List <AccessPath> pathList) {
        List <String> ret = new LinkedList<>();
        for(AccessPath ap : pathList) {
            ret.add(ap.getPath().getLast());
        }
        return ret;
    }

    //Myimpl

    public static AccessPath getAccp(Value var,SootField sf){
        AccessPath ap= new AccessPath();
        prependBaseToAccp(var, ap);
        ap.getPath().add(sf.getName());
        return ap;
    }


    public static List<AccessPath> nested_Accp(Value var, Type varType) {
        debug d = new debug("Flatten.java", "nested_Accp()");
//        d.turnOff();
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        d.dg("LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            d.dg("OptionalTypeInfo.typeMap: " + OptionalTypeInfo.typeMap);
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            d.dg("optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        List<SootField> allFields = getAllFields(typeClass);

        for(SootField sf : allFields) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            if(isStarToManyField(sf)) {
                d.dg("check Manytomany sf: " + sf.getName() + " , type = " + sf.getType());
                AccessPath ap = new AccessPath();
                prependBaseToAccp(var, ap);
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
//            }
        }
        d.dg("returning: " + ret);
        return ret;
    }

    public static List<SootField> getNestedFields(Value var, Type varType) {
        debug d = new debug("Flatten.java", "getNestedFields()");
//        d.turnOff();
        List <SootField> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        d.dg("LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            d.dg("OptionalTypeInfo.typeMap: " + OptionalTypeInfo.typeMap);
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            d.dg("optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        List<SootField> allFields = getAllFields(typeClass);

        for(SootField sf : allFields) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            d.dg("tags= "+ sf.getTags());
//            d.dg("annotation type= "+ Utils.getAnnotationTags(sf.getTags()));
            List<AnnotationTag> annTags= Utils.getAnnotationTags(sf.getTags());
            for(AnnotationTag tg:annTags){
                if(tg.getType().toString().equals("Ljavax/persistence/Id;")){
                    d.dg("Id fields="+sf.getName());
                }
            }
            if(isStarToManyField(sf)) {
//                JimpleLocal local=new JimpleLocal(sf.getName(),sf.getType());
                d.dg("check *ToMany sf: " + sf.getName() + " , type = " + sf.getType());
                ret.add(sf);
            }

            if(isAStarToOneField(sf)) {
//                JimpleLocal local=new JimpleLocal(sf.getName(),sf.getType());
                d.dg("check *ToOne sf: " + sf.getName() + " , type = " + sf.getType());
                ret.add(sf);
            }
//            }
        }
        d.dg("returning: " + ret);
        return ret;
    }


    //////// Myimpl end ////


    /*-
     * #%L
     * Soot - a J*va Optimization Framework
     * %%
     * Copyright (C) 1997 - 2018 Raja Vallée-Rai and others
     * %%
     * This program is free software: you can redistribute it and/or modify
     * it under the terms of the GNU Lesser General Public License as
     * published by the Free Software Foundation, either version 2.1 of the
     * License, or (at your option) any later version.
     *
     * This program is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Lesser Public License for more details.
     *
     * You should have received a copy of the GNU General Lesser Public
     * License along with this program.  If not, see
     * <http://www.gnu.org/licenses/lgpl-2.1.html>.
     * #L%
     */
    //copied from soot
    // Returns a list of fields in class sc and its superclasses
    public static List<SootField> getAllFields(SootClass sc) {
        debug d =new debug("Flatten.java","getAllField()");
        // Get list of reachable methods declared in this class
        // Also get list of fields declared in this class
        List<SootField> allFields = new ArrayList<SootField>();
        for (SootField field : sc.getFields()) {
            allFields.add(field);
        }

        // Add reachable methods and fields declared in superclasses
        SootClass superclass = sc;
//        d.dg("superclass = "+superclass);
        if (superclass.hasSuperclass()) {
            superclass = superclass.getSuperclass();
        }
        while (superclass.hasSuperclass()) // we don't want to process Object
        {
            for (SootField scField : superclass.getFields()) {
                allFields.add(scField);
            }
            superclass = superclass.getSuperclass();
//            d.dg("superclass = "+superclass);
        }
        return allFields;
    }



}
