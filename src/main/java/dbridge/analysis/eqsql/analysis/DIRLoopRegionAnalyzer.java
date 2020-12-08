package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.trans.Rule;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.LoopRegion;
import io.geetam.github.accesspath.AccessPath;
import io.geetam.github.accesspath.Flatten;
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
    /* Singleton */
    private DIRLoopRegionAnalyzer(){
        try {
            userInputRules = getUserInputRules();
        }
        catch (Exception e) {
            userInputRules = new ArrayList<>();
            System.out.println(e);
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


        VarNode loopingVar = getLoopingCol(headDIR);
        d.dg("loopingVar: " + loopingVar);

        Collection <Unit> units = ((LoopRegion) region).body.getUnits();
        Value iterator = getIterator(units, bodyVEMap);
        Collection <VarNode> foldVars = getFoldVars(bodyVEMap, iterator, loopingVar);
        d.dg("foldVars: " + foldVars);
        DIR loopDIR = new DIR();
        for(VarNode uvar : foldVars) {
            d.dg("uvar: " + uvar);
            if (uvar.equals(loopingVar)) {
                List<Node> fieldExprs = new ArrayList<>();
                Collection<VarNode> fieldVarNodes = fieldVarNodesOfIterator(iterator);
                for (VarNode vn : fieldVarNodes) {
                    fieldExprs.add(bodyVEMap.get(vn));
                }
                ListNode fieldExprListNode = new ListNode(fieldExprs.toArray(new Node[fieldExprs.size()]));
                AddWithFieldExprsNode addWithFieldExprsNode = new AddWithFieldExprsNode(fieldExprListNode);
                VarNode newColl = new VarNode(loopingVar.toString() + "_new");
                FoldNode fn = new FoldNode(addWithFieldExprsNode, newColl, loopingVar);
                d.dg("mapping new coll " + newColl + " -> " + fn);
                loopDIR.insert(newColl, fn);
            }
            else {
                Node fn = new FoldNode(bodyVEMap.get(uvar), uvar, loopingVar);
                for(Rule r : userInputRules) {
                    fn = fn.accept(r);
                }
                loopDIR.insert(uvar, fn);
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


        return loopDIR;
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
    private VarNode getLoopingCol(DIR headDIR)  {
        VarNode condVar = VarNode.getACondVar();
        assert headDIR.contains(condVar);
        Node loopCond = headDIR.find(condVar);

        /* loopCond is expected to be of the form:
            ==
              MethodInv
                l5
                HasNext()
              0
         */
        assert (loopCond instanceof EqNode || loopCond instanceof NotEqNode);
     //   EqNode eqNode = (EqNode)loopCond;
        assert (loopCond.getChild(0) instanceof InvokeMethodNode);
        InvokeMethodNode miNode = (InvokeMethodNode) loopCond.getChild(0);
        assert (miNode.getChild(1) instanceof MethodHasNextNode);

        assert miNode.getChild(0) instanceof VarNode;
        return (VarNode) miNode.getChild(0);
    }
}
