/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.ListOp;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends Node {
    //Names of columns whose values are given by children. Not always meaningful.
    public List<FieldRefNode> columns;
    public ListNode(Node... children) {
        super(new ListOp(children.length), children);
        columns = new ArrayList<>();
    }
}
