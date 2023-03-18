/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql;

import com.iisc.pav.*;
import dbridge.EqSQLDriverTestStoreResult;
import dbridge.analysis.eqsql.expr.node.*;
import io.geetam.github.OptionalTypeInfo;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.hibernate.construct.StmtInfo;
import dbridge.analysis.eqsql.util.FuncResolver;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.LoopRegion;
import io.geetam.github.SavePostProcess.SavePostProcess;
import io.geetam.github.loopHandler.DAGTillNow;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import mytest.debug;
import soot.*;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.Block;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Information about the current top level function that needs to be transformed,
 * and other functions in its call stack.
 * This may need some change when we consider rewriting only a region
 * instead of an entire function.
 */
public class FuncStackAnalyzer {
    /**
     * Subsignature of the top level func body
     */
    public static String topLevelFunc;
    /**
     * Stack representing the functions called from top level function.
     * Topmost element on the stack will be the leaf function in the corresponding
     * call graph
     */
    public static Stack funcCallStack;
    /**
     * Mapping between function signature and its topmost region.
     */
    public static HashMap<String, ARegion> funcRegionMap;
    /**
     * Mapping between function signature and its body.
     */
    public static HashMap<String, Body> funcBodyMap;
    /**
     * Mapping between function signature and its DIR.
     */
    public static HashMap<String, DIR> funcDIRMap;
    /**
     * True if query extraction is successful. False if query extraction fails.
     * Reasons for failure include loop preconditions not met, etc.
     */
    public static boolean success;
    /**
     * The query string (if query extraction is successful). Null otherwise.
     */
    public static String query;

    public FuncStackAnalyzer(String topLevelFunc) {
        this.topLevelFunc = topLevelFunc;
        this.funcCallStack = new Stack();
        this.funcRegionMap = new HashMap<>();
        this.funcBodyMap = new HashMap<>();
        this.funcDIRMap = new HashMap<>();
        this.success = false;
        this.query = null;
    }

    /**
     * Information about the variable to be rewritten.
     */
    private class RetNodeInfo {
        /** Transformed DIR for variable being rewritten */
        Node node;
        /** Region over which the variable is rewritten */
        ARegion region;
        /** Type of the variable being rewritten */
        Type varType;
        /** Loops swallowed by this expression */
        List<LoopRegion> loopsSwallowed;

        RetNodeInfo(Node node, ARegion region, Type varType, List<LoopRegion> loopsSwallowed) {
            this.node = node;
            this.region = region;
            this.varType = varType;
            this.loopsSwallowed = loopsSwallowed;
        }
    }

    RetNodeInfo retNodeInfo = null;

    /**
     * Construct the DIR for the function, and update retNodeInfo with the DIR for the return value,
     * along with the region corresponding to the return value.
     */
    public void run() {
        try {
            retNodeInfo = findMainFuncRetNode();
        } catch (RegionAnalysisException e) {
            e.printStackTrace();
            success = false;
            return;
        }
        Node retNode = retNodeInfo.node;

        if(UnAlgNode.isUnAlgNode(retNode)){
            /* Mark as failed if the return node is not algebrizable */
            success = false;
            return;
        }
        else{
            success = true;
        }

        /* resolve function references */
        retNode = retNode.accept(new FuncResolver(funcDIRMap));

        retNodeInfo.node = retNode;
    }

    /** Find the dag corresponding to return in the DIR for the top level function in the stack.
     * Updates funcDIRMap. */
    private RetNodeInfo findMainFuncRetNode() throws RegionAnalysisException {
        constructDIRsForStack(); //funcDIRMap updated in this
        /* find expression for return variable from main function */
        assert funcDIRMap.containsKey(topLevelFunc);
        DIR mainDir = funcDIRMap.get(topLevelFunc);
        EqSQLDriverTestStoreResult.result = mainDir;
        VarNode retVar = RetVarNode.getARetVar();
        assert mainDir.contains(retVar) : "mainDIR does not contain retVar";

        Node retNode = mainDir.find(retVar);
        return new RetNodeInfo(retNode, retNode.getRegion(), mainDir.findRetVarType(), retNode.getLoopsSwallowed());
    }

//    public void processSaveCalls(ARegion region, Map <VarNode, Node> veMap) {
//        Block basicBlock = region.getHead();
//        Iterator<Unit> iterator = basicBlock.iterator();
//
//        DIR dir = new DIR(); //dir for this region
//
//        StmtInfo stmtInfo = StmtInfo.nullInfo;
//        debug.dbg("Printing all units in the basic block: ");
//        while (iterator.hasNext()) {
//            Unit curUnit = iterator.next();
//            //  System.out.println(curUnit);
//            if(curUnit instanceof JInvokeStmt) {
//                System.out.println("above instance of JInvokeStmt");
//                JInvokeStmt saveStmt = (JInvokeStmt) curUnit;
//                System.out.println("args list: ");
//                List <Value> argsList = saveStmt.getInvokeExpr().getArgs();
//                System.out.println(argsList);
//
//                for(Value arg : argsList) {
//                    System.out.println("arg: " + arg.toString());
//                    System.out.println(arg.getType());
//                    SootClass classofArg = Scene.v().loadClass(arg.getType().toString(), 1);
//                    System.out.println("classofArg: " + classofArg.getName());
//                    List <VarNode> fieldAccessList = utils.getVarNodeFieldAccessListOfBaseVar(arg);
//                    System.out.println("Field Access List: " + fieldAccessList.toString());
//                    for(VarNode fieldAccess : fieldAccessList) {
//                        System.out.println("field access = " + fieldAccess);
//                        System.out.println("ve-Map Entry: " + veMap.get(fieldAccess));
//                    }
//                }
//            }
//        }
//    }

    /** Construct DIRs for each function in the stack and store them in funcDIRMap */
    private void constructDIRsForStack() throws RegionAnalysisException {
        long startTime = System.currentTimeMillis();
        debug d = new debug("FuncStackAnalyzer.java", "constructDIRForStack()");
        d.dg("FSA: constructDIRsForStack: Stack = " + funcCallStack);

        d.dg("top function sig: " + topLevelFunc);


        SootMethod rootMethod = Scene.v().getMethod("<" + topLevelFunc + ">");
        Body b = rootMethod.retrieveActiveBody();
        d.dg("body rootmethod before analysis: " + b.getUnits());
        ARegion topRegion = funcRegionMap.get(topLevelFunc);
        OptionalTypeInfo.typeMap = OptionalTypeInfo.analyzeBCEL(topLevelFunc);
        d.dg("Top level func i.e. " + topLevelFunc + "has typemap: " + OptionalTypeInfo.typeMap);
        DIR dag = (DIR) topRegion.analyze();
        System.out.println("Resultant dag : " + dag);
        System.out.println("final FuncStackAnalyzer3 = \n" + FuncStackAnalyzer.funcDIRMap);
        d.dg( "Printing veMap for method: " + topLevelFunc);
        d.dg("VEMap Num Entries: " + dag.getVeMap().keySet().size());
//        for(VarNode node : dag.getVeMap().keySet()) {
//            node = (VarNode) node.accept(new FuncResolver(funcDIRMap));
//            System.out.println("key: " + node);
//            System.out.println("value: " + dag.getVeMap().get(node));
//        }
        d.dg("key set of ve map of root function: " + dag.getVeMap().keySet());
        Map <VarNode, Node> cascadedEntries = new HashMap<>();
        for(VarNode vn : dag.getVeMap().keySet()) {
            System.out.println("Final veMap of " + vn + " = \n" + dag.getVeMap().get(vn));
            if(vn.toString().equals("this.voteServiceImpl.postVoteRepository")) {
                d.dg("break");
            }
            //   if(vn.repoType != null) {
            Node mapping = dag.getVeMap().get(vn);
            SavePostProcess savePostProcess = new SavePostProcess(vn, new ArrayList<>(), dag);
            Node newMapping = mapping.accept(savePostProcess);
            dag.getVeMap().put(vn, newMapping);
            cascadedEntries.putAll(savePostProcess.cascadedEntries);
            //  }

        }
        dag.getVeMap().putAll(cascadedEntries);
//        DAGTillNow.updateDag(dag);
        funcDIRMap.put(topLevelFunc, dag);

        DIR dagc = funcDIRMap.get(topLevelFunc);
        Map <VarNode, Node> veMap = dagc.getVeMap();
//        applyLoop1changesToResult(veMap);
        for(VarNode node : veMap.keySet()) {
            node = (VarNode) node.accept(new FuncResolver(funcDIRMap));
            d.clndg("key: " + node);
            d.clndg("value: " + veMap.get(node)+"\n");
        }
        debug.dbg("FuncStackAnalyzer.java", "constructDIRsForStack()", "Printing veMap for method: " + topLevelFunc + " END");

        try {
//            AlloyGenerator alloygen = new AlloyGenerator(dag.getVeMap());

//            AlloyGen alloygen;
//            if(LoopIteratorCollectionHandler.isNRAProperty)
//                alloygen = new AlloyGeneratorNRA(dag.getVeMap());
//            else
//                alloygen = new AlloyGeneratorNonNRA(dag.getVeMap());

            GenerateAlloySummary alloygen = new GenerateAlloySummary(dag.getVeMap());
            alloygen.printWriter.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        double timesec = ((double)(endTime - startTime)) / 1000.0;
        System.out.println("Time elapsed: " + timesec + " seconds");
        // try {
        //FileWriter fileWriter = new FileWriter("timings.txt");
        //PrintWriter printWriter = new PrintWriter(fileWriter);
        //printWriter.println(topLevelFunc + " :::::: " + timesec + " seconds");
        //printWriter.close();
        // } catch (IOException ioException) {
        //     ioException.printStackTrace();
        // }
        System.exit(0);

    }

    private void applyLoop1changesToResult(Map<VarNode, Node> veMap) {
        if(LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps.keySet().size() == 0)
            return;
//        for(VarNode key : LoopIteratorCollectionHandler.summarizedLoopVEMap.keySet()){
//            String newKey = key.toString();
//            if(newKey.contains("."))
//                newKey = newKey.substring(newKey.lastIndexOf('.')+1);
//            for(VarNode finalVEMapKey : veMap.keySet()){
//                if(finalVEMapKey.toString().endsWith(newKey))
//                    veMap.put(finalVEMapKey, LoopIteratorCollectionHandler.summarizedLoopVEMap.get(key));
//            }
//        }
        for(VarNode key : LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps.keySet()){
            veMap.put(key, LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps.get(key));
        }
    }

    public Node getExpr(){
        assert retNodeInfo != null;
        return retNodeInfo.node;
    }

    public ARegion getRetRegion(){
        assert retNodeInfo != null;
        return retNodeInfo.region;
    }

    public Type getVarType(){
        assert retNodeInfo != null;
        return retNodeInfo.varType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Body getTopLevelFuncBody(){
        return funcBodyMap.get(topLevelFunc);
    }

    public List<LoopRegion> getLoopsSwallowed(){
        return retNodeInfo.loopsSwallowed;
    }
}