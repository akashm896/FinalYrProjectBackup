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
