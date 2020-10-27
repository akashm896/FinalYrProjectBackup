package com.geetam.accesspath;

import com.geetam.OptionalTypeInfo;
import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import soot.*;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JimpleLocal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Flatten {
    int BOUND = 1;
    public Flatten(int bound) {
        this.BOUND = bound;
    }

    public static List<AccessPath> flatten(Value var, Type varType) {
        debug d = new debug("Flatten.java", "flatten()");
        d.turnOff();
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        d.dg("LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            d.dg("optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        for(SootField sf : typeClass.getFields()) {
            d.dg("Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isTerminalType(sf.getType()) == false) {
                JimpleLocal localForField = new JimpleLocal(sf.getName(), sf.getType());
                List <AccessPath> accessPathsFromSF = flatten(localForField, sf.getType());
                for(AccessPath ap : accessPathsFromSF) {
                    if(var instanceof JInstanceFieldRef) {
                        d.dg("var instance of fieldref");
                        JInstanceFieldRef fieldRef = (JInstanceFieldRef) var;
                        d.dg("fieldtostring: " + fieldRef.getField().toString());
                        ap.getPath().addFirst(fieldRef.getBase().toString() + "." + fieldRef.getField().getName());
                    }
                    else {
                        ap.getPath().addFirst(var.toString());
                    }
                }
            ret.addAll(accessPathsFromSF);
            }
            else {
                AccessPath ap = new AccessPath();
                if(var instanceof JInstanceFieldRef) {
                    d.dg("var instance of fieldref");
                    JInstanceFieldRef fieldRef = (JInstanceFieldRef) var;
                    d.dg("fieldtostring: " + fieldRef.getField().toString());
                    ap.getPath().addFirst(fieldRef.getBase().toString() + "." + fieldRef.getField().getName());
                }
                else {
                    ap.getPath().addFirst(var.toString());
                }
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
        }
        d.dg("returning: " + ret);
        return ret;
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
        for(SootField sf : typeClass.getFields()) {
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
