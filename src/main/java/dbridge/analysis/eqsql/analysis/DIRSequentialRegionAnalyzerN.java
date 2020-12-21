//Based on DIRSequentialRegionAnalyzer
package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.InvokeMethodNode;
import dbridge.analysis.eqsql.expr.node.MethodIteratorNode;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.LoopRegion;
import mytest.debug;

import java.util.Map;


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
        debug d = new debug("DIRSequentialRegionAnalyzerN.java", "constructDIR()");
        d.dg("subregions: " + region.getSubRegions());
        DIR mergedDag = new DIR();
        for(ARegion subRegion : region.getSubRegions()) {
            d.dg("subregion class: " + subRegion.getClass());
            DIR subRegionDIR = (DIR) subRegion.analyze();
            if(subRegion instanceof LoopRegion) {
                VarNode iterator = getKeyMappedToIterator(mergedDag);
                if(iterator != null) {
                    InvokeMethodNode iteratorMapping = (InvokeMethodNode) mergedDag.find(iterator);
                    mergedDag.getVeMap().put(iterator, iteratorMapping.getChild(0));
                }
            }

            d.dg("merging subregion: " + subRegion);
            d.dg("subregionDIR: " + subRegionDIR);
            d.dg("prevDIR: " + mergedDag);
            mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
        }
        return mergedDag;
    }

    public static VarNode getKeyMappedToIterator(DIR dir) {
        Map<VarNode, Node> veMap = dir.getVeMap();
        for(VarNode v : veMap.keySet()) {
            Node mapping = veMap.get(v);
            if(mapping instanceof InvokeMethodNode) {
                InvokeMethodNode imn = (InvokeMethodNode) mapping;
                Node[] imnChildren = imn.getChildren();
                if(imnChildren[1] instanceof MethodIteratorNode) {
                    return v;
                }
            }
        }
        return null;
    }
}
