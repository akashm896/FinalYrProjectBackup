//Not part of base DBridge
package dbridge.analysis.region.regions;


import io.geetam.github.StructuralAnalysis.StructuralAnalysis;

public class IfThenRegion extends ARegion {
    public ARegion headRegion;
    public ARegion thenRegion;

    public IfThenRegion() {
        this.CTRegionType = StructuralAnalysis.RegionType.IfThen;
    }
}
