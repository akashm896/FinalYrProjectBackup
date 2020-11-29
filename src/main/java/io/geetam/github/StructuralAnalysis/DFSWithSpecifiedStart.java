package io.geetam.github.StructuralAnalysis;

public class DFSWithSpecifiedStart extends DFS {


    private final Vertex start;

    public DFSWithSpecifiedStart(Graph g, Vertex start) {
        super(g);
        this.start = start;
    }

    @Override
    public void dfs() {
        for(Vertex v : gr.getVertices()) {
            colorMap.put(v, color.WHITE);
        }
        dfsVisit(gr, start);
    }
}
