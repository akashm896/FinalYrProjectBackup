/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

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
