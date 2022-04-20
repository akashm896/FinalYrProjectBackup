/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.operator;

/**
 * Created by ek on 23/5/16.
 */
public class Operator {
    protected String name;
    protected OpType type;
    protected int arity;

    /**
     * Package local constructor. For use within OpType enum
     */
    Operator(){};

    public Operator(String name, OpType type, int arity) {
        this.name = name;
        this.type = type;
        this.arity = arity;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){ this.name=name;}

    public OpType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operator operator = (Operator) o;

        return arity == operator.arity && type == operator.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + arity;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getArity() {
        return arity;
    }

    public void print(){
        System.out.println(toString());
    }
}
