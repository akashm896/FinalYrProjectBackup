package dbridge.analysis.eqsql;

import dbridge.analysis.eqsql.util.SootClassHelper;
import dbridge.analysis.region.regions.*;
import io.geetam.github.StructuralAnalysis.Graph;
import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
import io.geetam.github.StructuralAnalysis.Vertex;
import mytest.debug;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ContextSensitiveCallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BriefBlockGraph;

import java.util.*;

/**
 * Created by ek on 26/4/16.
 */
public class FuncStackInfoBuilder extends SceneTransformer {
    private FuncStackAnalyzer funcStackAnalyzer;

    public ARegion createARegionTree(Vertex ctRoot, Map <Vertex, Set <Vertex>> ctChildren, BriefBlockGraph bbg,
                                     Map <Vertex, StructuralAnalysis.RegionType> structType) {
        debug d = new debug("FuncStackInfoBuilder", "createARegionTree()");
        ARegion ret;
        Set <Vertex> empt = new HashSet<>();
        List <Vertex> rootChildren = new ArrayList<>(ctChildren.getOrDefault(ctRoot, empt));
        List <ARegion> childRegions = new ArrayList<>();
        for(Vertex childV : rootChildren) {
            ARegion childARegion = createARegionTree(childV, ctChildren, bbg, structType);
            childRegions.add(childARegion);
        }

        d.dg("structType = " + structType);
        d.dg("ctRoot = " + ctRoot);
        d.dg("childRegions.length: " + childRegions.size());
        if(structType.containsKey(ctRoot) == false) {
            structType.put(ctRoot, StructuralAnalysis.RegionType.BasicBlock);
        }
        switch (structType.get(ctRoot)) {
            case IfThen:
                ret = new IfThenRegion();
                IfThenRegion itr = (IfThenRegion) ret;
                if(structType.get(rootChildren.get(0)).equals(StructuralAnalysis.RegionType.IfHead)) {
                    itr.headRegion = childRegions.get(0);
                    itr.thenRegion = childRegions.get(1);
                }
                else {
                    itr.headRegion = childRegions.get(1);
                    itr.thenRegion = childRegions.get(0);
                }
                break;
            case IfThenElse:
                ret = new IfThenElseRegion();
                IfThenElseRegion ifteReg = (IfThenElseRegion) ret;
                d.dg("ifte childregions =" + childRegions);
                ifteReg.headRegion = childRegions.get(0);
                ifteReg.thenRegion = childRegions.get(1);
                ifteReg.elseRegion = childRegions.get(2);
                break;
            case Sequential:
                ret = new SequentialRegionN();

                break;
            case NaturalLoop:
            case SelfLoop:
            case WhileLoop:
                ret = new LoopRegion(childRegions.get(0), childRegions.get(1));
                break;
            default:
                ret = new Region(bbg.getBlocks().get(Integer.parseInt(ctRoot.dat)));
                Region bbRegion = (Region) ret;
                d.dg("Created bb region, num: " + bbRegion.getHead().getIndexInMethod());
                d.dg("bb units: " + bbRegion.getHead().getBody().getUnits());
                ret.CTRegionType = StructuralAnalysis.RegionType.BasicBlock;
        }
        ret.addChildren(childRegions);
        return ret;
    }

    public ARegion regionTreeForBody(Body body) {
        BriefBlockGraph bbg = new BriefBlockGraph(body);
        debug d = new debug("FuncStackInfoBuilder.java", "regionTreeForBody()");
        d.dg("bbg = " + bbg);
        Graph sagraph = new Graph();
        List <Block> blocks = bbg.getBlocks();

        for(Block b : blocks) {
            int bbnum = b.getIndexInMethod();
            Vertex bv = new Vertex(bbnum);
            sagraph.addVertex(bv);
            List <Block> bsuccs = bbg.getSuccsOf(b);
            for(Block succ : bsuccs) {
                Vertex succv = new Vertex(succ.getIndexInMethod());
                sagraph.addEdge(bv, succv);
            }
        }
        d.dg("cfgdot: " + sagraph.toDOT());

        StructuralAnalysis sa = new StructuralAnalysis();
        sa.structuralAnalysis(sagraph, new Vertex(bbg.getHeads().get(0).getIndexInMethod()));
        d.dg("PRINTING Control Tree");
        d.dg(sa.controlTreeString());
        d.dg("bbg = " + bbg);

        Vertex ctRoot = sa.controlTreeRoot();
        Map <Vertex, Set <Vertex> > ctChildren = sa.ctChildren;

        ARegion topr = createARegionTree(ctRoot, ctChildren, bbg, sa.structType);
        d.dg("created region tree for root = " + ctRoot);
        d.dg(topr.toString());
        return topr;
    }

    public FuncStackInfoBuilder(FuncStackAnalyzer funcStackAnalyzer) {
        super();
        this.funcStackAnalyzer = funcStackAnalyzer;
    }

    protected void internalTransform(String phaseName, Map options){
        System.out.println("FuncStackInfoBuilder internalTransform called!");
        try{
            internalTransformHelper(funcStackAnalyzer);
            funcStackAnalyzer.setSuccess(true);
        }
        catch (Exception | AssertionError e){
            e.printStackTrace();
            funcStackAnalyzer.setSuccess(false);
        }
    }



    private void internalTransformHelper(FuncStackAnalyzer fsa) {
        debug d = new debug("FuncStackInfoBuilder.java", "internalTransformHelper()");
        Queue funcCall = new LinkedList<>();
        Body body;
        RegionGraph regionGraph;
        ARegion topRegion;

        /* Get info about the top level function */
        String methodFullSign = fsa.topLevelFunc;
        String className = SootClassHelper.getClassName(fsa.topLevelFunc);
        String methodSubsign = SootClassHelper.getMethodSubsignature(fsa.topLevelFunc);

        fsa.funcCallStack.add(methodFullSign);
        SootClass c = Scene.v().forceResolve(className, SootClass.BODIES);
        SootMethod method = c.getMethod(methodSubsign);
        funcCall.add(method);

        body = method.retrieveActiveBody();
        System.out.println("FuncStackInfoBuilder.java: body: \n" + body);
        fsa.funcBodyMap.put(trim(method.toString()), body);

        
        ARegion topr = regionTreeForBody(body);
        if(topr == null)
            d.dg("topr is null");
        else
            d.dg("topr is not null");
        d.dg("topr = \n" + topr);


      //  regionGraph = new RegionGraph(body);
      //  topRegion = regionGraph.getHeads().get(0);
        fsa.funcRegionMap.put(trim(method.toString()), topr);

        /* Get info about other callee functions */
        CallGraph cg = Scene.v().getCallGraph();
        System.out.println("CGSTART: \n" + cg + "CGEND\n");


        while (!funcCall.isEmpty()){
            MethodOrMethodContext caller = (MethodOrMethodContext) funcCall.poll();
            Iterator callees = cg.edgesOutOf(caller);
         //   System.out.println("FSIB: InternalTransformHelper: callees = " + callees);

            while (callees.hasNext()){
                MethodOrMethodContext callee = ((Edge) callees.next()).getTgt();
                String calleeStrNotrim = callee.toString();
                String calleeStr = trim(calleeStrNotrim);
                System.out.println("FSIB: InternalTransformHelper: calleeStr = " + calleeStr);
                if(fsa.funcCallStack.search(calleeStr) == -1
                        && isInteresting(calleeStrNotrim)){
                    funcCall.add(callee);
                    fsa.funcCallStack.add(calleeStr);

                    body = ((SootMethod)callee).retrieveActiveBody();
                    fsa.funcBodyMap.put(calleeStr, body);

                //    regionGraph = new RegionGraph(body);
                  //  topRegion = regionGraph.getHeads().get(0);
                    topRegion = regionTreeForBody(body);
                    fsa.funcRegionMap.put(calleeStr, topRegion);
                }
            }
        }

        System.out.println("FuncStackInfoBuilder.java: functionstack: ");
        for(String funcName : fsa.funcRegionMap.keySet()) {
            System.out.println("    " + funcName);
        }
    }

    private boolean isInteresting(String methodSign) {
        //  System.out.println("FSIB: isInteresting: methodSign = " + methodSign);
        boolean ignore = false;
        ignore = methodSign.startsWith("<javax.")
                || methodSign.startsWith("<soot.")
                || methodSign.startsWith("<sun.")
                || methodSign.startsWith("<java.")
           //     || methodSign.startsWith("<org.")
                || methodSign.endsWith("<init>()>")
        ;
        return !ignore;
    }

    /** Remove the angular brackets appended by m.toString() to the method signature at beginning and end
     */
    private String trim(String methodSign){
        return methodSign.substring(1, methodSign.length() - 1);
    }
}
