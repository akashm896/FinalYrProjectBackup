/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.region.regions;

import io.geetam.github.SimpleDFS;
import mytest.debug;
import org.jbpt.algo.tree.rpst.RPST;
import org.jbpt.graph.DirectedEdge;
import org.jbpt.hypergraph.abs.Vertex;
import soot.Body;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.*;
import soot.jimple.internal.JIfStmt;
import soot.toolkits.graph.*;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.graph.pdg.RegionAnalysis;

import java.util.*;

public class RegionGraph implements DirectedGraph<ARegion> {

    private BlockGraph blockGraph;
    public UnitGraph ug;
    private List<ARegion> regions = new ArrayList<ARegion>();

    Map<Integer, Block> indexToBlock = new HashMap<Integer, Block>();
    Map<Integer, Block> indexToBlockOriginal = new HashMap<Integer, Block>();
    private UnitGraph ugClone;
    private BlockGraph bgClone;
    private Body bodyClone;

    Map<Block, Block> loopExitToHead = new HashMap<Block, Block>();

    public RegionGraph(Body b) {
        debug d = new debug("RegionGraph.java", "RegionGraph()");
        d.dg("RegionGraph.java: body = " + b);
        this.blockGraph = new BriefBlockGraph(b);
        UnitGraph unitGraph = new BriefUnitGraph(b);
        this.ug = unitGraph;

        System.out.println("unitGraph = " + unitGraph);
        ExceptionalBlockGraph cfg = new ExceptionalBlockGraph(b);
        ExceptionalUnitGraph excfg = new ExceptionalUnitGraph(b);
        EnhancedUnitGraph encfg = new EnhancedUnitGraph(b);
        RegionAnalysis ra = new RegionAnalysis(encfg, b.getMethod(), b.getMethod().getDeclaringClass());
        d.dg("regions = " + ra.getRegions());
        for(soot.toolkits.graph.pdg.Region r : ra.getRegions()) {
            d.dg("Region: " + r);
            d.dg("subregions:" + r.getChildRegions());
        }

        org.jbpt.graph.DirectedGraph dg = new org.jbpt.graph.DirectedGraph();
        int numBlocks = cfg.getBlocks().size();
        List <Vertex> vertices = new LinkedList<>();
        for(int i = 0; i < numBlocks; i++) {
            org.jbpt.hypergraph.abs.Vertex v = new org.jbpt.hypergraph.abs.Vertex(Integer.toString(i));
            vertices.add(v);
        }
        for(int i = 0; i < numBlocks; i++) {
            Block blk = cfg.getBlocks().get(i);
            List <Block> succs = blk.getSuccs();
            for(Block suc : succs) {
                dg.addEdge(vertices.get(i), vertices.get(suc.getIndexInMethod()));
            }
        }
        d.dg("dgdot=" + dg.toDOT());
        RPST pst = new RPST(dg);
        System.out.println("pst = " + pst.toDOT());

        SimpleDFS simpleDFS = new SimpleDFS(dg);
        simpleDFS.dfs();

        for(Vertex v : dg.getVertices()) {
            Collection <DirectedEdge> incoming = dg.getIncomingEdges(v);
            //isLoop criterian at least one back incoming edge, and one non-back edge.
            boolean atLeastOneBack = false;
            boolean atLeastOneNonBack = false;
            for(DirectedEdge e : incoming) {
                if(simpleDFS.edgeTypeMap.get(e).equals(SimpleDFS.edgeType.BACK)) {
                    atLeastOneBack = true;
                }
                else {
                    atLeastOneNonBack = true;
                }
            }
            if(atLeastOneBack && atLeastOneNonBack) {
                System.out.println("Loop Cond = " + v.toString());
            } else if(dg.getOutgoingEdges(v).size() > 1) {
                System.out.println("Conditional Node = " + v.toString());
            }
        }





        List <Block> cfgBlocks = cfg.getBlocks();
        for(int i = 0; i < cfgBlocks.size(); i++) {
            d.dg("Basic Block #" + i);
            d.dg(cfgBlocks.get(i));
        }

        bodyClone = (Body) b.clone();
        ugClone = new BriefUnitGraph(bodyClone);
        bgClone = new BriefBlockGraph(bodyClone);

        for (int i = 0; i < blockGraph.getBlocks().size(); i++) {
            indexToBlockOriginal.put(i, blockGraph.getBlocks().get(i));
        }

        for (int i = 0; i < blockGraph.getBlocks().size(); i++) {
            Block curBlock = blockGraph.getBlocks().get(i);

            if (curBlock.getTail() instanceof GotoStmt) {
                System.out.println("RegionGraph.java: RegionGraph(): curBlock.getSuccs().size()" + curBlock.getSuccs().size());
                if (curBlock.getSuccs().size() == 1) { //Geetam: They are assuming that only one successor => Loop
                    Block onlySucc = curBlock.getSuccs().get(0);
                    System.out.println("RegionGraph.java: RegionGraph(): onlySucc" + onlySucc);
                    if (loopExitToHead.containsKey(onlySucc)) {
                        Block loopHead = loopExitToHead.get(onlySucc);
                        List<Block> blks = new ArrayList<Block>();
                        blks.add(loopHead);
                        curBlock.setSuccs(blks);

                        blks = new ArrayList<Block>();
                        List<Block> preds = loopHead.getPreds();
                        blks.addAll(preds);
                        blks.add(curBlock);
                        loopHead.setPreds(blks);

                        blks = new ArrayList<Block>();
                        preds = onlySucc.getPreds();
                        blks.addAll(preds);
                        blks.remove(curBlock);
                        onlySucc.setPreds(blks);

                    }
                }
            }
        }

        for (int i = 0; i < blockGraph.getBlocks().size(); i++) {
            indexToBlock.put(i, bgClone.getBlocks().get(i));
        }

        List<Block> blocks = blockGraph.getBlocks();

        for (int i = 0; i < blocks.size(); ) {
            Block blk = blocks.get(i);
            Block succBlock = getSuccBlockToMerge(blk);
            if (succBlock == null) {
                i++;
                continue;
            }
            /****/
            //succ block should be loop head
            if (succBlock.getSuccs().size() == 2) {
                if (succBlock.getSuccs().get(0).getSuccs().size() == 1) {
                    if (succBlock.getSuccs().get(0).getSuccs().get(0).equals(succBlock)) {
                        i++;
                        continue;
                    }
                }
                if (succBlock.getSuccs().get(0).getSuccs().size() > 1) {
                    if (succBlock.getSuccs().get(0).getSuccs().contains(succBlock)) {
                        i++;
                        continue;
                    }
                }
                if (succBlock.getSuccs().get(1).getSuccs().size() == 1) {
                    if (succBlock.getSuccs().get(1).getSuccs().get(0).equals(succBlock)) {
                        i++;
                        continue;
                    }
                }
                if (succBlock.getSuccs().get(1).getSuccs().size() > 1) {
                    if (succBlock.getSuccs().get(1).getSuccs().contains(succBlock)) {
                        i++;
                        continue;
                    }
                }
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            Block blk = blocks.get(i);
            Unit u = blk.getTail();
            if (u instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt) u;
            }
        }

        Body body = blockGraph.getBody();
        unitGraph = new BriefUnitGraph(body);
        ug = unitGraph;
        wrapBlocks(unitGraph);

        if (regions.size() > 1) {
            constructRegions();
        }

    }


    private Block getSuccBlockToMerge(Block b) {
        Unit curBlockTail = b.getTail();
        if (!(curBlockTail instanceof IfStmt))
            return null;
        List<Block> succBlocks = b.getSuccs();

        // If last if-else block, then nothing to merge - Tejas
        if (succBlocks.size() != 2)
            return null;
        // Multiple if-else can be considered as multiple nested single if-else. So, each conditional region
        // preconditions satisfied - Tejas
        Block blk1 = succBlocks.get(0);
        Block blk2 = succBlocks.get(1);

        //These two conditions would never be true according to me, and the return value will always be null - Tejas
        if (blk1.getSuccs().contains(blk2)) {
            // if (blk1.getHead().equals(blk1.getTail())
            // &&
            if (blk1.getTail() instanceof IfStmt && isCandidate(blk1))
                return blk1;
        }
        if (blk2.getSuccs().contains(blk1)) {
            // if (blk2.getHead().equals(blk2.getTail())
            // &&
            if (blk2.getTail() instanceof IfStmt && isCandidate(blk2))
                return blk2;
        }
        return null;
    }

    private boolean isCandidate(Block b) {
        Iterator<Unit> units = b.iterator();
        while (units.hasNext()) {
            Stmt stmt = (Stmt) units.next();
            if (stmt instanceof IfStmt)
                continue;
            if (stmt instanceof AssignStmt) {
                AssignStmt as = (AssignStmt) stmt;


                //Tejas. Condition that Block b should not contain definition of any local on which its condition
				// depends
                boolean proceed = true;
                if (b.getTail() instanceof JIfStmt) {
                    Unit uTail = b.getTail();
                    List<ValueBox> lTail = uTail.getUseBoxes();
                    Iterator iter = b.iterator();
                    while (iter.hasNext()) {
                        Unit u = (Unit) iter.next();
                        for (ValueBox vbt : lTail) {
                            for (ValueBox vbi : u.getDefBoxes()) {
                                if (vbi.getValue().toString().equals(vbt.getValue().toString())) {
                                    proceed = false;
                                    break;
                                }
                            }
                            if (proceed == false)
                                break;
                        }
                        if (proceed == false)
                            break;
                    }
                }


                if (!as.getLeftOp().toString().startsWith("$") || !proceed)
                    return false;
            } else
                return false;

        }
        return true;
    }

    private void constructRegions() {

        for(ARegion r : regions) {
            System.out.println("RegionGraph.java: aregion: ");
            for (Unit unit : r.getUnits()) {
                System.out.println("  " + unit);
            }
        }
        System.out.println("END PRINTING OF REGIONS");
        boolean moreIterations = true;
        while (moreIterations) {
            moreIterations = false;
            ARegion merged = null;

            for (ARegion r : regions) {
                System.out.println("RegionGraph.java: aregion: ");
                for(Unit unit : r.getUnits()) {
                    System.out.println("    " + unit);
                }
                System.out.println("parent: " + r.getParent());
                System.out.println("Can Merge: " + r.canMerge());
                if (!r.canMerge()) {
                    continue;
                }

                merged = r.merge();

                System.out.println("After Merge: ");
                for(Unit unit : merged.getUnits()) {
                    System.out.println("    " + unit);
                }
                moreIterations = true;
                break;
            }
            if (moreIterations) {
                regions.add(merged);
                regions.removeAll(merged.getSubRegions());
            }
        }
        if(moreIterations == false) {
            System.out.println("Final Region Graph construction: " + regions);
        }

    }

    //adds all blocks except exception catch blocks to 'regions', and 'basicRegions' - Tejas
    private void wrapBlocks(UnitGraph unitGraph) {
        Map<Block, ARegion> basicRegions = new HashMap<Block, ARegion>();
        for (Block b : blockGraph) {
            if (!isCatchBlock(b)) {
                List<Block> preds = b.getPreds();
                List<Block> nonPreds = new ArrayList<Block>();
                boolean addBlock = true;
                int count = 0;
                for (Block blk : preds) {
                    if (!basicRegions.containsKey(blk)) {
                        //loop body - Tejas
                        if (!(b.getSuccs().size() == 1 && b.getPreds().size() == 1 && b.getSuccs().get(0).equals(b
								.getPreds().get(0)))) {
                            //when will this condition satisfy in addition to the above one? - Tejas
                            if (blk.getIndexInMethod() < b.getIndexInMethod()) {
                                count++;
                                nonPreds.add(blk);
                            }
                        }
                    }
                }
                // blocks other than loop body and first block- Tejas
                if (count == preds.size() && b.getIndexInMethod() != 0)
                    addBlock = false;
                // loop body. anything else? - Tejas
                if (count < preds.size()) {
                    List<Block> newPreds = new ArrayList<Block>();
                    for (Block blk : preds) {
                        if (!(nonPreds.contains(blk)))
                            newPreds.add(blk);

                    }
                    b.setPreds(newPreds);
                }
                // handling a block which has only one stmt - goto and 1 pred
                // and 1 succ : ignore such a block

                // Loop in catch block
                if (b.getSuccs().size() == 1 && b.getPreds().size() == 1 && b.getPreds().get(0).equals(b.getSuccs().get(0))) {
                    count = 0;
                    Block blkLoopHead = b.getPreds().get(0);
                    List<Block> preds2 = blkLoopHead.getPreds();
                    List<Block> nonPreds2 = new ArrayList<Block>();
                    for (Block blk : preds2) {
                        if (!basicRegions.containsKey(blk)) {
                            if (blk.getIndexInMethod() < blkLoopHead.getIndexInMethod()) {
                                count++;
                            }
                        }
                    }
                    if (count == preds2.size() && blkLoopHead.getIndexInMethod() != 0)
                        addBlock = false;
                }

                if (addBlock) {
                    if (b.getHead() instanceof IdentityStmt) {
                        IdentityStmt i = (IdentityStmt) b.getHead();

                        if (i.getRightOp().toString().equals("@caughtexception"))
                            continue;
                    }
                    Region r = new Region(b);
                    regions.add(r);
                    basicRegions.put(b, r);
                }
            }
        }
        for (ARegion r : regions) {
            r.init(basicRegions);
        }
    }

    private boolean isCatchBlock(Block blk) {
        if (blk.getHead() instanceof IdentityStmt) {
            IdentityStmt i = (IdentityStmt) blk.getHead();
            if (i.getRightOp().toString().equals("@caughtexception"))
                return true;
        }
        return false;
    }

    @Override
    public List<ARegion> getHeads() {
        return regions;
    }

    @Override
    public List<ARegion> getTails() {
        return null;
    }

    @Override
    public List<ARegion> getPredsOf(ARegion s) {
        return Arrays.asList(new ARegion[]{s.getParent()});
    }

    @Override
    public List<ARegion> getSuccsOf(ARegion s) {
        return s.getSubRegions();
    }

    @Override
    public int size() {
        return regions.size();
    }

    @Override
    public Iterator<ARegion> iterator() {
        return regions.iterator();
    }

    public String print() {
        return regions.get(0).print();
    }

}
