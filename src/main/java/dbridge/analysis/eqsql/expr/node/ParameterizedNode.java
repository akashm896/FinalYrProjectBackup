/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

/**
 * Created by ek on 6/11/16.
 */
interface ParameterizedNode {
    /**
     * Find the placeholders from <code>node</code> and store them. This function is intended to be called from the
     * constructor that builds the object from a given <code>node</code> without using explicit placeholder information.
     * @param node A parameterized node
     */
    void findPlaceholders(Node node);
}
