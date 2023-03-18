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
package io.geetam.github.StructuralAnalysis;

import java.util.*;

public class Graph {
    public Set <Vertex> vertices;
    public Map<Vertex, List<Vertex>> adj;
    public Map <Vertex, List <Vertex> > incoming;
    public Set <Edge> edges;

    public Graph() {
        vertices = new HashSet<>();
        adj = new HashMap<>();
        incoming = new HashMap<>();
        edges = new HashSet<>();
    }

    public Set <Vertex> getVertices() {
        return vertices;
    }

    public List <Vertex> getAdjacent(Vertex v) {
        return adj.get(v);
    }

    public void addEdge(Vertex v1, Vertex v2) {
        addVertex(v1);
        addVertex(v2);
        edges.add(new Edge(v1, v2));
        adj.get(v1).add(v2);
        incoming.get(v2).add(v1);
    }

    public void addEdge(Edge e) {
        edges.add(e);
        Vertex v1 = e.tail;
        Vertex v2 = e.head;
        addVertex(v1);
        addVertex(v2);
        adj.get(v1).add(v2);
        incoming.get(v2).add(v1);
    }

    public void removeEdge(Vertex v1, Vertex v2) {
        edges.remove(new Edge(v1, v2));
        adj.get(v1).remove(v2);
        incoming.get(v2).remove(v1);
    }

    public void removeEdge(Edge e) {
        Vertex v1 = e.tail;
        Vertex v2 = e.head;
        System.out.println("removeEdge: v1 = " + v1);
        System.out.println("removeEdge: v2 = " + v2);
        System.out.println("removeEdge: adj = " + adj);
        System.out.println("removeEdge: incoming = " + incoming);
        edges.remove(e);
        adj.get(v1).remove(v2);
        incoming.get(v2).remove(v1);
    }

    public int numSucc(Vertex v) {
        return adj.get(v).size();
    }

    public int numPred(Vertex v) {
        return incoming.get(v).size();
    }

    public Graph getInverted() {
        Graph ret = new Graph();
        ret.vertices.addAll(vertices);
        for(Edge e : edges) {
            ret.addEdge(e.head, e.tail);
        }
        return ret;
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
        if(adj.containsKey(v) == false) {
            adj.put(v, new ArrayList<>());
        }
        if(incoming.containsKey(v) == false) {
            incoming.put(v, new ArrayList<>());
        }
    }

    public void removeVertex(Vertex v) {
        System.out.println("removeVertex: adj = " + adj);
        System.out.println("removeVertex: incoming = " + incoming);
        System.out.println("removeVertex: v = " + v);
        for(Vertex v2 : incoming.get(v)) {
            System.out.println("removeVertex: v2 = " + v2);
            adj.get(v2).remove(v);
            edges.remove(new Edge(v2, v));
        }
        for(Vertex v2 : adj.get(v)) {
            incoming.get(v2).remove(v);
        }
        vertices.remove(v);
        adj.remove(v);
        incoming.remove(v);
    }

    public void removeVertices(Set <Vertex> vset) {
        System.out.println("removeVertices: vset = " + vset);
        for(Vertex v : vset) {
            System.out.println("removeVertices: v = " + v);
            removeVertex(v);
        }
    }

    @Override
    public String toString() {
        return edges.toString();
    }

    public String toDOT() {
        String ret = "digraph {\n";
        for(Edge e : edges) {
            ret += e.tail.toString() + " -> " + e.head.toString() + "\n";
        }
        ret += "}\n";
        return ret;
    }

}
