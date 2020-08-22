package com.geetam.accesspath;

import soot.RefType;
import soot.SootField;
import soot.Type;
import soot.Value;
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
        for(SootField sf : varRefType.getSootClass().getFields()) {
            if(sf.getType() instanceof RefType) {
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
        return ret;
    }
}
