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


}
