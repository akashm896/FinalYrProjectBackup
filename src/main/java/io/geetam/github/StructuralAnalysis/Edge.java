package io.geetam.github.StructuralAnalysis;

import java.util.Objects;

public class Edge {
    public Vertex tail;
    public Vertex head;

    public Edge(Vertex tail, Vertex head) {
        this.tail = tail;
        this.head = head;
    }

    @Override
    public boolean equals(Object s) {
        if(s instanceof Edge == false) {
            return false;
        }

        Edge otherEdge = (Edge) s;
        return otherEdge.head.equals(head) && otherEdge.tail.equals(tail);
    }

    @Override
    public String toString() {
        return tail.toString() + " --> " + head.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tail, head);
    }
}
