/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Based on DIRBranchRegionSpecialAnalyzer

package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.TernaryNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.eqsql.expr.node.ZeroNode;
import dbridge.analysis.region.exceptions.RegionAnalysisException;
import dbridge.analysis.region.regions.ARegion;
import dbridge.analysis.region.regions.IfThenRegion;
import io.geetam.github.Autowire.Util;
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

        //Geetam: Kind of a hack to say that condition in header is same as original if condition, needs
        //inversion here, rather than inversion of meaning Eq and NotEq as was the case originally.
        Node condition = Utils.extractCondition(headDIR);
        d.dg("condition: " + condition);
        if(condition instanceof TernaryNode == false)
            condition = Utils.invertCondition(condition);
        d.dg("condition after inversion: " + condition);
        DIR condDag = new DIR();
        for (Map.Entry<VarNode, Node> entry : trueDIR.getVeMap().entrySet()) {
            VarNode var = entry.getKey();
            Node dag = entry.getValue();
            TernaryNode ternaryNode = new TernaryNode(condition, dag, var, var.isCondVar());
            condDag.insert(var, ternaryNode);
        }
        d.dg("merging head with cond");
        DIR retDir = Utils.mergeSeqDirs(headDIR, condDag);
        d.dg("IfThenRegion: " + region);
        //This condvar business is just to "simulate" a "and" region created due to java "&&" operator.
        //TODO: introduce a new region type.
        VarNode condvar = VarNode.getACondVar();
        Node headcond = headDIR.find(condvar);
        if(headcond instanceof TernaryNode == false)
            headcond = Utils.invertCondition(headcond);
        Node thencond = trueDIR.find(condvar);
        if(thencond != null) {
            if(thencond instanceof TernaryNode == false)
                thencond = Utils.invertCondition(thencond);
            Node regioncond = new TernaryNode(headcond, thencond, new ZeroNode(), true);
            retDir.getVeMap().put(condvar, regioncond);
        }
        d.dg("IfThenRegionDIR: " +  retDir);
        return retDir;
    }

}
