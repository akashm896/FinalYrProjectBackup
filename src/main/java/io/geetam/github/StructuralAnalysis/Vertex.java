package io.geetam.github.StructuralAnalysis;

import java.util.Objects;

public class Vertex {
    public String dat;

    public Vertex(String dat) {
        this.dat = dat;
    }

    public Vertex(Object dat) {
        this.dat = dat.toString();
    }


    @Override
    public boolean equals(Object s) {
        if(s instanceof Vertex == false) {
            return false;
        }
        Vertex otherVertex = (Vertex) s;
        String otherDat = otherVertex.dat;
        return otherDat.equals(dat);
    }

    @Override
    public String toString() {
        return dat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dat);
    }
}
