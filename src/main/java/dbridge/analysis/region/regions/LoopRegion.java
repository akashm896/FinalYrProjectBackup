package dbridge.analysis.region.regions;

import soot.Unit;

import java.util.HashSet;
import java.util.Set;

public class LoopRegion extends ARegion {
    public LoopRegion(ARegion loopHead, ARegion loopBody) {
        super(loopHead, loopBody);
        ARegion newPredecessor = loopHead.getPredRegions().get(0).equals(loopBody) ? loopHead.getPredRegions().get(1)
                : loopHead.getPredRegions().get(0);
        newPredecessor.getSuccRegions().remove(loopHead);
        newPredecessor.addSuccRegion(this);

        ARegion newSuccessor = loopHead.getSuccRegions().get(0).equals(loopBody) ? loopHead.getSuccRegions().get(1) :
                loopHead.getSuccRegions().get(0);
        newSuccessor.getPredRegions().remove(loopHead);
        this.addSuccRegion(newSuccessor);
        this.regionType = RegionType.LoopRegion;
    }

    @Override
    public Unit firstStmt() {
        return getSubRegions().get(1).firstStmt();
    }

    @Override
    public Unit lastStmt() {
        return getSubRegions().get(0).lastStmt();
    }

    @Override
    public Set<Unit> getUnits() {
        Set<Unit> units = new HashSet<>();
        units.addAll(getSubRegions().get(1).getUnits());
        units.addAll(getSubRegions().get(0).getUnits());
        return units;
    }
}
