package dbridge.analysis.eqsql.analysis;

import com.geetam.OptionalTypeInfo;
import com.geetam.accesspath.AccessPath;
import com.geetam.accesspath.Flatten;
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
import soot.jimple.internal.*;
import soot.toolkits.graph.Block;
import soot.util.Switch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.geetam.OptionalTypeInfo.analyzeBCEL;
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
        Iterator<Unit> iterator = basicBlock.iterator();

        DIR dir = new DIR(); //dir for this region

        StmtInfo stmtInfo = StmtInfo.nullInfo;

        while (iterator.hasNext()) {
            Unit curUnit = iterator.next();
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit.toString());
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


                if(curUnit instanceof JGotoStmt) {
                    System.out.println("GOTO stmt in seq region");
                }


                if(curUnit instanceof JAssignStmt) {
                    debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit);
                    JAssignStmt stmt = (JAssignStmt) curUnit;
                    Value leftVal = stmt.leftBox.getValue();
                    Value rhsVal = stmt.rightBox.getValue();
                    //CASE: v.f = expr
                    if(leftVal instanceof JInstanceFieldRef) {
                        JInstanceFieldRef fieldRef = (JInstanceFieldRef) leftVal;
                        SootField field = fieldRef.getField();

                        //SUBCASE: v.f = expr, f is primitive
                        if(AccessPath.isTerminalType(field.getType())) {
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v.f = expr, f is primitive");

                            Node rhsNode = NodeFactory.constructFromValue(rhsVal);
                            VarResolver varResolver = new VarResolver(dir);
                            Node resolvedRHS = rhsNode.accept(varResolver);
                            dir.insert(new VarNode(leftVal.toString()), resolvedRHS);
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
                        if(leftVal.getType().toString().equals("java.util.Optional")) {
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "invokedSig = " + invokedSig);
                           // Map <String, String> typeTable = OptionalTypeInfo.analyzeBCEL(invokedSig);
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", " leftVal = " + leftVal);
                            String actualType = OptionalTypeInfo.typeMap.get(leftVal.toString());
                            if(actualType != null) {
                                debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "actualType = " + actualType);
                                SootClass typeSC = Scene.v().loadClassAndSupport(actualType);
                                leftType = typeSC.getType();
                            }
                            else {
                                debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "Actual type of Optional could not be determined");
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
                        List <AccessPath> accessPaths = Flatten.flatten(leftVal, leftType);
                        List <String> attributes = Flatten.attributes(accessPaths);
                        d.dg("funcDIRMap domain = " + FuncStackAnalyzer.funcDIRMap.keySet());
                        d.dg("funcDIRMap domain contains callee = " + FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig));
                        Map <VarNode, Node> calleeVEMap = FuncStackAnalyzer.funcDIRMap.get(invokedSig).getVeMap();
                        d.dg("Printing ve map of callee = " + invokedSig);
                        for(VarNode vn : calleeVEMap.keySet()) {
                            d.dg("key = " + vn);
                            d.dg("val = " + calleeVEMap.get(vn));
                        }
                        d.dg("Printing ve map of callee = " + invokedSig + " END");

//                        for(int i = 0; i < attributes.size(); i++) {
//                            VarNode key = new VarNode(accessPaths.get(i).toString());
//                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "key = " + key);
//                            VarNode lookup = new VarNode("return." + attributes.get(i));
//                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "lookup = " + lookup);
//                            Node val = calleeVEMap.get(lookup);
//                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "val = " + val);
//
//                            dir.insert(key, val);
//                        }

                        for(AccessPath ap : accessPaths) {
                            VarNode key = new VarNode(ap.toString());
                            d.dg("key = " + key);
                            VarNode lookup = new VarNode("return" + ap.toString().substring(ap.toString().indexOf(".")));
                            d.dg("lookup = " + lookup);
                            if(calleeVEMap.containsKey(lookup)) {
                                Node val = calleeVEMap.get(lookup);
                                d.dg("val = " + val);
                                dir.insert(key, val);
                            }
                            else {
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
                else if(curUnit instanceof JInvokeStmt) {
                    JInvokeStmt invokeStmt = (JInvokeStmt) curUnit;
                    String invokedSig = invokeStmt.getInvokeExpr().getMethodRef().getSignature();
                    analyzeBCEL(invokedSig);
                    ARegion topRegion = FuncStackAnalyzer.funcRegionMap.get(invokedSig);
                    topRegion.analyze();
                    DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
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
                    JimpleLocal local = new JimpleLocal("__modelattribute__" + attributeNameStr, attributeValue.getType());
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
            catch (UnknownStatementException e) {
                //Temporary fix. Todo: pass exception to higher level.
                e.printStackTrace();
                return null;
            } catch (RegionAnalysisException e) {
                e.printStackTrace();
            }

        }

        return dir;
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
}
