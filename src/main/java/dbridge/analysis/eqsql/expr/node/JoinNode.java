//Not part of base Dbridge
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.JoinOp;
import exceptions.HQLTranslationException;

public class JoinNode extends Node implements HQLTranslatable{

    public JoinNode(Node left, Node right) {
        super(new JoinOp(), left, right);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String left = ((HQLTranslatable) children[0]).toHibQuery();
        String right = ((HQLTranslatable) children[1]).toHibQuery();
        return "(Join " + left + " " + right + ")";
    }
}
