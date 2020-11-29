package dbridge.analysis.region.regions;

import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
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
        this.CTRegionType = StructuralAnalysis.RegionType.BasicBlock;
    }

}
