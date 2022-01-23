/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import exceptions.HQLTranslationException;

/**
 * Created by K. Venkatesh Emani on 12/16/2016.
 * All nodes which can be translated to Hibernate Query Language should implement this interface.
 */
public interface HQLTranslatable {
    /* Translation to Hibernate Query Language */
    String toHibQuery() throws HQLTranslationException;
}
