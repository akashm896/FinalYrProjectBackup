package dbridge.analysis.region.regions;

import soot.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BranchRegion extends ARegion {

    public BranchRegion(ARegion theOnlyPred, ARegion theSibling, ARegion region) {
        super(theOnlyPred, theSibling, region);
        theOnlyPred.changeSuccessorOfPreds(this);
        Set<ARegion> newSuccessors = new HashSet<>();
        newSuccessors.addAll(theSibling.getSuccRegions());
        List<ARegion> reg = new ArrayList<>();
        reg.add(region);
        reg.add(theSibling);
        this.setSuccRegions(new ArrayList(newSuccessors), reg);
    }

    @Override
    public Unit firstStmt() {
        return getSubRegions().get(0).firstStmt();
    }

    @Override
    public Unit lastStmt() {
        return getSubRegions().get(2).lastStmt();
    }

    @Override
    public Set<Unit> getUnits() {
        Set<Unit> units = new HashSet<>();
        units.addAll(getSubRegions().get(0).getUnits());
        units.addAll(getSubRegions().get(1).getUnits());
        units.addAll(getSubRegions().get(2).getUnits());
        return units;
    }
}
