package io.geetam.github.tests;

import java.util.Collection;
import java.util.LinkedList;

public class NatLoop {
    public void foo() {
        Collection<Integer> collInt = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            collInt.add(i);
        }
        System.out.println(collInt);
    }
}
