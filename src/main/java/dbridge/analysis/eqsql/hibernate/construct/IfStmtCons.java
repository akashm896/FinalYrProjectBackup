/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.*;
import exceptions.UnknownStatementException;
import jas.Var;
import mytest.debug;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;
import soot.jimple.internal.*;

/**
 * Created by ek on 18/5/16.
 */
public class IfStmtCons implements StmtDIRConstructor {

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        debug d = new debug("IfStmtCons.java", "construct()");
        assert (stmt instanceof IfStmt);
        IfStmt ifStmt = (IfStmt)stmt;
        Value condition = ifStmt.getCondition();
        d.dg("condition (got from sootstmt.getCondition()" + condition);
        Node condNode = null;
        try {
            if (condition instanceof JEqExpr) {

                JEqExpr eqExpr = (JEqExpr) condition;
                Value op1 = eqExpr.getOp1();
                Value op2 = eqExpr.getOp2();

                condNode = new EqNode(NodeFactory.constructFromValue(op1), NodeFactory.constructFromValue(op2));//true and false conditions are inverted for our regions. Probably because jimple inverts the condition. This is functionally correct but a bit convoluted. //TODO fix.
                return new StmtInfo(VarNode.getACondVar(), condNode);
            } else if (condition instanceof JNeExpr) {
                JNeExpr neExpr = (JNeExpr) condition;
                Value op1 = neExpr.getOp1();
                Value op2 = neExpr.getOp2();

                condNode = new NotEqNode(NodeFactory.constructFromValue(op1), NodeFactory.constructFromValue(op2));
                //true and false conditions are inverted for our regions. Probably because jimple inverts the condition. This is functionally correct but a bit convoluted. //TODO fix.

                return new StmtInfo(VarNode.getACondVar(), condNode);
            } else if (condition instanceof JLeExpr) {
                JLeExpr leExpr = (JLeExpr) condition;
                Value op1 = leExpr.getOp1();
                Value op2 = leExpr.getOp2();
                condNode = new LessThanEqNode(NodeFactory.constructFromValue(op1), NodeFactory.constructFromValue(op2));
                return new StmtInfo(VarNode.getACondVar(), condNode);
            }
            else if(condition instanceof JGeExpr) {
                JGeExpr greq = (JGeExpr) condition;
                Value op1 = greq.getOp1();
                Value op2 = greq.getOp2();
                condNode =  new LtNode(NodeFactory.constructFromValue(op1), NodeFactory.constructFromValue(op2));
                return new StmtInfo(VarNode.getACondVar(), condNode);
            }

            else {
                condNode = new UnknownNode();
                return new StmtInfo(VarNode.getACondVar(), condNode);
                //throw new RuntimeException("condition soot value: " + condition + " not supported");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return StmtInfo.nullInfo;
    }
}
