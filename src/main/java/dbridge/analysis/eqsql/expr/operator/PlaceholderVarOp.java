/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 5/11/16.
 */
public class PlaceholderVarOp extends Operator {

    private static int curId = 1;
    /** a unique id assigned to the place holder to distinguish it from placeholders
     * of other loops. */
    private int id;

    public PlaceholderVarOp() {
        super("PhVar", OpType.PlaceholderVar, 0);
        id = curId++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "<v" + id + ">";
    }
}
