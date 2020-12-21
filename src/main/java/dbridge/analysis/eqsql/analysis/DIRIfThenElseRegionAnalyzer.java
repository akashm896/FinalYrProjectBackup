//Based on DIRBranchRegionAnalyzer
package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.TernaryNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.IfThenElseRegion;
import mytest.debug;

import java.util.Map;

public class DIRIfThenElseRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRIfThenElseRegionAnalyzer(){};
    public static DIRIfThenElseRegionAnalyzer INSTANCE = new DIRIfThenElseRegionAnalyzer();

    @Override
    public DIR constructDIR(ARegion region) throws RegionAnalysisException {
        debug d = new debug("DIRIfThenElseRegionAnalyzer.java", "constructDIR()");
        d.dg("region tree: " + region.toString());
        IfThenElseRegion ifteRegion = (IfThenElseRegion) region;
        ARegion headRegion = ifteRegion.headRegion;
        ARegion trueRegion = ifteRegion.thenRegion;
        ARegion falseRegion = ifteRegion.elseRegion;
        d.dg(headRegion);
        d.dg(trueRegion);
        d.dg(falseRegion);

        DIR headDIR = (DIR) headRegion.analyze();
        DIR trueDIR = (DIR) trueRegion.analyze();
        DIR falseDIR = (DIR) falseRegion.analyze();
        Node condition = Utils.extractCondition(headDIR);

        DIR condRegDIR = new DIR();
        insertFromTrueDag(condRegDIR, condition, trueDIR, falseDIR);
        insertFromFalseDag(condRegDIR, condition, trueDIR, falseDIR);
        d.dg("headDIR: " + headDIR);
        d.dg("condRegDIR: " + condRegDIR);
        d.dg("merging head with cond");
        DIR retDIR = Utils.mergeSeqDirs(headDIR, condRegDIR);
        d.dg("IfThenElseDIR: " + retDIR);
        return retDIR;
    }

    /** This method constructs and inserts TernaryNodes for variables which are present
     * in false region only */
    private void insertFromFalseDag(DIR condRegDIR, Node condition, DIR trueDIR, DIR falseDIR) {
        for (Map.Entry<VarNode, Node> entry : falseDIR.getVeMap().entrySet()) {
            VarNode var = entry.getKey();
            Node falseDag = entry.getValue();

            TernaryNode ternaryNode;
            if(trueDIR.contains(var)){
                continue;
                /* We would have processed this already in insertFromTrueDag */
            }
            else{
                ternaryNode = new TernaryNode(condition, var, falseDag);
            }
            condRegDIR.insert(var, ternaryNode);
        }
    }

    /** This method constructs and inserts TernaryNodes for variables which are present
    * in both true region and false region, as well as variables which are present in
    * true region only. */
    private void insertFromTrueDag(DIR condRegDIR, Node condition, DIR trueDIR, DIR falseDIR) {
        for (Map.Entry<VarNode, Node> entry : trueDIR.getVeMap().entrySet()) {
            VarNode var = entry.getKey();
            Node trueDag = entry.getValue();

            TernaryNode ternaryNode;
            if(falseDIR.contains(var)){
                Node falseDag = falseDIR.find(var);
                ternaryNode = new TernaryNode(condition, trueDag, falseDag);
            }
            else{
                ternaryNode = new TernaryNode(condition, trueDag, var);
            }
            condRegDIR.insert(var, ternaryNode);
        }
    }
}
