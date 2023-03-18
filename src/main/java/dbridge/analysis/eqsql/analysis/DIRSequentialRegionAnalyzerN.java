/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Based on DIRSequentialRegionAnalyzer
package dbridge.analysis.eqsql.analysis;

import com.rits.cloning.Cloner;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.LoopRegion;
import io.geetam.github.loopHandler.DAGTillNow;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import mytest.debug;
import java.util.ArrayList;
import java.util.HashMap;
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
                separateLoopSummarizedVEMap(subRegionDIR);
                VarNode iterator = getKeyMappedToIterator(mergedDag);
                if(iterator != null) {
                    InvokeMethodNode iteratorMapping = (InvokeMethodNode) mergedDag.find(iterator);
                    mergedDag.getVeMap().put(iterator, iteratorMapping.getChild(0));
                    /////////////////////////////////////////////////////////////////////////////////////////////////////
//                    List<Node> changedLoopVarList = new ArrayList(LoopIteratorCollectionHandler.changedLoopPrimitiveFieldsMap.keySet());
//                    String iteratorname = changedLoopVarList.get(0).toString();
//                    iteratorname = iteratorname.substring(0, iteratorname.indexOf('.'));
//                    System.out.println(iteratorname);
//                    Node toReplaceVeMap = getToReplaceVEMap(iteratorname);
//
//                    for(Node changedKey : changedLoopVarList){
//                        Node toInlineVEMap = LoopIteratorCollectionHandler.changedLoopPrimitiveFieldsMap.get(changedKey);
//                        LoopIteratorCollectionHandler.replacePrimitives(toReplaceVeMap, changedKey, toInlineVEMap);
//                    }
//
//                    List<Node> changedLoopEntityList = new ArrayList(LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.keySet());
//                    for(Node changedKey : changedLoopEntityList){
//                        Node toInlineVEMap = LoopIteratorCollectionHandler.changedLoopEntityFieldsMap.get(changedKey);
//                        LoopIteratorCollectionHandler.replaceEntity(toReplaceVeMap, changedKey, toInlineVEMap);
//                    }
//
//                    mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////
                }

            }
            else{ // if regions present after LoopRegion and VEMap gets updated there then make the changes in summarizedLoopVEMap also
//                for(VarNode key : subRegionDIR.getVeMap().keySet()){
//                    if(LoopIteratorCollectionHandler.summarizedLoopVEMap.containsKey(key))
//                        LoopIteratorCollectionHandler.summarizedLoopVEMap.put(key, subRegionDIR.getVeMap().get(key));
//                }
            }

//            if(subRegion instanceof LoopRegion)
//                mergedDag = Utils.mergeLoopAndSeqDirs(mergedDag, subRegionDIR);
////            else
            mergedDag = Utils.mergeSeqDirs(mergedDag, subRegionDIR);
            d.dg("merging subregion: " + subRegion);
            d.dg("subregionDIR: " + subRegionDIR);
            d.dg("prevDIR: " + mergedDag);
//            if(LoopIteratorCollectionHandler.summarizedLoopVEMap.keySet().size() > 0){
//                for(VarNode key : LoopIteratorCollectionHandler.summarizedLoopVEMap.keySet())
//                    mergedDag.getVeMap().put(key, LoopIteratorCollectionHandler.summarizedLoopVEMap.get(key));
//            }
            System.out.println(mergedDag);
        }
        DAGTillNow.updateDag(mergedDag);
        System.out.println(DAGTillNow.getDag());
        for(VarNode key : mergedDag.getVeMap().keySet()){
            Node toReplace = mergedDag.getVeMap().get(key);
            if(LoopIteratorCollectionHandler.collectionVariable.contains(toReplace.toString()) && mergedDag.getVeMap().containsKey(toReplace)) {
                mergedDag.getVeMap().put(key, mergedDag.getVeMap().get(toReplace));
            }
        }
        return mergedDag;
    }

    public DIR getMergedDIR(){
        return this.mergedDag;
    }

    private void separateLoopSummarizedVEMap(DIR loopDIR) { // Akash
        boolean seenRepo = false;
        Map<VarNode, Node> loopDIRcopy = new Cloner().deepClone(loopDIR.getVeMap());
        for (VarNode key : loopDIRcopy.keySet()) {
            if (loopDIRcopy.get(key) instanceof UnknownNode) {
            }
            else {
                VarNode newKey = key;
//                if(key.toString().contains(".")){
//                    newKey = new VarNode(key.toString().substring(key.toString().lastIndexOf('.')+1));
//                }
//                if(newKey.toString().contains("Repository"))
//                    seenRepo = true;
//                LoopIteratorCollectionHandler.summarizedLoopVEMap.put(newKey, loopDIRcopy.get(key));
//            }
                if (!newKey.toString().startsWith("$r"))
                    LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps.put(newKey, loopDIRcopy.get(key));
            }
//        if(LoopIteratorCollectionHandler.summarizedLoopVEMap.size() == 2 && seenRepo){}
//        else
//            LoopIteratorCollectionHandler.summarizedLoopVEMap = new HashMap<>();
//        System.out.println(LoopIteratorCollectionHandler.summarizedLoopVEMap);
        System.out.println(LoopIteratorCollectionHandler.loopPatternSummarizedVEMaps);
//        System.out.println(FuncStackAnalyzer.funcDIRMap);
        }
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

    public Node getToReplaceVEMap(String iteratorname){
        for(Node key : mergedDag.getVeMap().keySet()){
            String keyName = key.toString();
            if(keyName.contains(iteratorname) && !keyName.contains("this.")){
                return mergedDag.getVeMap().get(key);
            }
        }
        return null;
    }

}
