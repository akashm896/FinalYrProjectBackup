/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

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
import io.geetam.github.loopHandler.DAGTillNow;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import mytest.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DIRSequentialRegionAnalyzerN extends AbstractDIRRegionAnalyzer {
    DIR mergedDag = new DIR();
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
//        DIR mergedDag = new DIR();
        for(ARegion subRegion : region.getSubRegions()) {
            if(subRegion.toString().equals("| BasicBlock0")) {
                d.dg("Break point!");
            }
            d.dg("subregion class: " + subRegion.getClass());
            DIR subRegionDIR = (DIR) subRegion.analyze();
            if(subRegion instanceof LoopRegion) {
                VarNode iterator = getKeyMappedToIterator(mergedDag);
                if(iterator != null) {
                    InvokeMethodNode iteratorMapping = (InvokeMethodNode) mergedDag.find(iterator);
                    mergedDag.getVeMap().put(iterator, iteratorMapping.getChild(0));
                    /////////////////////////////////////////////////////////////////////////////////////////////////////
                    List<Node> changedLoopVarList = new ArrayList(LoopIteratorCollectionHandler.changedLoopFieldsMap.keySet());
                    String iteratorname = changedLoopVarList.get(0).toString();
                    iteratorname = iteratorname.substring(0, iteratorname.indexOf('.'));
                    System.out.println(iteratorname);
                    Node toReplaceVeMap = null;
                    for(Node key : mergedDag.getVeMap().keySet()){
                        String keyName = key.toString();
                        if(keyName.contains(iteratorname) && !keyName.contains("this.")){
                            toReplaceVeMap = mergedDag.getVeMap().get(key);
                            break;
                        }
                    }
                    for(Node changedKey : changedLoopVarList){
                        Node changedVEMap = LoopIteratorCollectionHandler.changedLoopFieldsMap.get(changedKey);
                        LoopIteratorCollectionHandler.replacePrimitives(toReplaceVeMap, changedKey, changedVEMap);
                        System.out.println(toReplaceVeMap + " " + changedKey + " " + changedVEMap);
                    }
                    mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
//                    System.out.println(toReplaceVeMap);
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////
                }
            }

            d.dg("merging subregion: " + subRegion);
            d.dg("subregionDIR: " + subRegionDIR);
            d.dg("prevDIR: " + mergedDag);
            mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
//            DAGTillNow.setDag(mergedDag);
        }
        return mergedDag;
    }

    public DIR getMergedDIR(){
        return this.mergedDag;
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
