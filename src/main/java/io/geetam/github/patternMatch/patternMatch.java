package io.geetam.github.patternMatch;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import dbridge.analysis.eqsql.expr.node.AddWithFieldExprsNode;
import dbridge.analysis.eqsql.expr.node.FoldNode;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.operator.AddWithFieldExprsOp;
import dbridge.analysis.eqsql.expr.operator.FoldOp;
import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.trans.InputTree;
import dbridge.analysis.eqsql.trans.OutputTree;
import dbridge.analysis.eqsql.trans.Rule;
import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                    placeHolderIDMap.put(ithExpStr, idcount + 1);
                }

                if(strToNodeClassMap.containsKey(parsedExp.get(0))) {
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


    public static void main(String[] args) throws IOException, SexpParserException {
        strToNodeClassMap.put("fold", OpType.Fold);
        strToNodeClassMap.put("add_all_fields", OpType.AddWithFieldExprs);
        strToNodeClassMap.put("pi", OpType.Project);

        List<String> lines = Files.readAllLines(Paths.get(inpFile));
//        Sexp eg = SexpFactory.parse("(add expr_all_fields)");
//        TreeNode r = getTreeForParsedExp(eg);
//        System.out.println(r);

        for(int i = 0; i < lines.size(); i = i + 2) {
            Sexp ruleInput = SexpFactory.parse(new StringReader(lines.get(i)));
            InputTree inputTree = getInputPattern(ruleInput);

            Sexp ruleOutput = SexpFactory.parse(new StringReader(lines.get(i+1)));
            OutputTree outputTree = getOutputPattern(ruleOutput);

            System.out.println(inputTree);
            System.out.println();
            System.out.println();
            System.out.println(outputTree);

            System.out.println();
            System.out.println(placeHolderIDMap);
        }
    }
}
