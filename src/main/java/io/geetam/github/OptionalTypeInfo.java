/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
package io.geetam.github;

import io.geetam.github.RepoToEntity.RepoToEntity;
import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import soot.*;

import java.util.*;

public class OptionalTypeInfo {
    public static Map<String, String> typeMap;

    public static Map <String, String> analyzeBCEL(String funcSignature)  {
        debug d = new debug("OptionalTypeInfo.java", "analyzeBCEL()");
        d.turnOff();
        d.dg("Function to analyze for actual types of optional-typed variables: " + funcSignature);
        Map<String, String> typeMap = new HashMap<>();
        try {
            String classSignature = funcSignature.substring(0, funcSignature.indexOf(":"));
            d.dg("analyzeBCEL: classsig: " + classSignature);
            if(classSignature.equals("com.gorankitic.springboot.crudthymeleaf.dao.EmployeeRepository")) {
//                RepoToEntity.getEntityForRepo(classSignature);
                d.dg("break point");
            }
            d.dg(Repository.getRepository());
            d.dg(System.getProperty("java.class.path"));

            JavaClass cls = Repository.lookupClass(classSignature);
            //JavaClass cls = Repository.lookupClass("com.reljicd.service.impl.UserServiceImp");

            /*
                    iface0
                    /      \
         implements/        \extends
                cls          iface1

                In case of iface1 a simple union of methods(iface0) and methods(iface1) suffices.
                In case of cls, precedence to own methods over interface methods needs to be given.
             */
            List <Method> ifacemethods = getInterfaceMethods(cls);
            List <Method> ownmethods = Arrays.asList(cls.getMethods());
            List <Method> allmethods = new ArrayList<>();
            for(Method m : ownmethods) {
                allmethods.add(m);
            }
            for(Method m : ifacemethods) {
                allmethods.add(m);
            }
            d.dg("BCEL's method array for class to which input function belongs: " + allmethods);
            d.dg("curr method sootlike sig: " + funcSignature);
            d.dg("curr class sootlike sig:  " + classSignature);
            for (int i = 0; i < allmethods.size(); i++) {
                d.dg("\nLoop index = " + i);
                Method methodi = allmethods.get(i);
                d.dg(methodi);

                Code code = methodi.getCode();
                d.dg("methname: " + methodi.getName());
                String methsig = methodi.getSignature();
                d.dg("methsig: " + methodi.getSignature());
                String method_generic_sig = methodi.getGenericSignature();
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
                String sootRet = retType;
                if(retType.equals("V")) {
                    sootRet = "void";
                }
                String sootSigIthMethod = classSignature + ": " + sootRet + " " + methodi.getName() /*+ "(" + sootParamListSB.toString() + ")"*/;


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
                        type = resolveGenericType(type, classSignature);
                        d.dg("mapping key=" + key + " to val=" + type);
                        typeMap.put(key, type);
                        typeMap.put("return", type);
                    }
                    LocalVariableTable lvt = methodi.getLocalVariableTable();
                    d.dg("local var table = " + lvt);
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

    public static String resolveGenericType(String type, String repocls) {
        if(type.equals("T") == false)
            return type;
        return RepoToEntity.getEntityForRepo(repocls);
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


    public static List<Method> getInterfaceMethods(JavaClass cls) throws ClassNotFoundException {
        List <Method> ret = new ArrayList<>();
        JavaClass[] interfaces = cls.getAllInterfaces();
        for(JavaClass ifc : interfaces) {
            ret.addAll(Arrays.asList(ifc.getMethods()));
        }
        return ret;
    }




}
