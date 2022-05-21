/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Not part of base Dbridge
package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.JoinOp;
import exceptions.HQLTranslationException;
import jdk.nashorn.internal.ir.EmptyNode;

public class JoinNode extends Node implements HQLTranslatable{

    public String fieldType="";

    public JoinNode(Node left, Node right,Node condition) {

        super(new JoinOp(), left, right,condition);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        String left = ((HQLTranslatable) children[0]).toHibQuery();
        String right = ((HQLTranslatable) children[1]).toHibQuery();
        return "(Join " + left + " " + right + ")";
    }
}
