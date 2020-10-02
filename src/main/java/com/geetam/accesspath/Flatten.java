package com.geetam.accesspath;

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

    public  List<AccessPath> flatten(Value var) {
        List <AccessPath> ret = new LinkedList<>();
        Type varType = var.getType();
        assert varType instanceof RefType : "varType not reftype";
        RefType varRefType = (RefType) varType;

        System.out.println("Flatten: LVAL TYPE: " + varRefType);
        if(varRefType.toString().equals("java.util.Optional")) {
        }
        for(SootField sf : varRefType.getSootClass().getFields()) {
            System.out.println("Type of sf: " + sf + " = " + sf.getType());
            if(AccessPath.isTerminalType(sf.getType()) == false) {
                JimpleLocal localForField = new JimpleLocal(sf.getName(), sf.getType());
                List <AccessPath> accessPathsFromSF = flatten(localForField);
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
        System.out.println("Flatten: returning: " + ret);
        return ret;
    }
}
