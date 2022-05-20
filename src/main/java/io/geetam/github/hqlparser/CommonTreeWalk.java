/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
package io.geetam.github.hqlparser;

import dbridge.analysis.eqsql.expr.node.*;
import mytest.debug;
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
    public static String leftJoinedPath;
    public static String leftJoinedField;

    enum State {
        START,
        SELECT_ITEM,
        SELECT_ITEM_PATH,
        FROM,
        FROM_ENTITY_PERSISTER_REF,
        LEFT,
        LEFT_JOIN,
        WHERE
    };
    public static State state = State.START;

    public static void postOrder(Tree tree, int depth) {
        debug d = new debug("CommonTreeWalk.java", "postOrder()");
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
        //    assert select.getType() == HQLLexer.SELECT;

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
        } else if(state == State.FROM && tree.getType() == HQLLexer.PROPERTY_JOIN) {
            Tree left = tree.getChild(0);
            Tree fetch = tree.getChild(1);
            d.dg(left);
            d.dg(fetch);
            Tree accp = tree.getChild(3);
            Tree dot = accp.getChild(0);
            String accpstr = "";
            int dotccount =  dot.getChildCount();
            for(int i = 0; i < dotccount - 1; i++) {
                accpstr += dot.getChild(i) + ".";
            }
            accpstr += dot.getChild(dotccount - 1);
            leftJoinedPath = accpstr;
            leftJoinedField = dot.getChild(dotccount - 1).toString();
            d.dg(accpstr);
        }
        System.out.println(tree.getText());
        state = oldState;
    }

    public static void stateTransition(Tree nextNode) {
        int nextnodetype = nextNode.getType();
        System.out.println(nextnodetype);
        if(nextNode.getType() == HQLLexer.WHERE) {
            state = State.WHERE;
        } else if(nextNode.getType() == HQLLexer.SELECT_ITEM) {
            state = State.SELECT_ITEM;
        } else if(nextNode.getType() == HQLLexer.FROM) {
            state = State.FROM;
        }
//        } else if(state == State.LEFT && nextNode.getType() == HQLLexer.JOIN) {
//            state = State.LEFT_JOIN;
//        }


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
        debug d = new debug("CommonTreeWalk.java", "printInfo");
        d.dg("tableName = " + tableName);
        d.dg("tableAlias = " + tableAlias);
        d.dg("conditionOp = " + conditionOp);
        d.dg("conditionLeftOperand = " + conditionLeftOperand);
        d.dg("conditionRightOperand = " + conditionRightOperand);
        d.dg("selectedExpr = " + selectedExpr);
    }

    public static SelectNode getRelNode() {
        Boolean isCartesianProd = false;
        if(selectedExpr.equals(tableAlias)) {
            isCartesianProd = true;
        }
        Node conditionalNode = getConditionalNode();

        CartesianProdNode cartProd = null;
        if(isCartesianProd) {
            cartProd = new CartesianProdNode(new ClassRefNode(tableName));
        }

        assert cartProd != null && conditionalNode != null : "At least one of cartProd and conditional is null";
        SelectNode selectNode = new SelectNode(cartProd, conditionalNode);
        debug.dbg("check "+selectNode);
        return selectNode;
    }


    public static Node getConditionalNode() {
        Node leftOp = getOperandNode(conditionLeftOperand);
        Node rightOp = getOperandNode(conditionRightOperand);

        if(conditionOp.equals("=")) {
            return new EqNode(leftOp, rightOp);
        }
        else if(conditionOp.toLowerCase().equals("like")) {
            return new LikeNode(leftOp, rightOp);
        }

        assert false : "condition OP not supported";
        return null;
    }

    public static Node getOperandNode(String operand) {
        if(operand.startsWith("(PATH")) {
            String path = operand.substring(5);
            path = path.substring(2, path.indexOf(")"));
            String[] tokens = path.split(" ");
            String field = tokens[tokens.length - 1];
            StringBuilder classBld = new StringBuilder();
            for(int i = 1; i < tokens.length - 2; i++) {
                classBld.append(tokens[i] + ".");
            }
            classBld.append(tokens[tokens.length - 2]);
            return new FieldRefNode(classBld.toString(), field, classBld.toString());
        }
        else return new VarNode(operand);
    }
}
