package com.geetam.accesspath;

import com.geetam.OptionalTypeInfo;
import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import soot.*;
import soot.jimple.internal.JimpleLocal;

import java.util.LinkedList;
import java.util.List;

public class Flatten {
    int BOUND = 1;
    public Flatten(int bound) {
        this.BOUND = bound;
    }

    public static List<AccessPath> flatten(Value var, Type varType) {
        System.out.println("Flatten: var: " + var);
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        debug.dbg("Flatten.java", "flatten()", "LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            debug.dbg("Flatten.java", "flatten()", "optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        for(SootField sf : typeClass.getFields()) {
            debug.dbg("Flatten.java", "flatten()", "Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isTerminalType(sf.getType()) == false) {
                JimpleLocal localForField = new JimpleLocal(sf.getName(), sf.getType());
                List <AccessPath> accessPathsFromSF = flatten(localForField, sf.getType());
                for(AccessPath ap : accessPathsFromSF) {
                    ap.getPath().addFirst(var.toString());
                }
            ret.addAll(accessPathsFromSF);
            }
            else {
                AccessPath ap = new AccessPath();
                ap.getPath().add(var.toString());
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
        }
        debug.dbg("Flatten.java", "flatten()", "returning: " + ret);
        return ret;
    }
    //Only returns accesspaths of length 1, ending at a primitive
    public static List <AccessPath> flattenEntity(Value var, Type varType) {
        debug.dbg("Flatten.java", "flattenEntity()", "var: " + var);
        List <AccessPath> ret = new LinkedList<>();
        //Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        debug.dbg("Flatten.java", "flattenEntity()", "LVAL TYPE: " + varRefType);
        SootClass typeClass = varRefType.getSootClass();
        if(varRefType.toString().equals("java.util.Optional")) {
            String actualType = OptionalTypeInfo.typeMap.get(var.toString());
            debug.dbg("Flatten.java", "flattenEntity()","optional vars actual type: " + actualType);
            typeClass = Scene.v().getSootClass(actualType);
        }
        for(SootField sf : typeClass.getFields()) {
            debug.dbg("Flatten.java", "flattenEntity()","Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isPrimitiveType(sf.getType())) {
                AccessPath ap = new AccessPath();
                ap.getPath().add(var.toString());
                ap.getPath().add(sf.getName());
                ret.add(ap);
            }
        }
        debug.dbg("Flatten.java", "flattenEntity()","returning: " + ret);
        return ret;

    }

    public static List <String> flattenEntityClass(SootClass typeClass) {
        debug d = new debug("Flatten.java", "flattenEntityClass()");
        List <String> ret = new LinkedList<>();
        d.dg("typeClass.getFields() = " + typeClass.getFields());
        for(SootField sf : typeClass.getFields()) {
            debug.dbg("Flatten.java", "flattenEntityClass()","Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isPrimitiveType(sf.getType())) {
                ret.add(sf.getName());
            }
        }
        debug.dbg("Flatten.java", "flattenEntityClass()","returning: " + ret);
        return ret;
    }

    public static List <String> attributes(List <AccessPath> pathList) {
        List <String> ret = new LinkedList<>();
        for(AccessPath ap : pathList) {
            ret.add(ap.getPath().getLast());
        }
        return ret;
    }
}
