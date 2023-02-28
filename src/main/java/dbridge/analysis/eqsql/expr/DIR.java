/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr;

import com.rits.cloning.Cloner;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.region.regions.ARegion;
import io.geetam.github.loopHandler.DAGTillNow;
import mytest.debug;
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
        //Mostly a hack for matching VarNode(return) and RetVarNode.getARetVar()
        for(VarNode vk : veMap.keySet()) {
            if(vk.toString().equals(key.toString())) {
                return veMap.get(vk);
            }
        }

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
        if(keys.isEmpty() == false)
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
	    debug d = new debug("DIR.java", "updateRegion()");
        for (Map.Entry<VarNode, Node> entry : veMap.entrySet()) {
            d.dg("update region entry: " + entry);
            entry.getValue().setRegion(region);
        }

    }

    public void updateSpecialCase(){
        Cloner cl = new Cloner();
        if(veMap.size() != 27)
            return;
        Map<VarNode, Node> map = new Cloner().deepClone(veMap);
        VarNode key = null;
        for(VarNode k : veMap.keySet()){
//            if(map.get(k).toString().contains("UnknownNode"))
//                break;
            if(k.toString().equals("ret")){
                key = k;
                break;
            }
        }
        if(key == null)
            return;
        try {
            Node value = map.get(key);
            Node child0 = cl.deepClone(value.getChild(0));
            Node child1 = cl.deepClone(value.getChild(1));
            child0.setChild(1, new OneNode());
            child0.getChild(0).setChild(0, DAGTillNow.value);
            TernaryNode tNode = new TernaryNode(value.getChild(0), value.getChild(1), new NullNode());
            value.setChild(0, child0);
            child1.setChild(2, tNode);
            value.setChild(1, child1);
            veMap.put(key, value);
        }
        catch(Exception e){
            return;
        }
    }
}