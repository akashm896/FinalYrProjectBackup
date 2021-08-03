package io.geetam.github.accesspath;

import io.geetam.github.OptionalTypeInfo;
import mytest.debug;
import soot.*;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JimpleLocal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.isAStarToOneField;

public class Flatten {
    public static int BOUND = 0;

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
        // Get list of reachable methods declared in this class
        // Also get list of fields declared in this class
        List<SootField> allFields = new ArrayList<SootField>();
        for (SootField field : sc.getFields()) {
            allFields.add(field);
        }

        // Add reachable methods and fields declared in superclasses
        SootClass superclass = sc;
        if (superclass.hasSuperclass()) {
            superclass = superclass.getSuperclass();
        }
        while (superclass.hasSuperclass()) // we don't want to process Object
        {
            for (SootField scField : superclass.getFields()) {
                allFields.add(scField);
            }
            superclass = superclass.getSuperclass();
        }
        return allFields;
    }

}
