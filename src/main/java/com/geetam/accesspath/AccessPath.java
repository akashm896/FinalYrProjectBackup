package com.geetam.accesspath;

import java.util.ArrayDeque;
import java.util.Deque;


public class AccessPath {
    private Deque<String> path;

    AccessPath() {
        path = new ArrayDeque<>();
    }

    public String getBase() {
        return path.peek();
    }

    public Deque<String> getPath() {
        return path;
    }

}
