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
package io.geetam.github.Autowire;

import dbridge.analysis.eqsql.EqSQLDriver;
import mytest.debug;
import soot.*;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.internal.*;
import soot.util.NumberedString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static soot.SootClass.SIGNATURES;

public class ServiceAllocTransform extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        debug d = new debug("ServiceAllocTransform.java", "internalTransform()");
        String methodsig = b.getMethod().getSignature();
        d.dg("methodsig: " + methodsig);
        /*
     This is the name of the root application package.
     */
        instrumentServiceImplementations(b);
    }

    public static void instrumentServiceImplementations(Body b) {
        debug d = new debug("ServiceAllocTransform.java", "instrumentServiceImplementations()");
        d.dg("body methodname: " + b.getMethod().getName());
        SootClass clsofmethod = b.getMethod().getDeclaringClass();
        SootMethod currmethod = b.getMethod();
        d.dg("class of method: " + clsofmethod.getName());
        String currCaseSig = EqSQLDriver.currFuncSig;
        d.dg("currCaseSig: " + currCaseSig);
        int numDotsYet = 0;
        int secondDotIdx = -1;
        for(int i = 0; i < currCaseSig.length(); i++) {
            char c = currCaseSig.charAt(i);
            if(c == '.') {
                numDotsYet++;
            }
            if(numDotsYet == 2) {
                secondDotIdx = i;
                break;
            }
        }
        String applicationRootPackage = currCaseSig.substring(0, secondDotIdx);
        d.dg("package prefix: " + applicationRootPackage);
        String packagePath = applicationRootPackage.replace(".", "/");
        d.dg("body before service replacement with its implementation: ");
        d.clndg(b);
        Iterator<Unit> it = b.getUnits().snapshotIterator();
        List<Map.Entry<Value, SootClass>> serviceVarsImplementationPairList = new LinkedList<>();
        while(it.hasNext()) {
            Unit stmt = it.next();
            if(stmt instanceof JAssignStmt) {
                JAssignStmt assignStmt = (JAssignStmt) stmt;
                ValueBox rhsBox = assignStmt.rightBox;
                Value rhsVal = rhsBox.getValue();
                Type rhsValType = rhsVal.getType();
                if(Util.isService(rhsVal)) {
                    d.dg("Application Classes: " + Scene.v().getApplicationClasses());
                    String sootCp = Scene.v().getSootClassPath();
                    String cp = sootCp + "/" + packagePath;
                    try {
                        List <Path> files = Files.walk(Paths.get(cp))
                                .filter(Files::isRegularFile).collect(Collectors.toList());
                        for(Path file : files) {
                            StringBuilder fileStrBuilder = new StringBuilder(file.toString());
                            String signatureWithSlashes = fileStrBuilder.substring(sootCp.length() + 1, file.toString().length() - 6);
                            String signature = signatureWithSlashes.replace("/", ".");
                            d.dg(signature);
                           // Scene.v().addBasicClass(signature, SIGNATURES);
                            SootClass cls = Scene.v().forceResolve(signature, SIGNATURES);
                            for(SootClass interfaceImplemented : cls.getInterfaces()) {
                                String interfaceImplementedName = interfaceImplemented.getName();
                                d.dg("interface implemented = " + interfaceImplementedName);
                                d.dg("rhsValType =" + rhsValType);
                                if(interfaceImplemented.getType().equals(rhsValType)) {
                                    d.dg("interface implementation found: " + signature);

                                    ValueBox lhsBox = assignStmt.leftBox;
                                    Value leftVal = lhsBox.getValue();
                                    JAssignStmt newStmt = new JAssignStmt(leftVal, new JNewExpr(cls.getType()));
                                    b.getUnits().insertAfter(newStmt, stmt);
                                    b.getUnits().remove(stmt);
                                    d.dg(newStmt);
                                    serviceVarsImplementationPairList.add(new AbstractMap.SimpleEntry<>(assignStmt.leftBox.getValue(), cls));
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
//                Type rhsValType = rhsVal.getType();
//                //System.out.println("Type rhsVal = " + rhsVal.getType());
//                if(rhsValType.toString().endsWith("Service")) {
//                    System.out.println(stmt);
//                    System.out.println(rhsVal);
//                    System.out.println("Type rhsVal = " + rhsVal.getType());
//                    System.out.println("Class rhs box = " + rhsBox.getClass());
//
//                }
            }
        }

        //Replace interface invoke calls with virtual invoke call with service var as receiver var:
        d.dg("service list = " + serviceVarsImplementationPairList);
        for(Map.Entry <Value, SootClass> pr : serviceVarsImplementationPairList) {
            Value left = pr.getKey();
            SootClass serviceimplcls = pr.getValue();
            d.dg("service var = " + left);
            d.dg("implementation class = " + serviceimplcls.getName());
            it = b.getUnits().snapshotIterator();
            d.dg("iterating through all units to find service.method() invoke statements");
            while(it.hasNext()) {
                Unit stmt = it.next();
                d.dg("cst = " + stmt);
                d.dg("class of cst: " + stmt.getClass());
                InterfaceInvokeExpr invokeExpr = null;
                if(stmt instanceof JAssignStmt && ((JAssignStmt) stmt).rightBox.getValue() instanceof InterfaceInvokeExpr) {
                    invokeExpr = (InterfaceInvokeExpr) ((JAssignStmt) stmt).rightBox.getValue();
                } else if(stmt instanceof JInvokeStmt) {
                    JInvokeStmt invk = (JInvokeStmt) stmt;
                    InvokeExpr invkExpr = invk.getInvokeExpr();
                    if(invkExpr instanceof InterfaceInvokeExpr) {
                        invokeExpr = (InterfaceInvokeExpr) invkExpr;
                        d.dg("Found a interface invoke. Case: stmt instanceof InterfaceInvokeExpr. stmt: " + stmt);
                    }
                }
                if(invokeExpr != null) {
                    d.dg("invoke base = " + invokeExpr.getBase());
                    d.dg("left = " + left);
                    if(left.toString().equals("$r2")) {
                        d.dg("Break");
                    }
                    if(invokeExpr.getBase().equals(left)) {
                        d.dg("fields of class of method: " + clsofmethod.getFields());
                        d.dg("service var: " + left);
                        d.dg("service var type: " + left.getType());
                        String removedfield = removeFieldOfType(clsofmethod, left.getType());
                        if(removedfield != null) {
                            addField(clsofmethod, (RefType) left.getType(), removedfield, serviceimplcls.getType());
                        }
                        SootField serviceimplfield = getFieldByType(clsofmethod, serviceimplcls.getType());
                        SootFieldRef serviceimplfieldref = new SootFieldRef() {
                            @Override
                            public SootClass declaringClass() {
                                return clsofmethod;
                            }

                            @Override
                            public String name() {
                                return serviceimplfield.getName();
                            }

                            @Override
                            public Type type() {
                                return serviceimplfield.getType();
                            }

                            @Override
                            public boolean isStatic() {
                                return serviceimplfield.isStatic();
                            }

                            @Override
                            public String getSignature() {
                                return serviceimplfield.getSignature();
                            }

                            @Override
                            public SootField resolve() {
                                return serviceimplfield;
                            }
                        };
                        SootMethodRef smf = invokeExpr.getMethodRef();
                        StringBuilder newSig = new StringBuilder();
                        newSig.append("<" + serviceimplcls.getName());
                        newSig.append(smf.getSignature().substring(smf.getSignature().indexOf(":")));

                        SootMethodRef newRef = new SootMethodRef() {
                            @Override
                            public SootClass declaringClass() {
                                return serviceimplcls;
                            }

                            @Override
                            public String name() {
                                return smf.name();
                            }

                            @Override
                            public List<Type> parameterTypes() {
                                return smf.parameterTypes();
                            }

                            @Override
                            public Type returnType() {
                                return smf.returnType();
                            }

                            @Override
                            public boolean isStatic() {
                                return smf.isStatic();
                            }

                            @Override
                            public NumberedString getSubSignature() {
                                return smf.getSubSignature();
                            }

                            @Override
                            public String getSignature() {
                                return newSig.toString();
                            }

                            @Override
                            public Type parameterType(int i) {
                                return null;
                            }

                            @Override
                            public SootMethod resolve() {
                                return Scene.v().getMethod(newSig.toString());
                            }
                        };

                        Value base = invokeExpr.getBase();
                        ValueBox baseBox = new VariableBox(base);
                        JVirtualInvokeExpr newInvokeExpr = new JVirtualInvokeExpr(base, newRef, invokeExpr.getArgs());

                        JInstanceFieldRef thisdotserviceimpl = new JInstanceFieldRef(b.getThisLocal(), serviceimplfieldref);
                        if(stmt instanceof JInvokeStmt) {
                            InvokeStmt newinvoke = new JInvokeStmt(newInvokeExpr);
                            b.getUnits().insertAfter(newinvoke, stmt);
                            b.getUnits().remove(stmt);
                            JAssignStmt serviceimplasign = new JAssignStmt(thisdotserviceimpl, left);
                            b.getUnits().insertAfter(serviceimplasign, newinvoke);
                        }
                        else if(stmt instanceof JAssignStmt) {
                            JAssignStmt assignStmt = (JAssignStmt) stmt;
                            assignStmt.setRightOp(newInvokeExpr);
                            JAssignStmt serviceimplasign = new JAssignStmt(thisdotserviceimpl, left);
                            b.getUnits().insertAfter(serviceimplasign, stmt);
                        }

                    }
                }
            }
            d.dg("body AFTER service replacement with its implementation");
            d.clndg(b);

            }
    }

    public static void addField(SootClass clsofmethod, RefType type, String name, Type serviceimplclstype) {
        boolean alreadyexists = false;
        for(SootField sf : clsofmethod.getFields()) {
            if(sf.getName().equals(name + "Impl") && sf.getType().toString().equals(serviceimplclstype)) {
                alreadyexists = true;
                break;
            }
        }
        if(alreadyexists == false) {
            SootField serviceimpfield = new SootField(name + "Impl", serviceimplclstype);
            clsofmethod.addField(serviceimpfield);
        }
    }

    public static String removeFieldOfType(SootClass cls, Type t) {
        SootField rem = null;
        for(SootField sf : cls.getFields()) {
            if(sf.getType().toString().equals(t.toString())) {
                rem = sf;
                break;
            }
        }
        if(rem != null) {
            cls.removeField(rem);
        }
        return rem == null? null : rem.getName();
    }

    public static SootField getFieldByType(SootClass sc, Type t) {
        for(SootField sf : sc.getFields()) {
            if(sf.getType().toString().equals(t.toString())) {
                return sf;
            }
        }
        return null;
    }


}
