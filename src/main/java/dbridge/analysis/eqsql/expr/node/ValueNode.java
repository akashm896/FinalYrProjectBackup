/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ValueOp;
import soot.Value;
import soot.jimple.NullConstant;

/**
 * Created by ek on 26/10/16.
 */
public class ValueNode extends LeafNode implements HQLTranslatable {

    /** Intentionally package local. This constructor can only be accessed indirectly
    through NodeFactory.constructFromValue() */
    ValueNode(Value val) {
        super(new ValueOp(val));
    }

    public Value getValue() {
        return ((ValueOp)operator).getValue();
    }

    @Override
    public String toString() {
        return ((ValueOp)operator).getValue().toString();
    }


    public boolean isNull(){
        return ((ValueOp)operator).getValue() instanceof NullConstant;
    }

    @Override
    public String toHibQuery() {
        return toString();
    }
}
