package io.geetam.github.loopHandler;

import dbridge.analysis.eqsql.expr.DIR;

public class DAGTillNow {
    public static DIR dag;
    DAGTillNow dagTillNow;

    public DAGTillNow() {
    }

    public static DIR getDag() {
        return dag;
    }

//    public static void setDag(DIR dag) {
//        this.dag = dag;
//    }
}
