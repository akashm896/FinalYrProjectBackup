package com.geetam.hqlparser;

import dbridge.analysis.eqsql.expr.node.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.hibernate.hql.ast.origin.hql.parse.HQLLexer;

public class CommonTreeWalk {
    public static String tableName;
    public static String tableAlias;
    public static String conditionOp;
    public static String conditionLeftOperand;
    public static String conditionRightOperand;
    public static String selectedExpr;

    enum State {
        START,
        SELECT_ITEM,
        SELECT_ITEM_PATH,
        FROM,
        FROM_ENTITY_PERSISTER_REF,
        WHERE
    };
    public static State state = State.START;

    public static void postOrder(Tree tree, int depth) {
        State oldState = state; //to restore at end
        stateTransition(tree);
        int numChild = tree.getChildCount();
        for(int i = 0; i < numChild; i++) {
            postOrder(tree.getChild(i), depth + 1);
        }
        System.out.print("|");
        for(int i = 0; i < depth; i++) {
            System.out.print("  |");
        }
        if(tree.getType() == HQLLexer.SELECT_FROM) {
            Tree select = tree.getChild(0);
            assert select.getType() == HQLLexer.SELECT;

        }

        if(state == State.FROM && tree.getType() == HQLLexer.ENTITY_PERSISTER_REF) {
            Tree table = tree.getChild(0);
            Tree alias = tree.getChild(1);
            tableName = table.getText();
            tableAlias = alias.getText();
        } else if(state == State.SELECT_ITEM && tree.getType() == HQLLexer.PATH) {
            Tree selectedExprNode = tree.getChild(0);
            selectedExpr = selectedExprNode.getText();
        } else if(tree.getType() == HQLLexer.WHERE) {
            Tree condition = tree.getChild(0);
            conditionOp = condition.getText();
            Tree opr1 = condition.getChild(0);
            Tree opr2 = condition.getChild(1);
            conditionLeftOperand = opr1.toStringTree();
            conditionRightOperand = opr2.toStringTree();
        }
        System.out.println(tree.getText());
        state = oldState;
    }

    public static void stateTransition(Tree nextNode) {
        if(nextNode.getType() == HQLLexer.WHERE) {
            state = State.WHERE;
        } else if(nextNode.getType() == HQLLexer.SELECT_ITEM) {
            state = State.SELECT_ITEM;
        }
        else if(nextNode.getType() == HQLLexer.FROM) {
            state = State.FROM;
        }

//        else if(state == State.SELECT_ITEM
//                && nextNode.getType() == HQLLexer.PATH) {
//            state = State.SELECT_ITEM_PATH;
//        }

//         else if(state == State.FROM && nextNode.getType() == HQLLexer.ENTITY_PERSISTER_REF) {
//            state = State.FROM_ENTITY_PERSISTER_REF;
//        }

    }

    public static Tree getSelectFromNode(Tree tree, int depth) {
        int numChild = tree.getChildCount();
        for(int i = 0; i < numChild; i++) {
            return getSelectFromNode(tree.getChild(i), depth + 1);
        }

        if(tree.getType() == HQLLexer.SELECT_FROM) {
            return tree;
        }
        return null;
    }

    public static void printInfo() {
        System.out.println("tableName = " + tableName);
        System.out.println("tableAlias = " + tableAlias);
        System.out.println("conditionOp = " + conditionOp);
        System.out.println("conditionLeftOperand = " + conditionLeftOperand);
        System.out.println("conditionRightOperand = " + conditionRightOperand);
        System.out.println("selectedExpr = " + selectedExpr);
    }

    public static SelectNode getRelNode() {
        Boolean isCartesianProd = false;
        if(selectedExpr.equals(tableAlias)) {
            isCartesianProd = true;
        }
        EqNode eqNode = null;
        if(conditionOp.equals("=")) {
            VarNode leftOp = new VarNode(conditionLeftOperand);
            VarNode rightOp = new VarNode(conditionRightOperand);
            eqNode = new EqNode(leftOp, rightOp);
        }

        CartesianProdNode cartProd = null;
        if(isCartesianProd) {
            cartProd = new CartesianProdNode(new ClassRefNode(tableName));
        }

        assert cartProd != null && eqNode != null : "At least one of cartProd and eqNode is null";
        SelectNode selectNode = new SelectNode(cartProd, eqNode);
        return selectNode;
    }
}
