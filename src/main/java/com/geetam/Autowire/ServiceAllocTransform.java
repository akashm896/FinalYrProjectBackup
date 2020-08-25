package com.geetam.Autowire;

import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JNewExpr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static soot.SootClass.SIGNATURES;

public class ServiceAllocTransform extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        /*
     This is the name of the root application package.
     */
        String applicationRootPackage = "com.reljicd";
        String packagePath = applicationRootPackage.replace(".", "/");

        System.out.println("ServiceAllocTransform: internalTransform called!");
        Iterator<Unit> it = b.getUnits().snapshotIterator();
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
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println(b.toString());
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
    }
}
