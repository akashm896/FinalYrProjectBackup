package dbridge.analysis.region.regions;

import soot.Unit;
import soot.toolkits.graph.Block;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by K. Venkatesh Emani on 12/19/2016.
 */
public class Region extends ARegion {
    public Region(Block b) {
        this.head = b;
        this.regionType = RegionType.BasicBlockRegion;
    }

    @Override
    public Unit firstStmt() {
        return getHead().getHead();
    }

    @Override
    public Unit lastStmt() {
        return getHead().getTail();
    }

    @Override
    public Set<Unit> getUnits() {
        Set<Unit> units = new HashSet<>();
        units.addAll(head.getBody().getUnits());
        return units;
    }

}
