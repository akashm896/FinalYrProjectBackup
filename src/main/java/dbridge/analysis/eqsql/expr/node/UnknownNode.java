//based on BottomNode
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.UnknownOp;

public class UnknownNode extends LeafNode {

    private static UnknownNode v;
    /**
     * Constructor does not use super() because it is a special kind of Node,
     * which has no children.
     */
    public UnknownNode() {
        super(new UnknownOp());
    }

    public static boolean isBottom(Object o){
        return (o != null)
            && (o instanceof UnknownNode);
    }

    public static UnknownNode v(){
        if(v == null){
            v = new UnknownNode();
        }
        return v;
    }

    @Override
    public String toString() {
        return "BottomNode";
    }
}
