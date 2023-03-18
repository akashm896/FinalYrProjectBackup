/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Not part of base DBridge
package dbridge.analysis.region.regions;

import dbridge.analysis.eqsql.expr.DIR;
import io.geetam.github.StructuralAnalysis.StructuralAnalysis;
import soot.Unit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SequentialRegionN extends ARegion {
    DIR mergedDag = new DIR();
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
