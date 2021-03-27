package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.region.api.RegionAnalysis;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import exceptions.DIRConstructionException;

/**
 * Created by K. Venkatesh Emani on 12/18/2016.
 * Abstract class that is used to contain logic which is common across all
 * "*RegionAnalyzer" classes. For example: dir.updateRegion(region)
 */
public abstract class AbstractDIRRegionAnalyzer implements RegionAnalysis<DIR>{
    @Override
    public DIR run(ARegion region) throws RegionAnalysisException {
        DIR dir = constructDIR(region);
        System.out.println("AbstractRegion dir constructed: " + dir);
        dir.updateRegion(region);
        return dir;
    }

    public abstract DIR constructDIR(ARegion region) throws RegionAnalysisException;
}
