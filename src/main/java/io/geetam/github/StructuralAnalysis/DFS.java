/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
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
