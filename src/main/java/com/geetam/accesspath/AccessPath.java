package com.geetam.accesspath;

import soot.RefType;
import soot.Type;

import java.util.ArrayDeque;
import java.util.Deque;


public class AccessPath {
    private Deque<String> path;

    AccessPath() {
        path = new ArrayDeque<>();
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
            case "java.lang.String":
            case "java.util.Collection":
            case "java.lang.Integer": {
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
}
