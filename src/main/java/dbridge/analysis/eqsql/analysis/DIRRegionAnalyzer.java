package dbridge.analysis.eqsql.analysis;

import com.geetam.OptionalTypeInfo;
import com.geetam.accesspath.AccessPath;
import com.geetam.accesspath.Flatten;
import com.geetam.formalToActualVisitor.FormalToActual;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.hibernate.construct.StmtDIRConstructionHandler;
import dbridge.analysis.eqsql.hibernate.construct.StmtInfo;
import dbridge.analysis.eqsql.util.SootClassHelper;
import dbridge.analysis.eqsql.utils;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.Region;
import exceptions.DIRConstructionException;
import exceptions.UnknownStatementException;
import dbridge.analysis.eqsql.util.VarResolver;
import dbridge.analysis.region.regions.ARegion;
import mytest.debug;
import soot.*;
import soot.JastAddJ.*;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.internal.*;
import soot.toolkits.graph.Block;
import soot.util.Switch;

import java.util.*;
import java.util.List;

import static com.geetam.OptionalTypeInfo.*;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.fetchBaseValue;


/**
 * Created by ek on 4/5/16.
 */
public class DIRRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRRegionAnalyzer(){};
    public static DIRRegionAnalyzer INSTANCE = new DIRRegionAnalyzer();



    @Override
    public DIR constructDIR(ARegion region) {
        debug d = new debug("DIRRegionAnalyzer.java", "constructDIR()");
        Block basicBlock = region.getHead();

        d.dg("basic block = " + basicBlock.getBody().getUnits());
        Iterator<Unit> iterator = basicBlock.iterator();

        DIR dir = new DIR(); //dir for this region

        StmtInfo stmtInfo = StmtInfo.nullInfo;

        while (iterator.hasNext()) {
            Unit curUnit = iterator.next();
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit.toString());
            if(curUnit.toString().equals("specialinvoke $r2.<com.reljicd.model.Post: void <init>()>()")) {
                d.dg("break point!");
            }
            try {

//                if(curUnit instanceof JAssignStmt && ((JAssignStmt) curUnit).rightBox.toString().contains("getBy")) {
//                  //  debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ",
//                  //          "curunit instanceof jassignstmt and contains getBy");
//                    JAssignStmt getByStatement = (JAssignStmt) curUnit;
//                    System.out.println(getByStatement.leftBox.getValue().getType());
//                    Type typeOfLeft = getByStatement.leftBox.getValue().getType();
//                    Value leftVal = getByStatement.leftBox.getValue();
//                    SootClass classOfLeft = Scene.v().loadClass(typeOfLeft.toString(), 1);
//               //     List <VarNode> fieldAccesses = utils.getVarNodeFieldAccessListOfBaseVar(leftVal);
//                    //Instead of creating a dbridge-fieldrefnode explicitly, maybe a soot-fieldref can be created which
//                    //can be passed on to constructFromValue to get dbridge equivalent. Is it useful?
//                    for(SootField sf : classOfLeft.getFields()) {
//                        VarNode varNode = utils.getFieldAccessVarNode(leftVal, sf);
//                        SQLSelectValueNode rval = new SQLSelectValueNode(leftVal, sf.getName(), "dummy_table");
//                        dir.insert(varNode, rval);
//                    }
//                }

                if(curUnit instanceof JReturnStmt) {
                    JReturnStmt retStmt = (JReturnStmt) curUnit;
                    Value retval = retStmt.getOp();
                    Type retType = retval.getType();
                    if(retType.toString().equals("java.util.Optional")) {
                        retType = getKnownOptionalsActualType("return");
                    }
                    if(!AccessPath.isTerminalType(retType)) {
                        Value retLocal = new JimpleLocal("return", retType);
                        DIR dirRetStmt = processPointerAssignment(retLocal, retval, dir);
                        dir.getVeMap().putAll(dirRetStmt.getVeMap());
                        continue;
                    }
                }

                if(curUnit instanceof JGotoStmt) {
                    System.out.println("GOTO stmt in seq region");
                }


                if(curUnit instanceof JAssignStmt) {
                    debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit);
                    JAssignStmt stmt = (JAssignStmt) curUnit;
                    Value leftVal = stmt.leftBox.getValue();
                    Value rhsVal = stmt.rightBox.getValue();
                    if(stmt.toString().equals("post = (com.reljicd.model.Post) $r1")) {
                        d.dg("Break!");
                    }
                    d.dg("leftClass = " + leftVal.getClass());
                    d.dg("rightClass = " + rhsVal.getClass());

                    //CASE: v1 = v2, type(v1, v2) = ptr
                    if(leftVal instanceof JimpleLocal && rhsVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())) {
                        d.dg("CASE: v1 = v2, type(v1, v2) = ptr");
                        DIR dirStmt = processPointerAssignment(leftVal, rhsVal, dir);
                        dir.getVeMap().putAll(dirStmt.getVeMap());
                    }
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JCastExpr) {
                        JCastExpr castExpr = (JCastExpr) rhsVal;
                        Value right = castExpr.getOp();
                        DIR dirStmt = processPointerAssignment(leftVal, right, dir);
                        dir.getVeMap().putAll(dirStmt.getVeMap());
                    }
                    //CASE: v.f = expr
                    else if(leftVal instanceof JInstanceFieldRef) {
                        JInstanceFieldRef fieldRef = (JInstanceFieldRef) leftVal;
                        SootField field = fieldRef.getField();

                        //SUBCASE: v.f = expr, f is primitive
                        if(AccessPath.isTerminalType(field.getType())) {
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v.f = expr, f is primitive");

                            Node rhsNode = NodeFactory.constructFromValue(rhsVal);
                            VarResolver varResolver = new VarResolver(dir);
                            Node resolvedRHS = rhsNode.accept(varResolver);
                            String accpStr = fieldRef.getBase().toString() + "." + fieldRef.getField().getName();
                            d.dg("Mapping: " + accpStr + " -> " + resolvedRHS);
                            dir.insert(new VarNode(accpStr), resolvedRHS);
                        }
                        //SUBCASE: v.f = expr, f is not primitive
                        else {
                            d.dg("CASE: v.f = expr, f is non-primtive");
                            List <AccessPath> accessPaths = Flatten.flatten(leftVal, leftVal.getType());
                            d.dg("accessPaths = " + accessPaths);
                            d.dg("right val = " + rhsVal);
                            //subcase: v1.f = v2
                            d.dg("Subcase: v1.f = v2");
                            for(AccessPath ap : accessPaths) {
                                AccessPath rightAP = AccessPath.replaceBase(ap, rhsVal.toString());
                                dir.insert(ap.toVarNode(), rightAP.toVarNode());
                                d.dg("Mapped: " + ap.toString() + " -> " + rightAP.toString());
                            }

                            //can subcase v.f = methodcall be present?
                        }

                    }
                    //CASE: v = new
                    else if (leftVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())
                            && rhsVal instanceof JNewExpr) {
                        debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v = new");

                        List <AccessPath> accessPaths = new Flatten(1).flatten(leftVal, leftVal.getType());
                        for(AccessPath ap : accessPaths) {
                            VarNode vn = new VarNode(ap.toString());
                            System.out.println("Mapping " + ap.toString() + " to Bottomnode");
                            dir.insert(vn, BottomNode.v());
                        }
                    }

                    //CASE v1 = v2.foo(v3)
                    else if(rhsVal instanceof InvokeExpr &&
                                !AccessPath.isPrimitiveType(leftVal.getType())) {
                        debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE v1 = v2.foo(v3)");
                        InvokeExpr invokeExpr = (InvokeExpr) rhsVal;
                        Utils.parseInvokeExpr(invokeExpr);
                        String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethodRef().getSignature());
                        //Map <VarNode, Node> veMapCallee = FuncStackAnalyzer.funcDIRMap.get(invokedSig).getVeMap();
                        //TODO: get actual type in case of Optional, flatten and then implement handleSideEffects
                        Type leftType = leftVal.getType();
                        d.dg("left type = " + leftType);
                        if(leftVal.getType().toString().equals("java.util.Optional")) {
                            d.dg("v1 type is optional");
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "invokedSig = " + invokedSig);
                            Map <String, String> typeTable = OptionalTypeInfo.analyzeBCEL(invokedSig);
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", " leftVal = " + leftVal);
                           // String actualType = OptionalTypeInfo.typeMap.get(leftVal.toString());
                            String lookup = "return_" + invokedSig;
                            String actualType = typeTable.get(lookup);
                            if(actualType != null) {
                                debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "actualType = " + actualType);
                                SootClass typeSC = Scene.v().loadClassAndSupport(actualType);
                                leftType = typeSC.getType();
                            }
                            else {
                                d.wrn("Actual type of Optional could not be determined");
                                d.dg("typemap = " + typeMap);
                                d.dg("lookup = " + leftVal);
                            }
                        }
                        //after this call, ve map of callee is guaranteed to be present
                        Utils.parseInvokeExpr(invokeExpr);
                        d.dg("method sig = " + invokedSig);
                        if(invokedSig.equals("java.util.Optional: java.lang.Object get()")) {
                            d.dg("get invoked");
                            Value base = fetchBaseValue(invokeExpr);
                            d.dg("base = " + base);
                            d.dg("typemap = " + OptionalTypeInfo.typeMap);
                            String lt = OptionalTypeInfo.typeMap.get(base.toString());
                            leftType = Scene.v().loadClassAndSupport(lt).getType();
                            d.dg("leftType = " + leftType);

                        }

                        //TODO: handle side effects here also, logic is in case v1.foo(v2). Also make the code less complex
                        if(!AccessPath.isTerminalType(leftType)) {
                            d.dg("going to flatten (var, type) = " + leftVal + ", " + leftType);
                            List<AccessPath> accessPaths = Flatten.flatten(leftVal, leftType);
                            List<String> attributes = Flatten.attributes(accessPaths);
                            d.dg("funcDIRMap domain = " + FuncStackAnalyzer.funcDIRMap.keySet());
                            d.dg("callee = " + invokedSig);
                            d.dg("funcDIRMap domain contains callee = " + FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig));
                            DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
                            Map<VarNode, Node> calleeVEMap = calleeDIR.getVeMap();
                            d.dg("Printing ve map of callee = " + invokedSig);
                            for (VarNode vn : calleeVEMap.keySet()) {
                                d.dg("key = " + vn);
                                d.dg("val = " + calleeVEMap.get(vn));
                            }
                            d.dg("Printing ve map of callee = " + invokedSig + " END");

                            for (AccessPath ap : accessPaths) {
                                VarNode key = new VarNode(ap.toString());
                                d.dg("key = " + key);
                                VarNode lookup = new VarNode("return" + ap.toString().substring(ap.toString().indexOf(".")));
                                d.dg("lookup = " + lookup);
                                if (calleeVEMap.containsKey(lookup)) {
                                    Node val = calleeVEMap.get(lookup);
                                    d.dg("val = " + val);
                                    dir.insert(key, val);
                                } else {
                                    d.dg("No entry for lookup = " + lookup);
                                }
                            }


                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "flattenedEntity = " + accessPaths);
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "attributes = " + attributes);
                        }

                        else {
                            stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                            if (stmtInfo == StmtInfo.nullInfo) {
                                continue;
                            }
                            VarNode dest = stmtInfo.getDest();
                            Node source = stmtInfo.getSource();

                            VarResolver varResolver = new VarResolver(dir);
                            Node resolvedSource = source.accept(varResolver);

                            dir.insert(dest, resolvedSource);
                        }

                    }

                    else {

                        stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                        if (stmtInfo == StmtInfo.nullInfo) {
                            continue;
                        }
                        VarNode dest = stmtInfo.getDest();
                        Node source = stmtInfo.getSource();

                        VarResolver varResolver = new VarResolver(dir);
                        Node resolvedSource = source.accept(varResolver);

                        dir.insert(dest, resolvedSource);
                    }


                }
                //CASE: v1.foo(v2)
                else if(curUnit instanceof JInvokeStmt) {
                    d.dg("CASE: v1.foo(v2)");
                    JInvokeStmt invokeStmt = (JInvokeStmt) curUnit;
                    Value base = fetchBaseValue(invokeStmt.getInvokeExpr());
                    d.dg("invokeStmt = " + invokeStmt);
                    List <Value> actualArgs = invokeStmt.getInvokeExpr().getArgs();
                    actualArgs.add(base);

                    String invokedSig = SootClassHelper.trimSootMethodSignature(invokeStmt.getInvokeExpr().getMethodRef().getSignature());
                    d.dg("invokedSig = " + invokedSig);
                    if(FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
                        SootMethod method = Scene.v().getMethod(invokeStmt.getInvokeExpr().getMethodRef().getSignature());
                        d.dg("soot method = " + method);
                        List<Local> formalArgs = (method.getActiveBody()).getParameterLocals();
                        formalArgs.add(method.getActiveBody().getThisLocal());
                        d.dg("formalArgs = " + formalArgs);
                        d.dg("actualArgs = " + actualArgs);
                        if(formalArgs.size() != actualArgs.size()) {
                            d.dg("WARN: unequal number of actual and formal args");
                        }
                        analyzeMethod(invokedSig);
                        DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
                        List <Node> affectedKeys = new LinkedList<>();
                        for (int i = 0; i < formalArgs.size(); i++) {
                            Local formal = formalArgs.get(i);
                            Value actual = actualArgs.get(i);
                            Type formalType = getLocalsActualType(invokedSig, formal);
                            if(!AccessPath.isTerminalType(formalType)) {
                                d.dg("Going to flatten: " + formal);
                                List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType);
                                d.dg("flattened res = " + formalAccpList);
                                for (AccessPath formalAccp : formalAccpList) {
                                    String formalAccpStr = formalAccp.toString();
                                    d.dg("formal access path in callee: " + formalAccpStr);
                                    d.dg("callee ve map domain: " + calleeDIR.getVeMap().keySet());
                                    Node eedag = calleeDIR.find(new VarNode(formalAccpStr));
                                    d.dg("formalaccp eedag = " + eedag);

                                    if(eedag != null) {
                                        String actualAccpStr = actual.toString() + formalAccpStr.substring(formalAccpStr.indexOf("."));
                                        VarNode actualAccpNode = new VarNode(actualAccpStr);
                                        affectedKeys.add(actualAccpNode);
                                        dir.insert(actualAccpNode, eedag);
                                    }
                                }
                            }
                        }
                        d.dg("Affected keys = " + affectedKeys);
                        for (int i = 0; i < formalArgs.size(); i++) {
                            Local formal = formalArgs.get(i);
                            Value actual = actualArgs.get(i);
                            Type formalType = getLocalsActualType(invokedSig, formal);
                            if(!AccessPath.isTerminalType(formalType)) {
                                //This is the list of formal access paths that will be replace by actuals in eedag of affectedKeys
                                List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType);
                                d.dg("formalAccpList = " + formalAccpList);
                                for (AccessPath formalAccp : formalAccpList) {
                                    String formalAccpStr = formalAccp.toString();
                                    String actualAccpStr = actual.toString() + formalAccpStr.substring(formalAccpStr.indexOf("."));
                                    Node formalAccpNode = new VarNode(formalAccpStr);
                                    Node actualAccpNode = new VarNode(actualAccpStr);
                                    FormalToActual formalToActualVisitor = new FormalToActual(formalAccpNode, actualAccpNode);
                                    for(Node key : affectedKeys) {
                                        Node eedag = dir.find((VarNode) key);
                                        d.dg("for key = " + key);
                                        d.dg("eedag before formaltoactual: " + eedag);
                                        Node newEEDag = eedag.accept(formalToActualVisitor);
                                        dir.insert((VarNode) key, newEEDag);
                                        d.dg("after: " + eedag);

                                    }
                                }
                            } else {
                                VarNode fml = new VarNode((JimpleLocal)formal);
                                Node actualNode = NodeFactory.constructFromValue(actual);
                                if(actualNode instanceof VarNode) {
                                    VarNode lookup = (VarNode) actualNode;
                                    actualNode = dir.find(lookup);
                                }
                                d.dg("formal = " + fml + ", actual = " + actualNode);

                                FormalToActual formalToActualVisitor = new FormalToActual(fml, actualNode);
                                for(Node key : affectedKeys) {
                                    Node eedag = dir.find((VarNode) key);
                                    d.dg("for key = " + key);
                                    d.dg("eedag before formaltoactual: " + eedag);
                                    Node newEEDag = eedag.accept(formalToActualVisitor);
                                    dir.insert((VarNode) key, newEEDag);
                                    d.dg("after: " + eedag);
                                }

                            }
                        }
                    }
                    else {
                        d.dg("Wont handle method");
                    }
                }
                //TODO: Check if this is required, keeping it only for consistency with base DBridge
                else {

                    stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                    if (stmtInfo == StmtInfo.nullInfo) {
                        continue;
                    }
                    VarNode dest = stmtInfo.getDest();
                    Node source = stmtInfo.getSource();
                    VarResolver varResolver = new VarResolver(dir);
                    Node resolvedSource = source.accept(varResolver);
                    dir.insert(dest, resolvedSource);
                }





                if(curUnit instanceof JInvokeStmt && isModelAdd(((JInvokeStmt) curUnit).getInvokeExpr().getMethodRef())) {
                    d.dg("Model Add Attribute Statement");
                    JInvokeStmt addAttributeInvoke = (JInvokeStmt) curUnit;
                    InvokeExpr addAttributeExpr = addAttributeInvoke.getInvokeExpr();
                    List <Value> args = addAttributeExpr.getArgs();
                    assert args.size() == 2 || args.size() == 1: "too many/few args to attAttribute call";
                    Value attributeName = null;
                    String attributeNameStr = null;
                    Value attributeValue = null;
                    if(args.size() == 2) {
                        attributeName = args.get(0);
                        attributeNameStr = attributeName.toString().substring(1, attributeName.toString().length() - 1);
                        attributeValue = args.get(1);
                    } else if(args.size() == 1) {
                        attributeValue = args.get(0);
                        RefType valueType = (RefType) attributeValue.getType();
                        attributeNameStr = valueType.getSootClass().getShortName().toLowerCase();
                    }
                    //TODO: Handle optional type
                    JimpleLocal local = new JimpleLocal("__modelattribute__" + attributeNameStr, attributeValue.getType());
                    if(!AccessPath.isTerminalType(attributeValue.getType())) {
                        DIR dirStmt = processPointerAssignment(local, attributeValue, dir);
                        dir.getVeMap().putAll(dirStmt.getVeMap());
                    } else {
                        JAssignStmt stmt = new JAssignStmt(local, attributeValue);
                        stmtInfo = StmtDIRConstructionHandler.constructDagSS(stmt);
                        if (stmtInfo == StmtInfo.nullInfo) {
                            continue;
                        }
                        VarNode dest = stmtInfo.getDest();
                        Node source = stmtInfo.getSource();
                        VarResolver varResolver = new VarResolver(dir);
                        Node resolvedSource = source.accept(varResolver);
                        dir.insert(dest, resolvedSource);
                    }
                }



            }
            catch (UnknownStatementException e) {
                //Temporary fix. Todo: pass exception to higher level.
                d.dg("UnknownStatementException");
                e.printStackTrace();
                return null;
            } catch (RegionAnalysisException e) {
                d.dg("RegionAnalysisException");

                e.printStackTrace();
                return null;
            }

        }
        d.dg("Finished with all the statements");

        return dir;
    }

    //updates ve map of invoked method
    private void analyzeMethod(String invokedSig) throws RegionAnalysisException {
        analyzeBCEL(invokedSig);
        if(FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig)) {
            return;
        }

        if (FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
            ARegion topRegion = FuncStackAnalyzer.funcRegionMap.get(invokedSig);
            DIR dir = (DIR) topRegion.analyze();
            FuncStackAnalyzer.funcDIRMap.put(invokedSig, dir);
        }
    }


    public Boolean isModelAdd(SootMethodRef methodRef) {
        String methodName = methodRef.name();
        String classBase = methodRef.declaringClass().getName();
        if(methodName.startsWith("addAttribute") || methodName.startsWith("addObject"))
            return true;
        if((classBase.equals("java.util.Map") || classBase.endsWith("ModelMap"))
                && methodName.equals("put")) {
            return true;
        }
        return false;
    }
    //CASE v1 = v2
    private DIR processPointerAssignment(Value v1, Value v2, DIR dir) {
        //TODO: Need to account for optional type
        DIR ret = new DIR();
        debug d = new debug("DIRRegionAnalyzer.java", "processPointerAssignment()");
        if(v1.getType().equals(v2.getType()) == false) {
            d.dg("WARN: v1 and v2 have diff types");
        }

        List <AccessPath> v1Paths = Flatten.flatten(v1, v1.getType());
        for(AccessPath v1p : v1Paths) {
            AccessPath lookupAP = AccessPath.replaceBase(v1p, v2.toString());
            if(dir.contains(lookupAP.toVarNode())) {
                ret.insert(v1p.toVarNode(), dir.find(lookupAP.toVarNode()));
            }
            else {
                ret.insert(v1p.toVarNode(), lookupAP.toVarNode());
            }
        }

        return ret;
    }
}
