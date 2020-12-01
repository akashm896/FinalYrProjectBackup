//Based on DIRSequentialRegionAnalyzer
package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;


public class DIRSequentialRegionAnalyzerN extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRSequentialRegionAnalyzerN(){};
    public static DIRSequentialRegionAnalyzerN INSTANCE = new DIRSequentialRegionAnalyzerN();

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {
//        ARegion first = region.getSubRegions().get(0);
//        ARegion second = region.getSubRegions().get(1);
//
//        DIR d1 = (DIR) first.analyze();
//        DIR d2 = (DIR) second.analyze();
//
//        DIR mergedDag = Utils.mergeSeqDirs(d1, d2);

        DIR mergedDag = new DIR();
        for(ARegion subRegion : region.getSubRegions()) {
            DIR subRegionDIR = (DIR) subRegion.analyze();
            mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
        }
        return mergedDag;
    }
}
