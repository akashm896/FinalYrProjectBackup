/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.LazyFetchOp;

/**
 * Created by venkatesh on 9/7/17.
 * LazyFetchNode is pretty similar to FieldRefNode. It may make
 * sense for LazyFetchNode to extend FieldRefNode. Keeping it
 * separate for now.
 */
public class LazyFetchNode extends Node {

    public LazyFetchNode(FieldRefNode frn) {
        super(new LazyFetchOp(), frn);
    }

}
