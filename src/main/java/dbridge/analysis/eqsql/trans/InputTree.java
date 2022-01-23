/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.trans;

import dbridge.analysis.eqsql.expr.operator.AnyOp;
import dbridge.analysis.eqsql.expr.operator.Operator;
import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.util.PrettyPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ek on 31/10/16.
 */
public class InputTree {
    private OpType opType;
    private int id;
    private List<InputTree> children;
    /**
     * null if there is no parent
     */
    private InputTree parent;
    private int numNodes;

    public InputTree(OpType _opType, int _id, InputTree... _children) {
        this.opType = _opType;
        this.id = _id;
        this.parent = null;
        numNodes = 1;

        if(_children != null) {
            children = new ArrayList<>(Arrays.asList(_children));
            for (InputTree child : _children) {
                child.setParent(this);
                numNodes += child.getNumNodes();
            }
        }
    }

    public InputTree(OpType opType, int id) {
        this(opType, id, null);
    }

    public OpType getOpType() {
        return opType;
    }

    public List<InputTree> getChildren() {
        return children;
    }

    public boolean hasChildren(){
        return children != null;
    }

    public int getNumChildren(){
        if (children == null){
            return 0;
        }
        return children.size();
    }

    public InputTree getChild(int i){
        if(hasChildren()){
            return children.get(i);
        }
        return null;
    }

    public void addChild(InputTree child) {
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public int getId() {
        return id;
    }

    public int getNumNodes(){
        return numNodes;
    }

    @Override
    public String toString() {
        return PrettyPrinter.makeTreeString(opType, children);
    }

    public void setParent(InputTree parent) {
        this.parent = parent;
    }

    public InputTree getParent() {
        return parent;
    }

    /**
     * @return true if this is an "Any" pattern (i.e., the opType is Any)
     */
    public boolean isAny(){
        return opType == OpType.Any;
    }

}
