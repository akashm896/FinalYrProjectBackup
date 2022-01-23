/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.Operator;

import java.util.HashSet;

/**
 * Created by ek on 28/10/16.
 * Common constructor statements for all Leaf derivatives
 * (Note: Leaf derivatives do not use the constructor from Node)
 */
public class LeafNode extends Node implements Leaf {
    public LeafNode(Operator op) {
        children = null;
        operator = op;
    }
}
