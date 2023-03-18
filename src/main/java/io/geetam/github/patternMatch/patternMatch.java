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
package io.geetam.github.patternMatch;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;
import dbridge.analysis.eqsql.expr.operator.FoldOp;
import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.trans.InputTree;
import dbridge.analysis.eqsql.trans.OutputTree;
import dbridge.analysis.eqsql.trans.Rule;
import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;
import mytest.debug;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class patternMatch {
    static Map<String, OpType> strToNodeClassMap = new HashMap<>();
    static Map <String, Integer> placeHolderIDMap = new HashMap<>();

    static String inpFile = "inputs/patterns.txt";

    static int idcount = 0;

    public static boolean isPlaceHolder(String expr) {
        return expr.startsWith("<") && expr.endsWith(">");
    }

    public static InputTree getInputPattern(Sexp parsedExp) {
        InputTree root;
        String rootStr = parsedExp.get(0).toString();
        if(strToNodeClassMap.containsKey(rootStr)) {
            root = new InputTree(strToNodeClassMap.get(rootStr), idcount++);
        } else {
            root = new InputTree(OpType.Any, idcount++);
        }

        for(int i = 1; i < parsedExp.getLength(); i++) {
            Sexp ithExp = parsedExp.get(i);
            String ithExpStr = ithExp.toString();
            if(ithExp.isAtomic()) {
                if(isPlaceHolder(ithExpStr)) {
                    placeHolderIDMap.put(ithExpStr, idcount);
                }

                if(strToNodeClassMap.containsKey(ithExpStr)) {
                    root.addChild(new InputTree(strToNodeClassMap.get(ithExpStr), idcount++));
                } else {
                    root.addChild(new InputTree(OpType.Any, idcount++));
                }
            }
            else {
                root.addChild(getInputPattern(ithExp));
            }
        }
        return root;
    }

    public static OutputTree getOutputPattern(Sexp parsedExp) {
        OutputTree root;
        String rootStr = parsedExp.get(0).toString();
        if(strToNodeClassMap.containsKey(rootStr)) {
            root = new OutputTree(strToNodeClassMap.get(rootStr));
        } else {
            root = new OutputTree(OpType.Any);
        }

        for(int i = 1; i < parsedExp.getLength(); i++) {
            Sexp ithExp = parsedExp.get(i);
            String ithExpStr = ithExp.toString();
            if(ithExp.isAtomic()) {
                if(isPlaceHolder(ithExpStr)) {
                    int id = placeHolderIDMap.get(ithExpStr);
                    root.addChild(new OutputTree(id));
                }
                else if(strToNodeClassMap.containsKey(ithExpStr)){
                    OpType op = strToNodeClassMap.get(ithExpStr);
                    root.addChild(new OutputTree(op));
                }
                else {
                    root.addChild(new OutputTree(OpType.Any));
                }

            }
            else {
                root.addChild(getOutputPattern(parsedExp.get(i)));
            }
        }
        return root;
    }

    public static Collection<Rule> getUserInputRules() throws IOException, SexpParserException {
        debug d = new debug("patternMatch.java", "getUserInputRules");
        List <Rule> ret = new ArrayList<>();
        strToNodeClassMap.put("loop", OpType.Fold);
        strToNodeClassMap.put("add_all_fields", OpType.AddWithFieldExprs);
        strToNodeClassMap.put("pi", OpType.Project);
        strToNodeClassMap.put("list", OpType.List);
        strToNodeClassMap.put("0", OpType.Zero);
        strToNodeClassMap.put("=", OpType.Eq);
        strToNodeClassMap.put("union", OpType.Union);
        strToNodeClassMap.put("?", OpType.Ternary);
        strToNodeClassMap.put("select", OpType.Select);
        strToNodeClassMap.put("body_expr", OpType.FuncExpr);
        strToNodeClassMap.put("save", OpType.Save);
        strToNodeClassMap.put("next", OpType.Iterator);
        strToNodeClassMap.put("tuple", OpType.Tuple);
        strToNodeClassMap.put("join", OpType.Join);
        strToNodeClassMap.put("aref", OpType.ArrayRef);
        strToNodeClassMap.put("in", OpType.In);
        strToNodeClassMap.put("agg_sum", OpType.AggSum);
        strToNodeClassMap.put("+", OpType.ArithAdd);
        strToNodeClassMap.put("id", OpType.Id);
        strToNodeClassMap.put("-", OpType.RelMinus);
        strToNodeClassMap.put("Neq", OpType.NotEq);
        strToNodeClassMap.put("AND", OpType.And);
        strToNodeClassMap.put("OR", OpType.Or);
        strToNodeClassMap.put("more_than", OpType.MoreThan);



        List<String> lines = Files.readAllLines(Paths.get(inpFile));
        for (int i = 0; i < lines.size(); i = i + 2) {
            d.dg("i=" + i);
            Sexp ruleInput = SexpFactory.parse(new StringReader(lines.get(i)));
            d.dg("ruleInput: " + ruleInput);
            InputTree inputTree = getInputPattern(ruleInput);
            Sexp ruleOutput = SexpFactory.parse(new StringReader(lines.get(i + 1)));
            d.dg("rule output: " + ruleOutput);
            OutputTree outputTree = getOutputPattern(ruleOutput);
            System.out.println(inputTree);
            System.out.println();
            System.out.println();
            System.out.println(outputTree);
            System.out.println();
            System.out.println(placeHolderIDMap);
            Rule rule = new Rule(inputTree, outputTree);
            ret.add(rule);
        }
        return ret;
    }


    public static void main(String[] args) throws IOException, SexpParserException {

        List<String> lines = Files.readAllLines(Paths.get(inpFile));
//        Sexp eg = SexpFactory.parse("(add expr_all_fields)");
//        TreeNode r = getTreeForParsedExp(eg);
//        System.out.println(r);

//        for(int i = 0; i < lines.size(); i = i + 2) {
//            Sexp ruleInput = SexpFactory.parse(new StringReader(lines.get(i)));
//            InputTree inputTree = getInputPattern(ruleInput);
//
//            Sexp ruleOutput = SexpFactory.parse(new StringReader(lines.get(i+1)));
//            OutputTree outputTree = getOutputPattern(ruleOutput);
//
//            System.out.println(inputTree);
//            System.out.println();
//            System.out.println();
//            System.out.println(outputTree);
//
//            System.out.println();
//            System.out.println(placeHolderIDMap);
//
//            Rule rule = new Rule(inputTree, outputTree);
//            VarNode  collection = new VarNode("collection");
//            Node initVal = BottomNode.v();
//            VarNode f1 = new VarNode("f1");
//            VarNode f2 = new VarNode("f2");
//            ListNode fieldExprList = new ListNode(f1, f2);
//            AddWithFieldExprsNode add = new AddWithFieldExprsNode(fieldExprList);
//            Node foldNode = new FoldNode(add, initVal, collection);
//            System.out.println(rule);
//            System.out.println("input dag = " + foldNode);
//            Node transformed = foldNode.accept(rule);
//            System.out.println("transformed=" + transformed);
//        }
    }
}
