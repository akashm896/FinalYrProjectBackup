package dbridge.analysis.eqsql.analysis;

import io.geetam.github.OptionalTypeInfo;
import io.geetam.github.accesspath.AccessPath;
import io.geetam.github.accesspath.Flatten;
import io.geetam.github.formalToActualVisitor.FormalToActual;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.hibernate.construct.StmtDIRConstructionHandler;
import dbridge.analysis.eqsql.hibernate.construct.StmtInfo;
import dbridge.analysis.eqsql.util.SootClassHelper;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import exceptions.UnknownStatementException;
import dbridge.analysis.eqsql.util.VarResolver;
import dbridge.analysis.region.regions.ARegion;
import mytest.debug;
import org.apache.commons.lang.SerializationUtils;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;
import soot.toolkits.graph.Block;

import java.util.*;
import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.trim;
import static io.geetam.github.OptionalTypeInfo.*;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.fetchBaseValue;

import com.rits.cloning.*;
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
        d.dg("Basic block num: " + basicBlock.getIndexInMethod());
        d.dg("basic block = " + basicBlock.getBody().getUnits());
        Iterator<Unit> iterator = basicBlock.iterator();
        DIR dir = new DIR(); //dir for this region
        StmtInfo stmtInfo = StmtInfo.nullInfo;
        while (iterator.hasNext()) {
            Unit curUnit = iterator.next();
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit.toString());
            if(curUnit.toString().equals("$r3 = virtualinvoke userPayment.<com.bookstore.domain.UserPayment: java.lang.Long getId()>()")) {
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
                        Node resolvedSource = getResolvedEEDag(dir, source);
                        dir.insert(dest, resolvedSource);
                    }
                }
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
                    //CASE: v = iterator.next()
//                    if(rhsVal instanceof InvokeExpr && rhsVal.toString().contains("<java.util.Iterator: java.lang.Object next()>")) {
//                        d.dg("CASE: v = iterator.next()");
//                        String tableClass = leftVal.getType().toString();
//                        List <AccessPath> flattenedIterator = Flatten.flattenEntity(leftVal, leftVal.getType());
//                        List <String> tableAttributes = Flatten.attributes(flattenedIterator);
//                        for(int i = 0; i < flattenedIterator.size(); i++) {
//                            String atr = tableAttributes.get(i);
//                            FieldRefNode fieldRefAtr = new FieldRefNode(tableClass, atr, tableClass);
//                            VarNode varNodeAtr = new VarNode(flattenedIterator.get(i).toString());
//                            dir.insert(varNodeAtr, fieldRefAtr);
//                        }
//                        d.dg("flattenedIterator = " + flattenedIterator);
//                    }
                    //CASE: v1 = v2, type(v1, v2) = ptr
                    if(leftVal instanceof JimpleLocal && rhsVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())) {
                        d.dg("CASE: v1 = v2, type(v1, v2) = ptr");
                        DIR dirStmt = processPointerAssignment(leftVal, rhsVal, dir);
                        dir.getVeMap().putAll(dirStmt.getVeMap());
                    }
                    //CASE: v1 = (type1) v2
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JCastExpr) {
                        d.dg("CASE: v1 = (type1) v2");
                        JCastExpr castExpr = (JCastExpr) rhsVal;
                        Value right = castExpr.getOp();
                        Type castType = castExpr.getCastType();
                        d.dg("type1: " + castType);
                        d.dg("castExpr: " + castExpr);
                        d.dg("v2: " + right);
                        if(right.getType() instanceof RefType) {
                            VarNode rightVar = new VarNode(right);
                            d.dg("cur dir: " + dir.getVeMap());
                            boolean rightInDIR = dir.getVeMap().containsKey(rightVar);
                            d.dg("rightVar in dir: " + dir.getVeMap().containsKey(rightVar));
                            if (rightInDIR && dir.getVeMap().get(rightVar) instanceof NextNode) {
                                d.dg("CASE: actual_iterator = (type1) it");
                                Node rightMapping = dir.getVeMap().get(rightVar);
                                d.dg("rightVar's value in dir: " + rightMapping);
                                if (rightMapping instanceof NextNode) {
                                    String castTypeStr = castType.toString();
                                    List<AccessPath> flattenedIterator = Flatten.flattenEntity(leftVal, castType);
                                    List<String> tableAttributes = Flatten.attributes(flattenedIterator);
                                    for (int i = 0; i < flattenedIterator.size(); i++) {
                                        String atr = tableAttributes.get(i);
                                        FieldRefNode fieldRefAtr = new FieldRefNode(castTypeStr, atr, castTypeStr);
                                        VarNode varNodeAtr = new VarNode(flattenedIterator.get(i).toString());
                                        dir.insert(varNodeAtr, fieldRefAtr);
                                    }
                                    d.dg("flattenedIterator = " + flattenedIterator);
                                    d.dg("dir: " + dir.getVeMap());

                                }
                            } else if (AccessPath.isCollectionType(castType)) {
                                VarNode rvnode = new VarNode(right);
                                Node resolvedMapping = getResolvedEEDag(dir, rvnode);
                                dir.insert(new VarNode(leftVal), resolvedMapping);
                            } else {
                                DIR dirStmt = processPointerAssignment(leftVal, right, dir);
                                dir.getVeMap().putAll(dirStmt.getVeMap());
                            }
                        }
                        //CASE: v1 = (type1) v2, v1 and v2 are primitives
                        else {
                            VarNode rvnode = new VarNode(right);
                            Node resolvedMapping = getResolvedEEDag(dir, rvnode);
                            dir.insert(new VarNode(leftVal), resolvedMapping);
                        }
                    }
                    //CASE: v.f = expr
                    else if(leftVal instanceof JInstanceFieldRef) {
                        JInstanceFieldRef fieldRef = (JInstanceFieldRef) leftVal;
                        SootField field = fieldRef.getField();
                        //SUBCASE: v.f = expr, f is primitive
                        if(AccessPath.isTerminalType(field.getType())) {
                            debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v.f = expr, f is primitive");
                            Node rhsNode = NodeFactory.constructFromValue(rhsVal);
                            Node resolvedRHS = getResolvedEEDag(dir, rhsNode);
                            String accpStr = fieldRef.getBase().toString() + "." + fieldRef.getField().getName();
                            d.dg("Mapping: " + accpStr + " -> " + resolvedRHS);
                            dir.insert(new VarNode(accpStr), resolvedRHS);
                        }
                        //SUBCASE: v.f = expr, f is not primitive
                        else {
                            d.dg("CASE: v.f = expr, f is non-primtive");
                            List <AccessPath> accessPaths = Flatten.flatten(leftVal, leftVal.getType(), 1);
                            d.dg("accessPaths = " + accessPaths);
                            d.dg("right val = " + rhsVal);
                            //subcase: v1.f = v2
                            d.dg("Subcase: v1.f = v2");
                            for(AccessPath ap : accessPaths) {
                                AccessPath rightAP = AccessPath.replaceBase(ap, rhsVal.toString());
                                //TODO: Check if rightAP.toVarNode() should be resolved first.
                                dir.insert(ap.toVarNode(), rightAP.toVarNode());
                                d.dg("Mapped: " + ap.toString() + " -> " + rightAP.toString());
                            }
                            //can subcase v.f = methodcall be present?
                        }

                    }
                    //CASE v1 = v2.f
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JInstanceFieldRef) {
                        Type exprsType = leftVal.getType();
                        //Subcase f is not primitive
                        //TODO: Find a better way to find repositories
                        if(!AccessPath.isTerminalType(exprsType)
                                && /*rhsVal.toString().contains("repository") == false*/ valueIsRepository(rhsVal) == false) {
                            d.dg("CASE v1 = v2.f, f is not primitive");
                            List <AccessPath> destPaths = Flatten.flatten(leftVal, exprsType, 0);
                            d.dg("destPaths: " + destPaths);
                            for(AccessPath daccp : destPaths) {
                                AccessPath saccp = AccessPath.replaceBase(daccp, rhsVal.toString());
                                int saccpLength = daccp.getBase().length() + 1;
                                if(saccpLength <= Flatten.BOUND) {
                                    VarNode rhsVN = saccp.toVarNode();
                                    Node resolvedAccp = getResolvedEEDag(dir, rhsVN);
                                    dir.insert(daccp.toVarNode(), resolvedAccp);
                                    d.dg("mapping " + daccp.toVarNode() + " -> " + resolvedAccp);
                                }
                                else {
                                    //TODO: Maybe create a bot node
                                    dir.insert(daccp.toVarNode(), new NullNode());
                                }
                            }
                        } else {
                            d.dg("CASE v1 = v2.f, f is primitive");
                            JInstanceFieldRef fieldRef = (JInstanceFieldRef) rhsVal;
                            String rhsStr = fieldRef.getBase().toString() + "." + fieldRef.getField().getName();
                            VarNode rhsVarNode = new VarNode(rhsStr);
                            Node rhsResolved = getResolvedEEDag(dir, rhsVarNode);
                            JimpleLocal leftLocal = (JimpleLocal) leftVal;
                            VarNode leftVarNode = new VarNode(leftLocal);
                            dir.insert(leftVarNode, rhsResolved);
                            d.dg("mapping " + leftVarNode + " -> " + rhsResolved);

                        }
                    }
                    //CASE: v = new
                    else if (leftVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())
                            && rhsVal instanceof JNewExpr) {
                        debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v = new");
                        List <AccessPath> accessPaths = Flatten.flatten(leftVal, leftVal.getType(), 0);
                        for(AccessPath ap : accessPaths) {
                            VarNode vn = new VarNode(ap.toString());
                            System.out.println("Mapping " + ap.toString() + " to Bottomnode");
                            dir.insert(vn, BottomNode.v());
                        }
                    }

                    //CASES containing method calls in rhs
                    if(rhsVal instanceof InvokeExpr) {
                        InvokeExpr invokeExpr = (InvokeExpr) rhsVal;
                        //Updates func dir map
                        //Condition for library methods: node not instance of MethodWonthandle and not instance of NonLibraryMeth
                        Node methodRet = Utils.parseInvokeExpr(invokeExpr);
                        //CASE: v1 = v2.foo(v3), foo is library method
                        if (methodRet instanceof MethodWontHandleNode == false && methodRet instanceof NonLibraryMethodNode == false) {
                            stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                            if (stmtInfo == StmtInfo.nullInfo) {
                                continue;
                            }
                            VarNode dest = stmtInfo.getDest();
                            Node source = stmtInfo.getSource();
                            Node resolvedSource = getResolvedEEDag(dir, source);
                            dir.insert(dest, resolvedSource);
                        } else if (methodRet instanceof NonLibraryMethodNode) {

                            //CASE v1 = v2.foo(v3), foo isn't a library method
                            if (!AccessPath.isPrimitiveType(leftVal.getType())) {
                                debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE v1 = v2.foo(v3)");
                                String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethodRef().getSignature());
                                //Map <VarNode, Node> veMapCallee = FuncStackAnalyzer.funcDIRMap.get(invokedSig).getVeMap();
                                //TODO: get actual type in case of Optional, flatten and then implement handleSideEffects
                                Type leftType = leftVal.getType();
                                d.dg("left type = " + leftType);
                                if (leftVal.getType().toString().equals("java.util.Optional")) {
                                    d.dg("v1 type is optional");
                                    debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "invokedSig = " + invokedSig);
                                    Map<String, String> typeTable = OptionalTypeInfo.analyzeBCEL(invokedSig);
                                    debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", " leftVal = " + leftVal);
                                    // String actualType = OptionalTypeInfo.typeMap.get(leftVal.toString());
                                    String lookup = "return_" + invokedSig;
                                    String actualType = typeTable.get(lookup);
                                    if (actualType != null) {
                                        debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "actualType = " + actualType);
                                        SootClass typeSC = Scene.v().loadClassAndSupport(actualType);
                                        leftType = typeSC.getType();
                                    } else {
                                        d.wrn("Actual type of Optional could not be determined");
                                        d.dg("typemap = " + typeMap);
                                        d.dg("lookup = " + leftVal);
                                    }
                                }
                                d.dg("method sig = " + invokedSig);
                                if (invokedSig.equals("java.util.Optional: java.lang.Object get()")) {
                                    d.dg("get invoked");
                                    Value base = fetchBaseValue(invokeExpr);
                                    d.dg("base = " + base);
                                    d.dg("typemap = " + OptionalTypeInfo.typeMap);
                                    String lt = OptionalTypeInfo.typeMap.get(base.toString());
                                    leftType = Scene.v().loadClassAndSupport(lt).getType();
                                    d.dg("leftType = " + leftType);

                                }
                                DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
                                Map<VarNode, Node> calleeVEMap = calleeDIR.getVeMap();
                                //TODO: handle side effects here also, logic is in case v1.foo(v2). Also make the code less complex
                                if (!AccessPath.isTerminalType(leftType)) { //Case where flattening can and should be done
                                    d.dg("going to flatten (var, type) = " + leftVal + ", " + leftType);
                                    List<AccessPath> accessPaths = Flatten.flatten(leftVal, leftType, 0);
                                    List<String> attributes = Flatten.attributes(accessPaths);
                                    d.dg("funcDIRMap domain = " + FuncStackAnalyzer.funcDIRMap.keySet());
                                    d.dg("callee = " + invokedSig);
                                    d.dg("funcDIRMap domain contains callee = " + FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig));

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
                                } else {
                                    d.dg("CASE v1 = v2.foo(v3), type of v1 is terminal");
                                    d.dg("calleeDIR domain " + calleeDIR.getVeMap().keySet());
                                    VarNode retnode = RetVarNode.getARetVar();
                                    Node dag = callersDagForCalleesKey(retnode, calleeDIR, dir, invokeExpr);
                                    dir.insert(new VarNode(leftVal), dag);
                                }
                            }
                            //CASE: v1 = v2.foo(v3),
                            //v1 is primitive, foo is not a library method
                            else if (AccessPath.isPrimitiveType(leftVal.getType())) {
                                handleSideEffects(dir, invokeExpr);
                                VarNode retNode = RetVarNode.getARetVar();
                                String methodSig = trim(invokeExpr.getMethod().getSignature());
                                d.dg("methodSig: " + methodSig);
                                d.dg("FuncStackAnalyzer.funcDIRMap: " + FuncStackAnalyzer.funcDIRMap);
                                DIR dirCallee = FuncStackAnalyzer.funcDIRMap.get(methodSig);
                                d.dg("dir callee: " + dirCallee);
                                Node retCallee = dirCallee.getVeMap().get(retNode);
                                Cloner cloner = new Cloner();
                                Node retCalleeCloned = cloner.deepClone(retCallee);
                                d.dg("retCalleeCloned: " + retCalleeCloned);
                                retCalleeCloned = dagFormalsToActuals(retCalleeCloned, invokeExpr);
                                d.dg("after dag formals to actuals: " + retCalleeCloned);
                                Node retEEDag = getResolvedEEDag(dir, retCalleeCloned);
                                JimpleLocal leftLocal = (JimpleLocal) leftVal;
                                VarNode leftVarNode = new VarNode(leftLocal);
                                dir.insert(leftVarNode, retEEDag);
                            }
                        }
                        else if(methodRet instanceof MethodWontHandleNode) {
                            JimpleLocal leftLocal = (JimpleLocal) leftVal;
                            VarNode leftVarNode = new VarNode(leftLocal);
                            dir.insert(leftVarNode, methodRet);
                        }
                    }
                }
                //CASE v1.save(v2)
                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("save(") && curUnit.toString().contains("Repository")) {
                    JInvokeStmt saveStmt = (JInvokeStmt) curUnit;
                    d.dg("savestmt: " + saveStmt);
                    JInterfaceInvokeExpr invokeExpr = (JInterfaceInvokeExpr) saveStmt.getInvokeExpr();
                    d.dg("savestmt invoke expr: " + invokeExpr);
                    Value base = invokeExpr.getBase();
                    VarNode baseVarNode = new VarNode(base);
                    d.dg("baseVarNode: " + baseVarNode);
                    d.dg("dir till now: " + dir);
                    VarNode repo = (VarNode) dir.find(baseVarNode);
                    if(repo == null)
                        repo = baseVarNode;
                    d.dg("ve map:" + dir.getVeMap());
                    d.dg("repo: " + repo);
                    Value itval = invokeExpr.getArg(0);
                    Collection <VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(itval);
                    List <FieldRefNode> columns = new ArrayList<>();
                    RefType argType = (RefType) itval.getType();
                    List <String> attributes = Flatten.flattenEntityClass(argType.getSootClass());
                    String table = argType.toString();
                    d.dg("argType: " + argType);
                    d.dg("table: " + table);
                    d.dg("attributes: " + attributes);
                    d.dg("fieldVarNodes: " + fieldVarNodes);
                    for(String att : attributes) {
                        FieldRefNode attFR = new FieldRefNode(table, att, table);
                        columns.add(attFR);
                    }
                    List <Node> fieldExprs = new ArrayList<>();
                    for(VarNode vn : fieldVarNodes) {
                        if(dir.getVeMap().containsKey(vn)) {
                            fieldExprs.add(dir.find(vn));
                        }
                        else fieldExprs.add(vn);
                    }
                    ListNode listNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
                    listNode.columns.addAll(columns);
                    d.dg("listNode.columns: " + listNode.columns);
                  //  AddWithFieldExprsNode addWithFieldExprsNode = new AddWithFieldExprsNode(listNode);
                  //  dir.insert(repo, addWithFieldExprsNode);
              //      d.dg("mapping: " + repo + " -> " + addWithFieldExprsNode);
                    SaveNode saveNode = new SaveNode(listNode);
                    dir.insert(repo, saveNode);
                    d.dg("mapping: " + repo + " -> " + saveNode);

                    d.dg("savestmt args: " + saveStmt.getInvokeExpr().getArgs());
                }
                //CASE v1.delete(v2)
                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("delete(") && curUnit.toString().contains("Repository")) {
                    JInvokeStmt deleteStmt = (JInvokeStmt) curUnit;
                    d.dg("deleteStmt: " + deleteStmt);
                    JInterfaceInvokeExpr invokeExpr = (JInterfaceInvokeExpr) deleteStmt.getInvokeExpr();
                    d.dg("deleteStmt invoke expr: " + invokeExpr);
                    Value base = invokeExpr.getBase();
                    VarNode baseVarNode = new VarNode(base);
                    d.dg("baseVarNode: " + baseVarNode);
                    d.dg("dir till now: " + dir);
                    VarNode repo = (VarNode) dir.find(baseVarNode);
                    if(repo == null)
                        repo = baseVarNode;
                    d.dg("ve map:" + dir.getVeMap());
                    d.dg("repo: " + repo);
                    Value itval = invokeExpr.getArg(0);
                    Collection <VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(itval);
                    List <FieldRefNode> columns = new ArrayList<>();
                    RefType argType = (RefType) itval.getType();
                    List <String> attributes = Flatten.flattenEntityClass(argType.getSootClass());
                    String table = argType.toString();
                    d.dg("argType: " + argType);
                    d.dg("table: " + table);
                    d.dg("attributes: " + attributes);
                    d.dg("fieldVarNodes: " + fieldVarNodes);
                    for(String att : attributes) {
                        FieldRefNode attFR = new FieldRefNode(table, att, table);
                        columns.add(attFR);
                    }
                    List <Node> fieldExprs = new ArrayList<>();
                    for(VarNode vn : fieldVarNodes) {
                        if(dir.getVeMap().containsKey(vn)) {
                            fieldExprs.add(dir.find(vn));
                        }
                        else fieldExprs.add(vn);
                    }
                    ListNode listNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
                    listNode.columns.addAll(columns);
                    d.dg("listNode.columns: " + listNode.columns);
                    //  AddWithFieldExprsNode addWithFieldExprsNode = new AddWithFieldExprsNode(listNode);
                    //  dir.insert(repo, addWithFieldExprsNode);
                    //      d.dg("mapping: " + repo + " -> " + addWithFieldExprsNode);
                    RelMinusNode minusNode = new RelMinusNode(repo, listNode);
                    dir.insert(repo, minusNode);
                    d.dg("mapping: " + repo + " -> " + minusNode);

                    d.dg("deleteStmt args: " + deleteStmt.getInvokeExpr().getArgs());
                }

                //CASE: v1.foo(v2)
                else if(curUnit instanceof JInvokeStmt) {
                    d.dg("CASE: v1.foo(v2)");
                    JInvokeStmt invokeStmt = (JInvokeStmt) curUnit;
                    handleSideEffects(dir, invokeStmt.getInvokeExpr());
                }
                //TODO: Check if this is required, keeping it only for consistency with base DBridge

                else {
                    stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                    if (stmtInfo == StmtInfo.nullInfo) {
                        continue;
                    }
                    VarNode dest = stmtInfo.getDest();
                    Node source = stmtInfo.getSource();
                    Node resolvedSource = getResolvedEEDag(dir, source);
                    dir.insert(dest, resolvedSource);
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
        d.dg("BasicBlockRegion: " + region);
        d.dg("BasicBlockDIR: " + dir);
        return dir;
    }

    private Node getResolvedEEDag(DIR dir, Node root) {
        VarResolver varResolver = new VarResolver(dir);
        return root.accept(varResolver);
    }

    private void handleSideEffects(DIR dir, InvokeExpr invokeExpr) throws RegionAnalysisException {
        debug d = new debug("DIRRegionAnalyzer.java", "handleSideEffects()");
        Value base = fetchBaseValue(invokeExpr);
        d.dg("invokeExpr = " + invokeExpr);
        List<Value> actualArgs = invokeExpr.getArgs();
        boolean invokeIsStatic = invokeExpr instanceof StaticInvokeExpr;
        if(invokeIsStatic == false)
            actualArgs.add(base);

        String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());
        d.dg("invokedSig = " + invokedSig);
        if(FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
            SootMethod method =invokeExpr.getMethod();
            d.dg("soot method = " + method);
            JimpleBody jbod =  (JimpleBody) FuncStackAnalyzer.funcBodyMap.get(invokedSig);

            //List<Local> formalArgs = (method.getActiveBody()).getParameterLocals();

            List<Local> formalArgs = getParameterLocals(jbod);
            if(invokeIsStatic == false)
                formalArgs.add(jbod.getThisLocal());
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
                d.dg("ith formal: " + formal);
                d.dg("ith formal type: " + formalType);
                d.dg("ith actual: " + actual);
                if(!AccessPath.isTerminalType(formalType)) {
                    d.dg("Going to flatten: " + formal);
                    List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType, 0);
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
                d.dg("formal ith: " + formal);
                d.dg("actual ith: " + actual);
                Type formalType = getLocalsActualType(invokedSig, formal);
                if(!AccessPath.isTerminalType(formalType)) {
                    //This is the list of formal access paths that will be replace by actuals in eedag of affectedKeys
                    List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType, 0);
                    d.dg("formalAccpList = " + formalAccpList);
                    for (AccessPath formalAccp : formalAccpList) {
                        String formalAccpStr = formalAccp.toString();
                        String actualAccpStr = actual.toString() + formalAccpStr.substring(formalAccpStr.indexOf("."));
                        Node formalAccpNode = new VarNode(formalAccpStr);
                        Node actualAccpNode = new VarNode(actualAccpStr);
                        FormalToActual formalToActualVisitor = new FormalToActual(formalAccpNode, actualAccpNode);
                        for(Node key : affectedKeys) {
                            Node eedag = dir.find((VarNode) key);
                            d.dg("callee dir: " + dir);
                            d.dg("for key = " + key);
                            d.dg("eedag before formaltoactual: " + eedag);
                            Node newEEDag = eedag.accept(formalToActualVisitor);
                            if(newEEDag instanceof VarNode) {
                                VarNode newEEVarNode = (VarNode) newEEDag;
                                if(dir.contains(newEEVarNode)) {
                                    dir.insert((VarNode) key, dir.find(newEEVarNode));
                                }
                                else {
                                    dir.insert((VarNode) key, newEEDag);
                                }
                            }
                            else {
                                dir.insert((VarNode) key, newEEDag);
                            }
                            d.dg("after: " + dir.find((VarNode) key));
                        }
                    }
                } else {
                    VarNode fml = new VarNode((JimpleLocal)formal);
                    d.dg("Case the formal param is a primitive");
                    d.dg("fml: " + fml);
                    d.dg("actual arg (Soot Value): " + actual);
                    Node actualNode = NodeFactory.constructFromValue(actual);
                    d.dg("actualNode (Node constructed from value): " + actualNode);
//                    if(actualNode instanceof VarNode) {
//                        VarNode lookup = (VarNode) actualNode;
//                        d.dg("Renaming formals to actuals in affected keys: curr formal is terminal-typed. DIR of preceding statements: ");
//                        System.out.println(dir);
//                        d.dg("END of preceding dir");
//                        d.dg("lookup: " + lookup);
//                        actualNode = dir.find(lookup);
//
//                    }
                    d.dg("formal = " + fml + ", actual = " + actualNode);
                    Node resolvedActual = getResolvedEEDag(dir, actualNode);
                    d.dg("resolvedActual: " + resolvedActual);
                    FormalToActual formalToActualVisitor = new FormalToActual(fml, resolvedActual);
                    for(Node key : affectedKeys) {
                        Node eedag = dir.find((VarNode) key);
                        d.dg("for key = " + key);
                        d.dg("eedag before formaltoactual: " + eedag);
                        Node newEEDag = eedag.accept(formalToActualVisitor);
                        dir.insert((VarNode) key, newEEDag);
                        d.dg("Case the formal param is a primitive, " + eedag);
                    }
                }
            }
        }
        else {
            d.wrn("Wont handle method");
        }
    }

    /*
        root: a dag that references formal params.
        output: an equivalent dag with formals renamed to actuals
     */
    public Node dagFormalsToActuals(Node root, InvokeExpr invokeExpr) {
        debug d = new debug("DIRRegionAnalyzer.java", "dagFormalsToActuals()");
        //This list contains elements that are either terminal access paths, primitives or collections
        List<Node> formalList = new ArrayList<>();
        List<Node> actualList = new ArrayList<>();
        boolean invokeIsStatic = invokeExpr instanceof StaticInvokeExpr;

        Value base = fetchBaseValue(invokeExpr);
        d.dg("invokeExpr = " + invokeExpr);
        List<Value> actualArgs = invokeExpr.getArgs();
        if(invokeIsStatic == false)
            actualArgs.add(base);
        String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());
        d.dg("invokedSig = " + invokedSig);
        d.dg("FuncStackAnalyzer.funcRegionMap.domain: " + FuncStackAnalyzer.funcRegionMap.keySet());
        if (FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
            SootMethod method = invokeExpr.getMethod();
            d.dg("soot method = " + method);
            JimpleBody jbod =  (JimpleBody) FuncStackAnalyzer.funcBodyMap.get(invokedSig);

            //List<Local> formalArgs = (method.getActiveBody()).getParameterLocals();

            List<Local> formalArgs = getParameterLocals(jbod);
            if(invokeIsStatic == false)
                formalArgs.add(jbod.getThisLocal());
            d.dg("formalArgs = " + formalArgs);
            d.dg("actualArgs = " + actualArgs);
            if (formalArgs.size() != actualArgs.size()) {
                d.dg("WARN: unequal number of actual and formal args");
            }
            for (int i = 0; i < formalArgs.size(); i++) {
                Local formal = formalArgs.get(i);
                Value actual = actualArgs.get(i);
                Type formalType = getLocalsActualType(invokedSig, formal);
                if (!AccessPath.isTerminalType(formalType)) {
                    //This is the list of formal access paths that will be replace by actuals in eedag of affectedKeys
                    List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType, 0);
                    d.dg("formalAccpList = " + formalAccpList);
                    for (AccessPath formalAccp : formalAccpList) {
                        String formalAccpStr = formalAccp.toString();
                        String actualAccpStr = actual.toString() + formalAccpStr.substring(formalAccpStr.indexOf("."));
                        Node formalAccpNode = new VarNode(formalAccpStr);
                        Node actualAccpNode = new VarNode(actualAccpStr);
                        formalList.add(formalAccpNode);
                        actualList.add(actualAccpNode);
                    }
                } else {
                    VarNode fml = new VarNode((JimpleLocal) formal);
                    Node actualNode = NodeFactory.constructFromValue(actual);
                    d.dg("formal = " + fml + ", actual = " + actualNode);
                    formalList.add(fml);
                    actualList.add(actualNode);
                }
            }
            Node ret = root;
            for (int i = 0; i < formalList.size(); i++) {
                FormalToActual ithFtoAVisitor = new FormalToActual(formalList.get(i), actualList.get(i));
                ret = ret.accept(ithFtoAVisitor);
            }
            return ret;
        }
        //TODO: Do we need renaming for functions without a body?
        return root;
    }

    public Node formToActAndResolve(Node root, InvokeExpr invokeExpr, DIR dir) {
        Node ret = dagFormalsToActuals(root, invokeExpr);
        ret = getResolvedEEDag(dir, ret);
        return ret;
    }

    // Ideally this method should always be used when peeking inside ve map of a callee
    public Node callersDagForCalleesKey(VarNode calleeKey, DIR calleeDIR, DIR callerDIR, InvokeExpr invokeExpr) {
        debug d = new debug("DIRRegionAnalyzer.java", "callersDagForCalleesKey()");
        d.dg("calleeKey: " + calleeKey);
        Cloner cloner = new Cloner();
        Node calleeDag = cloner.deepClone(calleeDIR.find(calleeKey));
        d.dg("calleeDag: " + calleeDag);
        Node ret = dagFormalsToActuals(calleeDag, invokeExpr);
        ret = getResolvedEEDag(callerDIR, ret);
        return ret;
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
        debug d = new debug("DIRRegionAnalyzer.java", "processPointerAssignment()");
        DIR ret = new DIR();
        if(v1.getType().equals(v2.getType()) == false) {
            d.wrn("v1 and v2 have diff types");
            d.wrn("type(v1) = " + v1.getType());
            d.wrn("type(v2) = " + v2.getType());
        }
        d.dg("lhs of pointer assignment: " + v1);
        d.dg("rhs of pointer assignment: " + v2);

        List <AccessPath> v1Paths = Flatten.flatten(v1, v1.getType(), 0);
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
    //TODO: maybe should be defined in one of the Utils file, maybe we can have a single Util file for auxillary functions?
    public boolean valueIsRepository(Value val) {
        debug d = new debug("DIRRegionAnalyzer.java", "valIsRepository()");
        d.dg("fpar val: " + val);
        assert val.getType() instanceof RefType;
        RefType refType = (RefType) val.getType();
        d.dg("refType: " + refType);
        SootClass valClass = refType.getSootClass();
        d.dg("valClass: " + valClass.getName());
//        if (valClass.hasSuperclass() == false)
//            return false;
//        SootClass valSuperClass = valClass.getSuperclass();
//        d.dg("valSuperClass: " + valSuperClass.getName());
//        boolean ret = valSuperClass.getName().contains("Repository");
        boolean ret = valClass.getName().contains("Repository");
        d.dg("ret: " + ret);
        return ret;
    }


    /* Soot - a J*va Optimization Framework
     * Copyright (C) 1997-1999 Raja Vallee-Rai
     *
     * This library is free software; you can redistribute it and/or
     * modify it under the terms of the GNU Lesser General Public
     * License as published by the Free Software Foundation; either
     * version 2.1 of the License, or (at your option) any later version.
     *
     * This library is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
     * Lesser General Public License for more details.
     *
     * You should have received a copy of the GNU Lesser General Public
     * License along with this library; if not, write to the
     * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
     * Boston, MA 02111-1307, USA.
     */

    /*
     * Modified by the Sable Research Group and others 1997-1999.
     * See the 'credits' file distributed with Soot for the complete list of
     * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
     */
    /**
     * Get all the LHS of the identity statements assigning from parameter references.
     *
     * @return a list of size as per <code>getMethod().getParameterCount()</code> with all elements ordered as per the parameter index.
     * @throws RuntimeException if a parameterref is missing
     */
    public static List<Local> getParameterLocals(Body body){
        debug d = new debug("DIRRegionAnalyzer.java", "getParameterLocals()");
        final int numParams = body.getMethod().getParameterCount();
        final List<Local> retVal = new ArrayList<Local>(numParams);
        d.dg("printing body of method: " + body.getMethod().getName() + " whose locals are to be extracted");
        for(Unit u : body.getUnits()) {
            System.out.println(u);
        }
        //Parameters are zero-indexed, so the keeping of the index is safe
        for (Unit u : body.getUnits()){
            d.dg("Potential param unit, u: " + u);
            if (u instanceof IdentityStmt){
                d.dg("u is IdentityStmt");
                IdentityStmt is = ((IdentityStmt)u);
                Value uRight = is.getRightOp();
                Value uLeft = is.getLeftOp();
                d.dg("rhs of u: " + uRight);
                d.dg("lhs of u: " + uLeft);
                d.dg("class of rhs: " + uRight.getClass());
                d.dg("class of lhs: " + uLeft.getClass());
                if (uRight instanceof ParameterRef){
                    d.dg("rhs is a ParameterRef");
                    ParameterRef pr = (ParameterRef) uRight;
                    retVal.add(pr.getIndex(), (Local) uLeft);
                } else {
                    d.dg("rhs is not ParameterRef");
                }
            }
            else {
                d.dg("u is not IdentityStmt");
            }
            d.dg("");
        }
        d.dg("retVal.size(): " + retVal.size());
        d.dg("numParams: " + numParams);
        if (retVal.size() != numParams)
            throw new RuntimeException("couldn't find parameterref! in " + body.getMethod());
        d.dg("returning");
        return retVal;
    }
}



