package io.geetam.github;

import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import soot.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptionalTypeInfo {
    public static Map<String, String> typeMap;

    public static Map <String, String> analyzeBCEL(String funcSignature)  {
        debug d = new debug("OptionalTypeInfo.java", "analyzeBCEL()");
        d.dg("Function to analyze for actual types of optional-typed variables: " + funcSignature);
        d.turnOff();
        Map<String, String> typeMap = new HashMap<>();
        try {
            String classSignature = funcSignature.substring(0, funcSignature.indexOf(":"));
            d.dg("analyzeBCEL: classsig: " + classSignature);
            d.dg(Repository.getRepository());
            d.dg(System.getProperty("java.class.path"));
            JavaClass cls = Repository.lookupClass(classSignature);
            //JavaClass cls = Repository.lookupClass("com.reljicd.service.impl.UserServiceImp");

            Method[] methods = cls.getMethods();
            d.dg("BCEL's method array for class to which input function belongs: " + Arrays.asList(methods));
            d.dg("curr method sootlike sig: " + funcSignature);
            d.dg("curr class sootlike sig:  " + classSignature);
            for (int i = 0; i < methods.length; i++) {
                d.dg("\nLoop index = " + i);
                d.dg(methods[i]);

                Code code = methods[i].getCode();
                d.dg("methname: " + methods[i].getName());
                String methsig = methods[i].getSignature();
                d.dg("methsig: " + methods[i].getSignature());
                String method_generic_sig = methods[i].getGenericSignature();
                d.dg("method_generic_sig: " + method_generic_sig);
                int lastIndRpn = methsig.lastIndexOf(")");
                String retType = "";
                StringBuilder retTypeSB = new StringBuilder(methsig.substring(lastIndRpn + 1));
                if(retTypeSB.length() > 1) {
                    retType = retTypeSB.substring(1, retTypeSB.length() - 1).replace("/", ".");
                }
                else {
                    retType = retTypeSB.toString().replace("/", ".");
                }

                String paramList = methsig.substring(methsig.indexOf("(") + 1, lastIndRpn);
                String[] params = paramList.split(";");
                StringBuilder sootParamListSB = new StringBuilder();
                //TODO: BCEL does not preserve primtives for some reason in signature, fix.
//                for(String prm : params) {
//                    sootParamListSB.append(prm.substring(1).replace("/", "."));
//                    if(prm != params[params.length - 1]) {
//                        sootParamListSB.append(",");
//                    }
//                }
                String sootSigIthMethod = classSignature + ": " + retType + " " + methods[i].getName() /*+ "(" + sootParamListSB.toString() + ")"*/;


                d.dg("retType = " + retType);

                d.dg("code != null = " + (code != null));
                if(code != null) {
                    d.dg(code);
                }
                d.dg("input function signature = " + funcSignature);
                d.dg("signature constructed from BCEL's method signature = " + sootSigIthMethod);

                if(funcSignature.startsWith(sootSigIthMethod)) {
                    d.dg("BCEL's method array index = " + i + " matches the input function: " + funcSignature);
                    if(method_generic_sig != null && method_generic_sig.contains("Ljava/util/Optional")) {
                        d.dg("return type is optional");
                        String key = "return_" + funcSignature;
                        int idx_actual_type = method_generic_sig.indexOf("Ljava/util/Optional") + "Ljava/util/Optional".length() + 1;
                        String type = method_generic_sig.substring(idx_actual_type + 1, method_generic_sig.length() - ";>;".length()).replace("/",
                                ".");
                        d.dg("mapping key=" + key + " to val=" + type);
                        typeMap.put(key, type);
                        typeMap.put("return", type);
                    }

                    if (code != null) {
                        d.dg("Method body exists");
                        d.dg(code);
                        Attribute[] attributes = code.getAttributes();
                        d.dg("att len: " + attributes.length);
                        for(Attribute attribute : attributes) {
                            if(attribute.getName().equals("LocalVariableTypeTable")) {
                                LocalVariableTypeTable typeTable = (LocalVariableTypeTable) attribute;
                                LocalVariable[] localVariables = typeTable.getLocalVariableTypeTable();
                                for(LocalVariable lvar : localVariables) {
                                    String sigVar = lvar.getSignature();
                                    d.dg("lvar sig: " + sigVar);
                                    if(sigVar.startsWith("Ljava/util/Optional")) {
                                        String name = lvar.getName();
                                        d.dg("lvar name = " + name);
                                        StringBuilder actualTypeSB = new StringBuilder(sigVar);
                                        actualTypeSB.delete(0, "Ljava/util/Optional<".length());
                                        actualTypeSB.delete(actualTypeSB.length() - ";>;".length(), actualTypeSB.length());
                                        String actualType = actualTypeSB.toString();
                                        d.dg("actualType = " + actualType);
                                        typeMap.put(name, actualType.substring(1).replace("/", "."));
                                    }
                                }
                            }
                            d.dg("method_generic_sig = " + method_generic_sig);

                        }
                        break;
                    }
                    break;
                }


            }
        } catch (Exception e) {d.dg(e);}
        d.dg("returning");
        return typeMap;
    }

    public static Type getActualType(String methodSignature,  Value var) {
        debug d = new debug("OptinalTypeInfo.java", "getOptionalsType()");
        Type type = var.getType();
        if(type.toString().equals("java.util.Optional")) {
            Map<String, String> typeTable = analyzeBCEL(methodSignature);
            d.dg("typeTable of method: " + methodSignature);
            d.dg(typeTable);
            String actualTypeStr = typeTable.get(var.toString());
            if(actualTypeStr != null) {
                d.dg("actualType = " + actualTypeStr);
                SootClass typeSC = Scene.v().loadClassAndSupport(actualTypeStr);
                type = typeSC.getType();
            }
            else {
                d.dg("Optionals actual type could not be found");
            }
        }
        return type;
    }

    public static Type getKnownOptionalsActualType(String methodSignature,  String var) {
        debug d = new debug("OptinalTypeInfo.java", "getKnownOptionalsActualType()");
        Map<String, String> typeTable = analyzeBCEL(methodSignature);
        Type type = null;
        String actualTypeStr = typeTable.get(var);
        if (actualTypeStr != null) {
            d.dg("actualType = " + actualTypeStr);
            SootClass typeSC = Scene.v().loadClassAndSupport(actualTypeStr);
            type = typeSC.getType();
        } else {
            d.dg("Optionals actual type could not be found");
        }

        return type;
    }

    public static Type getKnownOptionalsActualType(String var) {
        debug d = new debug("OptinalTypeInfo.java", "getKnownOptionalsActualType()");
        Map<String, String> typeTable = OptionalTypeInfo.typeMap;
        d.dg("Current method's typeTable (which is global): " + typeTable);
        Type type = null;
        String actualTypeStr = typeTable.get(var);
        if (actualTypeStr != null) {
            d.dg("actualType = " + actualTypeStr);
            SootClass typeSC = Scene.v().loadClassAndSupport(actualTypeStr);
            type = typeSC.getType();
        } else {
            d.dg("Optionals actual type could not be found");
        }

        return type;
    }


    public static Type getLocalsActualType(String methodSignature, Local var) {
        debug d = new debug("OptinalTypeInfo.java", "getLocalsActualType()");
        Type type = var.getType();
        if(type.toString().equals("java.util.Optional")) {
            Map<String, String> typeTable = analyzeBCEL(methodSignature);
            String actualTypeStr = typeTable.get(var.toString());
            if(actualTypeStr != null) {
                d.dg("actualType = " + actualTypeStr);
                SootClass typeSC = Scene.v().loadClassAndSupport(actualTypeStr);
                type = typeSC.getType();
            }
            else {
                d.dg("Optionals actual type could not be found");
            }
        }
        return type;
    }




}
