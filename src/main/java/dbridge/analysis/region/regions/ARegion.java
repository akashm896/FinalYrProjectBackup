/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.region.regions;

import dbridge.analysis.eqsql.analysis.DIRRegionAnalyzer;
import dbridge.analysis.region.api.RegionAnalysis;
import dbridge.analysis.region.api.RegionAnalyzer;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
import mytest.debug;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.toolkits.graph.Block;

import java.util.*;

/**
 * Abstract class for region - first cut.
 * //TODO Need to revisit this and make it more compact
 */
public abstract class ARegion {

    /**
     * This is the basic block which has access to individual statements. Right now, this basic block is in terms
     * of soot's Block with access to individual soot Units. If we want to make it generic, we have to define a MyBlock
     * with appropriate interface to iterate over each individual MyStmt.
     */
    public Block head;
    private ARegion parent; // the enclosing region.
    protected List<ARegion> subRegions = new ArrayList<ARegion>();

    private List<ARegion> predRegions = new ArrayList<ARegion>();
    /**
     * edges to the subsequent regions (not subRegion)
     */
    private List<ARegion> succRegions = new ArrayList<ARegion>();

    protected RegionType regionType;
    public StructuralAnalysis.RegionType CTRegionType = StructuralAnalysis.RegionType.DefVal;

    /**
     * RegionAnalyzer.initialize() should have been invoked prior to the first call to this method.
     *
     * @return the result of the analysis. If an appropriate analyzer for this region could not be found,
     * or if RegionAnalyzer has not been initialized, then return null.
     * <p>
     * //TODO: Use singleton pattern and load the analyzer during constructor in each region.
     */
    public Object analyze() throws RegionAnalysisException {
        debug d = new debug("ARegion.java", "analyze()");
        RegionAnalysis analyzer = RegionAnalyzer.fetchAnalyzer(this.getClass());
        if (analyzer != null) {
            try {
                if(analyzer instanceof DIRRegionAnalyzer) {
                    DIRRegionAnalyzer dirazIns = (DIRRegionAnalyzer) analyzer;
                    dirazIns.curmethod = head.getBody().getMethod();
                    d.dg("set cur method of analyzer to: " + dirazIns.curmethod.getName());
                }
                return analyzer.run(this);
            } catch (Exception e) {
                d.wrn("RegionAnalysisException");
                d.wrn(e.toString());
                e.printStackTrace();
                throw new RegionAnalysisException(e.getMessage());
            }
        }
        return null;
    }

    protected ARegion(){
        //Default constructor. Do nothing.
    }

    public void addChild(ARegion child) {
        this.subRegions.add(child);
    }
    public void addChildren(Collection <ARegion> children) {
        this.subRegions.addAll(children);
    }


    /**
     * Constructor that is called from children classes other than Region
     */
    public ARegion(ARegion... subRegions) {
        this.subRegions.addAll(Arrays.asList(subRegions));
        this.head = this.subRegions.get(0).getHead();
        for (ARegion s : this.subRegions) {
            s.parent = this;
        }
        this.regionType = RegionType.BasicBlockRegion;
        /* By default, we set regionType as BasicBlockRegion. Should be set appropriately in the
        child class constructor.*/
    }


    public void setSuccRegionsSpecial(List<ARegion> succRegions2, List<ARegion> succ) {
        this.succRegions.clear();
        for (ARegion s : succRegions2) {
            for (int i = 0; i < succ.size(); i++) {
                if (s.predRegions.contains(succ.get(i)))
                    s.predRegions.remove(succ.get(i));
            }
            addSuccRegion(s);
        }
    }

    public void changeSuccessorOfPreds(ARegion newRegion) {
    	for (ARegion p : this.getPredRegions()) {
			int indexOfThis = p.succRegions.indexOf(this);
			p.succRegions.remove(this);
			p.succRegions.add(indexOfThis, newRegion);
			newRegion.predRegions.add(p);
		}
    }

    public void init(Map<Block, ARegion> basicRegions) {
        for (Block s : head.getSuccs())
            addSuccRegion(basicRegions.get(s));
    }

    public ARegion getParent() {
        return parent;
    }

    public Block getHead() {
        return head;
    }

    public List<ARegion> getPredRegions() {
        return predRegions;
    }

//    public String toString(){
//        String prettyString = regionType.toString();
//        for (ARegion child: subRegions) {
//            String childStr = (child == null) ? "Null" : child.toString();
//            childStr = doIndent(childStr);
//            prettyString = prettyString + "\n" +
//                    childStr;
//        }
//
//        return prettyString;
//    }
    public String toString(){
        String ret = "| ";
        if(CTRegionType.equals(StructuralAnalysis.RegionType.BasicBlock)) {
            //ret += getUnits().toString();
            ret += "BasicBlock" + head.getIndexInMethod();
        }
        else {
            ret += CTRegionType.name() + "\n";
            for(ARegion childr : subRegions) {
                String childStr = childr.toString();
                String[] childLines = childStr.split("\n");
                for(String cl : childLines) {
                    ret += "| " + cl + "\n";
                }
            }
        }
        return ret;
    }

    /**
     * Indent each new line in str (including the first)
     */
    private static String doIndent(String str) {
        String indent = "  ";
        str = indent + str;
        str = str.replace("\n","\n" + indent);

        return str;
    }

    public void addSuccRegion(ARegion s) {

        this.succRegions.add(s);
        s.predRegions.add(this);
    }

    public List<ARegion> getSuccRegions() {
        return succRegions;
    }

    public List<ARegion> getSubRegions() {
        return subRegions;
    }

    public boolean canMerge() {
        // TableSwitchStmt
        Stmt stmt = (Stmt) head.getTail();
        if (stmt instanceof TableSwitchStmt) {
            int succSize = succRegions.size();
            if (succSize > 1)
                return true;
        }

        // Self loop

        if (isSelfLoop())
            return true;

        if (getPredRegions().size() == 2 && getSuccRegions().size() == 1) {
            List<ARegion> preds = getPredRegions();
            ARegion succ = getSuccRegions().get(0);
            if (succ.getSuccRegions().contains(this) && preds.contains(succ))
                return true;
        }

        // If the predecessor has a successor other than this, do not merge
        if (getPredRegions().size() != 1)
            return false;
        // If this has more than one successors, do not merge unless
        // If this has 2 successors and one of the successors is an ancestor
        // The only pred should have only one succ
        if (getSuccRegions().size() > 1) {
            if (getSuccRegions().size() == 2) {
                int curRegionIndex = head.getIndexInMethod();
                int succ1Index = getSuccRegions().get(0).getHead().getIndexInMethod();
                int succ2Index = getSuccRegions().get(1).getHead().getIndexInMethod();
                ARegion theOnlyPred = getPredRegions().get(0);
                if (succ1Index < curRegionIndex || succ2Index < curRegionIndex)
                    if (theOnlyPred.getSuccRegions().size() == 1)
                        return true;
            }
            return false;
        }
        if (hasSibling() && isTriangleWithReturn()) {
            return true;
        }
        if (getSuccRegions().isEmpty() && getPredRegions().get(0).getSuccRegions().size() != 1)
            return false;
        if (hasSibling()) {
            return isTriangleOrDiamondOrLoop();

        }
        return true; // All conditions failed (?)
    }

    private boolean isTriangleWithReturn() {
        if (!hasSibling())
            return false;

        ARegion theOnlyPred = getPredRegions().get(0);
        ARegion theSibling = theOnlyPred.getSuccRegions().get(0).equals(this) ? theOnlyPred.getSuccRegions().get(1) :
                theOnlyPred.getSuccRegions().get(0);
        if (theSibling.isSelfLoop()) // ReviewStories/ rubbos
            return false;
        if (getSuccRegions().contains(theSibling) || theSibling.getSuccRegions().contains(this))
            return false;

        // Given this is a return block; the only pred should not point to region which has a lower index number  //
        // About me lwI
        if (getSuccRegions().size() == 0) {
            if (theSibling.getHead().getIndexInMethod() < theOnlyPred.getHead().getIndexInMethod())
                return false;
        }
        if (getSuccRegions().size() == 0) {
            if (theSibling.getSuccRegions().size() > 1) {
                if (theSibling.getSuccRegions().size() == 2) {  /// do whilw with return About me
                    ARegion succ1OfSibling = theSibling.getSuccRegions().get(0);
                    ARegion succ2OfSibling = theSibling.getSuccRegions().get(1);
                    int thisRegionNumber = this.head.getIndexInMethod();
                    if (succ1OfSibling.getHead().getIndexInMethod() < thisRegionNumber || succ2OfSibling.getHead()
                            .getIndexInMethod() < thisRegionNumber)
                        return true;
                }
                return false;
            }
            if (theSibling.getSuccRegions().size() == 0)
                return true;
            if (theSibling.getSuccRegions().size() == 1) {
                ARegion succOfSibling = theSibling.getSuccRegions().get(0);
                // The successor of the sibling is involved in a while loop
                //This has not been checked for region graph
                if (succOfSibling.getSuccRegions().size() == 2) {
                    ARegion succOfsuccOfSibling1 = succOfSibling.getSuccRegions().get(0);
                    ARegion succOfsuccOfSibling2 = succOfSibling.getSuccRegions().get(1);


                    if (succOfsuccOfSibling1.head.getIndexInMethod() < succOfSibling.getHead().getIndexInMethod()) {
                        if (succOfsuccOfSibling1.getPredRegions().size() == 1 && succOfsuccOfSibling1.getSuccRegions
                                ().size() == 1)
                            if (succOfsuccOfSibling1.getPredRegions().get(0).equals(succOfsuccOfSibling1
                                    .getSuccRegions().get(0))) {
                                return false;
                            }
                    }
                    if (succOfsuccOfSibling2.head.getIndexInMethod() < succOfSibling.getHead().getIndexInMethod()) {
                        if (succOfsuccOfSibling2.getPredRegions().size() == 1 && succOfsuccOfSibling2.getSuccRegions
                                ().size() == 1) {
                            if (succOfsuccOfSibling2.getPredRegions().get(0).equals(succOfsuccOfSibling2
                                    .getSuccRegions().get(0))) {
                                return false;
                            }
                        }
                    }
                }

                if (succOfSibling.getPredRegions().size() != 1)
                    return true;

            }
        }
        // }else if(theSibling.getSuccRegions().size()==0)
        // return true;
        return false;
    }


    private boolean isTriangleOrDiamondOrLoop() {
        ARegion theOnlyPred = getPredRegions().get(0);
        ARegion theOnlySucc = getSuccRegions().get(0);
        ARegion theSibling = theOnlyPred.getSuccRegions().get(0).equals(this) ? theOnlyPred.getSuccRegions().get(1) :
                theOnlyPred.getSuccRegions().get(0);
        if (theOnlySucc.equals(theOnlyPred))
            return true; // cycle
//		if(theOnlySucc.succRegions.size()>1)
//			return false;
        Stmt stmt = (Stmt) theSibling.head.getTail();
        if (theOnlySucc.equals(theSibling)) {
            if (theSibling.subRegions.size() == 0 && stmt instanceof TableSwitchStmt)
                return false;
            return true;
        }
        // triangle

        int size = theSibling.getSuccRegions().size();
        // if(size == 0 && theSibling.getPredRegions().size() == 1) return true;
        if (size == 0)
            return false;
        if (size == 1 && theSibling.getSuccRegions().get(0).equals(theOnlySucc))
            return true; // diamond
        return false;
    }

    private boolean hasSibling() {
        return getPredRegions().get(0).getSuccRegions().size() > 1;
    }

    private boolean isSelfLoop() {
        if (getPredRegions().size() == 2 && getSuccRegions().size() == 2) {
            if (getPredRegions().contains(getSuccRegions().get(0)) && getSuccRegions().get(0).equals(this))
                return true;
            if (getPredRegions().contains(getSuccRegions().get(1)) && getSuccRegions().get(1).equals(this))
                return true;
        }
        return false;
    }

    public ARegion merge() {
        debug d = new debug("ARegion.java", "merge()");
        Stmt stmt = (Stmt) head.getTail();
        ARegion theOnlyPred = getPredRegions().get(0);
        d.dg("getSuccRegions.isEmpty() = " + getSuccRegions().isEmpty() + "theOnlyPred.succRegions.size() = " + theOnlyPred.succRegions.size());
        d.dg("successors: " + getSuccRegions());
        d.dg("predecessors: " + getPredRegions());
        d.dg("theOnlyPred.succRegions: " + theOnlyPred.succRegions);


        /*
        Geetam: If this region has a sibling and their predecessor has only 2 children then
                create a branch region.
         TODO: Check why BranchRegionSpecial's params are named so and not thenRegion/elseRegion.
         */
        if(theOnlyPred.succRegions.size() == 2) {
            //TODO: Test more
            //warning for now
            d.wrn("theOnlyPred.succRegions.size() == 2");
            ARegion thenRegion = theOnlyPred.succRegions.get(0);
            ARegion elseRegion = theOnlyPred.succRegions.get(1);
            return new BranchRegion(theOnlyPred, thenRegion, elseRegion);
        }

        if (getSuccRegions().isEmpty() && theOnlyPred.succRegions.size() == 1)
            return new SequentialRegion(theOnlyPred, this);
        d.dg("ARegion.java: merge(): stmt: " + stmt);

        ARegion theOnlySucc = getSuccRegions().get(0);

        if (theOnlyPred.equals(theOnlySucc))
            return new LoopRegion(theOnlyPred, this);

        //Geetam: Seems the logic here should be that even if any of the preds is also a succ,
        //then it is a loop region.
        //case
        // for {
        //  if
        //  else
        // }

        List <ARegion> preds = getPredRegions();
        List <ARegion> succs = getSuccRegions();

        if(preds.size() == 2 && succs.size() == 1) {
            if(preds.get(0).equals(succs.get(0))) {
                return new LoopRegion(preds.get(1), succs.get(0));
            }
            else if(preds.get(1).equals(succs.get(0))) {
                return new LoopRegion(preds.get(0), succs.get(0));
            }
        }

        List<ARegion> succsOfPred = theOnlyPred.getSuccRegions();
        if (succsOfPred.size() == 1)
            return new SequentialRegion(theOnlyPred, this);

        ARegion theSibling = succsOfPred.get(0).equals(this) ? succsOfPred.get(1) : succsOfPred.get(0);

        int x = this.succRegions.get(0).predRegions.size();
        if (theSibling.predRegions.size() > 1) {
            if (succRegions.size() > 0 && theSibling.getSuccRegions().size() > 0) {
                if (succRegions.get(0).equals(theSibling.succRegions.get(0))) {
                    // return new BranchRegionSpecial2(theOnlyPred,theSibling,
                    // this);
                }
            }
        }

        if (x > 1 && (theOnlySucc.equals(theSibling) || (theSibling.getSuccRegions().size() > 0 && theOnlySucc.equals(theSibling.getSuccRegions().get(0))))) {
            if (theOnlySucc.equals(theSibling)) {
                return new BranchRegionSpecial(theOnlyPred, theSibling, this);
            } else {
            }
        }

        return new SequentialRegion(theOnlyPred, this);
    }

    public void setSuccRegionsFrom(ARegion tail) {
        List<ARegion> newSuccs = tail.getSuccRegions();
        for (ARegion s : newSuccs) {
            s.predRegions.remove(tail);
            this.addSuccRegion(s);
        }
    }

    public void setSuccRegions(List<ARegion> succRegions2, List<ARegion> succ) {
        this.succRegions.clear();
        for (ARegion s : succRegions2) {
            for (int i = 0; i < succ.size(); i++) {
                if (s.predRegions.contains(succ.get(i)))
                    s.predRegions.remove(succ.get(i));
            }
            addSuccRegion(s);
        }
    }

    public String print() {
        if (subRegions.isEmpty())
            return "B" + head.getIndexInMethod() + " ";
        String tree = "( ";
        for (ARegion s : subRegions) {
            tree += s.print();
        }
        tree += ")";
        return tree;
    }

    public boolean isEmpty() {
        Block headBlock = head;
        if (headBlock.getHead().equals(headBlock.getTail()) && (headBlock.getHead() instanceof GotoStmt)) {
            GotoStmt gotoStmt = (GotoStmt) headBlock.getHead();
            if (gotoStmt.getTarget() instanceof NopStmt) {
                return true;
            }
        }
        return false;
    }
    //Geetam: Seems there is no reason for these methods firstStmt, lastStmt, getUnits to be abstact.
    //Also making getUnits just return units of all subregions.

//    public abstract Unit firstStmt();
//    public abstract Unit lastStmt();
//    /**
//     * @return The set of all units in the region.
//     * Currently, we do not care about the order of these units. So we use a set.
//     */
//    public abstract Set<Unit> getUnits();
    public Set <Unit> getUnits() {
        Set <Unit> ret = new HashSet<>();
        for(ARegion child : subRegions) {
            ret.addAll(child.getUnits());
        }
        if(CTRegionType.equals(StructuralAnalysis.RegionType.BasicBlock)) {
            ret.addAll(head.getBody().getUnits());
        }
        return ret;
    }
}



