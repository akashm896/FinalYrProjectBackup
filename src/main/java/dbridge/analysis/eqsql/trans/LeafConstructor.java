/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.trans;

import dbridge.analysis.eqsql.expr.node.LeafNode;
import dbridge.analysis.eqsql.expr.operator.Operator;

/**
 * Created by venkatesh on 14/7/17.
 * Function wrapper to construct a leaf node using an operator.
 */
public interface LeafConstructor {
    LeafNode consLeaf(Operator op);
}
