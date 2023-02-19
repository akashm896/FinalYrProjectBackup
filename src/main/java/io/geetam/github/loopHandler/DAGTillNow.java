package io.geetam.github.loopHandler;

import com.rits.cloning.Cloner;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.VarNode;

import java.util.HashMap;
import java.util.Map;

public class DAGTillNow {
    public static Map<VarNode, Node> dags = new HashMap<>();
    public static Node value = null;

    public DAGTillNow() {
    }

    public static Map<VarNode, Node> getDag() {
        return dags;
    }


    public static void setDag(Map<VarNode, Node> dag) {
        dags = dag;
    }

    public static void updateDag(DIR dag){
        Map<VarNode, Node> dircpy = new Cloner().deepClone(dag.getVeMap());
        for(VarNode key : dircpy.keySet()){
            if(!dags.keySet().isEmpty() && dags.keySet().contains(key))
                dag.getVeMap().put((VarNode) key, dircpy.get(key));
            else
                dags.put((VarNode) key, dircpy.get(key));
        }
    }
}
