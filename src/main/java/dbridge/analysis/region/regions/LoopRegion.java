/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.region.regions;

import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
import soot.Unit;

import java.util.HashSet;
import java.util.Set;

public class LoopRegion extends ARegion {
    public ARegion head;
    public ARegion body;
    public LoopRegion(ARegion loopHead, ARegion loopBody) {
        super();
        head = loopHead;
        body = loopBody;
        System.out.println("LoopRegion created");
        this.CTRegionType = StructuralAnalysis.RegionType.WhileLoop;
//
//        if(loopHead.getPredRegions().size() > 1) {
//            ARegion newPredecessor = loopHead.getPredRegions().get(0).equals(loopBody) ? loopHead.getPredRegions().get(1)
//                    : loopHead.getPredRegions().get(0);
//            newPredecessor.getSuccRegions().remove(loopHead);
//            newPredecessor.addSuccRegion(this);
//        }
//        if(loopHead.getSuccRegions().size() > 1) {
//            ARegion newSuccessor = loopHead.getSuccRegions().get(0).equals(loopBody) ? loopHead.getSuccRegions().get(1) :
//                    loopHead.getSuccRegions().get(0);
//            newSuccessor.getPredRegions().remove(loopHead);
//            this.addSuccRegion(newSuccessor);
//            this.regionType = RegionType.LoopRegion;
//        }


    }
//
//    @Override
//    public Unit firstStmt() {
//        return getSubRegions().get(1).firstStmt();
//    }
//
//    @Override
//    public Unit lastStmt() {
//        return getSubRegions().get(0).lastStmt();
//    }
//
//    @Override
//    public Set<Unit> getUnits() {
//        Set<Unit> units = new HashSet<>();
//        units.addAll(getSubRegions().get(1).getUnits());
//        units.addAll(getSubRegions().get(0).getUnits());
//        return units;
//    }
}
