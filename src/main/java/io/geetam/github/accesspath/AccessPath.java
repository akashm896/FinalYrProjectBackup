package io.geetam.github.accesspath;

import dbridge.analysis.eqsql.expr.node.VarNode;
import soot.RefType;
import soot.Type;

import java.util.ArrayDeque;
import java.util.Deque;


public class AccessPath {
    private Deque<String> path;

    AccessPath() {
        path = new ArrayDeque<>();
    }
    AccessPath(AccessPath ap) {
        path = new ArrayDeque<>();
        path.addAll(ap.getPath());
    }

    public String getBase() {
        return path.peek();
    }

    public Deque<String> getPath() {
        return path;
    }

    public static boolean isTerminalType(Type type) {
        String typeStr = type.toString();
        switch (typeStr) {
            case "java.lang.Long":
            case "java.time.LocalDate":
            case "java.lang.String":
            case "java.util.Collection":
            case "java.lang.StringBuilder":
            case "java.lang.Integer":
            case "java.lang.Iterable":
            case "java.util.List":
            case "org.springframework.data.domain.Page": {
                return true;
            }

            default:
                return type instanceof RefType == false;
        }
    }

    public static boolean isPrimitiveType(Type type) {
        String typeStr = type.toString();
        switch (typeStr) {
            case "java.lang.Long":
            case "java.time.LocalDate":
            case "java.lang.String":
            case "java.lang.Integer": {
                return true;
            }

            default:
                return type instanceof RefType == false;
        }
    }


    //Number of dots in the access path
    int getLength() {
        return path.size() - 1;
    }

    @Override
    public String toString() {
        StringBuilder retBuilder = new StringBuilder();
        for(String sub : path) {
            retBuilder.append(sub);
            if(sub != path.getLast()) {
                retBuilder.append(".");
            }
        }
        return retBuilder.toString();
    }

    public static AccessPath replaceBase(AccessPath acp, String toReplaceWith) {
        AccessPath newAccp = new AccessPath(acp);
        newAccp.getPath().removeFirst();
        newAccp.getPath().addFirst(toReplaceWith);
        return newAccp;
    }

    public VarNode toVarNode() {
        return new VarNode(toString());
    }
}
