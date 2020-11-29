package io.geetam.github.StructuralAnalysis;


import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws IOException {
        Graph g = new Graph();
        String inpFile = "input/graph.txt";
        Path inpPath = Paths.get(inpFile);
        System.out.println(inpPath.toAbsolutePath());
        Vertex entry = null;
        List<String> lines = Files.readAllLines(inpPath);
        for(String li : lines) {
            String[] dats = li.split(" ");
            Vertex v1 = new Vertex(dats[0]);
            Vertex v2 = new Vertex(dats[1]);
            if(entry == null) {
                if (v1.dat.equals("entry"))
                    entry = v1;
                if (v2.dat.equals("entry"))
                    entry = v2;
            }
            g.addEdge(v1, v2);
        }
        System.out.println("graph = " + g);
        System.out.println("g.adj = " + g.adj);
        System.out.println("g.incoming = " + g.incoming);
        System.out.println("entry = " + entry);

        StructuralAnalysis sa = new StructuralAnalysis();
        sa.structuralAnalysis(g, entry);
        Map<Vertex, Set<Vertex>> controlTree = sa.ctChildren;

        System.out.println("control tree = " + controlTree);
        System.out.println("g.vertices = " + g.vertices);

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
            System.out.println("control tree = " + tnroot.toString());
        }
    }
}
