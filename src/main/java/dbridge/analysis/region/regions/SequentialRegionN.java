//Not part of base DBridge
package dbridge.analysis.region.regions;

import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
import soot.Unit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SequentialRegionN extends ARegion {
    public SequentialRegionN() {
        this.regionType = RegionType.SequentialRegion;
        this.CTRegionType = StructuralAnalysis.RegionType.Sequential;
    }

    public SequentialRegionN(Collection <ARegion> subregions) {
        addChildren(subregions);
        this.regionType = RegionType.SequentialRegion;
        this.CTRegionType = StructuralAnalysis.RegionType.Sequential;
    }


}
