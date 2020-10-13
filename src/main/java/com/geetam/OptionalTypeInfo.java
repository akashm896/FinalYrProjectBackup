package com.geetam;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;

import java.util.HashMap;
import java.util.Map;

public class OptionalTypeInfo {
    public static Map<String, String> typeMap;

    public static Map <String, String> analyzeBCEL(String funcSignature)  {
        Map<String, String> typeMap = new HashMap<>();
        try {
            String classSignature = funcSignature.substring(0, funcSignature.indexOf(":"));
            System.out.println("analyzeBCEL: classsig: " + classSignature);
            System.out.println(Repository.getRepository());
            System.out.println(System.getProperty("java.class.path"));
            JavaClass cls = Repository.lookupClass(classSignature);
            //JavaClass cls = Repository.lookupClass("com.reljicd.service.impl.UserServiceImp");

            Method[] methods = cls.getMethods();
            System.out.println("curr method sootlike sig: " + funcSignature);
            System.out.println("curr class sootlike sig:  " + classSignature);
            for (int i = 0; i < methods.length; i++) {
                System.out.println(methods[i]);

                Code code = methods[i].getCode();
                System.out.println("methname: " + methods[i].getName());
                String methsig = methods[i].getSignature();
                System.out.println("methsig: " + methods[i].getSignature());
                String method_generic_sig = methods[i].getGenericSignature();
                System.out.println("method_generic_sig: " + method_generic_sig);
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
                for(String prm : params) {
                    sootParamListSB.append(prm.substring(1).replace("/", "."));
                    if(prm != params[params.length - 1]) {
                        sootParamListSB.append(",");
                    }
                }
                String sootSigIthMethod = classSignature + ": " + retType + " " + methods[i].getName() + "(" + sootParamListSB.toString() + ")";


                System.out.println("retType = " + retType);
                System.out.println("sootSig = " + sootSigIthMethod);



                if (code != null /*&&& sootSigIthMethod.equals(funcSignature)*/) {
                    System.out.println(code);
                    Attribute[] attributes = code.getAttributes();
                    System.out.println("att len: " + attributes.length);
                    for(Attribute attribute : attributes) {
                        if(attribute.getName().equals("LocalVariableTypeTable")) {
                            LocalVariableTypeTable typeTable = (LocalVariableTypeTable) attribute;
                            LocalVariable[] localVariables = typeTable.getLocalVariableTypeTable();
                            for(LocalVariable lvar : localVariables) {
                                String sigVar = lvar.getSignature();
                                System.out.println("lvar sig: " + sigVar);
                                if(sigVar.startsWith("Ljava/util/Optional")) {
                                    String name = lvar.getName();
                                    System.out.println("lvar name = " + name);
                                    StringBuilder actualTypeSB = new StringBuilder(sigVar);
                                    actualTypeSB.delete(0, "Ljava/util/Optional<".length());
                                    actualTypeSB.delete(actualTypeSB.length() - ";>;".length(), actualTypeSB.length());
                                    String actualType = actualTypeSB.toString();
                                    System.out.println("actualType = " + actualType);
                                    typeMap.put(name, actualType.substring(1).replace("/", "."));
                                }
                            }
                        }
                        if(method_generic_sig != null && method_generic_sig.contains("Ljava/util/Optional")) {
                            String key = "return_" + sootSigIthMethod;
                            int idx_actual_type = method_generic_sig.indexOf("Ljava/util/Optional") + "Ljava/util/Optional".length() + 1;
                            String type = method_generic_sig.substring(idx_actual_type + 1, method_generic_sig.length() - ";>;".length()).replace("/",
                                    ".");
                            typeMap.put(key, type);
                        }

                    }
                }
            }
        } catch (Exception e) {System.out.println(e);}
        return typeMap;
    }

}
