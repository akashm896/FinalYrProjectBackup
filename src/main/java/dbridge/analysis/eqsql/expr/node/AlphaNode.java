package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.AlphaOp;
import dbridge.analysis.eqsql.expr.operator.ListOp;
import dbridge.analysis.eqsql.expr.operator.ProjectOp;

import java.util.ArrayList;
import java.util.List;

public class AlphaNode extends Node{

    public ClassRefNode table;
    public AlphaNode(ClassRefNode tableName) {
        super(new AlphaOp());
        this.table = tableName;

    }
}
