/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

/**
 * Created by ek on 27/10/16.
 * Special methods are methods which are of interest to us with
 * respect to DIR creation and transformations. We define a separate class
 * to represent each special method. Other methods are represented using
 * simply a string such as their signature.
 */
public interface MethodRef extends Leaf {
}
