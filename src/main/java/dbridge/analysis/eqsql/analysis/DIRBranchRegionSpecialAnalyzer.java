/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.TernaryNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;

import java.util.Map;

/**
 * Created by ek on 4/5/16.
 * //TODO Why do we need a BranchRegionSpecialAnalyzer when we have a BranchRegionAnalyzer?
 * Remove this.
 */
public class DIRBranchRegionSpecialAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRBranchRegionSpecialAnalyzer(){};
    public static DIRBranchRegionSpecialAnalyzer INSTANCE = new DIRBranchRegionSpecialAnalyzer();

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {

        ARegion headRegion = region.getSubRegions().get(0);
        ARegion trueRegion = region.getSubRegions().get(1); // small hack -- Tejas

        DIR headDIR = (DIR) headRegion.analyze();
        DIR trueDIR = (DIR) trueRegion.analyze();
        Node condition = Utils.extractCondition(headDIR);

        DIR condDag = new DIR();
        for (Map.Entry<VarNode, Node> entry : trueDIR.getVeMap().entrySet()) {
            VarNode var = entry.getKey();
            Node dag = entry.getValue();
            TernaryNode ternaryNode = new TernaryNode(condition, dag, var);
            condDag.insert(var, ternaryNode);
        }

        DIR retDir = Utils.mergeSeqDirs(headDIR, condDag);
        return retDir;
    }

}
