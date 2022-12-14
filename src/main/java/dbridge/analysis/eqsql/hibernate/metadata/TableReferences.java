/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.hibernate.metadata;

/*
 * TableReferences Stores information about the reference relations exist between tables of the database
 */


public class TableReferences {

    public String className;
    public String refType;              // Type can be one-to-many , many-to-many
    public String refTableA;            // Table reference from refClassA to refClassB
    public String refTableB;
    public String refTableAColumn;      // ClassA column used for the reference relation
    public String refTableBColumn;      // ClassB column used for the reference relation

    public TableReferences(String className, String refType, String refTableA,
                           String refTableB, String refTableAColumn, String refTableBColumn) {
        super();
        this.className = className;
        this.refType = refType;
        this.refTableA = refTableA;
        this.refTableB = refTableB;
        this.refTableAColumn = refTableAColumn;
        this.refTableBColumn = refTableBColumn;
    }

    public String getRefTableA() {
        return refTableA;
    }

    public String getRefTableB() {
        return refTableB;
    }

    public String getRefTableAColumn() {
        return refTableAColumn;
    }

    public String getRefTableBColumn() {
        return refTableBColumn;
    }

    @Override
    public String toString() {

        return "Class : " + this.className + " | Type : " + this.refType + " | " + this.refTableA + "(" + this.refTableAColumn + ") -> " + this.refTableB + "(" + this.refTableBColumn + ")";

    }


}
