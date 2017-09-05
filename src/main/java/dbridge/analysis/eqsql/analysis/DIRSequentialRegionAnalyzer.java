package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;

/**
 * Created by ek on 4/5/16.
 */
public class DIRSequentialRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRSequentialRegionAnalyzer(){};
    public static DIRSequentialRegionAnalyzer INSTANCE = new DIRSequentialRegionAnalyzer();

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {
        ARegion first = region.getSubRegions().get(0);
        ARegion second = region.getSubRegions().get(1);

        DIR d1 = (DIR) first.analyze();
        DIR d2 = (DIR) second.analyze();

        DIR mergedDag = Utils.mergeSeqDirs(d1, d2);

        return mergedDag;
    }
}
