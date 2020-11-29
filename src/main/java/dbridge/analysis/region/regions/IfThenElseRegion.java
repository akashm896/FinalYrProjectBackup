//Not part of base DBridge
package dbridge.analysis.region.regions;


import io.geetam.github.StructuralAnalysis.StructuralAnalysis;

public class IfThenElseRegion extends ARegion {
    public ARegion headRegion;
    public ARegion thenRegion;
    public ARegion elseRegion;

    public IfThenElseRegion() {
        this.CTRegionType = StructuralAnalysis.RegionType.IfThenElse;
    }
}
