package io.geetam.github.Autowire;

import dbridge.analysis.eqsql.EqSQLDriver;
import javafx.util.Pair;
import mytest.debug;
import soot.*;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;
import soot.util.NumberedString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static soot.SootClass.SIGNATURES;

public class ServiceAllocTransform extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        debug d = new debug("ServiceAllocTransform.java", "internalTransform()");
        /*
     This is the name of the root application package.
     */
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
        System.out.println(b);
        Iterator<Unit> it = b.getUnits().snapshotIterator();
        List <Pair<Value, SootClass>> serviceVarsImplementationPairList = new LinkedList<>();
        while(it.hasNext()) {
            Unit stmt = it.next();
            if(stmt instanceof JAssignStmt) {
                JAssignStmt assignStmt = (JAssignStmt) stmt;
                ValueBox rhsBox = assignStmt.rightBox;
                Value rhsVal = rhsBox.getValue();
                Type rhsValType = rhsVal.getType();
                if(Util.isService(rhsVal)) {
                    System.out.println("Application Classes: " + Scene.v().getApplicationClasses());
                    String sootCp = Scene.v().getSootClassPath();
                    String cp = sootCp + "/" + packagePath;
                    try {
                        List <Path> files = Files.walk(Paths.get(cp))
                                .filter(Files::isRegularFile).collect(Collectors.toList());
                        for(Path file : files) {
                            StringBuilder fileStrBuilder = new StringBuilder(file.toString());
                            String signatureWithSlashes = fileStrBuilder.substring(sootCp.length() + 1, file.toString().length() - 6);
                            String signature = signatureWithSlashes.replace("/", ".");
                            System.out.println(signature);
                           // Scene.v().addBasicClass(signature, SIGNATURES);
                            SootClass cls = Scene.v().forceResolve(signature, SIGNATURES);
                            for(SootClass interfaceImplemented : cls.getInterfaces()) {
                                String interfaceImplementedName = interfaceImplemented.getName();
                                System.out.println("interface implemented = " + interfaceImplementedName);
                                System.out.println("rhsValType =" + rhsValType);
                                if(interfaceImplemented.getType().equals(rhsValType)) {
                                    System.out.println("interface implementation found: " + signature);

                                    ValueBox lhsBox = assignStmt.leftBox;
                                    Value leftVal = lhsBox.getValue();
                                    JAssignStmt newStmt = new JAssignStmt(leftVal, new JNewExpr(cls.getType()));
                                    b.getUnits().insertAfter(newStmt, stmt);
                                    b.getUnits().remove(stmt);
                                    System.out.println(newStmt);
                                    serviceVarsImplementationPairList.add(new Pair<>(assignStmt.leftBox.getValue(), cls));
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
        for(Pair <Value, SootClass> pr : serviceVarsImplementationPairList) {
            Value left = pr.getKey();
            SootClass sc = pr.getValue();
            d.dg("service var = " + left);
            d.dg("implementation class = " + sc.getName());
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
                        System.out.println("Break");
                    }
                    if(invokeExpr.getBase().equals(left)) {
                        SootMethodRef smf = invokeExpr.getMethodRef();
                        StringBuilder newSig = new StringBuilder();
                        newSig.append("<" + sc.getName());
                        newSig.append(smf.getSignature().substring(smf.getSignature().indexOf(":")));

                        SootMethodRef newRef = new SootMethodRef() {
                            @Override
                            public SootClass declaringClass() {
                                return sc;
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
                        if(stmt instanceof JInvokeStmt) {
                            b.getUnits().insertAfter(new JInvokeStmt(newInvokeExpr), stmt);
                            b.getUnits().remove(stmt);
                        }
                        else if(stmt instanceof JAssignStmt) {
                            JAssignStmt assignStmt = (JAssignStmt) stmt;
                            assignStmt.setRightOp(newInvokeExpr);
                        }
                    }
                }
            }
            d.dg("body AFTER service replacement with its implementation");
            System.out.println(b);

            }
    }


}
