package io.geetam.github.StructuralAnalysis;/*
DFS From CLRS
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFS {
    public enum color {
      WHITE,
      GRAY,
      BLACK
    }

    public enum edgeType {
        TREE,
        BACK,
        FORWARD
    }
    public Graph gr;
    public Map<Vertex, color> colorMap;
    public Map<Edge, edgeType> edgeTypeMap;

    public List<Vertex> dfsOrder;
    public List <Vertex> dfsPostOrder;
    public Map <Vertex, Integer> dfsNumMap;

    int dfsNum = 0;

    public DFS(Graph g) {
        this.gr = g;
        colorMap = new HashMap<>();
        edgeTypeMap = new HashMap<>();
        dfsOrder = new ArrayList<>();
        dfsPostOrder = new ArrayList<>();
        dfsNumMap = new HashMap<>();
    }

    public void dfs() {
        for(Vertex v : gr.getVertices()) {
            colorMap.put(v, color.WHITE);
        }

        for(Vertex v : gr.getVertices()) {
            if(colorMap.get(v) == color.WHITE) {
                dfsVisit(gr, v);
            }
        }
    }

    public void dfsVisit(Graph gr, Vertex start) {
        System.out.println("dfsVisit: start = " + start);
        dfsNumMap.put(start, dfsNum++);
        dfsOrder.add(start);
        colorMap.put(start, color.GRAY);
        for(Vertex v : gr.getAdjacent(start)) {
            Edge e = new Edge(start, v);
            if(colorMap.get(v).equals(color.WHITE)) {
                edgeTypeMap.put(e, edgeType.TREE);
                dfsVisit(gr, v);
            }
            else if(colorMap.get(v).equals(color.GRAY)) {
                edgeTypeMap.put(e, edgeType.BACK);
            }
            else {
                edgeTypeMap.put(e, edgeType.FORWARD);
            }
        }
        dfsPostOrder.add(start);
        colorMap.put(start, color.BLACK);
    }
}
