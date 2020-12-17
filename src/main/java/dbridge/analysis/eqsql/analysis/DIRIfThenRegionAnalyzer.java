//Based on DIRBranchRegionSpecialAnalyzer

package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.TernaryNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.IfThenRegion;
import mytest.debug;

import java.util.Map;


public class DIRIfThenRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRIfThenRegionAnalyzer(){};
    public static DIRIfThenRegionAnalyzer INSTANCE = new DIRIfThenRegionAnalyzer();

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {
        debug d = new debug("DIRIfThenRegionAnalyzer.java", "constructDIR()");
        IfThenRegion ifThenRegion = (IfThenRegion) region;
        ARegion headRegion = ifThenRegion.headRegion;
        ARegion trueRegion = ifThenRegion.thenRegion;
        d.dg("headRegion: " + headRegion);
        d.dg("trueRegion: " + trueRegion);
        DIR headDIR = (DIR) headRegion.analyze();
        DIR trueDIR = (DIR) trueRegion.analyze();

        d.dg("headDIR: " + headDIR);
        d.dg("trueDIR: " + trueDIR);

        Node condition = Utils.extractCondition(headDIR);
        d.dg("condition: " + condition);
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
