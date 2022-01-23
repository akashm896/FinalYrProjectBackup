/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.DaoOp;

/**
 * Created by K. Venkatesh Emani on 1/6/2017.
 * Node that represents a Data Access Object reference (Data Access Objects
 * provide functions to retrieve and store data to/from the database).
 * In our implementation, we do not access any specific state of Dao objects,
 * so we can remove them from the DIR during simplifications. This node
 * facilitates that removal.
 */
public class DaoNode extends LeafNode {
    public DaoNode(){
        super(new DaoOp());
    }
}
