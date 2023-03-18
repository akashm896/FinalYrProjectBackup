/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

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
