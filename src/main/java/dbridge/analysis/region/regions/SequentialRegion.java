/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.region.regions;

import soot.Unit;

import java.util.HashSet;
import java.util.Set;

public class SequentialRegion extends ARegion {

    public SequentialRegion(ARegion first, ARegion second) {
        super(first, second);
        first.changeSuccessorOfPreds(this);
        this.setSuccRegionsFrom(second);
        this.regionType = RegionType.SequentialRegion;
    }

//    @Override
//    public Unit firstStmt() {
//        return getSubRegions().get(0).firstStmt();
//    }
//
//    @Override
//    public Unit lastStmt() {
//        return getSubRegions().get(1).lastStmt();
//    }
//
//    @Override
//    public Set<Unit> getUnits() {
//        Set<Unit> units = new HashSet<>();
//        units.addAll(getSubRegions().get(0).getUnits());
//        units.addAll(getSubRegions().get(1).getUnits());
//        return units;
//    }
}
