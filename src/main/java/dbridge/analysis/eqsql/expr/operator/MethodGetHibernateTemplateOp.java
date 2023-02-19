/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by K. Venkatesh Emani on 1/6/2017.
 */
public class MethodGetHibernateTemplateOp extends Operator {
    public MethodGetHibernateTemplateOp() {
        super("GetHibernateTemplate()", OpType.MethodGetHibernateTemplate, 0);
        /* No children */
    }
}
