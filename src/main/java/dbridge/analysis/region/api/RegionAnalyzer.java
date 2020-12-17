package dbridge.analysis.region.api;

import dbridge.analysis.eqsql.analysis.DIRIfThenElseRegionAnalyzer;
import dbridge.analysis.eqsql.analysis.DIRIfThenRegionAnalyzer;
import dbridge.analysis.eqsql.analysis.DIRSequentialRegionAnalyzer;
import dbridge.analysis.eqsql.analysis.DIRSequentialRegionAnalyzerN;
import dbridge.analysis.region.regions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ek on 4/5/16.
 */
public class RegionAnalyzer {

    private static Map<Class, RegionAnalysis> regionAnalyzerMap;

    private static boolean initDone = false;

    public static void initialize(RegionAnalysis basicBlockAnalyzer,
                                  RegionAnalysis branchRegionSpecialAnalyzer,
                                  RegionAnalysis branchRegionAnalyzer,
                                  RegionAnalysis loopRegionAnalyzer,
                                  RegionAnalysis sequentialRegionAnalyzer) {

        regionAnalyzerMap = new HashMap<>();
        regionAnalyzerMap.put(BranchRegionSpecial.class, branchRegionSpecialAnalyzer);
        regionAnalyzerMap.put(BranchRegion.class, branchRegionAnalyzer);
        regionAnalyzerMap.put(LoopRegion.class, loopRegionAnalyzer);
        regionAnalyzerMap.put(SequentialRegion.class, sequentialRegionAnalyzer);
        regionAnalyzerMap.put(Region.class, basicBlockAnalyzer);
        regionAnalyzerMap.put(SequentialRegionN.class, DIRSequentialRegionAnalyzerN.INSTANCE);
        regionAnalyzerMap.put(IfThenElseRegion.class, DIRIfThenElseRegionAnalyzer.INSTANCE);
        regionAnalyzerMap.put(IfThenRegion.class, DIRIfThenRegionAnalyzer.INSTANCE);
        initDone = true;
    }


    public static RegionAnalysis fetchAnalyzer(Class<? extends ARegion> aClass) {
        if (initDone && regionAnalyzerMap.containsKey(aClass)) {
            return regionAnalyzerMap.get(aClass);
        }

        return null;
    }
}
