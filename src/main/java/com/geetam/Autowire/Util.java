package com.geetam.Autowire;

import soot.Type;
import soot.Value;

public class Util {
    public static boolean isService(Value val) {
        String valStr = val.toString();
        String valTypeStr = val.getType().toString();
        if(valStr.startsWith("this") && valTypeStr.toLowerCase().endsWith("service")) {
            return true;
        }
        else return false;
    }
}
