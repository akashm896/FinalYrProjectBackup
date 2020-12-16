package io.geetam.github.StructuralAnalysis;


import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import mytest.debug;

import java.util.*;


public class StructuralAnalysis {
    public enum RegionType {
        Sequential,
        SelfLoop,
        WhileLoop,
        NaturalLoop,
        IfThen,
        IfThenElse,
        Improper,
        Then,
        Else,
        IfHead,
        BasicBlock,
        DefVal
    }

    public int absRegionCtr = 0;
    public int postCtr;
   // public int postMax;

    public Map<Vertex, RegionType> structType;
    public Set <Vertex> structures;
    public Map <Vertex, Vertex> structOf;
    public Map <Vertex, Set <Vertex> > structVertices;
    public Set <Vertex> ctVertices;
    public Map <Vertex, Set <Vertex> > ctChildren;
    public List<Vertex> dfsPostOrder;

    public StructuralAnalysis() {
        structType = new HashMap<>();
        structures = new HashSet<>();
        structOf = new HashMap<>();
        structVertices = new HashMap<>();
        ctVertices = new HashSet<>();
        ctChildren = new HashMap<>();
    }

    public Set <Vertex> reachUnderSet(Graph g, DFS oldDFS, Vertex header) {
        System.out.println("reachUnderSet: header = " + header);
        Set <Vertex> ret = new HashSet<>();
        Set <Vertex> backEdgeNodes = new HashSet<>();
        for(Vertex tail : g.incoming.get(header)) {
            Edge e = new Edge(tail, header);
         //   System.out.println("reachUnderSet: e = " + e);
         //   System.out.println("edge type = " + oldDFS.edgeTypeMap);
            if(oldDFS.edgeTypeMap.get(e).equals(DFS.edgeType.BACK)) {
                backEdgeNodes.add(tail);
            }
        }
        System.out.println("reachUnderSet: backedgenodes: " + backEdgeNodes);

        Graph inverted = g.getInverted();
        for(Vertex ben : backEdgeNodes) {
            DFSDontVisitN dfsDontVisitN = new DFSDontVisitN(inverted, header);
            dfsDontVisitN.dfs(ben);
            ret.addAll(dfsDontVisitN.dfsOrder);
        }
        return ret;
    }

    public void structuralAnalysis(Graph g, Vertex start) {
        debug d = new debug("StructuralAnalysis.java", "structuralAnalysis()");
        DFSWithSpecifiedStart dfs = new DFSWithSpecifiedStart(g, start);
        dfs.dfs();
        dfsPostOrder = dfs.dfsPostOrder;
        System.out.println("structuralAnalysis: initial dfspostorder: " + dfsPostOrder);
        //postMax = dfsPostOrder.size() - 1;
        postCtr = 1;
        if(g.vertices.size() == 1) {
            Vertex ctRoot = g.vertices.iterator().next();
            structType.put(ctRoot, RegionType.BasicBlock);
            structures.add(ctRoot);
            ctChildren.put(ctRoot, new HashSet<>());
            return;
        }

        while(g.vertices.size() > 1 && postCtr < dfsPostOrder.size()) {
            Vertex currVer = dfsPostOrder.get(postCtr);
            System.out.println("structuralAnalysis: currVer = " + currVer);
            Set<Vertex> vertexSet = new LinkedHashSet<>();
            RegionType rtype = acyclicRegionType(g, currVer, vertexSet);
            System.out.println();
            if(rtype != null) {
                Vertex reduced = reduce(g, rtype, vertexSet, dfs);
                if(vertexSet.contains(start)) {
                    start = reduced;
                }
            }

            else {
                System.out.println("structuralAnalysis: root = " + currVer);
                Set <Vertex> reachUnder = new HashSet<>();
                reachUnder.add(currVer);
                reachUnder.addAll(reachUnderSet(g, dfs, currVer));
                System.out.println("structuralAnalysis: root: " + currVer);
                System.out.println("structuralAnalysis: reachUnderSet: " + reachUnder);
                RegionType rCyclictype = cyclicRegionType(g, currVer, reachUnder);
                if(rCyclictype != null) {
                    Vertex reduced = reduce(g, rCyclictype, reachUnder, dfs);
                    if(reachUnder.contains(start)) {
                        start = reduced;
                    }
                }
                else {
                    //structType.put(currVer, RegionType.BasicBlock);
                    postCtr++;
                }

            }
        }
        d.dg("structType: " + structType);
    }

    public RegionType acyclicRegionType(Graph g, Vertex root, Set <Vertex> vset) {
        Vertex currVer = root;
        boolean currVerHasOnePred = true;
        boolean currVerHasOneSucc = g.numSucc(root) == 1;

        System.out.println("acyclicRegionType: going down phase");
        List <Vertex> goingDown = new ArrayList<>();
        while(currVerHasOnePred && currVerHasOneSucc) {
            System.out.println("currVer = " + currVer);
            goingDown.add(currVer);
            if(g.numSucc(currVer) > 0)
                currVer = g.adj.get(currVer).get(0);
            currVerHasOneSucc = g.numSucc(currVer) == 1;
            currVerHasOnePred = g.numPred(currVer) == 1;
        }

        if(currVerHasOnePred) {
            goingDown.add(currVer);
        }
        currVer = root;
        currVerHasOnePred = g.numPred(currVer) == 1;
        currVerHasOneSucc = true;

        Deque <Vertex> goingUpReverseOrdered = new ArrayDeque<>();
        System.out.println("acyclicRegionType: going up phase");
        while(currVerHasOnePred && currVerHasOneSucc) {
            goingUpReverseOrdered.addFirst(currVer);
            if(g.numPred(currVer) > 0)
                currVer = g.incoming.get(currVer).get(0);
            currVerHasOneSucc = g.numSucc(currVer) == 1;
            currVerHasOnePred = g.numPred(currVer) == 1;
        }

        if(currVerHasOneSucc) {
            goingUpReverseOrdered.addFirst(currVer);
        }
        for(Vertex v : goingUpReverseOrdered) {
            vset.add(v);
        }
        for(Vertex v : goingDown) {
            vset.add(v);
        }

        System.out.println("acyclicRegionType: computed vset = " + vset);

        if(vset.size() >= 2) {
            System.out.println("ayclicRegionType: ret = Sequential");
            return RegionType.Sequential;
        }

        else if(g.numSucc(root) == 2) {
            Vertex succ1 = g.adj.get(root).get(0);
            Vertex succ2 = g.adj.get(root).get(1);
            Set <Vertex> succsOfSucc1 = new HashSet<>(g.adj.get(succ1));
            Set <Vertex> succsOfSucc2 = new HashSet<>(g.adj.get(succ2));

            if(succ1.equals(root) == false && succ2.equals(root) == false && g.adj.get(succ1).contains(succ2)) {
                vset.clear();
                vset.add(root); //TODO: extend struct of to tag type of leaf, here cond
                vset.add(succ1);
                structType.put(succ1, RegionType.Then);
                return RegionType.IfThen;
            }
            else if(succ1.equals(root) == false && succ2.equals(root) == false && g.adj.get(succ2).contains(succ1)) {
                vset.clear();
                vset.add(root);
                vset.add(succ2);
                structType.put(succ2, RegionType.Then);
                return RegionType.IfThen;
            }

            else if(succsOfSucc1.equals(succsOfSucc2) && succsOfSucc1.size() == 1 && g.numPred(succ1) == 1 && g.numPred(succ2) == 1) {
                vset.clear();
                vset.add(root);
                vset.add(succ1);
                vset.add(succ2);
                System.out.println("ayclicRegionType: ret = IfThenElse");
                return RegionType.IfThenElse;
            }
        }
        System.out.println("ayclicRegionType: ret = null");
        return null;
    }

    public RegionType cyclicRegionType(Graph g, Vertex root, Set <Vertex> reachUnder) {
        System.out.println("cyclicRegionType: root = " + root);
        System.out.println("cyclicRegionType: reachUnder = " + reachUnder);
        if(reachUnder.size() == 1) {
            if(g.edges.contains(new Edge(root, root))) {
                return RegionType.SelfLoop;
            }
            else {
                return null;
            }
        }
        DFSWithSpecifiedStart dfsWithSpecifiedStart = new DFSWithSpecifiedStart(g, root);
        dfsWithSpecifiedStart.dfs();
        Set <Vertex> verticesReachableFromRoot = new HashSet<>(dfsWithSpecifiedStart.dfsOrder);

        //check for jumps inside body from outside of header
        for(Vertex v : reachUnder) {
            if(verticesReachableFromRoot.contains(v) == false) {
                //TODO: minimizeImproper
                return RegionType.Improper;
            }
        }

        Vertex[] reachUnderVer = reachUnder.toArray(new Vertex[reachUnder.size()]);

        Vertex someBodyVer = reachUnderVer[0].equals(root) ? reachUnderVer[1] : reachUnderVer[0];

        if(g.numSucc(root) == 2 && g.numSucc(someBodyVer) == 1 && g.numPred(root) == 2 && g.numPred(someBodyVer) == 1) {
            return RegionType.WhileLoop;
        }
        else return RegionType.NaturalLoop;
    }

    public Vertex reduce(Graph g, RegionType rtype, Set <Vertex> vset, DFS dfs) {
        debug d = new debug("StructuralAnalysis.java", "reduce()");
        Vertex newAbsRegionVertex = new Vertex("R_" + rtype.name() + absRegionCtr++);
        d.dg("newAbsRegionVertex: " + newAbsRegionVertex);
        replace(g, newAbsRegionVertex, vset, dfs);
        d.dg("structType before: " + structType);
        structType.put(newAbsRegionVertex, rtype);
        d.dg("structType after: " + structType);
        structures.add(newAbsRegionVertex);
        for(Vertex v : vset) {
            structOf.put(v, newAbsRegionVertex);
        }
        structVertices.put(newAbsRegionVertex, vset);
        return newAbsRegionVertex;
    }

    public void replace(Graph g, Vertex ctVertex, Set <Vertex> vset, DFS dfs) {
        Set <Vertex> outGoingSet = new HashSet<>();
        Set <Vertex> incomingSet = new HashSet<>();
        Map <Vertex, DFS.edgeType> outType = new HashMap<>();
        Map <Vertex, DFS.edgeType> inType = new HashMap<>();

        for(Vertex v : vset) {
            for(Vertex o : g.adj.get(v)) {
                if(vset.contains(o) == false) {
                    outGoingSet.add(o);
                    outType.put(o, dfs.edgeTypeMap.get(new Edge(v,o)));
                }
            }
            for(Vertex i : g.incoming.get(v)) {
                if(vset.contains(i) == false) {
                    incomingSet.add(i);
                    inType.put(i, dfs.edgeTypeMap.get(new Edge(i, v)));
                }
            }
        }
        compact(g, ctVertex, vset);
        for(Vertex out : outGoingSet) {
            Edge e = new Edge(ctVertex, out);
            dfs.edgeTypeMap.put(e, outType.get(out));
            g.addEdge(e);

        }
        for(Vertex in : incomingSet) {
            Edge e = new Edge(in, ctVertex);
            dfs.edgeTypeMap.put(e, inType.get(in));
            g.addEdge(e);
        }

        ctVertices.add(ctVertex);
        ctChildren.put(ctVertex, vset);
    }

    public void compact(Graph g, Vertex ctVertex, Set <Vertex> vset) {
        System.out.println("compact: vset = " + vset);
        System.out.println("compact: g.vertices before: " + g.vertices);
        System.out.println("compact: postCtr before: " + postCtr);
        System.out.println("compact: dfsPostOrder before: " + dfsPostOrder);

        g.removeVertices(vset);
        g.addVertex(ctVertex);
        int hpos = -1;
        for(int i = 0; i < dfsPostOrder.size(); i++) {
            Vertex vi = dfsPostOrder.get(i);
            if(vset.contains(vi)) {
                hpos = i;
            }
        }
        dfsPostOrder.add(hpos + 1, ctVertex);
        dfsPostOrder.removeAll(vset);
        //postMax = dfsPostOrder.size() - 1;
        for(int i = 0; i < dfsPostOrder.size(); i++) {
            Vertex vi = dfsPostOrder.get(i);
            if(vi.equals(ctVertex)) {
                postCtr = i;
                break;
            }
        }
        System.out.println("compact: g.vertices after: " + g.vertices);
        System.out.println("compact: postCtr after: " + postCtr);
        System.out.println("compact: dfsPostOrder after: " + dfsPostOrder);
    }

    public Vertex controlTreeRoot() {
        Map<Vertex, Set<Vertex>> controlTree = ctChildren;

        System.out.println("control tree = " + controlTree);

        Map<Vertex, Vertex> parentMap = new HashMap<>();
        for(Vertex root : controlTree.keySet()) {
            for(Vertex child : controlTree.get(root)) {
                parentMap.put(child, root);
            }
        }
        System.out.println("parent map = " + parentMap);
        Vertex treeRoot = null;
        for(Vertex v : controlTree.keySet()) {
            if(parentMap.containsKey(v) == false) {
                treeRoot = v;
                System.out.println("found root = " + treeRoot);
                break;
            }
        }
        return treeRoot;
    }

    public String controlTreeString() {
        Map<Vertex, Set<Vertex>> controlTree = ctChildren;
        Vertex treeRoot = controlTreeRoot();
        TreeNode tnroot = new ArrayMultiTreeNode(treeRoot.dat);
        Queue <TreeNode> q = new LinkedList();
        q.add(tnroot);
        while(q.isEmpty() == false) {
            // System.out.println("q not empty and is = " + q);
            TreeNode cur = q.poll();
            String strv = cur.data().toString();
            //System.out.println("strv = " + strv);
            Vertex curV = new Vertex(strv);
            //System.out.println("curv = " + curV);
            for(Vertex child : controlTree.getOrDefault(curV, new HashSet<>())) {
                TreeNode childTN = new ArrayMultiTreeNode(child.dat);
                q.add(childTN);
                cur.add(childTN);
            }
        }
        if(tnroot != null) {
            return tnroot.toString();
        }
        return null;
    }


}
