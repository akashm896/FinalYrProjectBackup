/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.hibernate.metadata;


public class TableAttribute {

    public String attributeName;
    public String attributeType;
    public String classAttrName;

    public TableAttribute(String attributeName, String attributeType,
                          String classAttrName) {
        super();
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.classAttrName = classAttrName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getClassAttrName() {
        return classAttrName;
    }

    public void setClassAttrName(String classAttrName) {
        this.classAttrName = classAttrName;
    }

    @Override
    public String toString() {

        return "ClassAttribute : " + this.classAttrName + " | TableAttribute : " + this.attributeName + " | Type : " + this.attributeType;
    }
}
