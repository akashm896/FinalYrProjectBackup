/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

//Not part of base DBridge

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.NonLibraryMethodOp;

public class NonLibraryMethodNode extends LeafNode implements HQLTranslatable {
    public NonLibraryMethodNode() {
        super(new NonLibraryMethodOp());
    }

    @Override
    public String toHibQuery() {
        return "NonLibraryMethodNode";
    }
}
