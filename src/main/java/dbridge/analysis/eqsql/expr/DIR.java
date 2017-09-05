package dbridge.analysis.eqsql.expr;

import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.RetVarNode;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.region.regions.ARegion;
import soot.Type;

import java.util.*;

public class DIR {
    private final Map<VarNode, Node> veMap;
    private ARegion region;

    public DIR() {
        veMap = new HashMap<>();
    }

    public void insert(VarNode target, Node subDag) {
        veMap.put(target, subDag);
    }

    public Map<VarNode, Node> getVeMap() {
        return veMap;
    }

    /**
     * copy  constructor - shallow copy
     */
    public DIR(DIR eeDag) {
        this.veMap = new HashMap<>();
        for (Map.Entry<VarNode, Node> entry : eeDag.getVeMap().entrySet()) {
            this.veMap.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean contains(VarNode key){
        Node dag = this.find(key);
        return (dag != null);
    }

    public Node find(VarNode key) {
        if (veMap.containsKey(key)) {
            return veMap.get(key);
        }
        return null;
    }

    /* Return the type of the return variable if present,
    * if not, return null. */
    public Type findRetVarType(){
        VarNode retVarKey = RetVarNode.getARetVar();
        for (Map.Entry<VarNode, Node> entry : veMap.entrySet()) {
            VarNode key = entry.getKey();
            if(key.equals(retVarKey)){
                assert key instanceof RetVarNode;
                return ((RetVarNode)key).getOrigRetVarType();
            }
        }
        return null;
    }

    public Map.Entry findEntry(VarNode key){
        for (Map.Entry<VarNode, Node> entry : veMap.entrySet()) {
            if(entry.getKey().equals(key)){
                return entry;
            }
        }
        return null;
    }

    public Set<VarNode> getVars(){
        if(!isEmpty()) {
            return veMap.keySet();
        }
        return new HashSet<>(); //empty Set
    }

    public boolean isEmpty() {
        return veMap == null || veMap.isEmpty();
    }

    public String toString() {
        /* Sort the keys so that they are concatenated in order */
        List<VarNode> keys = new ArrayList<>();
        keys.addAll(veMap.keySet());
        Collections.sort(keys);

        String toStr = "";
        for (VarNode key : keys) {
            toStr += "~~~ " + key + " ~~~\n";
            toStr += veMap.get(key) + "\n\n";
        }

        return toStr;
    }

    public ARegion getRegion() {
		return region;
	}

    /** Update the region for each node in the DIR.veMap */
	public void updateRegion(ARegion region) {
        for (Map.Entry<VarNode, Node> entry : veMap.entrySet()) {
            entry.getValue().setRegion(region);
        }

    }
}