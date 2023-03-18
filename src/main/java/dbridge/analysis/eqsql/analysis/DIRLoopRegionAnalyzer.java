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

import com.rits.cloning.Cloner;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.trans.Rule;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.LoopRegion;
import io.geetam.github.ExprRepVisitor;
import io.geetam.github.accesspath.AccessPath;
import io.geetam.github.accesspath.Flatten;
import io.geetam.github.loopHandler.DAGTillNow;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import mytest.debug;
import polyglot.ast.Loop;
import soot.Body;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JInvokeStmt;

import java.util.*;
import io.geetam.github.patternMatch.patternMatch;

import static io.geetam.github.patternMatch.patternMatch.getUserInputRules;

/**
 * Created by ek on 4/5/16.
 */
public class DIRLoopRegionAnalyzer extends AbstractDIRRegionAnalyzer {
    Collection <Rule> userInputRules;
    Map<VarNode, Node> dir;
    private static boolean isIterationOverList;

    /* Singleton */
    private DIRLoopRegionAnalyzer(){
        debug d = new debug("DIRLoopRegionAnalyzer.java", "DIRLoopRegionAnalyzer()");
        try {
            userInputRules = getUserInputRules();
        }
        catch (Exception e) {
            userInputRules = new ArrayList<>();
            d.dg(e);
        }
    };
    public static DIRLoopRegionAnalyzer INSTANCE = new DIRLoopRegionAnalyzer();

    public static Collection <VarNode> getFoldVars(Map <VarNode, Node> veMap, Value iterator, VarNode collection) {
        Set <VarNode> ret = new HashSet<>();
        if(collectionModified(veMap, iterator)) {
            ret.add(collection);
        }
        ret.addAll(veMap.keySet());
        return ret;
    }

    public static boolean collectionModified(Map <VarNode, Node> veMap, Value iterator) {
        List <AccessPath> flattenedIterator = Flatten.flattenEntity(iterator, iterator.getType());
        List <String> tableAttributes = Flatten.attributes(flattenedIterator);
        for(int i = 0; i < flattenedIterator.size(); i++) {
            String atr = tableAttributes.get(i);
            FieldRefNode fieldRefAtr = new FieldRefNode(iterator.getType().toString(), atr, iterator.getType().toString());
            VarNode varNodeAtr = new VarNode(flattenedIterator.get(i).toString());
            if(veMap.get(varNodeAtr).equals(fieldRefAtr) == false) {
                return true;
            }
        }
        return false;
    }

    public static Collection <VarNode> fieldVarNodesOfIterator(Value iterator) {
        List <VarNode> ret = new ArrayList<>();
        List <AccessPath> flattenedIterator = Flatten.flattenEntity(iterator, iterator.getType());
        List <String> tableAttributes = Flatten.attributes(flattenedIterator);
        for(int i = 0; i < flattenedIterator.size(); i++) {
            String atr = tableAttributes.get(i);
            VarNode varNodeAtr = new VarNode(flattenedIterator.get(i).toString());
            ret.add(varNodeAtr);
        }
        return ret;
    }

    public static Value getIterator(Collection <Unit> units, Map <VarNode, Node> bodyVEMap) {
        Iterator itr = units.iterator();
        while (itr.hasNext()) {
            Unit unit = (Unit) itr.next();
            if(unit instanceof JAssignStmt) {
                JAssignStmt assignStmt = (JAssignStmt) unit;
                if(assignStmt.rightBox.getValue() instanceof JCastExpr) {
                    JCastExpr castExpr = (JCastExpr) assignStmt.rightBox.getValue();
                    Value right = castExpr.getOp();
                    VarNode rightVN = new VarNode(right);
                    if(bodyVEMap.containsKey(rightVN)) {
                        Node rightVEMapping = bodyVEMap.get(rightVN);
                        if(rightVEMapping instanceof NextNode) {
                            return assignStmt.leftBox.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {
//        dir = DAGTillNow.getDag();
        assert region instanceof LoopRegion;
        LoopRegion loopR = (LoopRegion) region;
        debug d = new debug("DIRLoopRegionAnalyzer.java", "constructDIR()");
 //       ARegion head = region.getSubRegions().get(0);
   //     ARegion loopBody = region.getSubRegions().get(1);
        ARegion head = loopR.head;
        ARegion loopBody = loopR.body;

        d.dg("Analyzing loop head");
        DIR headDIR = (DIR) head.analyze();
        d.dg("Analyzing loop body");
        DIR bodyDIR = (DIR) loopBody.analyze();
        d.dg("Done with analyzing loop body");

        Map <VarNode, Node> bodyVEMap = bodyDIR.getVeMap();
        d.dg("bodyVEMap: " + bodyVEMap);
        d.dg("headVEMap: " + headDIR.getVeMap());
        d.dg("headR: " + head);
        System.out.println(FuncStackAnalyzer.funcDIRMap);
        VarNode i_itr = null;
        i_itr = getArrayIntItr(bodyVEMap, i_itr);
        Boolean isIterationOverArray = i_itr != null || bodyVEMap.containsKey(new VarNode("iteration_over_array"));
        VarNode javaSrcItr = null;
        DIR loopDIR = new DIR();
        Map<VarNode, Node> dir = DAGTillNow.getDag();
        if (isIterationOverArray) {
            javaSrcItr = getArrayIntJavaSrcItr(bodyVEMap, i_itr, javaSrcItr);
        }
        isIterationOverList = bodyDIR.getVeMap().containsKey(new VarNode("l4"));
        VarNode loopingVar = getLoopingCol(headDIR, bodyDIR);
        if(loopingVar == null) {
//            loopDIR = new DIR();
            for(VarNode vn : bodyDIR.getVeMap().keySet()) {
                loopDIR.insert(vn, new UnknownNode());
            }
            return loopDIR;
        }

        d.dg("loopingVar: " + loopingVar);

        Collection <Unit> units = ((LoopRegion) region).body.getUnits();
        Value iterator = getIterator(units, bodyVEMap);
        Collection <VarNode> foldVars = bodyVEMap.keySet();
        if(iterator != null) {
            foldVars = getFoldVars(bodyVEMap, iterator, loopingVar); // contains pet.id, pet.name, pet.visits etc
        }
        d.dg("foldVars: " + foldVars);
//        loopDIR = new DIR();
        if (isIterationOverArray) {
            replaceArefItrWithNext(bodyVEMap, javaSrcItr);
        }

        //***********************************************************************************************************//
        Collection<VarNode> itrPrimitiveFields = new ArrayList();
        if(iterator != null)
            itrPrimitiveFields = fieldVarNodesOfIterator(iterator); // pet.id, pet.name, pet.birthDate
//        for(VarNode key : bodyVEMap.keySet()){
//            if(key.toString().indexOf(iterator.toString()) != -1 && !itrPrimitiveFields.contains(key)){
//                LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.put(key, bodyVEMap.get(key));
//            }
//        }
//        LoopIteratorCollectionHandler loopIteratorCollectionHandler = new LoopIteratorCollectionHandler();
////        loopIteratorCollectionHandler.printJimpleLHSRHS(loopBody);
//        loopIteratorCollectionHandler.inLineCollectionIteratorToCollection(iteratorEntityVars, bodyVEMap, region, loopDIR);
//        System.out.println(LoopIteratorCollectionHandler.changedLoopEntityFieldsMap);
        //***********************************************************************************************************//



        Node iteratorInVEMap = getKeyMappedToNext(bodyVEMap);
        for(VarNode uvar : foldVars) {
            d.dg("uvar: " + uvar);
            if (uvar.equals(loopingVar) && iterator != null) { // iterator of loop got changed
                try {
                    List<Node> fieldExprs = new ArrayList<>();
                    Collection<VarNode> fieldVarNodes = fieldVarNodesOfIterator(iterator);
                    ///////////////////////////////////////////////////////
                    for (VarNode key : bodyVEMap.keySet()) {
                        if (key.toString().indexOf(iterator.toString()) != -1 && !itrPrimitiveFields.contains(key))
                            LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.put(key, bodyVEMap.get(key));
                        if (key.toString().indexOf(iterator.toString()) != -1 && itrPrimitiveFields.contains(key))
                            LoopIteratorCollectionHandler.changedLoopPrimitiveFieldsMap.put(key, bodyVEMap.get(key));
//                    System.out.println(LoopIteratorCollectionHandler.changedLoopFieldsMap);
                    }

//                InvokeMethodNode iteratorMapping = (InvokeMethodNode) dir.find(iterator);
//                dir.getVeMap().put(iterator, iteratorMapping.getChild(0));
                    /////////////////////////////////////////////////////////////////////////////////////////////////////
                    List<Node> changedLoopVarList = new ArrayList(LoopIteratorCollectionHandler.changedLoopPrimitiveFieldsMap.keySet());
                    String iteratorname = changedLoopVarList.get(0).toString();
                    iteratorname = iteratorname.substring(0, iteratorname.indexOf('.'));
//                System.out.println(iteratorname);
                    VarNode toReplaceKey = getToReplaceKey(iteratorname);
                    Node toReplaceVeMap = dir.get(toReplaceKey);
                    boolean seenRepo = false;
                    for(VarNode key : loopDIR.getVeMap().keySet()){
                        if(key.toString().contains("Repository"))
                            seenRepo = true;
                    }

                    if(seenRepo == true){
                        while(toReplaceKey.toString().contains("Repository")){
                            dir.remove(toReplaceKey);
                            toReplaceKey = getToReplaceKey(iteratorname);
                        }
                        // already seen the Repository VEMap for this collection,
                        // so create VEMap of this collection using the Repository VEMap already created and present in loopDIR
                        getVEMapOfCollection(loopDIR, toReplaceKey, uvar);
                        continue;
                    }
                    for (Node changedKey : changedLoopVarList) {
                        Node toInlineVEMap = LoopIteratorCollectionHandler.changedLoopPrimitiveFieldsMap.get(changedKey);
                        LoopIteratorCollectionHandler.replacePrimitives(toReplaceVeMap, changedKey, toInlineVEMap);
                    }

                    List<Node> changedLoopEntityList = new ArrayList(LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.keySet());
                    for (Node changedKey : changedLoopEntityList) {
                        Node toInlineVEMap = LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.get(changedKey);
                        LoopIteratorCollectionHandler.replaceEntity(toReplaceVeMap, changedKey, toInlineVEMap); // apply the inlining of Geetam
                    }
                    if(toReplaceKey != null && toReplaceVeMap != null)
                        loopDIR.getVeMap().put(toReplaceKey, toReplaceVeMap);
                }
                catch (Exception e){}
            }
            else {
                System.out.println(LoopIteratorCollectionHandler.collectionVariable);
                if(uvar.toString().equals("ret"))
                    System.out.println("Breakpoint");
                Node fn = new FoldNode(bodyVEMap.get(uvar), uvar, loopingVar, new NextNode());
                for(Rule r : userInputRules) {
                    fn = fn.accept(r);
                }
                if(fn instanceof FoldNode) {
                    System.out.println("Body_Expr:");
                    System.out.println(bodyVEMap.get(uvar));
                    loopDIR.insert(uvar, new UnknownNode());
                }
                else {
                    checkSummarizedVEMap(fn, bodyDIR, iterator);
                    LoopIteratorCollectionHandler.loopPatternSummarizedKeys.add(uvar);
                    loopDIR.insert(uvar, fn);
                }
            }
        }
        d.dg("loopDIR: " + loopDIR.getVeMap());

//        Map<VarNode, Set<VarNode>> varRsMap = fetchReadSets(bodyDIR);
//        Set<VarNode> aggVars = findAggregatedVars(varRsMap);
//        for (VarNode aggVar : aggVars) {
//            /* Compute intersection of the var's readSet and the set of aggregated vars */
//            Set<VarNode> intersection = new HashSet<>(varRsMap.get(aggVar));
//            intersection.retainAll(aggVars);
//
//            if(intersection.size() == 1){
//                /* Precondition satisfied (aggVar has cyclic dependency only with itself) */
//                FoldNode foldNode = new FoldNode(bodyDIR.find(aggVar), aggVar, loopingVar);
//                foldNode.addLoopSwallowed((LoopRegion) region);
//                loopDIR.insert(aggVar, foldNode);
//            }
//            else {
//                /* Precondition not satisfied. So add a not algebrizable expression for this var */
//                loopDIR.insert(aggVar, UnAlgNode.v());
//            }
//        }


        //Likely, will never need this block of code
//        for(Unit stmt : loopBody.getUnits()) {
//            System.out.println(stmt);
//            if(stmt instanceof JInvokeStmt) {
//                JInvokeStmt invoke = (JInvokeStmt) stmt;
//                System.out.println("DIRLoopRegionAnalyzer.java: invoke stmt = " + invoke);
//                InvokeExpr invokeExpr = invoke.getInvokeExpr();
//                List<Value> args = invokeExpr.getArgs();
//                System.out.println("DIRLoopRegionAnalyzer.java: invoke args  " + args);
//                for(Value arg : args) {
//                    System.out.println("key: " + arg);
//                    System.out.println("value: " + bodyVEMap.get(NodeFactory.constructFromValue(arg)));
//                }
//                String methodShortName = invokeExpr.getMethodRef().name();
//                if(methodShortName.contains("set")) {
//                    System.out.println("Set methodInvoked: " + methodShortName);
//                    String attName = methodShortName.substring(3);
//                    //newkey is iterator(collection).field
//                    VarNode newkey = new VarNode("iterator(" + loopingVar.toString() + ")." + attName);
//                    assert args.size() == 1 : "Num args in a set method should be 1, instead it is = " + args.size();
//                    Node keysValue = bodyVEMap.get(NodeFactory.constructFromValue(args.get(0)));
//                    loopDIR.insert(newkey, keysValue);
//                }
//            }
//        }

        System.out.println(LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps);
        return loopDIR;
    }

    private void replaceArefItrWithNext(Map<VarNode, Node> bodyVEMap, VarNode javaSrcItr) {
        Node javaSrcItrMapping = bodyVEMap.get(javaSrcItr);

        ExprRepVisitor exprRepVisitor = new ExprRepVisitor(javaSrcItrMapping, new NextNode());
        for (VarNode vn : bodyVEMap.keySet()) {
            Node val = bodyVEMap.get(vn);
            Node replacement = val.accept(exprRepVisitor);
            bodyVEMap.put(vn, replacement);
        }
    }

    private VarNode getArrayIntJavaSrcItr(Map<VarNode, Node> bodyVEMap, VarNode i_itr, VarNode javaSrcItr) {
        for (VarNode vn : bodyVEMap.keySet()) {
            Node mapping = bodyVEMap.get(vn);
            if (mapping instanceof ArrayRefNode) {
                ArrayRefNode arn = (ArrayRefNode) mapping;
                Node coll = arn.getChild(0);
                Node idx = arn.getChild(1);
                Boolean indexMatchesIterator = false;
                if (i_itr != null) {
                    indexMatchesIterator = idx.toString().equals(i_itr.toString());
                } else {
                    // This is a workaround a soot bug where at times the next
                    // array element is obtained as next = arr[next]
                    indexMatchesIterator = (bodyVEMap.containsKey(new VarNode(idx.toString())) &&
                            bodyVEMap.get(new VarNode(idx.toString())) instanceof ArrayRefNode);
                }
                if (indexMatchesIterator) {
                    javaSrcItr = vn;
                    break;
                }
            }
        }
        return javaSrcItr;
    }

    private VarNode getArrayIntItr(Map<VarNode, Node> bodyVEMap, VarNode i_itr) {
        for (VarNode vn : bodyVEMap.keySet()) {
            Node mapping = bodyVEMap.get(vn);
            if (mapping instanceof ArithAddNode && ((ArithAddNode) mapping).isItr == true) {
                i_itr = vn;
                break;
            }
        }
        return i_itr;
    }

    /**
     * Find all the variables that are aggregated in the loop.
     * A variable "var" is determined to be aggregated in the loop if "var" is present in its
     * own read set.
     */
    private Set<VarNode> findAggregatedVars(Map<VarNode, Set<VarNode>> varRsMap) {
        Set<VarNode> aggVars = new HashSet<>();
        for (VarNode var : varRsMap.keySet()) {
            Set<VarNode> varReadset = varRsMap.get(var);
            if(varReadset.contains(var)){
                aggVars.add(var);
            }
        }
        return aggVars;
    }

    /**
     * Return a map containing variable and its read set, for each variable updated inside the loop body.
     * Since readSet() is computed by a traversal of the dag expression, populating readSets and
     * reusing them (instead of calling varNode.readSet() whenever required) does less work.
     */
    private Map<VarNode, Set<VarNode>> fetchReadSets(DIR bodyDIR)  {
        Map<VarNode, Set<VarNode>> varReadsetMap = new HashMap<>();
        for (VarNode var : bodyDIR.getVars()) {
            if(!var.isJimpleVar()){
                continue;
            }
            Set<VarNode> readSet = bodyDIR.find(var).readSet();
            varReadsetMap.put(var, readSet);
        }
        return varReadsetMap;
    }

    /**
     * @return The query or collection variable over which the loop iterates
     */
    private VarNode getLoopingCol(DIR headDIR, DIR bodyDIR)  {
        debug d = new debug("DIRLoopRegionAnalyzer.java", "getLoopingCol()");
        d.dg("headDIR: " + headDIR);
        VarNode condVar = VarNode.getACondVar();
        assert headDIR.contains(condVar);
        Node loopCond = headDIR.find(condVar);
        //Don't handle the case where loopCond is unknown.
        if(loopCond == null) {
            return null;
        }
        /*
            When the iteration is over primitive arrays then, loopcond will be of form a < b
         */
        if(loopCond instanceof LtNode) {
            Node left = loopCond.getChild(0);
            for(VarNode vnb : bodyDIR.getVeMap().keySet()) {
                Node mappingvnb = bodyDIR.getVeMap().get(vnb);
                if(mappingvnb instanceof ArrayRefNode) {
                    ArrayRefNode arn = (ArrayRefNode) mappingvnb;
                    Node collection = arn.getChild(0);
                    Node iterator = arn.getChild(1);
                    if(left.toString().equals(iterator.toString())) {
                        VarNode itvn = (VarNode) iterator;
                        //bodyDIR.insert(itvn, new NextNode());
                        return (VarNode) collection;
                    }
                }
            }
        }
        /* loopCond is expected to be of the form:
            ==
              MethodInv
                l5
                HasNext()
              0
         */
        if(loopCond instanceof EqNode == false && loopCond instanceof NotEqNode == false) {
            return null;
        }
        assert (loopCond instanceof EqNode || loopCond instanceof NotEqNode);
     //   EqNode eqNode = (EqNode)loopCond;
        assert (loopCond.getChild(0) instanceof InvokeMethodNode);
        InvokeMethodNode miNode = (InvokeMethodNode) loopCond.getChild(0);
        assert (miNode.getChild(1) instanceof MethodHasNextNode);

        assert miNode.getChild(0) instanceof VarNode;
        return (VarNode) miNode.getChild(0);
    }

    Node getKeyMappedToNext(Map <VarNode, Node> veMap)
    {
        for(Map.Entry <VarNode, Node> ent : veMap.entrySet())
        {
            if (ent.getValue() instanceof NextNode) {
                return ent.getKey();
            }
        }
        return new NullNode();
    }

    public VarNode getToReplaceKey(String iteratorname){
//        System.out.println(FuncStackAnalyzer.funcDIRMap);
        dir = DAGTillNow.getDag();
        for(VarNode key : dir.keySet()){
            String keyName = key.toString();
            keyName = keyName.substring(keyName.indexOf('.')+1);
            if(keyName.contains(iteratorname) && !keyName.contains("this.")){
                return key;
            }
        }
        return null;
    }

    private void getVEMapOfCollection(DIR loopDIR, VarNode toReplaceKey, VarNode iterator_l4) {
        Node repoVEMap = null;
        Node collectionVEMap = null;
        for (VarNode key : loopDIR.getVeMap().keySet()) {
            if (key.toString().contains("Repository"))
                repoVEMap = loopDIR.getVeMap().get(key);
        }
        System.out.println(repoVEMap);
        if (repoVEMap == null)
            return;
        else {
            if (repoVEMap instanceof UnionNode && repoVEMap.getChild(0) instanceof RelMinusNode && repoVEMap.getChild(1) instanceof UnionNode)
                collectionVEMap = repoVEMap.getChild(1);
            else
                return;

            String collectionName = "";
            if (toReplaceKey.toString().contains(".")) {
                collectionName = toReplaceKey.toString();
                collectionName = collectionName.substring(collectionName.lastIndexOf(".") + 1);
            }
            else
                collectionName = toReplaceKey.toString();
            Node collectionNode = new VarNode(collectionName);
            // replace the iterator_l4 node in collectionVEMap with collectionNode
            replaceNodeWithNode(collectionVEMap, iterator_l4, collectionNode);
            replaceNodeWithNode(repoVEMap, iterator_l4, collectionNode);
        }
            loopDIR.getVeMap().put(toReplaceKey, collectionVEMap);
        }

    private Node replaceNodeWithNode(Node collectionVEMap, VarNode iteratorL4, Node collectionNode) {
        if(collectionVEMap.toString().equals(iteratorL4.toString()))
            return collectionNode;
        for(int i=0; i<collectionVEMap.getNumChildren(); i++){
            collectionVEMap.setChild(i, replaceNodeWithNode(collectionVEMap.getChild(i), iteratorL4, collectionNode));
        }
        return collectionVEMap;
    }

    public void checkSummarizedVEMap(Node loopSummarizedVEMap, DIR bodyDIR, Value iterator){
        if(isIterationOverList == false)
            return;
        Node l4VEMap = bodyDIR.getVeMap().get(new VarNode("l4"));
        System.out.println(l4VEMap.toString());
        if(!l4VEMap.toString().contains("List"))
            return;
        Node listNodeOfl4 = getFirstSubMapContainingString(l4VEMap, "List");
        Node listNodeOfSummarizedVEMap = getFirstSubMapContainingString(loopSummarizedVEMap, "List");

        Collection<VarNode> itrPrimitiveFields = fieldVarNodesOfIterator(iterator);
        String field = findBooleanChangedPrimitiveField(itrPrimitiveFields, listNodeOfSummarizedVEMap);
        Cloner cloner = new Cloner();
        Node oneList = cloner.deepClone(listNodeOfl4);
        Node zeroList = cloner.deepClone(listNodeOfl4);
        LoopIteratorCollectionHandler.replaceNodeWithNode(oneList, new VarNode(field), new OneNode());
        LoopIteratorCollectionHandler.replaceNodeWithNode(zeroList, new VarNode(field), new ZeroNode());
        System.out.println(itrPrimitiveFields +" " + oneList + zeroList);

        Node list0, list1;
        Node unionNode = loopSummarizedVEMap.getChild(1);
        list1 = unionNode.getChild(0);
        list0 = unionNode.getChild(1);
        list0 = getFirstSubMapContainingStringAndReplace(list0, "List", zeroList);
        list1 = getFirstSubMapContainingStringAndReplace(list1, "List", oneList);
        System.out.println("" + list1 + list0);
        return;
    }

    public String findBooleanChangedPrimitiveField(Collection<VarNode> itrPrimitiveFields, Node listNodeOfSummarizedVEMap){
        String changedPrimitiveField = "";
        Set<String> summarizedVEMapPrimitives = new HashSet<>();
        for(Node child : listNodeOfSummarizedVEMap.getChildren()){
            String val = child.toString();
            if(val.contains("."))
                val = val.substring(val.lastIndexOf(".")+1);
            if(val.endsWith(")"))
                val = val.substring(0, val.length()-1);
            summarizedVEMapPrimitives.add(val);
        }
        for(VarNode field : itrPrimitiveFields){
            String val = field.toString();
            if(val.contains("."))
                val = val.substring(val.lastIndexOf(".")+1);
            if(val.endsWith(")"))
                val = val.substring(0, val.length()-1);
            if(!summarizedVEMapPrimitives.contains(val)) {
                changedPrimitiveField = val;
                break;
            }
        }
        return changedPrimitiveField;
    }

    public Node getFirstSubMapContainingString(Node veMap, String str){
        if(veMap.getNumChildren() == 0)
            return null;
        if(veMap.getOperator().toString().equals(str))
            return veMap;
        System.out.println(veMap.getOperator());
        Node res = null;
        for(int i=0; i<veMap.getNumChildren(); i++){
            Node child = veMap.getChild(i);
            res = getFirstSubMapContainingString(child, str);
            if(res != null)
                break;
        }
        return res;
    }

    public Node getFirstSubMapContainingStringAndReplace(Node veMap, String str, Node replaceVal){
        if(veMap.getNumChildren() == 0)
            return null;
        if(veMap.getOperator().toString().equals(str))
            return veMap;
        System.out.println(veMap.getOperator());
        Node res = null;
        for(int i=0; i<veMap.getNumChildren(); i++){
            Node child = veMap.getChild(i);
            res = getFirstSubMapContainingStringAndReplace(child, str, replaceVal);
            if(res != null) {
                veMap.setChild(i, replaceVal);
                break;
            }
        }
        return res;
    }


} // class ends
