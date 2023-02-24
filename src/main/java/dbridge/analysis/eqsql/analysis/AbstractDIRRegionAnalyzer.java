/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.region.api.RegionAnalysis;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import exceptions.DIRConstructionException;
import io.geetam.github.loopHandler.DAGTillNow;
import mytest.debug;
import soot.SootMethod;

/**
 * Created by K. Venkatesh Emani on 12/18/2016.
 * Abstract class that is used to contain logic which is common across all
 * "*RegionAnalyzer" classes. For example: dir.updateRegion(region)
 */
public abstract class AbstractDIRRegionAnalyzer implements RegionAnalysis<DIR>{
    public SootMethod curmethod;

    @Override
    public DIR run(ARegion region) throws RegionAnalysisException {
        debug d = new debug("AbstractDIRRegionAnalyzer.java", "run()");
        DIR dir = constructDIR(region);
        d.dg("AbstractRegion dir constructed: " + dir);
        dir.updateRegion(region);
        dir.updateSpecialCase();
        System.out.println("" + dir);
//        DAGTillNow.setDag(dir.getVeMap());
        return dir;
    }

    public abstract DIR constructDIR(ARegion region) throws RegionAnalysisException;
}
