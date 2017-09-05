package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.UnAlgNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.eqsql.util.VarResolver;

import java.util.Map;

/**
 * Created by ek on 24/10/16.
 * Package local class.
 */
class Utils {

    static DIR mergeSeqDirs(DIR precedingDIR, DIR followingDIR)  {
        /* Resolve variable references in followingDIR with their expressions from precedingDIR */
        VarResolver resolver = new VarResolver(precedingDIR);
        for (Map.Entry<VarNode, Node> d2Entry : followingDIR.getVeMap().entrySet()) {
            Node node = d2Entry.getValue();
            Node resolvedNode = node.accept(resolver);
            followingDIR.insert(d2Entry.getKey(), resolvedNode);
        }

        /* Now merge precedingDIR and followingDIR. If followingDIR is a loop region, then
         * some variables may be marked not algebrizable. This information propagates
          * during merging. */
        for (Map.Entry<VarNode, Node> d2entry : followingDIR.getVeMap().entrySet()) {
            VarNode key = d2entry.getKey();
            if(UnAlgNode.isUnAlgNode(precedingDIR.find(key))){
                continue;//do not insert entry if it is already marked as an UnAlgNode
            }
            precedingDIR.insert(key, d2entry.getValue());
        }

        /* The two loops above cannot be merged. Although they iterate on the same set of values,
        * the entries from followingDIR get modified in the first loop BEFORE they are inserted
        * into precedingDIR in the second loop */

        return precedingDIR;
    }

    static Node extractCondition(DIR dir){
        VarNode condVar = VarNode.getACondVar();
        assert dir.contains(condVar);
        Node cond = dir.find(condVar);
        return cond;
    }

}
