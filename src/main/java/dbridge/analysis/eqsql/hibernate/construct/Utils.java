/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.analysis.DIRRegionAnalyzer;
import dbridge.analysis.eqsql.expr.node.Node;
import io.geetam.github.RepoToEntity.RepoToEntity;
import io.geetam.github.accesspath.AccessPath;
import io.geetam.github.accesspath.Flatten;
import io.geetam.github.accesspath.NRA;
import io.geetam.github.formalToActualVisitor.FormalToActual;
import io.geetam.github.hqlparser.CommonTreeWalk;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;

import java.util.*;

import dbridge.analysis.eqsql.util.SootClassHelper;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import exceptions.UnknownStatementException;
import mytest.debug;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.hibernate.hql.ast.origin.hql.parse.HQLLexer;
import org.hibernate.hql.ast.origin.hql.parse.HQLParser;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.*;

import static io.geetam.github.OptionalTypeInfo.*;
import static io.geetam.github.accesspath.Flatten.getAllFields;
import com.iisc.pav.AlloyGenerator;
import org.apache.bcel.classfile.*;

/**
 * Created by ek on 24/10/16.
 */


public class Utils {
    static VarNode fetchBase(Value source)  {
        Value base = null;
        if(source instanceof InstanceInvokeExpr)
            base = ((InstanceInvokeExpr)(source)).getBase();
//        else if(source instanceof InterfaceInvokeExpr)
//            base = ((InterfaceInvokeExpr)(source)).getBase();

        assert base instanceof JimpleLocal;
        Node var = NodeFactory.constructFromValue(base);
        assert var instanceof VarNode;
        return (VarNode) var;
    }

    public static Value fetchBaseValue(Value source)  {
        Value base = null;
        if(source instanceof InstanceInvokeExpr)
            base = ((InstanceInvokeExpr)(source)).getBase();
        return base;
    }


    public static VarNode getVarNode(ValueBox valueBox) throws UnknownStatementException {
        Value value = valueBox.getValue();
        debug d = new debug("Utils.java", "getVarNode()");
        d.dg(value);
        if(value.toString().equals("$i0")) {
            System.out.println("break point");
        }
        d.dg(valueBox);
//        if(!(value instanceof JimpleLocal)){
//            throw new UnknownStatementException(value + " is not JimpleLocal");
//        }

        Node var = NodeFactory.constructFromValue2(value);
        assert var instanceof VarNode;
        return (VarNode) var;
    }

    public static VarNode getVarNode(Value value) throws UnknownStatementException {

//        if(!(value instanceof JimpleLocal)){
//            throw new UnknownStatementException(value + " is not JimpleLocal");
//        }
        Node var = NodeFactory.constructFromValue(value);
        assert var instanceof VarNode;
        return (VarNode) var;
    }


    private static  Node[] makeNodeArray(List<Value> valueList){
        Node[] valueNodes = new Node[valueList.size()];
        int i = 0;
        for (Value value : valueList) {
            Node v = NodeFactory.constructFromValue(value);
            valueNodes[i] = v;
            i++;
        }
        return valueNodes;
    }

    /**
     * Create and return a Node by parsing InvokeExpr
     */
    public static Node parseInvokeExpr(InvokeExpr invokeExpr, int srcLineNumber){
        debug d=new debug("construct/Utils.java","parseInvokeExpr()");
        String methodName = invokeExpr.getMethod().getName();
        String methodSignature = trim(invokeExpr.getMethod().toString());
        d.dg("methodName = " +methodName);
        d.dg("methodSignature = "+ methodSignature);

        if(invokeExpr instanceof JStaticInvokeExpr){
            d.dg("JstaticInvokeExpr");
            return parseStaticInvoke(invokeExpr, methodName, methodSignature, srcLineNumber);
        }
        else{
            return parseObjectInvoke(invokeExpr, methodName, methodSignature, srcLineNumber);
        }
    }

    private static Node parseStaticInvoke(InvokeExpr invokeExpr, String methodName, String methodSignature, int srcLineNumber)
    {
        //wrong methodSignature is passed by caller, it does a invokeExpr.getMethod().toString() which
        //is wrong. TODO: clean
        methodSignature = normalizeMethodSignature(invokeExpr);
        debug d = new debug("Utils.java", "parseStaticInvoke()");
        d.dg("parseStaticInvoke: name: " + methodName);
        if(methodName.equals("valueOf") || methodName.equals("unmodifiableList")) {
            Node retNode;
            List<Value> args = invokeExpr.getArgs();
            assert args.size() == 1;
            retNode = NodeFactory.constructFromValue(args.get(0));
            return retNode;
        }

        String invokeExprStr = invokeExpr.toString();
        String callSiteStr = invokeExprStr + Integer.toString(srcLineNumber);
        d.dg("FuncStackAnalyzer.funcRegionMap.domain: ");
        d.dg(FuncStackAnalyzer.funcRegionMap.keySet());
        if(FuncStackAnalyzer.funcRegionMap.containsKey(methodSignature)
                && FuncStackAnalyzer.funcRegionMap.get(methodSignature) == null) {
            d.dg("method wont handle 1");
            return new MethodWontHandleNode(callSiteStr);
        }
        else if(FuncStackAnalyzer.funcRegionMap.containsKey(methodSignature)) {//only analyze methods whose body is available
            //get top region and call analyze
            debug.dbg("ConstrUtils.java", "parseObjectInvoke()", "method = " + methodSignature + " has an active body");
            ARegion calleeRegion = FuncStackAnalyzer.funcRegionMap.get(methodSignature);
            d.dg("calleeRegion class: " + calleeRegion.getClass());
            try {
                Map <String, String> oldTypeMap = new HashMap<>(typeMap);
                typeMap = analyzeBCEL(methodSignature);
                DIR calleeDIR = (DIR) calleeRegion.analyze();
                typeMap = oldTypeMap;

                FuncStackAnalyzer.funcDIRMap.put(methodSignature, calleeDIR);
                d.dg("Put DIR of callee = " + methodSignature + " in the map");
            } catch (RegionAnalysisException e) {
                e.printStackTrace();
            }
            return new NonLibraryMethodNode();
        }

        else {
            d.dg("method wont handle 2");
            return new MethodWontHandleNode(callSiteStr);
        }
    }

    public static String normalizeMethodSignature(InvokeExpr invokeExpr) {
        debug d = new debug("Utils.java", "normalizeMethodSignature()");
        String methodSignature;
        d.dg("invokeExpr.getMethod().getSignature(): " + invokeExpr.getMethod().getSignature());
        d.dg("invokeExpr.getMethodRef().getSignature(): " + invokeExpr.getMethodRef().getSignature());
        //methodSignature = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethodRef().getSignature());
        methodSignature = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());

        return methodSignature;
    }

    //Returns new NonLibraryMethodNode() for cases where return value is of pointer type but not a collection, relevant info is put into funcdirmap
    //Returns MethodWontHandleNode if body not present and it is not one of the library methods that are handled.
    //Returns return node for cases when return type is terminal.
    private static Node parseObjectInvoke(InvokeExpr invokeExpr, String methodName, String methodSignature, int srcLineNumber) {
        debug d = new debug("construct/Utils.java", "parseObjectInvoke()");
        d.dg(invokeExpr);
        methodSignature = normalizeMethodSignature(invokeExpr);

        d.dg( "methodName = " + methodName);
        d.dg( "invokeExpr = " + invokeExpr);
        d.dg( "methodSignature = " + methodSignature);


        Node[] args;
        MethodRef methodNode;
        FuncParamsNode funcParamsNode;
        VarNode baseObj;

        baseObj = fetchBase(invokeExpr);
        Value base = fetchBaseValue(invokeExpr);
        String invokeExprStr = invokeExpr.toString();
        String callSiteStr = invokeExprStr + Integer.toString(srcLineNumber);
        switch (methodSignature) {
            case "java.util.Optional: boolean isPresent()":
                NotEqNode notEmptySetBase = new NotEqNode(baseObj, new EmptySetNode());
                return notEmptySetBase;
            case "java.util.Optional: java.lang.Object get()":
                Type type = getKnownOptionalsActualType(base.toString());
                d.dg("base (arg0 to Flatten.flatten): " + base);
                d.dg("type (arg1 to Flatten.flatten): " + type);
                List <AccessPath> paths = Flatten.flatten(base, type, 0);
                d.dg("get(): paths = " + paths);
                DIR methodDir = FuncStackAnalyzer.funcDIRMap.get(methodSignature);
                if(methodDir == null) {
                    methodDir = new DIR();
                    FuncStackAnalyzer.funcDIRMap.put(methodSignature, methodDir);
                }
                AccessPath optret = new AccessPath("optionalret");
                methodDir.insert(optret.toVarNode(), new VarNode(base));
                AccessPath ret = new AccessPath("return");
                methodDir.insert(ret.toVarNode(), new VarNode(base));
                d.dg("methodDIR: " + methodDir);
                for(AccessPath ap : paths) {
                    String keyStr = "return" + ap.toString().substring(ap.toString().indexOf("."));
                    d.dg("get(): keyStr = " + keyStr);
                    d.dg("get(): val = " + ap);
                    methodDir.insert(new VarNode(keyStr), new VarNode(ap.toString()));
                }
                d.dg("get done");
                return new NonLibraryMethodNode();
            case "java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)":
                return BottomNode.v();
            case "java.util.Iterator: java.lang.Object next()":
                return new NextNode();
            case "java.math.BigDecimal: java.math.BigDecimal add(java.math.BigDecimal)":
                VarNode baseVarNode = new VarNode(base);
                Value argVal = invokeExpr.getArg(0);
                return new ArithAddNode(baseVarNode, NodeFactory.constructFromValue(argVal));
            case "java.math.BigDecimal: java.math.BigDecimal multiply(java.math.BigDecimal)":
                baseVarNode = new VarNode(base);
                argVal = invokeExpr.getArg(0);
                return new ArithMultiplyNode(baseVarNode, NodeFactory.constructFromValue(argVal));
            case "java.lang.Boolean: boolean booleanValue()":
                baseVarNode = new VarNode(base);
                VarNode retvn = new VarNode("return");
                methodDir = FuncStackAnalyzer.funcDIRMap.get(methodSignature);
                if(methodDir == null) {
                    methodDir = new DIR();
                    FuncStackAnalyzer.funcDIRMap.put(methodSignature, methodDir);
                }
                methodDir.insert(retvn, baseVarNode);
            case "java.lang.Double: double doubleValue()":
                baseVarNode = new VarNode(base);
                return baseVarNode;
        }

        switch (methodName) {

            case "equals":
                args = makeNodeArray(invokeExpr.getArgs());
                assert args.length == 1;
                return new EqNode(baseObj, args[0]); //Note the return here

            case "iterator":
                methodNode = new MethodIteratorNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);

//
//            case "next":
//                methodNode = new MethodNextNode();
//                funcParamsNode = FuncParamsNode.getEmptyParams();
//                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);
//

            case "hasNext":
                methodNode = new MethodHasNextNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);


            case "getHibernateTemplate":
                methodNode = new MethodGetHibernateTemplateNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);


//            case "booleanValue":
//                methodNode = new MethodBooleanValueNode();
//                funcParamsNode = FuncParamsNode.getEmptyParams();
//                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);


            case "add":
                args = makeNodeArray(invokeExpr.getArgs());
                funcParamsNode = new FuncParamsNode(args);
                methodNode = new MethodInsertNode();
                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);


            case "put":
                args = makeNodeArray(invokeExpr.getArgs());
                funcParamsNode = new FuncParamsNode(args);
                methodNode = new MethodMapPutNode();
                return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);

            //TODO: Remove this hardcoding
//            case "getPets":
////                String table = "pets";
////                Node cond = new EqNode(new VarNode("ownerId"), new VarNode("owner.id"));
////                SelectNode selectNode = new SelectNode(new ClassRefNode(table), cond);
////                return selectNode;
//                return new CartesianProdNode(new ClassRefNode("pets"));
            case "findPetTypes":
                String tablename = "PetType";
                return new CartesianProdNode(new ClassRefNode(tablename)); //note the return here

            case "loadAll":
                args = makeNodeArray(invokeExpr.getArgs());
                assert args.length == 1;
                assert args[0] instanceof ClassRefNode;
                return new CartesianProdNode((ClassRefNode)args[0]); //note the return here
            case "findOne":
                DIR dir = new DIR();
                VarNode projEl = new VarNode("id");
                String tableName = invokeExpr.getMethodRef().declaringClass().toString();
                Value idArg = invokeExpr.getArg(0);
                Node idArgNode = NodeFactory.constructFromValue(idArg);
                String repostr = base.getType().toString();
                d.dg("repostr in findOne case: " + repostr);
                String entity = RepoToEntity.getEntityForRepo(repostr);
                d.dg("entity for repo in findOne case: " + entity);
                SootClass entitycls = Scene.v().loadClass(entity, 1);
                d.dg("entity class findOne case: " + entitycls);
                //Node eqCondition = new EqNode(new VarNode("id"), idArgNode);
                Node eqCondition = new EqNode(new FieldRefNode(entitycls.getName(), "id", entitycls.getName()), idArgNode);
                SelectNode relation = new SelectNode(new ClassRefNode(tableName), eqCondition);
                d.dg("check 1 ="+relation);
                mapDBFetchAccessGraph(dir.getVeMap(), new AccessPath("return"), relation, entitycls, 0);
                FuncStackAnalyzer.funcDIRMap.put(methodSignature, dir);
                return new NonLibraryMethodNode();
//            case "findAll":
//                String table = invokeExpr.getMethodRef().declaringClass().toString();
//                return new CartesianProdNode(new ClassRefNode(table)); //note the return here
            default:
                String table;
                if(methodName.startsWith("findAll") && DIRRegionAnalyzer.valueIsRepository(base)) {
                    d.dg("Case : findAll");

                    String sig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethod().getSignature());
                    Map.Entry <Node, String> relExpField =  getRelExpForMethod(invokeExpr);
                    Node relExp=null;
                    d.dg("relExpAndJoinedField = "+relExpField);
                    if(relExpField == null){
                        Type tableEntity= AccessPath.getCollectionEntityType(invokeExpr);
                        String retType= tableEntity.toString().substring(tableEntity.toString().lastIndexOf("//.")+1);
                        // Akash
                        String repo = invokeExprStr.substring(invokeExprStr.indexOf("<")+1, invokeExprStr.indexOf(":"));
//                        relExp = new SelectNode(new ClassRefNode(retType),new NullNode());
                        relExp = new SelectNode(new ClassRefNode(repo),new NullNode());

                    }
                    else{
                        relExp = relExpField.getKey();
                    }
                    dir = new DIR();
                    dir.insert(new VarNode("return"),relExp);
                    FuncStackAnalyzer.funcDIRMap.put(sig,dir);
                    return new NonLibraryMethodNode(); //note the return here
                }
                else
                if(methodName.startsWith("findBy")) {

                    d.dg("Case : findBy");
                    Map.Entry <Node, String> relExpAndJoinedField =  getRelExpForMethod(invokeExpr);
                    d.dg("relExpFor method : "+invokeExpr);
                    d.dg("expr : "+relExpAndJoinedField);
                    if(relExpAndJoinedField != null) {
                        Node relExp = relExpAndJoinedField.getKey();
                        d.dg("relExp = "+relExp);
                        // Akash
                        String newTableName = invokeExprStr.substring(invokeExprStr.lastIndexOf(':')+2, invokeExprStr.lastIndexOf(' '));
                        if(!newTableName.contains("Collection")) {
                            ClassRefNode newTable = new ClassRefNode(newTableName);
                            exchangeClassRefNode(null, relExp, newTable, 0);
                        }

                        String joinedField = relExpAndJoinedField.getValue();
                        String attName = methodName.substring(6);
                        String sig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethodRef().getSignature());
                        String retTypeStr = invokeExpr.getMethodRef().returnType().toString();
                        if(retTypeStr.equals("java.util.Optional")) {
                            Map<String, String> typeTable = analyzeBCEL(sig);
                            retTypeStr = typeTable.get("return_" + sig);
                        }
                        SootClass entityClass = Scene.v().loadClassAndSupport(retTypeStr);
                        d.dg("entityClass = "+ entityClass);
                        Type retType = entityClass.getType();
                        d.dg("retType = "+retType);
                        if(AccessPath.isCollectionType(retType) && AccessPath.isReturnTypeEntity(invokeExpr)){
                            dir = new DIR();
                            dir.insert(new VarNode("return"), relExp);
                            FuncStackAnalyzer.funcDIRMap.put(methodSignature, dir);
                            return new NonLibraryMethodNode();
                        }
                        if(AccessPath.isTerminalType(retType)) {
                            d.dg("retType is terminalType");
                            return relExp;
                        } else {
                            List <String> attributes = Flatten.flattenEntityClass(entityClass);
                            d.dg("attributes = " + attributes);
                            dir = new DIR();
                            dir.insert(new VarNode("return"), relExp);
                            for(String att : attributes) {
                                ProjectNode projNode = new ProjectNode(relExp, new VarNode(att));
                                VarNode key = new VarNode("return." + att);
                                dir.insert(key, projNode);
                                d.dg("Mapped " + key + " to " + projNode);
                            }
                            String joinrightop = bcelActualCollectionFieldType(retTypeStr, joinedField);
                            d.dg("nested field type = "+ joinrightop);
                            // Akash
//                            String[] classNameSplit= joinrightop.split("\\.");
//                            JoinNode join = new JoinNode(relExp, new ClassRefNode(classNameSplit[classNameSplit.length-1]),new NullNode());
                            JoinNode join = new JoinNode(relExp, new ClassRefNode(joinrightop),new NullNode());

                            VarNode baseDotJoinedField = new VarNode("return." + joinedField);
                            d.dg("check = "+join);
                            dir.insert(baseDotJoinedField, join);
                            FuncStackAnalyzer.funcDIRMap.put(methodSignature, dir);
                            return new NonLibraryMethodNode();
                        }
                    }
                    else if (DIRRegionAnalyzer.valueIsRepository(base)){
                        //How Spring creates query based on method name:
                        // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
                        // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions

                        String attName = methodName.substring(6);
                        String sig = SootClassHelper.trimSootMethodSignature(invokeExpr.getMethodRef().getSignature());
                        String retTypeStr = invokeExpr.getMethodRef().returnType().toString();
                        boolean findByReturnsOptional = retTypeStr.equals("java.util.Optional");
                        if(findByReturnsOptional) {
                            d.dg("Return is optional-typed for function: " + methodSignature);
                            Map<String, String> typeTable = analyzeBCEL(sig);
                            d.dg("typeTable after analyzeBCEL: " + typeTable);
                            retTypeStr = typeTable.get("return_" + sig);
                        }
                        d.dg("retTypeStr: " + retTypeStr);
                        SootClass entityClass = Scene.v().loadClassAndSupport(retTypeStr);
                        Type retType = entityClass.getType();
                        d.dg("retType = " + retType);
                        d.dg("entityClass = " + entityClass);
                        if(AccessPath.isCollectionType(retType) == false) {
                            String []entityClassNameSplit = entityClass.toString().split("//.");
                            tableName = entityClassNameSplit[entityClassNameSplit.length-1];
                            d.dg("tableName = " + tableName);
                            List<Value> arglist = invokeExpr.getArgs();
                            assert arglist.size() == 1;
                            Value arg = arglist.get(0);
                            Node actualParam = NodeFactory.constructFromValue(arg);
                            Node condition = new EqNode(new FieldRefNode(tableName, attName, tableName), actualParam);
                            SelectNode select = new SelectNode(new ClassRefNode(tableName), condition);
                            d.dg("check = "+select);
                            List<String> attributes = Flatten.flattenEntityClass(entityClass);
                            d.dg("attributes = " + attributes);
                            dir = new DIR();
//                        for(String att : attributes) {
//                            ProjectNode projNode = new ProjectNode(select, new VarNode(att));
//                            VarNode key = new VarNode("return." + att);
//                            dir.insert(key, projNode);
//                            d.dg("Mapped " + key + " to " + projNode);
//                        }
                            AccessPath retAccp = new AccessPath("return");
                            d.dg("retAccp: " + retAccp.toString());
                            d.dg("check 2");
                            mapDBFetchAccessGraph(dir.getVeMap(), retAccp, select, entityClass, 0);
                            d.dg("dir after mapDBFetchAccessGraph: " + dir.getVeMap());
                            FuncStackAnalyzer.funcDIRMap.put(methodSignature, dir);
                            d.dg("@Query not present, relnode = " + select);

                            if(findByReturnsOptional) {
                                AccessPath optionalRet = new AccessPath("optionalret");
                                dir.insert(optionalRet.toVarNode(), select);
                            }
                        }
                        else {
                            table = invokeExpr.getMethodRef().declaringClass().toString();
                            d.dg("table = "+table);
                            List<Value> arglist = invokeExpr.getArgs();
                            assert arglist.size() == 1;
                            Value arg = arglist.get(0);
                            Node retNode;
                            if(AccessPath.isPrimitiveType(arg.getType())) {
                                Node actualParam = NodeFactory.constructFromValue(arg);
                                Node condition = new EqNode(new FieldRefNode(table, attName, table), actualParam);
                                retNode = new SelectNode(new ClassRefNode(table), condition);
                                d.dg("check ="+retNode);
                            } else {
                                Node actualParam = NodeFactory.constructFromValue(arg);
                                retNode = new JoinNode(actualParam, new ClassRefNode(table),new NullNode());
                            }
                            dir = new DIR();
                            dir.insert(RetVarNode.getARetVar(), retNode);
                            FuncStackAnalyzer.funcDIRMap.put(methodSignature, dir);
                            d.dg("@Query not present, relnode = " + retNode);
                        }
                        return new NonLibraryMethodNode();
                    }
                }

                else if(methodName.startsWith("add")) { //union to a field which is a collection
                    String attName = methodName.substring(3).toLowerCase();
                    d.dg("baseObj: " + baseObj);
                    String keyStr = baseObj.toString() + "." + attName;
                    VarNode key = new VarNode(keyStr);
                    VarNode oldFieldCollection = new VarNode(keyStr);
                    Node argNode = NodeFactory.constructFromValue(invokeExpr.getArg(0));
                    UnionNode unionNode = new UnionNode(oldFieldCollection, argNode);
                    return unionNode;
                }

//                args = makeNodeArray(invokeExpr.getArgs());
//                funcParamsNode = new FuncParamsNode(args);
//                methodNode = new MethodRefNode(methodSignature);
//
//                break;
        }
        d.dg("FuncStackAnalyzer.funcRegionMap.domain: " + FuncStackAnalyzer.funcRegionMap.keySet());
        //Case: Method has improper region structure.
        if(FuncStackAnalyzer.funcRegionMap.containsKey(methodSignature)
                && FuncStackAnalyzer.funcRegionMap.get(methodSignature) == null) {
            d.dg("method wont handle 3");
            return new MethodWontHandleNode(callSiteStr);
        }
        else if(FuncStackAnalyzer.funcRegionMap.containsKey(methodSignature)) {//only analyze methods whose body is available
            //get top region and call analyze
            if(methodSignature.equals("com.bookstore.domain.User: com.bookstore.domain.ShoppingCart getShoppingCart()")) {
                d.dg("break");
            }
            debug.dbg("ConstrUtils.java", "parseObjectInvoke()", "method = " + methodSignature + " has an active body");
            ARegion calleeRegion = FuncStackAnalyzer.funcRegionMap.get(methodSignature);
            d.dg("calleeRegion class: " + calleeRegion.getClass());
            try {
                Map <String, String> oldTypeMap = new HashMap<>(typeMap);
                typeMap = analyzeBCEL(methodSignature);
                DIR calleeDIR = (DIR) calleeRegion.analyze();
                typeMap = oldTypeMap;

                FuncStackAnalyzer.funcDIRMap.put(methodSignature, calleeDIR);
                d.dg("Put DIR of callee = " + methodSignature + " in the map");
            } catch (RegionAnalysisException e) {
                e.printStackTrace();
            }
            return new NonLibraryMethodNode();
        }

        else {
            d.dg("method wont handle 4");
            return new MethodWontHandleNode(callSiteStr);
        }

    }

    private static void exchangeClassRefNode(Node parent, Node relExp, ClassRefNode newTable, int childNum) {
        if(relExp instanceof ClassRefNode)
            parent.setChild(childNum, newTable);
        for(int i=0; i<relExp.getNumChildren(); i++){
            exchangeClassRefNode(relExp, relExp.getChild(i), newTable, i);
        }
    }


    /** Remove the angular brackets appended by SootMethod.toString() to the method signature at the beginning and
     * the end
     */
    public static String trim(String methodSign){
        return methodSign.substring(1, methodSign.length() - 1);
    }

    public static Map.Entry<Node, String> getRelExpForMethod(InvokeExpr invokeExpr) {
        debug d = new debug("Utils.java", "getRelExpForMethod()");
        String joinedField = "";
        d.dg("getRelExpForMethod: " + invokeExpr);
        SootMethod methodInvoked = invokeExpr.getMethod();
        List <Value> args = invokeExpr.getArgs();

        d.dg("actualargs = " + args);
        d.dg("methodInvoked = " + methodInvoked);
        List <Tag> tagList = methodInvoked.getTags();
        d.dg("taglist: \n" + tagList);
        /*TODO: For now assuming that the query has only one param
         */
        String paramName = getQueryParamNameFromTagList(tagList);
        String query = getQueryFromTagList(tagList);
        if(query == null) {
            return null;
        }
        CommonTree parsedTree = getParsedTree(query);
        CommonTreeWalk.postOrder(parsedTree, 0);
        joinedField = CommonTreeWalk.leftJoinedField;
        d.dg("Info collected by walk: ");
        CommonTreeWalk.printInfo();
        SelectNode relExp = CommonTreeWalk.getRelNode();
        Value actualArg = args.get(0);
        Node actaulArgDBNode = NodeFactory.constructFromValue(actualArg);
        FormalToActual formalToActualVisitor = new FormalToActual(new VarNode(paramName), actaulArgDBNode);
        relExp.accept(formalToActualVisitor);
        return new AbstractMap.SimpleEntry<>(relExp, joinedField);
    }

    public static String getCorrespondingPlaceholderForIthParam(String methodName, int paramNumber) {
        return null;
    }

    public static CommonTree getParsedTree(String query) {
        ANTLRStringStream antlrStream = new ANTLRStringStream(query);
        HQLLexer lexer = new HQLLexer( antlrStream );
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        HQLParser parser = new HQLParser( tokens );
        HQLParser.statement_return statement = null;
        try {
            statement = parser.statement();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        // System.out.println( tokens.getTokens() );
        CommonTree tree = (CommonTree) statement.getTree();
        return tree;
    }

    public static String getQueryParamNameFromTagList(List <Tag> tagListOfMethod) {
        for(Tag tag : tagListOfMethod) {
            if (tag instanceof VisibilityParameterAnnotationTag) {
                VisibilityParameterAnnotationTag parameterAnnotationTag = (VisibilityParameterAnnotationTag) tag;
                List<VisibilityAnnotationTag> annotationList = parameterAnnotationTag.getVisibilityAnnotations();
                for(VisibilityAnnotationTag paramTag : annotationList) {
                    List <AnnotationTag> paramTagAnnotations = paramTag.getAnnotations();
                    for(AnnotationTag annotation : paramTagAnnotations) {
                        Collection<AnnotationElem> elems = annotation.getElems();
                        for(AnnotationElem elem : elems) {
                            String param = ((AnnotationStringElem)elem).getValue();
                            return param;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getQueryFromTagList(List<Tag> tagListOfMethod) {
        List<AnnotationTag> annotationTagList = getAnnotationTags(tagListOfMethod);
        for (AnnotationTag annotation : annotationTagList) {
            if (annotation.getType().equals("Lorg/springframework/data/jpa/repository/Query;")) {
                for (AnnotationElem annotationElem : annotation.getElems()) {
                    if (annotationElem instanceof AnnotationStringElem) {
                        AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
                        String query = annotationStringElem.getValue();
                        return query;
                    }
                }
            }
        }
        return null;
    }

    public static List<AnnotationTag> getAnnotationTags(List <Tag> tags) {
        List <AnnotationTag> ret = new ArrayList<>();
        for(Tag tag : tags) {
            if(tag instanceof VisibilityAnnotationTag) {
                VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
                ret.addAll(visibilityAnnotationTag.getAnnotations());
            }
        }
        return ret;
    }

    public static Collection <SootField> primFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        for(SootField sf : getAllFields(cls)) {
            if(AccessPath.isPrimitiveType(sf.getType())) {
                ret.add(sf);
            }
        }
        return ret;
    }

    public static Collection <SootField> oneToOneFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        for(SootField sf : cls.getFields()) {
            List <Tag> tags = sf.getTags();
            List <AnnotationTag> annotationTags = getAnnotationTags(tags);
            for(AnnotationTag ann : annotationTags) {
                if(ann.getType().toString().equals("Ljavax/persistence/OneToOne;")) {
                    ret.add(sf);
                }
            }
        }
        return ret;
    }
    public static Collection <SootField> starToOneFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        ret.addAll(oneToOneFields(cls));
        ret.addAll(manyToOneFields(cls));
        return ret;
    }

    public static Collection <SootField> collectionFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        for(SootField sf : cls.getFields()) {
            List <Tag> tags = sf.getTags();
            List <AnnotationTag> annotationTags = getAnnotationTags(tags);
            for(AnnotationTag ann : annotationTags) {
                if(ann.getType().toString().equals("Ljavax/persistence/OneToMany;")) {
                    ret.add(sf);
                }
            }
        }
        return ret;
    }

    public static Collection <SootField> manyToManyFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        for(SootField sf : cls.getFields()) {
            List <Tag> tags = sf.getTags();
            List <AnnotationTag> annotationTags = getAnnotationTags(tags);
            for(AnnotationTag ann : annotationTags) {
                if(ann.getType().toString().equals("Ljavax/persistence/ManyToMany;")) {
                    ret.add(sf);
                }
            }
        }
        return ret;
    }
    public static Collection <SootField> manyToOneFields(SootClass cls) {
        Collection <SootField> ret = new ArrayList<>();
        for(SootField sf : cls.getFields()) {
            List <Tag> tags = sf.getTags();
            List <AnnotationTag> annotationTags = getAnnotationTags(tags);
            for(AnnotationTag ann : annotationTags) {
                if(ann.getType().toString().equals("Ljavax/persistence/ManyToOne;")) {
                    ret.add(sf);
                }
            }
        }
        return ret;
    }
    public static boolean isAStarToOneField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().toString().equals("Ljavax/persistence/OneToOne;")
                    || ann.getType().toString().equals("Ljavax/persistence/ManyToOne;")
            ) {
                return true;
            }
        }

        return false;
    }

    public static void mapDBFetchAccessGraph(Map <VarNode, Node> veMap, AccessPath baseAccp, Node relExpBaseAccp, SootClass baseAccpCls, int depth) {
        if(depth > Flatten.BOUND) {
            return;
        }
        debug d=new debug("cosntruct/utils.java","mapDBFetchAccessGraph()");

        d.dg("check relExpBaseAccp= "+relExpBaseAccp );
        if(relExpBaseAccp == null) return;
        if(!(relExpBaseAccp.isLeaf()) && relExpBaseAccp.getOperator().getName().equals(relExpBaseAccp.getChild(0).getOperator().getName())){
            relExpBaseAccp = relExpBaseAccp.getChild(0);
        }
        veMap.put(baseAccp.toVarNode(), relExpBaseAccp);
        String clssig = baseAccpCls.getName();
        Collection <SootField> prims = primFields(baseAccpCls);
        d.dg("prim fields = "+prims);
        for(SootField primF : prims) {
            AccessPath newAccp = baseAccp.clone();
            newAccp.append(primF.getName());
            //Requires: The formal param name for column is same as the field name for the same column in the Entity cls
            Node projected = null;
            if(depth == 0) {
                projected = new VarNode(primF.getName());
            } else {
                projected = new FieldRefNode(clssig, primF.getName(), clssig);
            }
            ProjectNode projectNode = new ProjectNode(relExpBaseAccp, projected);
            veMap.put(newAccp.toVarNode(), projectNode);
        }

        Collection <SootField> oneToOneVars = oneToOneFields(baseAccpCls);
        d.dg("oneToOne fields= "+oneToOneVars);
        for(SootField mbVarF : oneToOneVars) {
            AccessPath newAccp = baseAccp.clone();
            newAccp.append(mbVarF.getName());
            ClassRefNode rightClsRefNode = new ClassRefNode(mbVarF.getType().toString());
            JoinNode newRelExpBase = new JoinNode(relExpBaseAccp, rightClsRefNode,new NullNode());
            d.dg("check = "+newRelExpBase);
            RefType ftype = (RefType) mbVarF.getType();
            veMap.put(newAccp.toVarNode(), newRelExpBase);
            mapDBFetchAccessGraph(veMap, newAccp, newRelExpBase, ftype.getSootClass(), depth + 1);
        }

        Collection <SootField> manyToOneVars = manyToOneFields(baseAccpCls);
        d.dg("manyToOne fields = "+manyToOneVars);
        for(SootField mbVarF : manyToOneVars) {
            AccessPath newAccp = baseAccp.clone();
            newAccp.append(mbVarF.getName());
            ClassRefNode rightClsRefNode = new ClassRefNode(mbVarF.getType().toString());
            JoinNode newRelExpBase = new JoinNode(relExpBaseAccp, rightClsRefNode,new NullNode());
            RefType ftype = (RefType) mbVarF.getType();
            mapDBFetchAccessGraph(veMap, newAccp, newRelExpBase, ftype.getSootClass(), depth + 1);
        }

        Collection <SootField> collectionFields = collectionFields(baseAccpCls);
        d.dg("collection fields = "+collectionFields);
        for(SootField collF : collectionFields) {
            String actualCollFEleType = bcelActualCollectionFieldType(clssig, collF.getName());
            AccessPath newAccp = baseAccp.clone();
            newAccp.append(collF.getName());
            ClassRefNode rightClsRefNode = new ClassRefNode(actualCollFEleType);
            JoinNode newRelExpBase = new JoinNode(relExpBaseAccp, rightClsRefNode,new NullNode());
            veMap.put(newAccp.toVarNode(), newRelExpBase);
        }

        Collection <SootField> manyToManyFields = manyToManyFields(baseAccpCls);
        d.dg("manyTomany fields = "+manyToManyFields);
        for(SootField mmf : manyToManyFields) {
            String actualCollFEleType = bcelActualCollectionFieldType(clssig, mmf.getName());
            AccessPath newAccp = baseAccp.clone();
            newAccp.append(mmf.getName());
            ClassRefNode rightClsRefNode = new ClassRefNode(actualCollFEleType);
            JoinNode newRelExpBase = new JoinNode(relExpBaseAccp, rightClsRefNode,new NullNode());
            veMap.put(newAccp.toVarNode(), newRelExpBase);
        }


    }


    //Myimpl
    public static boolean isStarToManyField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/OneToMany;")
                    || ann.getType().equals("Ljavax/persistence/ManyToMany;")
            ) {
                return true;
            }
        }

        return false;
    }

    public static boolean isOneToOneField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/OneToOne;"))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isOneToManyField(SootField sf) {
        debug d=new debug("Utils.java","isOneToManyField()");
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/OneToMany;"))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isManyToManyField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/ManyToMany;"))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isManyToOneField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/ManyToOne;"))
            {
                return true;
            }
        }

        return false;
    }


//    public static boolean isManyToManyFields(Field sf) {
//        debug d=new debug("Utils.java","isManyToMany(Field)");
//        AnnotationEntry[]  anntags= sf.getAnnotationEntries();
//        d.dg("sf= "+sf.getName());
//        for(AnnotationEntry ann : anntags) {
//            d.dg("ann= " + ann.toString());
////            d.dg("ann type= " + ann.getAnnotationType());
//            if(ann.getAnnotationType().equals("Ljavax/persistence/OneToMany;")
//                    || ann.getAnnotationType().equals("Ljavax/persistence/ManyToMany;")
//            ) {
//            return true;
//        }
//        }
//
//        return false;
//    }

    public static boolean isTransientField(SootField sf) {
        List <Tag> tags = sf.getTags();
        List <AnnotationTag> annotationTags = getAnnotationTags(tags);
        for(AnnotationTag ann : annotationTags) {
            if(ann.getType().equals("Ljavax/persistence/Transient;")
            ) {
                return true;
            }
        }

        return false;
    }


    public static String bcelActualCollectionFieldType(String className, String fieldName) {
        debug d = new debug("Construct/Utils.java", "bcelActualCollectionFieldType()");
        d.dg("className= "+className + "\n fieldName= "+fieldName);
        org.apache.bcel.util.Repository bcelrepo = Repository.getRepository();
        try {
            JavaClass bcelclass = Repository.lookupClass(className);
            Field[] fields = bcelclass.getFields();
            for(Field field : fields) {
                d.dg("bcel field = " + field);
                if(field.getName().equals(fieldName)) {
                    AnnotationEntry[] annotations=field.getAnnotationEntries();
                    for(AnnotationEntry ae:annotations){
                        d.dg("annotation Entry= "+ae.getAnnotationType());
                        if(ae.getAnnotationType().equals("Ljavax/persistence/ManyToOne;")){
                            d.dg("ManytoOne field= "+fieldName);
                            return field.getType().toString();
                        }

                        if(ae.getAnnotationType().equals("Ljavax/persistence/Transient;")){
                            d.dg("Transient field= "+fieldName);
                            return field.getType().toString();
                        }

                        if(ae.getAnnotationType().equals("Ljavax/persistence/OneToOne;")){
                            d.dg("OneToOne field= "+fieldName);
                            return field.getType().toString();
                        }

                    }
                    Attribute[] attributes = field.getAttributes();
                    for(Attribute att : attributes) {
                        d.dg("att = "+att);
                        if(att instanceof Signature) {
//                            d.dg("att= "+att);
                            Signature sigatt = (Signature) att;
                            d.dg("sigatt = " + sigatt);
                            String sigstr = sigatt.getSignature();
                            if(sigstr.startsWith("Ljava/util/Collection")
                                    || sigstr.startsWith("Ljava/util/Set")
                                    || sigstr.startsWith("Ljava/util/List")
                                    || sigstr.startsWith("Ljava.lang/Iterable")
                                    || sigstr.startsWith("Ljava/util/Queue")
                                    || sigstr.startsWith("Ljava/util/Set")
                                    || sigstr.startsWith("Ljava/util/Dequeue")) {
                                Integer angledOpenIndex = sigstr.indexOf("<");
                                Integer angledCloseIndex = sigstr.indexOf(">");
                                return sigstr.substring(angledOpenIndex + 2, angledCloseIndex - 1).replace("/", ".");
                            }
                        }
                    }
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    //////// Myimpl end ////


}
