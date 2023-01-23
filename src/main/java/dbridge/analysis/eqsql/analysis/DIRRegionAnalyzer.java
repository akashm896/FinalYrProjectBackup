/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/
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
package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.operator.ClassRefOp;
import io.geetam.github.OptionalTypeInfo;
import io.geetam.github.RepoToEntity.RepoToEntity;
import io.geetam.github.accesspath.AccessPath;
import io.geetam.github.accesspath.Flatten;
import io.geetam.github.accesspath.NRA;
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
import io.geetam.github.loopHandler.DAGTillNow;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import jas.Var;
import mytest.debug;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.mapping.Join;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.Block;

import java.util.*;
import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.*;
import static io.geetam.github.OptionalTypeInfo.*;
import static io.geetam.github.accesspath.NRA.genExprNra;

import io.geetam.github.accesspath.NRA;

import com.rits.cloning.*;
import soot.util.Chain;
import com.iisc.pav.AlloyGenerator;
/**
 * Created by ek on 4/5/16.
 */
public class DIRRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    public SootMethod curmethod;

    /* Singleton */
    private DIRRegionAnalyzer(){};
    public static DIRRegionAnalyzer INSTANCE = new DIRRegionAnalyzer();



    @Override
    public DIR constructDIR(ARegion region) {
        debug d = new debug("DIRRegionAnalyzer.java", "constructDIR()");
        d.dg("region = "+region);
        Block basicBlock = region.getHead();
        d.dg("Basic block num: " + basicBlock.getIndexInMethod());
        d.dg("basic block = " + basicBlock.getBody().getUnits());
        Iterator<Unit> iterator = basicBlock.iterator();
        DIR dir = new DIR(); //dir for this region
        StmtInfo stmtInfo = StmtInfo.nullInfo;
        while (iterator.hasNext()) {
            Unit curUnit = iterator.next();
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit.toString());
            d.dg("curUnit = "+curUnit);
            // Workaround for soot bug where iterator of for (iterator : arr) is incremented instead of fetch next from Array.
            // i.e. ideally it should be iterator = arr[i++] in ith of the loop.

            if(curUnit.toString().contains("$r8 = new java.util.HashSet"))
                d.dg("Debug stop");

            if(curUnit instanceof JAssignStmt
                    && AccessPath.isTerminalType(((JAssignStmt) curUnit).getLeftOp().getType())
                    && dir.getVeMap().containsKey(new VarNode(((JAssignStmt) curUnit).getLeftOp()))
                    && dir.getVeMap().get(new VarNode(((JAssignStmt) curUnit).getLeftOp())) instanceof ArrayRefNode)
            {
                d.dg("curUnit instance of JAssignStmt and other conditions");
                VarNode vn = new VarNode("iteration_over_array");
                dir.insert(vn, new OneNode());
                continue;
            }
            if(curUnit.toString().contains("total = staticinvoke <java.lang.Double")) {
                d.dg("break point 2");
            }
            try {



                if(curUnit instanceof JReturnStmt) {
                    d.dg("curUnit instance of JReturnStmt");
                    caseReturnStmt(dir, (JReturnStmt) curUnit);
                }
                if(curUnit instanceof JGotoStmt) {
                    d.dg("GOTO stmt in seq region");
                }
                if(curUnit instanceof JAssignStmt) {
                    d.dg("curUnit instance of JAssignStmt");
                    debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit);
                    JAssignStmt stmt = (JAssignStmt) curUnit;
                    Value leftVal = stmt.leftBox.getValue();
                    Value rhsVal = stmt.rightBox.getValue();
                    if(stmt.toString().equals("$r1 = staticinvoke <java.lang.Long: java.lang.Long valueOf(long)>(id)")) {
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


                    if(rhsVal instanceof JArrayRef) {
                        JArrayRef aref = (JArrayRef) rhsVal;
                        Value arr = aref.getBase();
                        Value index = aref.getIndex();
                        ArrayRefNode arn = new ArrayRefNode(NodeFactory.constructFromValue(arr), NodeFactory.constructFromValue(index));
                        dir.insert(new VarNode(leftVal), arn);
                    }
                    //CASE: v1 = v2, type(v1, v2) = ptr
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())) {
                        d.dg("CASE: v1 = v2, type(v1, v2) = ptr");
                        DIR dirStmt = processPointerAssignment(leftVal, rhsVal, dir);
                        dir.getVeMap().putAll(dirStmt.getVeMap());
                        if(LoopIteratorCollectionHandler.collectionVariable.contains(rhsVal.toString())) {
                            dir.getVeMap().put(new VarNode(leftVal.toString()), new VarNode(rhsVal.toString()));
                        }
                    }
                    //CASE: v1 = (type1) v2
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JCastExpr) {
                        caseCast(d, dir, leftVal, (JCastExpr) rhsVal);
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
                            Type lefttype = leftVal.getType();
                            List <AccessPath> accessPaths = Flatten.flatten(leftVal, lefttype, 1);
                            d.dg("accessPaths = " + accessPaths);
                            d.dg("right val = " + rhsVal);
                            //subcase: v1.f = v2
                            d.dg("Subcase: v1.f = v2");
                            for(AccessPath ap : accessPaths) {
                                AccessPath rightAP = AccessPath.replaceBase(ap, rhsVal.toString());
                                Node resolvedright = getResolvedEEDag(dir, rightAP.toVarNode());
                                //dont have invalid entries with intermediate vars
                                if((resolvedright instanceof VarNode && resolvedright.toString().startsWith("$r")) == false) {
                                    dir.insert(ap.toVarNode(), resolvedright);
                                    d.dg("Mapped: " + ap.toString() + " -> " + rightAP.toString());
                                } else {
                                    d.wrn("Invalid intermediate access path found");
                                }
                            }
                            //can subcase v.f = methodcall be present?
                        }

                    }
                    //CASE v1 = v2.f
                    else if(leftVal instanceof JimpleLocal && rhsVal instanceof JInstanceFieldRef) {
                        Type exprsType = leftVal.getType();
                        JInstanceFieldRef fieldRef = (JInstanceFieldRef) rhsVal;
                        String rhsStr = fieldRef.getBase().toString() + "." + fieldRef.getField().getName();

                        //Subcase f is not primitive

                        if(!AccessPath.isTerminalType(exprsType)
                                && /*rhsVal.toString().contains("repository") == false*/ valueIsRepository(rhsVal) == false) {
                            d.dg("CASE v1 = v2.f, f is not primitive");
                            RefType exprreftype = (RefType) exprsType;
                            SootClass sc = exprreftype.getSootClass();
                            List <AnnotationTag> annotations = getAnnotationTags(sc.getTags());
                            for(AnnotationTag an : annotations) {
                                if(an.getType().equals("Ljavax/persistence/Entity;")) {
                                    VarNode v2_fvn = new VarNode(rhsStr);
                                    Node v2_fvnresolved = getResolvedEEDag(dir, v2_fvn);
                                    dir.insert(new VarNode(leftVal), v2_fvnresolved);
                                    break;
                                }
                            }
                            List <AccessPath> destPaths = Flatten.flatten(leftVal, exprsType, 0);
                            d.dg("destPaths: " + destPaths);
                            for(AccessPath daccp : destPaths) {
                                d.dg("destination accp: " + daccp);
                                AccessPath saccp = AccessPath.replaceBase(daccp, rhsStr);
                                d.dg("source accp: " + saccp);
                                String daccpbase = daccp.getBase();
                                char[] daccpbasearr = daccpbase.toCharArray();
                                int dotcount = 0;
                                for(char c : daccpbasearr) {
                                    if(c == '.') {
                                        dotcount++;
                                    }
                                }
                                int saccpLength = dotcount + 1;
                                if(saccpLength <= Flatten.BOUND) {
                                    VarNode rhsVN = saccp.toVarNode();
                                    d.dg("dir before resolving rhsVN (case v1 = v2.f, f not primitive)");
                                    d.dg(dir);
                                    Node resolvedAccp = getResolvedEEDag(dir, rhsVN);
                                    d.dg("resolvedAccp after resolution (case v1 = v2.f, f not primitive): " + resolvedAccp);
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
                            casePrimitiveGetField(d, dir, (JimpleLocal) leftVal, (JInstanceFieldRef) rhsVal);

                        }
                    }
                    //CASE: v = new
                    else if (leftVal instanceof JimpleLocal && !AccessPath.isTerminalType(leftVal.getType())
                            && rhsVal instanceof JNewExpr) {
                        if(rhsVal.toString().contains("HashSet"))
                            LoopIteratorCollectionHandler.collectionVariable.add(leftVal.toString());
                        caseAllocation(dir, leftVal);
                    }

                    //CASES containing method calls in rhs
                    else if(rhsVal instanceof InvokeExpr) {
                        d.dg("CASE method call in rhs");
                        InvokeExpr invokeExpr = (InvokeExpr) rhsVal;
                        if((invokeExpr.toString().contains("save(") ||
                                invokeExpr.toString().contains("saveAndFlush(")) && invokeExpr.toString().contains("Repository")) {
                            caseSave(d, dir, invokeExpr);
                            continue;
                        } else if(invokeExpr.toString().contains("orElse(java.lang.Object)>")) {
                            caseOrElse(dir, leftVal, invokeExpr);
                            continue;
                        } else if(invokeExpr instanceof DynamicInvokeExpr) {
                            continue;
                        } else if(invokeExpr.toString().contains("java.util.Optional: java.lang.Object orElseThrow")) {
                            Value v1 = leftVal;
                            Value v2 = fetchBaseValue(invokeExpr);
                            List <AccessPath> pathsstartwv1 = new ArrayList<>();
                            for(VarNode k : dir.getVeMap().keySet()) {
                                Node value = dir.getVeMap().get(k);
                                if(k.toString().startsWith(v2.toString()) && k.toString().contains(".")) {
                                    AccessPath kaccp = new AccessPath(k.toString());
                                    pathsstartwv1.add(kaccp);
                                }
                            }
                            for(AccessPath kaccp : pathsstartwv1) {
                                AccessPath laccp = new AccessPath(v1.toString() + kaccp.toString().substring(kaccp.toString().indexOf(".")));
                                dir.insert(laccp.toVarNode(), dir.find(kaccp.toVarNode()));
                            }
                        }
                        else if(invokeExpr.toString().contains("java.util.Optional: java.util.Optional ofNullable(java.lang.Object)")) {
                            Value arg0 = invokeExpr.getArg(0);
                            if(!arg0.getType().toString().equals("java.lang.Object")) {
                                DIR stmtdir = processPointerAssignmentKnownType(leftVal, arg0, dir, arg0.getType());
                                dir.getVeMap().putAll(stmtdir.getVeMap());
                            } else {
                                Map <VarNode, Node> stmtdir = new HashMap<>();
                                for(VarNode vn : dir.getVeMap().keySet()) {
                                    if(vn.toString().startsWith(arg0.toString() + ".")) {
                                        AccessPath vnaccp = new AccessPath(vn.toString());
                                        AccessPath lhsaccp = AccessPath.replaceBase(vnaccp, leftVal.toString());
                                        stmtdir.put(lhsaccp.toVarNode(), getResolvedEEDag(dir, dir.find(vn)));
                                    }
                                }
                                dir.getVeMap().putAll(stmtdir);
                            }
                            continue;
                        } else if(invokeExpr.toString().contains("java.lang.Long: long longValue()>()")) {
                            dir.insert(new VarNode(leftVal), getResolvedEEDag(dir, new VarNode(fetchBaseValue(invokeExpr))));
                            continue;
                        } else if(invokeExpr.toString().contains("java.lang.Long: java.lang.String toString()")) {
                            dir.insert(new VarNode(leftVal), getResolvedEEDag(dir, new VarNode(fetchBaseValue(invokeExpr))));
                            continue;
                        }
                        //Updates func dir map
                        d.dg("Update dir map");
                        //Condition for library methods: node not instance of MethodWonthandle and not instance of NonLibraryMeth
                        Node methodRet = Utils.parseInvokeExpr(invokeExpr, stmt.getJavaSourceStartLineNumber());
                        //CASE: v1 = v2.foo(v3), foo is library method
                        if (methodRet instanceof MethodWontHandleNode == false && methodRet instanceof NonLibraryMethodNode == false) {
                            d.dg("CASE: v1 = v2.foo(v3), foo is library method");
                            caseLibraryAssignment(d, dir, curUnit);
                            continue;
                        } else if (methodRet instanceof NonLibraryMethodNode) {
                            d.dg("CASE: v1 = v2.foo(v3), foo is Non-library method");
                            Type lefttype = leftVal.getType();
                            if(AccessPath.isCollectionType(lefttype) && AccessPath.isReturnTypeEntity(invokeExpr)){
                                lefttype = AccessPath.getCollectionEntityType(invokeExpr);
                            }
                            d.dg("leftType = "+lefttype);
                            if(invokeExpr.getMethod().getSignature().contains("java.util.Optional: java.lang.Object get()")) {
                                String basestr = fetchBaseValue(invokeExpr).toString();
                                lefttype = getKnownOptionalsActualType(basestr);
                            }
                            //CASE v1 = v2.foo(v3), foo isn't a library method

                            if (!AccessPath.isTerminalType(lefttype)) {
                                d.dg("leftType : "+lefttype);
                                d.dg("case : caseCallPtrAsgnMethodWBody()");
                                caseCallPtrAsgnMethodWBody(d, dir, leftVal, invokeExpr);
                            }
                            //CASE: v1 = v2.foo(v3),
                            //v1 is primitive or collection, foo is not a library method
                            else  {
                                d.dg("leftType : "+lefttype);
                                d.dg("case : caseCallToMethodWBodyRetPrim()");
                                caseCallToMethodWBodyRetPrim(d, dir, (JimpleLocal) leftVal, invokeExpr);
                            }
                        }
                        else if(methodRet instanceof MethodWontHandleNode) {
                            caseMethodWontHandle(d, dir, (JimpleLocal) leftVal, methodRet);
                        }
                    }
                    else {
                        genericCase(dir, curUnit);
                        continue;
                    }
                }
                //CASE v1.save(v2)
                else if(curUnit instanceof JInvokeStmt && (curUnit.toString().contains("save(") ||
                        curUnit.toString().contains("saveAndFlush(")) && curUnit.toString().contains("Repository")) {
                    caseSave(d, dir, ((JInvokeStmt) curUnit).getInvokeExpr());
                }
//                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("add(") && ((JInvokeStmt) curUnit).
//                        getInvokeExpr().getArgs().size() == 1) {
//                    JInvokeStmt addstmt = (JInvokeStmt) curUnit;
//                    Value arg = addstmt.getInvokeExpr().getArg(0);
//                    Value base = fetchBaseValue(addstmt.getInvokeExpr());
//                    VarNode basevn = new VarNode(base);
//                    List<Node> fieldExprs = new ArrayList<>();
//                    Collection<VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(arg);
//                    for (VarNode vn : fieldVarNodes) {
//                        fieldExprs.add(getResolvedEEDag(dir, vn));
//                    }
//                    ListNode fieldExprListNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
//                    AddWithFieldExprsNode addWithFieldExprsNode = new AddWithFieldExprsNode(fieldExprListNode);
//                    dir.insert(basevn, addWithFieldExprsNode);
//                }
                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("add(") && ((JInvokeStmt) curUnit).
                        getInvokeExpr().getArgs().size() == 1) {
                    caseAdd(dir, (JInvokeStmt) curUnit);
                }

                //CASE v1.delete(v2)
                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("delete(") && curUnit.toString().contains("Repository")) {
                    caseDelete(d, dir, (JInvokeStmt) curUnit);
                }

                else if(curUnit instanceof JInvokeStmt && curUnit.toString().contains("deleteBy") && curUnit.toString().contains("Repository")) {
                    caseDeleteBy((JInvokeStmt) curUnit, dir);
                }

                else if(curUnit instanceof JInvokeStmt &&
                        curUnit.toString().
                                contains("<org.springframework.web.servlet.ModelAndView: void <init>(java.lang.String)>")) {
                    caseMAVInit(dir, (InvokeStmt) curUnit);
                }
                else if(curUnit instanceof JInvokeStmt && isModelAdd(((JInvokeStmt) curUnit).getInvokeExpr().getMethodRef())) {
                    d.dg("Model Add Attribute Statement");
                    JInvokeStmt addAttributeInvoke = (JInvokeStmt) curUnit;
                    InvokeExpr addAttributeExpr = addAttributeInvoke.getInvokeExpr();
                    caseModelAddAttribute(dir, addAttributeExpr);
                }
                //CASE: v1.foo(v2)
                else if(curUnit instanceof JInvokeStmt) {
                    d.dg("CASE: v1.foo(v2)");
                    JInvokeStmt invokeStmt = (JInvokeStmt) curUnit;
                    handleSideEffects(dir, invokeStmt.getInvokeExpr());
                }
                //TODO: Check if this is required, keeping it only for consistency with base DBridge

                else {
                    genericCase(dir, curUnit);
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
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        d.dg("Finished with all the statements");
        d.dg("BasicBlockRegion: " + region);
        d.dg("BasicBlockDIR: " + dir);
        DAGTillNow.updateDag(dir);
        return dir;
    }
    private void caseAdd(DIR dir, JInvokeStmt curUnit) {
        JInvokeStmt addstmt = curUnit;
        Value arg = addstmt.getInvokeExpr().getArg(0);
        Value base = fetchBaseValue(addstmt.getInvokeExpr());
        VarNode basevn = new VarNode(base);
        Node basevarnoderes = getResolvedEEDag(dir, basevn);
        List<Node> fieldExprs = new ArrayList<>();
        VarNode argvn = new VarNode(arg);
        Node argvnresolved = getResolvedEEDag(dir, argvn);
        Collection<VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(arg);
        for (VarNode vn : fieldVarNodes) {
            Node vnmres = getResolvedEEDag(dir, vn);
            if(vnmres instanceof ProjectNode &&
                    ((ProjectNode) vnmres).getChild(0).toString().equals(argvnresolved.toString())) {
                ProjectNode pi = ((ProjectNode) vnmres);
                SelectNode basesel = (SelectNode) argvnresolved;
                ClassRefNode crn  =(ClassRefNode) basesel.getChild(0);
                String entitycls = ((ClassRefOp)crn.getOperator()).getClassName();
                FieldRefNode frn = new FieldRefNode(entitycls, vn.toString().substring(vn.toString().indexOf(".") + 1), entitycls);
                fieldExprs.add(frn);
            } else {
                fieldExprs.add(getResolvedEEDag(dir, vn));
            }

        }
        List<FieldRefNode> columns = new ArrayList<>();
        RefType argType = (RefType) arg.getType();
        List <String> attributes = Flatten.flattenEntityClass(argType.getSootClass());
        String table = argType.toString();

        for(String att : attributes) {
            FieldRefNode attFR = new FieldRefNode(table, att, table);
            columns.add(attFR);
        }

        ListNode listNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
        listNode.columns.addAll(columns);


        ListNode fieldExprListNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
        fieldExprListNode.columns = columns;
        TupleNode tuple = new TupleNode(argvnresolved, fieldExprListNode, argvn);
        AddWithFieldExprsNode addWithFieldExprsNode = new AddWithFieldExprsNode(basevn, tuple);
        dir.insert(basevn, addWithFieldExprsNode);
        if(argvnresolved instanceof VarNode && basevarnoderes instanceof VarNode) {
            VarNode basevnresolvedvn = (VarNode) basevarnoderes;
            dir.insert(basevnresolvedvn, new UnionNode(basevnresolvedvn, fieldExprListNode));
        }
    }



    private void caseModelAddAttribute(DIR dir, InvokeExpr addAttributeExpr) throws UnknownStatementException {
        List<Value> args = addAttributeExpr.getArgs();
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
            genericCase(dir, stmt);
        }
    }

    private void caseOrElse(DIR dir, Value leftVal, InvokeExpr invokeExpr) {
        Value base = fetchBaseValue(invokeExpr);
        VarNode basevn = new VarNode(base);
        Value other = invokeExpr.getArg(0);
        Node othernode = NodeFactory.constructFromValue(other);
        Node baseresolved = getResolvedEEDag(dir, basevn);
        Node otherresolved = getResolvedEEDag(dir, othernode);
        //optimization: if otherresolved is null then return baseresolved.
        if(otherresolved instanceof NullNode) {
            //case baseResolved is rel exp sel
            if(baseresolved instanceof SelectNode) {
                ClassRefNode crn = (ClassRefNode) baseresolved.getChild(0);
                ClassRefOp crop = (ClassRefOp) crn.getOperator();
                String cls = crop.getClassName();
                SootClass clssc = Scene.v().loadClassAndSupport(cls);
                DIR stmtdir = processPointerAssignmentKnownType(leftVal, base, dir, clssc.getType());
                stmtdir.insert(new VarNode(leftVal), baseresolved);
                dir.getVeMap().putAll(stmtdir.getVeMap());
            }
        } else {
            TernaryNode tn = new TernaryNode(new NotEqNode(baseresolved, new EmptySetNode()), baseresolved, otherresolved);
            dir.insert(new VarNode(leftVal), tn);
        }
    }

    private void casePrimitiveGetField(debug d, DIR dir, JimpleLocal leftVal, JInstanceFieldRef rhsVal) {
        JInstanceFieldRef fieldRef = rhsVal;
        String rhsStr = fieldRef.getBase().toString() + "." + fieldRef.getField().getName();
        VarNode rhsVarNode = new VarNode(rhsStr);
        Node rhsResolved = getResolvedEEDag(dir, rhsVarNode);
        JimpleLocal leftLocal = leftVal;
        VarNode leftVarNode = new VarNode(leftLocal);
        dir.insert(leftVarNode, rhsResolved);
        d.dg("mapping " + leftVarNode + " -> " + rhsResolved);
    }

    private void genericCase(DIR dir, Unit curUnit) throws UnknownStatementException {
        StmtInfo stmtInfo;
        stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
        if (stmtInfo == StmtInfo.nullInfo) {
            return;
        }
        VarNode dest = stmtInfo.getDest();
        Node source = stmtInfo.getSource();
        Node resolvedSource = getResolvedEEDag(dir, source);
        dir.insert(dest, resolvedSource);
        caseIfCheckCast(dest, resolvedSource, curUnit);
    }

    private void caseCast(debug d, DIR dir, Value leftVal, JCastExpr rhsVal) {
        d.dg("CASE: v1 = (type1) v2");
        JCastExpr castExpr = rhsVal;
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
                    caseIteratorCast(d, dir, leftVal, castType);
                }
            } else if (AccessPath.isCollectionType(castType)) {
                VarNode rvnode = new VarNode(right);
                Node resolvedMapping = getResolvedEEDag(dir, rvnode);
                dir.insert(new VarNode(leftVal), resolvedMapping);
            } else if(castType.toString().equals("java.io.Serializable")) {
                VarNode rvnode = new VarNode(right);
                Node resolvedMapping = getResolvedEEDag(dir, rvnode);
                dir.insert(new VarNode(leftVal), resolvedMapping);
            }
            /*
            TO handle
            $r1 = virtualinvoke optionalUser.<java.util.Optional: java.lang.Object get()>();
            user = (com.reljicd.model.User) $r1;
             */
            else if(right.getType().toString().equals("java.lang.Object")) {
                caseOptionalCast(dir, leftVal, right);
            }
            else {
                caseCastPtr(dir, leftVal, right);
            }
        }
        //CASE: v1 = (type1) v2, v1 and v2 are primitives
        else {
            VarNode rvnode = new VarNode(right);
            Node resolvedMapping = getResolvedEEDag(dir, rvnode);
            dir.insert(new VarNode(leftVal), resolvedMapping);
        }
    }
    private void caseIteratorCast(debug d, DIR dir, Value leftVal, Type castType) {
        String castTypeStr = castType.toString();
        List<AccessPath> flattenedIterator = Flatten.flattenEntity(leftVal, castType);
        List<String> tableAttributes = Flatten.attributes(flattenedIterator);
        Collection <SootField> stoonefs = Utils.starToOneFields(((RefType)leftVal.getType()).getSootClass());
        for (int i = 0; i < flattenedIterator.size(); i++) {
            String atr = tableAttributes.get(i);
            FieldRefNode fieldRefAtr = new FieldRefNode(castTypeStr, atr, castTypeStr);
            VarNode varNodeAtr = new VarNode(flattenedIterator.get(i).toString());
            dir.insert(varNodeAtr, fieldRefAtr);
        }
        for(SootField ssf : stoonefs) {
            String fname = ssf.getName();
            String leftName = leftVal.toString();
            AccessPath joinaccp = new AccessPath(leftName + "." + fname);
            JoinNode jn = new JoinNode(new NextNode(), new ClassRefNode(ssf.getType().toString()),new NullNode());
            d.dg("check : "+jn);
            dir.insert(joinaccp.toVarNode(), jn);

            Collection <SootField> primFs = Utils.primFields(((RefType)ssf.getType()).getSootClass());
            for(SootField primF : primFs) {
                AccessPath accpPrim = new AccessPath(leftVal.toString() + "." + ssf.getName() + "." + primF.getName());
                FieldRefNode ssfPrimFR = new FieldRefNode(ssf.getType().toString(), primF.getName(), ssf.getType().toString());
                dir.insert(accpPrim.toVarNode(), ssfPrimFR);
            }

        }
        d.dg("flattenedIterator = " + flattenedIterator);
        d.dg("dir: " + dir.getVeMap());
    }



    private void caseCastPtr(DIR dir, Value leftVal, Value right) {
        DIR dirStmt = processPointerAssignment(leftVal, right, dir);
        dir.getVeMap().putAll(dirStmt.getVeMap());
    }

    private void caseOptionalCast(DIR dir, Value leftVal, Value right) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseOptionalCast()");
        VarNode rvnode = new VarNode(right);
        Node resolvedMapping = getResolvedEEDag(dir, rvnode);
        dir.insert(new VarNode(leftVal), resolvedMapping);
        caseCastPtr(dir, leftVal, right);
    }

    private void caseAllocation(DIR dir, Value leftVal) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseAllocation()");
        debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ", "CASE: v = new");
        List<AccessPath> accessPaths = Flatten.flattenEntity(leftVal, leftVal.getType());
        for(AccessPath ap : accessPaths) {
            VarNode vn = new VarNode(ap.toString());
            d.dg("Mapping " + ap.toString() + " to Bottomnode");
            dir.insert(vn, new BottomNode());
        }
        if(AccessPath.isCollectionType(leftVal.getType())) {
            dir.insert(new VarNode(leftVal), BottomNode.v());
        }
    }

    private void caseReturnStmt(DIR dir, JReturnStmt curUnit) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseReturnStmt");
        JReturnStmt retStmt = curUnit;
        Value retval = retStmt.getOp();
        Type retType = retval.getType();
        if(retType.toString().equals("null_type")) {
            retType = curmethod.getReturnType();
            JimpleLocal retlocal = new JimpleLocal("return", retType);
            List <AccessPath> accps = Flatten.flatten(retlocal, retType, 0);
            d.dg("return null accps: " + accps);
            for(AccessPath accp : accps) {
                dir.insert(accp.toVarNode(), new UnknownNode());
            }
            return;
        }
        if(retType.toString().equals("java.util.Optional")) {
            retType = getKnownOptionalsActualType("return");
            AccessPath optionalReturn = new AccessPath("optionalret");
            dir.insert(optionalReturn.toVarNode(), getResolvedEEDag(dir, new VarNode(retval)));
        }
        if(!AccessPath.isTerminalType(retType)) {
            Value retLocal = new JimpleLocal("return", retType);
            DIR dirRetStmt = processPointerAssignment(retLocal, retval, dir);
            dir.getVeMap().putAll(dirRetStmt.getVeMap());
        }
    }

    private void caseCallPtrAsgnMethodWBody(debug d, DIR dir, Value leftVal, InvokeExpr invokeExpr) throws CloneNotSupportedException {
        d.dg("CASE v1 = v2.foo(v3)");
        d.dg("v1: " + leftVal);
        d.dg("v2.foo(v3): " + invokeExpr);
        //this method seems to have only one call site, before which the method's VEMap is constructed.
        //there, for saving the VEMAP, .getMathod().getSignature() is used.
        String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());

        //Map <VarNode, Node> veMapCallee = FuncStackAnalyzer.funcDIRMap.get(invokedSig).getVeMap();
        //TODO: get actual type in case of Optional, flatten and then implement handleSideEffects
        Type leftType = leftVal.getType();
        if(AccessPath.isCollectionType(leftType) && AccessPath.isReturnTypeEntity(invokeExpr)){
            leftType = AccessPath.getCollectionEntityType(invokeExpr);
        }
        d.dg("left type = " + leftType);
        boolean v1typeoptional = leftVal.getType().toString().equals("java.util.Optional");
        d.dg(invokeExpr.getType());
        d.dg("invoke methodref rettype: " + invokeExpr.getMethodRef().returnType());
        if (v1typeoptional) {
            d.dg("v1 type is optional");
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "invokedSig = " + invokedSig);
            //Actual types corresponding to the optionals, unfortunately, cannot be found in the base class.
            String refsig = trim(invokeExpr.getMethodRef().getSignature());
            Map<String, String> typeTable = OptionalTypeInfo.analyzeBCEL(refsig);
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", " leftVal = " + leftVal);
            // String actualType = OptionalTypeInfo.typeMap.get(leftVal.toString());
            String lookup = "return_" + refsig;
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
            dir.insert(new VarNode(leftVal), new VarNode(base));
        }
        try {
            if(invokedSig.equals("com.yyqian.imagine.service.impl.PostServiceImpl: java.util.Optional getPostById(long)")) {
                d.dg("break");
            }
            analyzeMethod(invokedSig);
        } catch (RegionAnalysisException e) {
            d.wrn("analyze method failure for method: " + invokedSig);
            e.printStackTrace();
        }

        DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
        d.dg("funcDIRMap = \n"+FuncStackAnalyzer.funcDIRMap);
        d.dg("calleeDIR= " +calleeDIR);
        Map<VarNode, Node> calleeVEMap = calleeDIR.getVeMap();
        d.dg("check 1");
        addReturnMappingIfEntity(d, dir, leftVal, invokeExpr, (RefType) leftType, calleeDIR);
        leftType = addReturnMappingIfFindOne(dir, leftVal, invokeExpr, invokedSig, leftType, calleeDIR);
        d.dg("leftType = "+leftType);
        try {
            handleSideEffects(dir, invokeExpr);
        } catch (RegionAnalysisException e) {
            e.printStackTrace();
        }
        if (!AccessPath.isTerminalType(leftType)) { //Case where flattening can and should be done
            d.dg("CASE v1 = v2.foo(v3), type(v1) is pointer non-collection");
            AlloyGenerator.tableSignatures.add(Scene.v().getSootClass(leftType.toString()));
            if(!AlloyGenerator.tableAndFields.containsKey(Scene.v().getSootClass(leftType.toString())))
                NRA.processTableandFields(Scene.v().getSootClass(leftType.toString()));
            d.dg("going to flatten (var, type) = " + leftVal + ", " + leftType);
            List<AccessPath> accessPaths = Flatten.flatten(leftVal, leftType, 1);
            d.dg("accesspaths = "+accessPaths);
            List<String> attributes = Flatten.attributes(accessPaths);
            d.dg("funcDIRMap domain = " + FuncStackAnalyzer.funcDIRMap.keySet());
            d.dg("callee = " + invokedSig);
            d.dg("funcDIRMap domain contains callee = " + FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig));

            //Myimpl
            ////////////////// handling return.pets ////////////
//            List<AccessPath> nestedAccps= Flatten.nested_Accp(leftVal, leftType);
            SootClass leftClass = Scene.v().getSootClass(leftType.toString());
            if(AccessPath.isCollectionType(invokeExpr.getType())){
                d.dg("invokeExpr = "+invokeExpr);
                d.dg("return type collection");
                VarNode retAccp = new VarNode("return");
                d.dg("lookup (retAccp) = " + retAccp);
                if (calleeVEMap.containsKey(retAccp)) {
                    Node relExp = callersDagForCalleesKey(retAccp, calleeDIR, dir, invokeExpr);
                    d.dg("key= "+retAccp + "\n value= "+relExp);
//                    NRA.visited.add(leftType+"~"+sf.getName());
                    Node nestDag= genExprNra(null,leftClass.getType(),leftClass.getType(),relExp,1);
                    d.dg("nested VeMap : ");
//                    d.dg(nestDag.getOperator().getName());
//                    d.dg(nestDag.getChild(0).getOperator().getName());
                    if(nestDag.getOperator().getName().equals(nestDag.getChild(0).getOperator().getName())){
                        d.dg("repeat node");
                        nestDag = nestDag.getChild(0);
                    }
                    d.dg("key : "+retAccp+ "\n value : \n "+nestDag);
                    d.dg("leftval = "+leftVal.toString());
//                    calleeVEMap.put(new VarNode(leftVal.toString()),nestDag);
                    VarNode key= new VarNode(leftVal);
                    dir.insert(key,nestDag);
                }
            }
            else{
                List<SootField> nestedFields= Flatten.getNestedFields(leftVal,leftType);
                d.dg("check nestedFields="+nestedFields);


                for(SootField sf:nestedFields){

                    AccessPath ap=Flatten.getAccp(leftVal,sf);
                    VarNode retAccp = new VarNode("return" + ap.toString().substring(ap.toString().indexOf(".")));
                    d.dg("lookup (retAccp) = " + retAccp);

                    String base_EntityName= leftType.toString().substring(leftType.toString().lastIndexOf(".")+1);
                    d.dg("baseEntity NAme="+base_EntityName);
                    String nestEntityName= bcelActualCollectionFieldType(leftType.toString(),sf.getName());
                    d.dg("nestedField Entity= "+nestEntityName);

                    SootClass nestClass= Scene.v().getSootClass(nestEntityName);
                    d.dg("From soot typeclass= "+ nestClass);

//                if(NRA.isVisited(NRA.visited,leftType.toString()+"~"+sf.getName())){
//                    continue;
//                }
//                Set<String> visited=new HashSet<>();
                    NRA.visited.add(leftType+"~"+sf.getName());
                    if (calleeVEMap.containsKey(retAccp)) {
                        Node nestedDag = callersDagForCalleesKey(retAccp, calleeDIR, dir, invokeExpr);
                        d.dg("key= "+retAccp + "\n value= "+nestedDag);
                        NRA.visited.add(leftType+"~"+sf.getName());
                        EqNode cond=NRA.getJoinCondFromField(sf,leftClass,nestClass);
//                        JoinNode joinedDag = new JoinNode(nestedDag,new ClassRefNode(NRA.getShortName(nestClass.getName())),cond);
                        Node relExp= nestedDag;
                        if(!(relExp instanceof JoinNode || relExp instanceof ProjectNode))continue;
                        if(nestedDag instanceof ProjectNode){
                            relExp = nestedDag.getChild(0);
                        }
                            d.dg("relExp = "+relExp);
                            relExp.setChild(2,cond);

                        Node nestDag= genExprNra(sf,nestClass.getType(),leftType,nestedDag,1);
//                        Node nestDag= genExprNra(sf,nestClass.getType(),leftType,joinedDag);
//                        nestedDag.getOperator().setName(base_EntityName + "." + sf.getName() +"=Pi");
//                        d.dg("projectNode name: "+nestDag.getOperator());

                        d.dg("nested VeMap : ");
                        if(nestDag.getOperator().getName().equals(nestDag.getChild(0).getOperator().getName())){
                            d.dg("repeat node");
                            nestDag = nestDag.getChild(0);
                        }
                        d.dg("key : "+retAccp+ "\n value : \n "+nestDag);
                        calleeVEMap.put(retAccp,nestDag);

                    }
                }
            }



//            NestedListNode n1=new NestedListNode(new Node[3]);
//            ListNode n1 =new ListNode(new Node[3]);
//            ProjectNode n2=new ProjectNode(getRelExpForMethod(invokeExpr).getKey(),new VarNode("col1"));
//            d.dg("invokeexpr="+invokeExpr+"  "+getRelExpForMethod(invokeExpr));
//            d.dg("projectNode="+n2);
//            VarNode n3= new VarNode("tempVarNode");
//
//            n1.setChild(0,n2);
//            n1.setChild(1,n3);
//
//            d.dg("check Listnode n1="+n1);
//            for(int i=0;i<2;i++){
//                d.dg("type of child "+i+" of n1= "+n1.getChild(i).getClass());
//            }


            //////// Myimpl end ////

            d.dg("Printing ve map of callee = " + invokedSig);
            for (VarNode vn : calleeVEMap.keySet()) {
                d.dg("key = " + vn);
                d.dg("val = " + calleeVEMap.get(vn));
            }
            d.dg("Printing ve map of callee = " + invokedSig + " END");

            if(!AccessPath.isCollectionType(invokeExpr.getType())){
                d.dg("v1 access paths: " + accessPaths);
                for (AccessPath ap : accessPaths) {
                    VarNode key = new VarNode(ap.toString());
                    d.dg("key = " + key);
//                    if(key.toString().equals("user.shoppingCart")) {
//                        d.dg("break point");
//                    }
                    VarNode retAccp = new VarNode("return" + ap.toString().substring(ap.toString().indexOf(".")));
                    d.dg("lookup (retAccp) = " + retAccp);
                    if (calleeVEMap.containsKey(retAccp)) {
                        Node callersDag = callersDagForCalleesKey(retAccp, calleeDIR, dir, invokeExpr);
                        d.dg("callersDag = " + callersDag);
                        d.dg("resolvedVal = " + callersDag);
                        dir.insert(key, callersDag);
                    } else {
                        d.dg("No entry for lookup (retAccp) = " + retAccp);
                    }
                }
            }


            if(v1typeoptional) {
                VarNode lookup = new VarNode("optionalret");
                Node relexp = callersDagForCalleesKey(lookup, calleeDIR, dir, invokeExpr);
                dir.insert(new VarNode(leftVal), relexp);
            }

        } else {
            d.dg("CASE v1 = v2.foo(v3), type of v1 is terminal");
            d.dg("calleeDIR domain " + calleeDIR.getVeMap().keySet());
            VarNode retnode = RetVarNode.getARetVar();
            Node dag = callersDagForCalleesKey(retnode, calleeDIR, dir, invokeExpr);
            dir.insert(new VarNode(leftVal), dag);
        }
    }

    private Type addReturnMappingIfFindOne(DIR dir, Value leftVal, InvokeExpr invokeExpr, String invokedSig, Type leftType, DIR calleeDIR) {
        if(invokedSig.contains("java.lang.Object findOne")) {
            String methodRefSig = invokeExpr.getMethodRef().getSignature();
            String reposig = methodRefSig.substring(1, methodRefSig.indexOf(":"));
            String entity = RepoToEntity.getEntityForRepo(reposig);
            SootClass entitycls = Scene.v().loadClass(entity, 1);
            leftType = entitycls.getType();
            VarNode retvn = new VarNode("return");
            Node v2vnresolved = callersDagForCalleesKey(retvn, calleeDIR, dir, invokeExpr);
            dir.insert(new VarNode(leftVal), v2vnresolved);
        }
        return leftType;
    }

    private void addReturnMappingIfEntity(debug d, DIR dir, Value leftVal, InvokeExpr invokeExpr, RefType leftType, DIR calleeDIR) {
        SootClass sc = leftType.getSootClass();
        sc.getTags();
        d.dg("soot class tags: " + sc.getTags());
        List<AnnotationTag> annotations = getAnnotationTags(sc.getTags());
        for(AnnotationTag an : annotations) {
            if(an.getType().equals("Ljavax/persistence/Entity;")) {
                VarNode retvn = new VarNode("return");
                Node v2vnresolved = callersDagForCalleesKey(retvn, calleeDIR, dir, invokeExpr);
                dir.insert(new VarNode(leftVal), v2vnresolved);
            }
        }
    }

    private void caseCallToMethodWBodyRetPrim(debug d, DIR dir, JimpleLocal leftVal, InvokeExpr invokeExpr) throws RegionAnalysisException {
        d.dg("CASE: v1 = v2.foo(v3), v1 is primitive, foo is not a library method");
        d.dg("v1: " + leftVal);
        d.dg("foo: " + invokeExpr.getMethod().getSignature());
        if(invokeExpr.getMethod().getSignature().equals("<java.lang.Object: java.lang.Object findOne(java.io.Serializable)>")) {
            d.dg("break");
        }
        handleSideEffects(dir, invokeExpr);
        VarNode retNode = null;
        String methodSig = trim(invokeExpr.getMethod().getSignature());
        d.dg("methodSig: " + methodSig);
        d.dg("FuncStackAnalyzer.funcDIRMap: " + FuncStackAnalyzer.funcDIRMap);
        DIR dirCallee = FuncStackAnalyzer.funcDIRMap.get(methodSig);
        for(VarNode k : dirCallee.getVeMap().keySet()) {
            if(k.toString().equals("return")) {
                retNode = k;
            }
        }
            d.dg("dir callee: " + dirCallee);
        Node retCallee = dirCallee.getVeMap().get(retNode);
        Cloner cloner = new Cloner();
        Node retCalleeCloned = cloner.deepClone(retCallee);
        d.dg("retCalleeCloned: " + retCalleeCloned);
        retCalleeCloned = dagFormalsToActuals(retCalleeCloned, invokeExpr);
        d.dg("after dag formals to actuals: " + retCalleeCloned);
        Node retEEDag = getResolvedEEDag(dir, retCalleeCloned);
        JimpleLocal leftLocal = leftVal;
        VarNode leftVarNode = new VarNode(leftLocal);
        dir.insert(leftVarNode, retEEDag);
    }

    private void caseMethodWontHandle(debug d, DIR dir, JimpleLocal leftVal, Node methodRet) {
        d.dg("Wont handle this method");
        JimpleLocal leftLocal = leftVal;
        VarNode leftVarNode = new VarNode(leftLocal);
        dir.insert(leftVarNode, methodRet);
    }

    private void caseMAVInit(DIR dir, InvokeStmt curUnit) {
        InvokeStmt invokestmt = curUnit;
        JSpecialInvokeExpr specialinvoke = (JSpecialInvokeExpr) invokestmt.getInvokeExpr();
        Value retview = specialinvoke.getArg(0);
        Node retviewnode = NodeFactory.constructFromValue(retview);
        VarNode mavview = new VarNode("mav_view");
        dir.insert(mavview, retviewnode);
    }

    private void caseDelete(debug d, DIR dir, JInvokeStmt curUnit) {
        JInvokeStmt deleteStmt = curUnit;
        d.dg("deleteStmt: " + deleteStmt);
        InterfaceInvokeExpr invokeExpr = (InterfaceInvokeExpr) deleteStmt.getInvokeExpr();
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
        Value delarg = invokeExpr.getArg(0);
        Type delargtype = delarg.getType();
        if(isEntityType(delargtype)) {
            caseDeleteEntity(dir, repo, delarg);
        } else { //Argument is a primary key
            String columnname = getReposIdColumn(base.getType());
            caseDeleteByColumn(dir, invokeExpr, base, columnname);
        }
    }

    private void caseDeleteEntity(DIR dir, VarNode repo, Value delentity) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseDeleteEntity");
        Collection<VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(delentity);
        List<FieldRefNode> columns = new ArrayList<>();
        RefType argType = (RefType) delentity.getType();
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

//        d.dg("deleteStmt args: " + deleteStmt.getInvokeExpr().getArgs());
    }

    private void caseSave(debug d, DIR dir, InvokeExpr saveinvokeexpr) {
//        JInvokeStmt saveStmt = curUnit;
//        d.dg("savestmt: " + saveStmt);
//        JInterfaceInvokeExpr invokeExpr = (JInterfaceInvokeExpr) saveStmt.getInvokeExpr();
        d.dg("savestmt invoke expr: " + saveinvokeexpr);
        Value base = fetchBaseValue(saveinvokeexpr);
        VarNode baseVarNode = new VarNode(base);
        d.dg("baseVarNode: " + baseVarNode);
        d.dg("dir till now: " + dir);
        VarNode repo = (VarNode) dir.find(baseVarNode);
        repo.repoType = base.getType();
        if(repo == null)
            repo = baseVarNode;
        d.dg("ve map:" + dir.getVeMap());
        d.dg("repo: " + repo);
        Value itval = saveinvokeexpr.getArg(0);
        Collection<VarNode> fieldVarNodes = DIRLoopRegionAnalyzer.fieldVarNodesOfIterator(itval);
        List<FieldRefNode> columns = new ArrayList<>();
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
        VarNode argvn = new VarNode(itval);
        Node relExpBase = argvn;
        // Could be replaced by getResolvedEEDag(argvn)
        if(dir.contains(argvn)) {
            relExpBase = dir.find(argvn);
        }
        TupleNode tuple = new TupleNode(relExpBase, listNode, argvn);
        SaveNode saveNode = new SaveNode(baseVarNode, tuple);
        dir.insert(repo, saveNode);
        d.dg("mapping: " + repo + " -> " + saveNode);

        d.dg("savestmt args: " + saveinvokeexpr.getArgs());
    }

    private void caseLibraryAssignment(debug d, DIR dir, Unit curUnit) throws UnknownStatementException {
        StmtInfo stmtInfo;
        d.dg("CASE: v1 = v2.foo(v3), foo is library method");
        JAssignStmt assignstmt = (JAssignStmt) curUnit;
        Value right = assignstmt.rightBox.getValue();
        InvokeExpr call = (InvokeExpr) right;
        if(isModelAdd(call.getMethodRef())) {
            caseModelAddAttribute(dir, call);
            return;
        }
        stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
        if (stmtInfo == StmtInfo.nullInfo) {
            return;
        }
        VarNode dest = stmtInfo.getDest();
        Node source = stmtInfo.getSource();
        Node resolvedSource = getResolvedEEDag(dir, source);
        dir.insert(dest, resolvedSource);
    }

    private void caseDeleteBy(JInvokeStmt curUnit, DIR dir) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseDeleteBy()");
        JInvokeStmt callstmt = curUnit;
        InvokeExpr call = callstmt.getInvokeExpr();
        Value base = fetchBaseValue(call);
        String methodname = call.getMethod().getName();
        String methodsig = call.getMethod().getSignature();
        String columnparam = methodname.substring("deleteBy".length());
        caseDeleteByColumn(dir, call, base, columnparam);
    }

    private void caseDeleteByColumn(DIR dir, InvokeExpr call, Value base, String columnparam) {
        debug d = new debug("DIRRegionAnalyzer.java", "caseDeleteByColumn()");
        Value idval = call.getArg(0);
        if(AccessPath.isPrimitiveType(idval.getType()) == false)
            return;
        VarNode idvalvn = new VarNode(idval);
        Node idnoderes = getResolvedEEDag(dir, idvalvn);
        // Node idnode = NodeFactory.constructFromValue(idval);
        String basestr = base.toString();
        String tablename = base.getType().toString();
        VarNode basevn = new VarNode(base);
        d.dg("base VarNode: " + basevn);
        d.dg("dir till now: " + dir);
        VarNode repo = (VarNode) dir.find(basevn);
        if(repo == null)
            repo = basevn;
        ClassRefNode repocrn = new ClassRefNode(repo.toString());
        SelectNode sel = new SelectNode(repocrn, new EqNode(new FieldRefNode(tablename, columnparam, tablename), idnoderes));
        d.dg("check = "+sel);
        RelMinusNode deleted = new RelMinusNode(repocrn, sel);
        dir.insert(repo, deleted);
    }

    private Node getResolvedEEDag(DIR dir, Node root) {
        VarResolver varResolver = new VarResolver(dir);
        return root.accept(varResolver);
    }

    //TODO: Could use some refactoring.
    private void handleSideEffects(DIR dir, InvokeExpr invokeExpr) throws RegionAnalysisException {
        debug d = new debug("DIRRegionAnalyzer.java", "handleSideEffects()");
        Value base = fetchBaseValue(invokeExpr);
        d.dg("invokeExpr = " + invokeExpr);
        d.dg("methodname: " + invokeExpr.getMethod().getName());
        if(invokeExpr.getMethod().getName().equals("upVotePostById")) {
            d.dg("Break");
        }
        List<Value> actualArgs = invokeExpr.getArgs();
        boolean invokeIsStatic = invokeExpr instanceof StaticInvokeExpr;
        if(invokeIsStatic == false)
            actualArgs.add(base);

        String invokedSig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());
        d.dg("invokedSig = " + invokedSig);
        if(FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
            SootMethod method = invokeExpr.getMethod();
            d.dg("soot method = " + method);
            JimpleBody jbod = (JimpleBody) FuncStackAnalyzer.funcBodyMap.get(invokedSig);

            //List<Local> formalArgs = (method.getActiveBody()).getParameterLocals();

            List<Local> formalArgs = getParameterLocals(jbod);
            if (invokeIsStatic == false)
                formalArgs.add(jbod.getThisLocal());
            d.dg("formalArgs = " + formalArgs);
            d.dg("actualArgs = " + actualArgs);
            if (formalArgs.size() != actualArgs.size()) {
                d.dg("WARN: unequal number of actual and formal args");
            }
            analyzeMethod(invokedSig);
            DIR calleeDIR = FuncStackAnalyzer.funcDIRMap.get(invokedSig);
            List<Node> affectedKeys = new LinkedList<>();
            for (int i = 0; i < formalArgs.size(); i++) {
                Local formal = formalArgs.get(i);
                Value actual = actualArgs.get(i);
                Type formalType = getLocalsActualType(invokedSig, formal);
                d.dg("ith formal: " + formal);
                d.dg("ith formal type: " + formalType);
                d.dg("ith actual: " + actual);
                if (!AccessPath.isTerminalType(formalType)) {
                    d.dg("Going to flatten: " + formal);
                    List<AccessPath> formalAccpList = Flatten.flatten(formal, formalType, 0);
                    d.dg("flattened res = " + formalAccpList);
                    for (AccessPath formalAccp : formalAccpList) {
                        String formalAccpStr = formalAccp.toString();
                        d.dg("formal access path in callee: " + formalAccpStr);
                        d.dg("callee ve map domain: " + calleeDIR.getVeMap().keySet());
                        //Node eedag = calleeDIR.find(new VarNode(formalAccpStr));
                        /*
                        Why the commented line is wrong: Node eedag = calleeDIR.find(new VarNode(formalAccpStr));
                        If there is only just a simple lookup, the leaves of the expression tree could be in terms
                        of "incoming variables", which may be defined in the DIR. Hence those incoming variables
                        should be replaced with their values in the DIR.
                         */
                        Node eedag = callersDagForCalleesKey(new VarNode(formalAccpStr), calleeDIR, dir, invokeExpr);
                        d.dg("formalaccp eedag = " + eedag);

                        if (eedag != null) {
                            Boolean containsBot = containsBottomNode(eedag);
                            if(containsBot == false) {
                                String actualAccpStr = actual.toString() + formalAccpStr.substring(formalAccpStr.indexOf("."));
                                VarNode actualAccpNode = new VarNode(actualAccpStr);
                                affectedKeys.add(actualAccpNode);
                                dir.insert(actualAccpNode, eedag);
                            }
                        }
                    }
                }
            }
//        }
            d.dg("method and affected keys = " + method.getName() + ", " + affectedKeys);
            if(method.getName().equals("upVotePostById")) {
                d.dg("break");
            }
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
                            d.dg("callee: " + invokedSig);
                            d.dg("callee dir: " + dir);
                            d.dg("for key = " + key);
                            if(key.toString().equals("this.userRepository")) {
                                d.dg("break point!");
                            }
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
                        Node newEEDag = null;
                        if(key.toString().equals("cartItem.qty")) {
                            dir.insert((VarNode) key, eedag);
                            continue;
                        }
                        newEEDag = eedag.accept(formalToActualVisitor);
                        dir.insert((VarNode) key, newEEDag);
                        d.dg("Case the formal param is a primitive, " + eedag);
                    }
                }
            }
        }
        else {
            // handle collection constructor with parameter
            System.out.println("");
            if(invokeExpr.toString().indexOf("void <init>(java.util.Collection)>(visits)") != -1)
                dir.insert(new VarNode(actualArgs.get(1)), new VarNode(actualArgs.get(0)));

//            dir.insert(new VarNode("$r0"), new VarNode("pet.visits"));
            d.wrn("Wont handle method");
        }
    }

    public static Boolean containsBottomNode(Node eedag) {
        if(eedag.getNumChildren() == 0)
            return eedag instanceof BottomNode;
        else {
            int nchild = eedag.getNumChildren();
            for(int i = 0; i < nchild; i++) {
                Node child = eedag.getChild(i);
                if(child instanceof BottomNode) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
        root: a dag that references formal params.
        output: an equivalent dag with formals renamed to actuals
     */
    public Node dagFormalsToActuals(Node root, InvokeExpr invokeExpr) {
        debug d = new debug("DIRRegionAnalyzer.java", "dagFormalsToActuals()");
        d.dg("dag to be renamed: " + root);
        d.dg("invoke expression: " + invokeExpr);
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
            d.dg("ret init val: " + ret);
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
        d.turnOff();
        d.dg("calleeKey: " + calleeKey);
        Cloner cloner = new Cloner();
        Node calleeDag = cloner.deepClone(calleeDIR.find(calleeKey));
        d.dg("calleeDag: " + calleeDag);
        if(calleeDag == null)
            return null;
        Node ret = dagFormalsToActuals(calleeDag, invokeExpr);
        ret = getResolvedEEDag(callerDIR, ret);
        return ret;
    }

    //updates ve map of invoked method
    private void analyzeMethod(String invokedSig) throws RegionAnalysisException {
        if(invokedSig.equals("com.yyqian.imagine.service.impl.UserServiceImpl: void authUserByToken(java.lang.String)")) {
            System.out.println("break");
        }
        Map <String, String> oldTypeMap = new HashMap<>(typeMap);
        typeMap = analyzeBCEL(invokedSig);
        if(FuncStackAnalyzer.funcDIRMap.containsKey(invokedSig)) {
            typeMap = oldTypeMap;
            return;
        }

        if (FuncStackAnalyzer.funcRegionMap.containsKey(invokedSig)) {
            ARegion topRegion = FuncStackAnalyzer.funcRegionMap.get(invokedSig);
            DIR dir = (DIR) topRegion.analyze();
            typeMap = oldTypeMap;
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
        RefType type = (RefType) v1.getType();
        SootClass sc = type.getSootClass();
        sc.getTags();
        d.dg("soot class tags: " + sc.getTags());
        List <AnnotationTag> annotations = getAnnotationTags(sc.getTags());
        VarNode v2vn = new VarNode(v2);
        VarNode v1vn = new VarNode(v1);
        for(AnnotationTag an : annotations) {
            if(an.getType().equals("Ljavax/persistence/Entity;")) {
                Node v2vnresolved = getResolvedEEDag(dir, v2vn);
                dir.insert(v1vn, v2vnresolved);
            }
        }
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

    //CASE v1 = v2
    private DIR processPointerAssignmentKnownType(Value v1, Value v2, DIR dir, Type type) {
        //TODO: Need to account for optional type
        debug d = new debug("DIRRegionAnalyzer.java", "processPointerAssignment()");
        DIR ret = new DIR();


        List <AccessPath> v1Paths = Flatten.flatten(v1, type, 0);
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
    public static boolean valueIsRepository(Value val) {
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
        d.turnOff();
        final int numParams = body.getMethod().getParameterCount();
        final List<Local> retVal = new ArrayList<Local>(numParams);
        d.dg("printing body of method: " + body.getMethod().getName() + " whose locals are to be extracted");
        for(Unit u : body.getUnits()) {
            d.clndg(u);
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

    private void caseIfCheckCast(VarNode dest, Node resolvedSource, Unit curUnit) {
        if(curUnit.toString().contains("if ignoreNew") && curUnit.toString().contains("getName()") && DAGTillNow.value == null)
            DAGTillNow.value = new Cloner().deepClone(resolvedSource);
    }

    public static boolean isEntityType(Type t) {
        if(t instanceof RefType == false)
            return false;
        RefType exprreftype = (RefType) t;
        SootClass sc = exprreftype.getSootClass();
        List <AnnotationTag> annotations = getAnnotationTags(sc.getTags());
        for(AnnotationTag an : annotations) {
            if(an.getType().equals("Ljavax/persistence/Entity;")) {
                return true;
            }
        }
        return false;
    }

    public static String getReposIdColumn(Type repotype) {
        debug d = new debug("DIRRegionAnalyzer.java", "getReposIdColumn()");

        String entity = RepoToEntity.getEntityForRepo(repotype.toString());
        d.dg("entity = " + entity);
        SootClass entitycls = Scene.v().forceResolve(entity, 1);
        d.dg("entity soot cls: " + entitycls);
        Chain<SootField> entityfields = entitycls.getFields();
        for (SootField sf : entityfields) {
            d.dg("field: " + sf);
            List<Tag> fieldtags = sf.getTags();
            List<AnnotationTag> annotations = getAnnotationTags(fieldtags);
            for (AnnotationTag at : annotations) {
                d.dg("annotation tag: " + at);
                if (at.getType().equals("Ljavax/persistence/Id;")) {
                    d.dg("id field = " + sf);
                    d.dg("id field name = " + sf.getName());
                    return sf.getName();
                }
            }
        }
        d.wrn("No field found with @Id annotation");
        return null;
    }



}



