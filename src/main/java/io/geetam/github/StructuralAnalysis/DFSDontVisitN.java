package io.geetam.github.StructuralAnalysis;

public class DFSDontVisitN extends DFS {
    public Vertex dontVisit;

    public DFSDontVisitN(Graph g, Vertex N) {
        super(g);
        this.dontVisit = N;
    }

    //assuming num connected components = 1
    public void dfs(Vertex start) {
        for(Vertex v : gr.getVertices()) {
            colorMap.put(v, color.WHITE);
        }

        if(start.equals(dontVisit) == false)
            dfsVisit(gr, start);
    }

    @Override
    public void dfsVisit(Graph gr, Vertex start) {
        dfsNumMap.put(start, dfsNum++);
        dfsOrder.add(start);
        colorMap.put(start, color.GRAY);
        for(Vertex v : gr.getAdjacent(start)) {
            if(v.equals(dontVisit)) {
                continue;
            }

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
