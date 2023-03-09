
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
package com.iisc.pav;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.expr.operator.*;
import dbridge.analysis.eqsql.util.FuncResolver;
import io.geetam.github.CMDOptions;
import io.geetam.github.accesspath.NRA;
import mytest.debug;
import soot.jimple.internal.JLengthExpr;

import javax.persistence.criteria.Join;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.iisc.pav.AlloyGenerator.tableAndFields;
import static dbridge.analysis.eqsql.FuncStackAnalyzer.funcDIRMap;

//initCreationForm    - 16
//initUpdateOwnerForm - 13
//initUpdateForm      - 22
//home                - 25
//newPost             - 26
//registration        - 29

public class GenerateAlloySummary {
    Map<Node, ClassRefNode> varToRelation = new HashMap<>();
    Map<String, String> superType = new HashMap<>();
    static String nullNodeName = "NullNode";
    static String bottomNodeName = "BottomNode";
    Map<VarNode, Node> veMap;
    String attribute = "";

    ////// new members to handle NRAveMaps are below
    String joinClass1 = "", joinClass2 = "";
    String selClassParent = "", selClassChild = "";
    List<String> NRArelationList = new ArrayList<>();
//    List<String> oneFieldDataSigList  = new ArrayList<>();
    int NRAdepth = 0;
    String expandingField = "";
    //    String attribute = "post.user.email";
//    String prefix = "__modelattribute__";
//    String prefix = "this.commentServiceImpl.commentRepository";
//    String prefix = "this.userServiceImpl.userPaymentRepository";
    //    String prefix = "$r0.employeeRepository";
//    String prefix = "$r2.postRepository";
//    String prefix = "this.owners";
//    String prefix = "this.userPaymentRepository";
//    String prefix = "this.orderRepository";
//    String prefix = "this.postServiceImpl.postRepository";
//    String prefix = "this.employeeServiceImpl.employeeRepository";
//    String prefix = "$r0.postServiceImpl.postRepository";
//    String prefix = "this.voteServiceImpl.postServiceImpl.postRepository";
//    String prefix = "$r1.cartItemRepository";
//    String prefix = "this.commentServiceImpl.commentRepository";
//    String prefix = "userShippingListRepo";
//      String prefix = "$r0.postRepository";
//    String prefix = "this.orderRepository";
//    String prefix = "$r7.userShippingRepository";
//    String prefix = "this.commentServiceImpl.postServiceImpl.postRepository";

    String prefix = CMDOptions.repo == null ? "__modelattribute__" : CMDOptions.repo;
    Map<String,String> type = new HashMap<>();
    Set<String> literals = new HashSet<>();
    Set<String> variables = new HashSet<>();
    Map<String, Set<String>> tables = new HashMap<>();
    Map<Node, String> selects = new HashMap<>();
    Set<String> lazyGenerates = new HashSet<>();
    Map<Node,String> modelAttributes = new HashMap<>();
    boolean generateBoolean = false;
    private String BooleanName = "Boolean";
    private String BooleanTrueName = "True";
    private String BooleanFalseName = "False";
    private static final String oneNodeName = "1";
    private static final String zeroNodeName = "0";
    public FileWriter fileWriter;
    public PrintWriter printWriter;
    private static  int nextUniqueNum = 0;
    private static Map<Node,Integer> uniqueNumOf = new HashMap<>(); // added by @raghavan
    private  static Map<Node, Integer> uniqueMethodWontHandleCounterMap = new HashMap<>();
    public static int methodWontHandleCounter = 0;

    private static String sanitizeName(String name) {
        return  name
                .replace(' ','_')
                .replace('(','_')
                .replace(')','_')
                .replace('$','_')
                .replace('|','_')
                .replace('\n','_')
                .replace('.','_')
                .replace('?','_')
                .replace('"','_')
                .replace('[', '_')
                .replace(']', '_')
                .replace('<', '_')
                .replace('>', '_')
                .replace(':', '_')
                .replace('-','_');
    }
    public GenerateAlloySummary(Map<VarNode, Node> veMap) throws IOException {
        fileWriter = new FileWriter(CMDOptions.outfile != null ? CMDOptions.outfile : "outputs/Alloy/p52.als");
        printWriter = new PrintWriter(fileWriter);

        this.veMap = veMap;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        Object[] keySet = veMap.keySet().toArray();
        Arrays.sort(keySet);
        //for(VarNode node : veMap.keySet()) {
        Boolean keypresent = false;

        System.out.println("Tables and Fields :\n");
        for(String  sc:tableAndFields.keySet()){
           /*
           String sig = "";
            sig += "u_" + getShortName(sc);
            Set<String> tableFields = new HashSet<>();
            Map<String, String> fields = tableAndFields.get(sc); // getting all the fields of the table
            for(String k : fields.keySet()) { // iterating over all the fields
                if(!fields.get(k).equals("FieldData")) {
                    String dataType = "u_" + fields.get(k);
                    k = "u_" + k;
                    type.put(k, dataType);

                }
                tableFields.add(k);

            }
            tables.put(sig, tableFields);
            */
            System.out.println(getShortName(sc)+" fields = ");
            System.out.println(tableAndFields.get(sc)+"\n");
        }
        System.out.println(tables);
        System.out.println(type);

        for(Object itr : keySet) { // @raghavan added the sorting of the keys
            VarNode node = (VarNode) itr;

//            if (node.toString().equals("__modelattribute__"+attribute)) {
            if (node.toString().startsWith(prefix+attribute)) {
                keypresent = true;
                node = (VarNode) node.accept(new FuncResolver(funcDIRMap));
//                System.out.println("key: " + node);
                Node expression = veMap.get(node);
//                System.out.println("value: " + expression);
                if(isDbNode(node)) {
                    varToRelation.put(node, getRelationForVar(node));
                    //other logic
                    ClassRefNode superTable = new ClassRefNode("ts_"+node.toString());
                    superType.put(getUniqueName(node),getUniqueName(superTable));
                    superType.put(getUniqueName(getRelationForVar(node)),getUniqueName(superTable));
                    generate(null,superTable,new HashSet<String>(),new HashMap<String,String>());
                }
                String primary = generate(node,expression,new HashSet<String>(), new HashMap<String, String>());
                write("//%s : m%s",node,getUniqueName(node));
                modelAttributes.put(node,primary);
            }
        }
        generateCommons();
        //outgoing value = incoming value
        if (keypresent == false) {
            String clnname = sanitizeName(prefix);
            write("sig u_" + clnname + " in univ {}");
            write("sig mu_" + clnname + " in univ {}");
            write("fact { mu_" + clnname + " = u_" + clnname + "}");
        }

    }

    private boolean isDbNode(VarNode node) {
        return !(node.toString().startsWith("__model"));
    }

    public String generate(Node parent, Node node, Set<String> columns, Map<String, String> extras) {
        boolean isNRA = isNested(node, false);
//        boolean isFlatFindAll = isFlatFindAll(node, 0);
//        if(isFlatFindAll) {
//            StringBuilder sb = new StringBuilder();
//            String ret = processFlatFindAll(node, sb);
//            lazyGenerates.add(sb.toString());
//            return ret;
//        }
        if(isNRA){ // checks if there is Project as child of List which means NRA
//            return "";
            System.out.println(" Node contains nested fields");
            return generateNRA(parent, node, columns, extras);
//            StringBuilder sb = new StringBuilder();
//            generateNestedJoinSummary(node, new ArrayList<String>(), sb, 0);
//            lazyGenerates.add(sb.toString());
//            return "";
        }
        if(node instanceof ProjectNode) {
            Node relation = node.getChild(0);
            if(relation instanceof VarNode) relation = getRelationForVar((VarNode) relation);
            Node project = node.getChild(1);
//            ArrayList<Node> projectFields = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            if(project instanceof ListNode) {
                StringBuilder sb1 = new StringBuilder();
                for(int i=0; i<project.getNumChildren(); i++) {
                    Node child = project.getChild(i);
                    if(child instanceof FieldRefNode) {
                        String childName = generate(project, child, columns, extras);
                        sb1.append(String.format("p.%s = s.%s\n",childName,childName));
                    }
                    else {
                        Node fieldOfChild = ((ListNode) project).columns.get(i);
                        String fieldOfChildName = generate(project,fieldOfChild, columns, extras);
                        String childName = generate(project,child,columns,extras);
                        sb1.append(String.format("p.%s = s\n",fieldOfChildName));
                    }
                }
                sb.append("fact {\n");
                sb.append(String.format("all s:%s|some p:%s {\n",getUniqueName(relation),getUniqueName(node)));
//                sb.append(String.format("all p:%s,s:%s {\n",getUniqueName(node),getUniqueName(relation)));
                sb.append(sb1.toString());
                sb.append("}\n");
                sb.append(String.format("all p:%s|some s:%s {\n",getUniqueName(node),getUniqueName(relation)));
                sb.append(sb1.toString());
                sb.append("}}\n");
            }
            else {//assuming its var node
                columns.add(getUniqueName(project));
            }
            generate(node, relation, columns, extras);
            sb.append(String.format("sig %s in %s {",getUniqueName(node), getSuperType(getUniqueName(relation))));
            superType.put(getUniqueName(node),getUniqueName(relation));
            sb.append("}\n");
            lazyGenerates.add(sb.toString());
//            lazyGenerates.add(String.format("fact { %s = %s }",getUniqueName(node),getUniqueName(relation)));
            if(project instanceof ListNode)
                return getUniqueName(node);
            else {
                lazyGenerates.add(String.format("fact { %s = %s }",getUniqueName(node),getUniqueName(relation)));
                return String.format("%s.%s",getUniqueName(node),getUniqueName(project));
            }
//            projectNodes.put(project, relation);
//            return getUniqueName(node)+'.'+getUniqueName(relation);
        }
        else if(node instanceof SelectNode) {
            //String relation = generate(node,node.getChild(0),columns, extras);
            //String relation = getUniqueName(node.getChild(0));
            Node relation = node.getChild(0);
            if(relation instanceof VarNode) {
                relation = getRelationForVar(relation);
            }

            Node condition = node.getChild(1);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s {}\n", getUniqueName(node), getUniqueName(relation)));
            superType.put(getUniqueName(node),getUniqueName(relation));
            sb.append(String.format("pred meets_selection_criteria_of_%s[x: %s] {\n", getUniqueName(node),getUniqueName(relation)));
            //if(columns == null) columns = new HashSet<Node>();
            if(!condition.toString().equals("NullOp"))
                sb.append(generate(node,condition,columns, extras)+'\n');
            sb.append("}\n");
            sb.append(String.format("fact { all y:%s | meets_selection_criteria_of_%s[y] <=> y in %s }" ,getUniqueName(relation),getUniqueName(node),getUniqueName(node)));
            selects.put(node,sb.toString());
            generate(node,relation,columns, extras);
            return getUniqueName(node);
        }
        else if(node instanceof EqNode || node instanceof LikeNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if (left == right) return "1 = 1";
            else if(left instanceof ZeroNode && (isComparison(right))) {
                return generate(node,negation(right),columns, extras);
            }
            else if(right instanceof ZeroNode && (isComparison(left))) {
                return generate(node,negation(left),columns, extras);
            }
            else if(left instanceof OneNode && (isComparison(right))) {
                return generate(node,right,columns, extras);
            }
            else if(right instanceof OneNode && (isComparison(left))) {
                return generate(node,left,columns, extras);
            }
            else if(left instanceof FieldRefNode && !(right instanceof FieldRefNode)) {

                return String.format("x.%s = %s",generate(node,left,columns, extras), generate(node,right,columns, extras));
            }
            else if(right instanceof FieldRefNode && !(left instanceof FieldRefNode)) {
                return String.format("%s = x.%s", generate(node,left,columns, extras),generate(node,right,columns,extras));
            }
            else if(left instanceof FieldRefNode && right instanceof FieldRefNode) {
                throw new AlloyGenerationException("both colums case, not handled.");
            }
            else {
                return String.format("%s = %s", generate(node, node.getChild(0), columns, extras), generate(node, node.getChild(1), columns, extras));
            }
        } else if (node instanceof NotEqNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if (left == right) return "1 != 1";
            else if(left instanceof ZeroNode && (isComparison(right))) {
                return generate(node,(right),columns, extras);
            }
            else if(right instanceof ZeroNode && (isComparison(left))) {
                return generate(node,(left),columns, extras);
            }
            else if(left instanceof OneNode && (isComparison(right))) {
                return generate(node,negation(right),columns, extras);
            }
            else if(right instanceof OneNode && (isComparison(left))) {
                return generate(node,negation(left),columns, extras);
            }
            else if(left instanceof FieldRefNode && !(right instanceof FieldRefNode)) {

                return String.format("x.%s != %s",generate(node,left,columns,extras), generate(node,right,columns,extras));
            }
            else if(right instanceof FieldRefNode && !(left instanceof FieldRefNode)) {
                return String.format("%s != x.%s", generate(node,left,columns,extras),generate(node,right,columns,extras));
            }
            else if(left instanceof FieldRefNode && right instanceof FieldRefNode) {
                throw new AlloyGenerationException("both colums case, not handled.");
            }
            else {
                return String.format("%s != %s", generate(node, node.getChild(0), columns,extras), generate(node, node.getChild(1), columns,extras));
            }
        } else if (node instanceof CartesianProdNode) {
            if (node.getNumChildren() > 1) throw new AlloyGenerationException("Multiple children in Cartesian node.");
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s {}\n", getUniqueName(node), getUniqueName(node.getChild(0))));
            superType.put(getUniqueName(node),getUniqueName(node.getChild(0)));
            sb.append(String.format("fact { %s = %s }\n", getUniqueName(node), getUniqueName(node.getChild(0))));
            lazyGenerates.add(sb.toString());
            Node relation = node.getChild(0);
            if(relation instanceof  VarNode) {
                relation = getRelationForVar(relation);
            }
            return generate(parent,relation,columns, extras);

//            for (Node child : node.getChildren()) {
//                generate(node, child, columns);
//            }
//            return getUniqueName(node);
        } else if (node instanceof VarNode) {
            if(varToRelation.containsKey(node)) {
                return generate(parent, varToRelation.get(node), columns, extras);
            }
            else {
                variables.add(getUniqueName(node));
                return (getUniqueName(node));
            }
        }
        else if(node instanceof NullNode) {
            literals.add(nullNodeName);
            return nullNodeName;
        } else if(node instanceof ValueNode) {
            if(childShouldBeInt(parent)) {
                String nodeStr = node.toString();
                //  floats to ints
                if(nodeStr.contains(".")) {
                    nodeStr = nodeStr.substring(0, nodeStr.indexOf("."));
                }
                return nodeStr;
            }
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if (node instanceof ClassRefNode) {
            String tableSuper = getSuperType(getUniqueName(node));
            if (tables.containsKey(tableSuper)) {
                tables.get(tableSuper).addAll(columns);
            } else {
                tables.put(tableSuper, new HashSet<>(columns));
            }
            columns.clear();
            String tableName = getUniqueName(node);
            if (tables.containsKey(tableName)) {
                tables.get(tableName).addAll(columns);
            } else {
                tables.put(tableName, new HashSet<>(columns));
            }
            return getUniqueName(node);
        } else if (node instanceof FieldRefNode) {
            columns.add(getUniqueName(node));
            return String.format("%s", getUniqueName(node));
        } else if (node instanceof UnknownNode) {
            if(parent instanceof EqNode || parent instanceof NotEqNode) {
//                generateBoolean = true;
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format("one sig %s in %s {}\n", getUniqueName(node), BooleanName));
//                sb.append(String.format("one sig %s in FieldData {}\n", getUniqueName(node)));
                lazyGenerates.add(String.format("lone sig %s in univ {}",getUniqueName(node)));
//                lazyGenerates.add(sb.toString());
//                return String.format("%s = %s",getUniqueName(node), BooleanTrueName);
                return getUniqueName(node);
            }
            else if(extras.getOrDefault("RetVarNode","false").equals("true")){
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("lone sig %s in ControllerOrView {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
            }
            else if(childShouldBeInt(parent)) {
                variables.add(getUniqueName(node));
                type.put(getUniqueName(node),"Int");
                return getUniqueName(node);
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("lone sig %s in FieldData {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
            }
        }
        else if (node instanceof TernaryNode) {
            //todo
            Node condition = node.getChild(0);
            Node trueDag = node.getChild(1);
            Node falseDag = node.getChild(2);
            if(parent.toString().startsWith(prefix)) {
                //its root node, will need its own sig.
                lazyGenerates.add(String.format("sig %s in univ {}",getUniqueName(node)));
                String fd = generate(node,falseDag,columns, extras);
                lazyGenerates.add(String.format("fact { %s = ((%s) => (%s) else (%s)) }",getUniqueName(node), generate(node,condition,columns,extras), generate(node,trueDag,columns,extras), fd));
                return getUniqueName(node);
            }
            if(condition instanceof UnknownNode) {
                String conditionStr = generate(node,condition,columns,extras);
                String trueDagStr = generate(node,trueDag,columns,extras);
                String falseDagStr = generate(node,falseDag,columns,extras);
                Node uv = new VarNode("UnknownVar"+node.hashCode());
                String uvn = generate(node,uv,columns,extras);
                return String.format("((%s = %s) => (%s) else (%s))", conditionStr,uvn , trueDagStr, falseDagStr);
            }
            return String.format("((%s) => (%s) else (%s))", generate(node,condition,columns,extras), generate(node,trueDag,columns,extras), generate(node,falseDag,columns,extras));

        } else if (node instanceof BottomNode) {
            lazyGenerates.add(String.format("sig %s in BottomNode {}",getUniqueName(node)));
            return getUniqueName(node);
//            bottomNodes.add(parent);
//            return bottomNodeName;
        } else if (node instanceof JoinNode) {
            StringBuilder sb = new StringBuilder();
            String right = generate(node, node.getChild(1), columns, extras);

            sb.append(String.format("sig %s in %s {}\n",getUniqueName(node),right));
            superType.put(getUniqueName(node),right);
            //_c denoting that is present as a field in alloy.
            if(node.getChild(0) instanceof MethodWontHandleNode)
                sb.append(String.format("fact { %s = %s }", getUniqueName(node), getUniqueName(node.getChild(0))));
            else
                sb.append(String.format("fact { %s = %s.%s_c }", getUniqueName(node), getUniqueName(node.getChild(0)), right));
            lazyGenerates.add(sb.toString());
            columns.add(right+"_c");
            type.put(right+"_c", getUniqueName(node.getChild(1)));
            generate(node, node.getChild(0), columns, extras);
            return getUniqueName(node);

        } else if (node instanceof UnionNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if(left instanceof ListNode || right instanceof ListNode) {
                ListNode list = null;
                Node table = null;
                if(left instanceof ListNode) {
                    list = (ListNode) left;
                    table = right;
                }
                else if(right instanceof ListNode) {
                    list = (ListNode) right;
                    table = left;
                }
                for(Node column: list.columns) {
                    columns.add(getUniqueName(column));
                }
                generate(node, table, columns, extras);
                String tableSuper = getSuperType(getUniqueName(table));
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("one sig %s in %s {}\n",getUniqueName(list),(tableSuper)));
                superType.put(getUniqueName(node),tableSuper);
                for(int i=0; i<list.getNumChildren(); i++) {
                    Node columnValue = list.getChild(i);
                    Node columnName = list.columns.get(i);
                    String columnValueString = generate(list,columnValue,columns,extras);
                    sb.append(String.format("fact { %s.%s = %s }\n",getUniqueName(list),getUniqueName(columnName),columnValueString));
                }
                lazyGenerates.add(sb.toString());
            }
            else {
                generate(node, node.getChild(1), columns,extras);
                generate(node, node.getChild(0), columns,extras);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s + %s {}\n", getUniqueName(node),getUniqueName(left),getUniqueName(right)));
            superType.put(getUniqueName(node),getUniqueName(left));
            sb.append(String.format("fact { %s = %s + %s }\n",getUniqueName(node), getUniqueName(left), getUniqueName(right)));
            lazyGenerates.add(sb.toString());
            return getUniqueName(node);
        }
        else if (node instanceof MethodWontHandleNode) {
            if(parent instanceof EqNode || parent instanceof NotEqNode) {
//                generateBoolean = true;
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format("one sig %s in %s {}\n", getUniqueName(node), BooleanName));
//                sb.append(String.format("one sig %s in FieldData {}\n", getUniqueName(node)));
                variables.add(getUniqueName(node));
//                lazyGenerates.add(sb.toString());
//                return String.format("%s = %s",getUniqueName(node), BooleanTrueName);
                return getUniqueName(node);
            }
            else if(childShouldBeInt(parent)) {
                variables.add(getUniqueName(node));
                type.put(getUniqueName(node),"Int");
                return getUniqueName(node);
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("sig %s in univ {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
//                throw new AlloyGenerationException("Methodwo't handle without comparison");
            }
        }
        else if(node instanceof EmptySetNode) {
            return "none";
        }
        else if(node instanceof RelMinusNode) {
            String leftName = generate(node,node.getChild(0),columns,extras);
            Node right = node.getChild(1);
            String rightName = null;
            StringBuilder sb = new StringBuilder();
            if(right instanceof ListNode) {
                String super_right = getSuperType(leftName);
                rightName = getUniqueName(right);
                sb.append(String.format("one sig %s in %s {}", rightName, super_right));
                superType.put(rightName, super_right);
                Set<String> cols = new HashSet<>();
                for(int i=0; i<right.getNumChildren(); i++) {
                    String value = generate(right, right.getChild(i), columns, extras);
                    String columnName = getUniqueName(((ListNode) right).columns.get(i));
                    cols.add(columnName);
                    sb.append(String.format("fact { %s.%s = %s }\n", rightName, columnName, value));

                    //comun += chiildi column
                    //fact valuei = childi
//                    sb.append(String.format());
                }
                Set<String> superRightCols = tables.get(super_right);
                superRightCols.addAll(cols);
            }
            else {
                rightName = generate(node, node.getChild(1), columns, extras);
            }
            sb.append(String.format("sig %s in %s {}\n",getUniqueName(node),leftName));
            superType.put(getUniqueName(node),leftName);
            sb.append(String.format("fact { %s = %s - %s }",getUniqueName(node),leftName,rightName));
            lazyGenerates.add(sb.toString());
            return getUniqueName(node);
        }
        else if(node instanceof OneNode) {
            if(childShouldBeInt(parent))
                return "1";
            if(parent instanceof TernaryNode && ((TernaryNode) parent).isBooleanTyped)
                return "1=1";
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if(node instanceof ZeroNode) {
            if(childShouldBeInt(parent))
                return "0";
            if(parent instanceof TernaryNode && ((TernaryNode) parent).isBooleanTyped)
                return "0!=0";
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if(node instanceof MoreThanEqNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generate(node,left,columns,extras);
            String rightStr = generate(node,right,columns,extras);
            if(left instanceof ValueNode) {
                ValueNode valnode = (ValueNode) left;
                ValueOp vop = (ValueOp) valnode.getOperator();
                if(vop.getValue() instanceof JLengthExpr) {
                    leftStr = leftStr.replaceAll("\\s+", "");
                    lazyGenerates.add("sig " + leftStr + " in Int {}");
                }
            }
            return String.format("%s >= %s",leftStr,rightStr);
        }
        else if(node instanceof MoreThanNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generate(node,left,columns,extras);
            String rightStr = generate(node,right,columns,extras);
            return String.format("%s > %s",leftStr,rightStr);
        }
        else if(node instanceof ArithAddNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generate(node,left,columns,extras);
            String rightStr = generate(node,right,columns,extras);
            return String.format("%s + %s",leftStr,rightStr);
        }
        else {
//            for(Node child: node.getChildren()) {
//                generate(node, child, columns,extras);
//            }
            Integer count;
            if(uniqueMethodWontHandleCounterMap.containsKey(node)) {
                count = uniqueMethodWontHandleCounterMap.get(node);
            } else {
                methodWontHandleCounter++;
                count = methodWontHandleCounter;
                uniqueMethodWontHandleCounterMap.put(node, count);
            }
            return generate(parent, new MethodWontHandleNode(Integer.toString(count)), columns, extras);
            //  throw new AlloyGenerationException("New node of type " + node.getClass().getName());
        }
    }

    public String generateNRA(Node parent, Node node, Set<String> columns, Map<String, String> extras) {
        String retName = "";
        if(parent instanceof VarNode && parent.toString().contains("modelattribute") && parent.toString().contains("."))
            expandingField = "u_" + parent.toString().substring(parent.toString().indexOf('.')+1);
        if(node instanceof ProjectNode) {
            Node relation = node.getChild(0);
            if(relation instanceof JoinNode && NRAdepth == 0){
                if(relation.getChild(0) instanceof SelectNode) {
                    retName = generateNRA(relation, relation.getChild(0), columns, extras) + "." + expandingField;
                }
                Node joinChild0 = relation.getChild(0);
                while(joinChild0!=null && !(joinChild0 instanceof ClassRefNode)){
                    joinChild0 = joinChild0.getChild(0);
                }
                joinClass1= getUniqueName(joinChild0);
                NRArelationList.add(joinClass1);
                Node joinChild1 = relation.getChild(1);
                joinClass2 = getUniqueName(joinChild1);
                NRArelationList.add(joinClass2);
                if(!tables.containsKey(joinClass2))
                    tables.put(joinClass2, new HashSet<String>());
                if(expandingField.length() != 0)
                    addFieldToTable(joinClass1, expandingField, joinClass2);

                Node joinCondition = relation.getChild(2);
                String cond = generateNRA(relation, joinCondition, columns, extras);
                lazyGenerates.add(cond);
                System.out.println(cond+retName+lazyGenerates);
            }
            else if(relation instanceof JoinNode && NRAdepth != 0){
                joinClass1 = joinClass2;
                joinClass2 = getUniqueName(relation.getChild(1));
                Node condition = relation.getChild(2);
                NRArelationList.add(joinClass2);
                String eqFact = generateNRA(relation, condition, columns, extras);
                lazyGenerates.add(eqFact);
                System.out.println(joinClass1+joinClass2+condition);
            }
            else if(relation instanceof SelectNode){
                String selName = generateNRA(node, relation, columns, extras);
                if(retName.length() == 0)
                    retName = selName;
                if(joinClass2.length() == 0) {
                    Node joinChild0 = relation.getChild(0);
                    while (joinChild0 != null && !(joinChild0 instanceof ClassRefNode)) {
                        joinChild0 = joinChild0.getChild(0);
                    }
                    joinClass2 = getUniqueName(joinChild0);
                    NRArelationList.add(joinClass2);
                }
            }
            else
                if(relation instanceof VarNode)
                    relation = getRelationForVar((VarNode) relation);
            Node project = node.getChild(1);
//            ArrayList<Node> projectFields = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            if(project instanceof ListNode) {
                StringBuilder sb1 = new StringBuilder();
                for(int i=0; i<project.getNumChildren(); i++) {
                    Node child = project.getChild(i);
                    if(child.getOperator().toString().contains("Pi")){
                        NRAdepth++;
                        expandingField = child.getOperator().toString();
                        expandingField = "u_" + expandingField.substring(expandingField.indexOf('.')+1, expandingField.indexOf('='));
                        String nestRet = generateNRA(project, child, columns, extras);
                        NRAdepth--;
                        NRArelationList.remove(NRArelationList.size()-1);
                        int relationSize = NRArelationList.size();
                        if(relationSize >=2)
                            joinClass1 = NRArelationList.get(relationSize-2);
                        if(relationSize >= 1)
                            joinClass2 = NRArelationList.get(relationSize-1);
                    }
                    else{
                        if(child instanceof NullNode)
                            continue;
                        if(child instanceof FieldRefNode || child instanceof VarNode) {
                            String fieldName = getUniqueName(child);
                            addFieldToTable(joinClass2, fieldName, "FieldData");
                        }
                        else {
                            generateNRA(project, child, columns, extras);
                        }
                    }
                }
            }
            return retName;
        }
        else
            if(node instanceof SelectNode) {
            //String relation = generateNRA(node,node.getChild(0),columns, extras);
            //String relation = getUniqueName(node.getChild(0));
            Node relation = node.getChild(0);
            if(relation instanceof VarNode) {
                relation = getRelationForVar(relation);
            }

            Node condition = node.getChild(1);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s {}\n", getUniqueName(node), getUniqueName(relation)));
            superType.put(getUniqueName(node),getUniqueName(relation));
            selClassParent = getUniqueName(relation);
            selClassChild = getUniqueName(node);
            sb.append(String.format("pred meets_selection_criteria_of_%s[x: %s] {\n", getUniqueName(node),getUniqueName(relation)));
            //if(columns == null) columns = new HashSet<Node>();
            if(!condition.toString().equals("NullOp"))
                sb.append(generateNRA(node,condition,columns, extras)+'\n');
            sb.append("}\n");
            sb.append(String.format("fact { all y:%s | meets_selection_criteria_of_%s[y] <=> y in %s }" ,getUniqueName(relation),getUniqueName(node),getUniqueName(node)));
            selects.put(node,sb.toString());
            generateNRA(node,relation,columns, extras);
            return getUniqueName(node);
        }
        else
            if(node instanceof EqNode || node instanceof LikeNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if(parent instanceof JoinNode){
                String returnFact = "fact { ";
                List<String> variableList = new ArrayList<>();
                int i=0;
                for(i=0; i<NRArelationList.size(); i++) { // creating variable list for chaining of NRA Join equals condition
                    String varName = "v"+i;
                    returnFact += " all " + varName + " : " + NRArelationList.get(i) + " | ";
                    variableList.add(varName);
                }
                String leftVal = getFieldName(left);
                String rightVal = getFieldName(right);
                addFieldToTable(joinClass1, leftVal, "FieldData");
                addFieldToTable(joinClass2, rightVal, "FieldData");
                System.out.println(leftVal+rightVal);

                returnFact += String.format("%s.%s = %s.%s <=> %s in %s.%s }",
                    variableList.get(i-2), leftVal, variableList.get(i-1), rightVal,
                    variableList.get(i-1), variableList.get(i-2), expandingField);
                addFieldToTable(joinClass1, expandingField, joinClass2);
                return returnFact;
            }
            if (left == right) return "1 = 1";
            else if(left instanceof ZeroNode && (isComparison(right))) {
                return generateNRA(node,negation(right),columns, extras);
            }
            else if(right instanceof ZeroNode && (isComparison(left))) {
                return generateNRA(node,negation(left),columns, extras);
            }
            else if(left instanceof OneNode && (isComparison(right))) {
                return generateNRA(node,right,columns, extras);
            }
            else if(right instanceof OneNode && (isComparison(left))) {
                return generateNRA(node,left,columns, extras);
            }
            else if(left instanceof FieldRefNode && !(right instanceof FieldRefNode)) {
                String leftVal = generateNRA(node,left,columns, extras);
                String rightVal = generateNRA(node,right,columns, extras);
                if(!right.getOperator().toString().equals("?"))
                    variables.add(rightVal);
                return String.format("x.%s = %s",leftVal, rightVal);
            }
            else if(right instanceof FieldRefNode && !(left instanceof FieldRefNode)) {
                return String.format("%s = x.%s", generateNRA(node,left,columns, extras),generateNRA(node,right,columns,extras));
            }
            else if(left instanceof FieldRefNode && right instanceof FieldRefNode) {
                String leftVal = getUniqueName(left);
                String rightVal = getUniqueName(right).toUpperCase();
                variables.add(rightVal);
                addFieldToTable(selClassParent, leftVal, "FieldData");
                return "x." + leftVal + " = " + rightVal;
//                throw new AlloyGenerationException("both colums case, not handled.");
            }
            else {
                return String.format("%s = %s", generateNRA(node, node.getChild(0), columns, extras), generateNRA(node, node.getChild(1), columns, extras));
            }
        }
        else if (node instanceof NotEqNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if (left == right) return "1 != 1";
            else if(left instanceof ZeroNode && (isComparison(right))) {
                return generateNRA(node,(right),columns, extras);
            }
            else if(right instanceof ZeroNode && (isComparison(left))) {
                return generateNRA(node,(left),columns, extras);
            }
            else if(left instanceof OneNode && (isComparison(right))) {
                return generateNRA(node,negation(right),columns, extras);
            }
            else if(right instanceof OneNode && (isComparison(left))) {
                return generateNRA(node,negation(left),columns, extras);
            }
            else if(left instanceof FieldRefNode && !(right instanceof FieldRefNode)) {

                return String.format("x.%s != %s",generateNRA(node,left,columns,extras), generateNRA(node,right,columns,extras));
            }
            else if(right instanceof FieldRefNode && !(left instanceof FieldRefNode)) {
                return String.format("%s != x.%s", generateNRA(node,left,columns,extras),generateNRA(node,right,columns,extras));
            }
            else if(left instanceof FieldRefNode && right instanceof FieldRefNode) {
                throw new AlloyGenerationException("both colums case, not handled.");
            }
            else {
                return String.format("%s != %s", generateNRA(node, node.getChild(0), columns,extras), generateNRA(node, node.getChild(1), columns,extras));
            }
        } else if (node instanceof CartesianProdNode) {
            if (node.getNumChildren() > 1) throw new AlloyGenerationException("Multiple children in Cartesian node.");
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s {}\n", getUniqueName(node), getUniqueName(node.getChild(0))));
            superType.put(getUniqueName(node),getUniqueName(node.getChild(0)));
            sb.append(String.format("fact { %s = %s }\n", getUniqueName(node), getUniqueName(node.getChild(0))));
            lazyGenerates.add(sb.toString());
            Node relation = node.getChild(0);
            if(relation instanceof  VarNode) {
                relation = getRelationForVar(relation);
            }
            return generateNRA(parent,relation,columns, extras);

//            for (Node child : node.getChildren()) {
//                generateNRA(node, child, columns);
//            }
//            return getUniqueName(node);
        } else if (node instanceof VarNode) {
            if(varToRelation.containsKey(node)) {
                return generateNRA(parent, varToRelation.get(node), columns, extras);
            }
            else {
                variables.add(getUniqueName(node));
                return (getUniqueName(node));
            }
        }
        else if(node instanceof NullNode) {
            literals.add(nullNodeName);
            return nullNodeName;
        } else if(node instanceof ValueNode) {
            if(childShouldBeInt(parent)) {
                String nodeStr = node.toString();
                //  floats to ints
                if(nodeStr.contains(".")) {
                    nodeStr = nodeStr.substring(0, nodeStr.indexOf("."));
                }
                return nodeStr;
            }
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if (node instanceof ClassRefNode) {
            String tableSuper = getSuperType(getUniqueName(node));
            if (tables.containsKey(tableSuper)) {
                tables.get(tableSuper).addAll(columns);
            } else {
                tables.put(tableSuper, new HashSet<>(columns));
            }
            columns.clear();
            String tableName = getUniqueName(node);
            if (tables.containsKey(tableName)) {
                tables.get(tableName).addAll(columns);
            } else {
                tables.put(tableName, new HashSet<>(columns));
            }
            return getUniqueName(node);
        } else if (node instanceof FieldRefNode) {
            columns.add(getUniqueName(node));

            addFieldToTable(joinClass2, getUniqueName(node), "FieldData");
            return String.format("%s", getUniqueName(node));
        } else if (node instanceof UnknownNode) {
            if(parent instanceof EqNode || parent instanceof NotEqNode) {
//                generateBoolean = true;
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format("one sig %s in %s {}\n", getUniqueName(node), BooleanName));
//                sb.append(String.format("one sig %s in FieldData {}\n", getUniqueName(node)));
                lazyGenerates.add(String.format("lone sig %s in univ {}",getUniqueName(node)));
//                lazyGenerates.add(sb.toString());
//                return String.format("%s = %s",getUniqueName(node), BooleanTrueName);
                return getUniqueName(node);
            }
            else if(extras.getOrDefault("RetVarNode","false").equals("true")){
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("lone sig %s in ControllerOrView {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
            }
            else if(childShouldBeInt(parent)) {
                variables.add(getUniqueName(node));
                type.put(getUniqueName(node),"Int");
                return getUniqueName(node);
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("lone sig %s in FieldData {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
            }
        }
        else if (node instanceof TernaryNode) {
            //todo
            Node condition = node.getChild(0);
            Node trueDag = node.getChild(1);
            Node falseDag = node.getChild(2);
            if(parent.toString().startsWith(prefix)) {
                //its root node, will need its own sig.
                lazyGenerates.add(String.format("sig %s in univ {}",getUniqueName(node)));
                String fd = generateNRA(node,falseDag,columns, extras);
                lazyGenerates.add(String.format("fact { %s = ((%s) => (%s) else (%s)) }",getUniqueName(node), generateNRA(node,condition,columns,extras), generateNRA(node,trueDag,columns,extras), fd));
                return getUniqueName(node);
            }
            if(condition instanceof UnknownNode) {
                String conditionStr = generateNRA(node,condition,columns,extras);
                String trueDagStr = generateNRA(node,trueDag,columns,extras);
                String falseDagStr = generateNRA(node,falseDag,columns,extras);
                Node uv = new VarNode("UnknownVar"+node.hashCode());
                String uvn = generateNRA(node,uv,columns,extras);
                return String.format("((%s = %s) => (%s) else (%s))", conditionStr,uvn , trueDagStr, falseDagStr);
            }
            return String.format("((%s) => (%s) else (%s))", generateNRA(node,condition,columns,extras), generateNRA(node,trueDag,columns,extras), generateNRA(node,falseDag,columns,extras));

        } else if (node instanceof BottomNode) {
            lazyGenerates.add(String.format("sig %s in BottomNode {}",getUniqueName(node)));
            return getUniqueName(node);
//            bottomNodes.add(parent);
//            return bottomNodeName;
        } else if (node instanceof JoinNode) {
            StringBuilder sb = new StringBuilder();
            String right = generateNRA(node, node.getChild(1), columns, extras);

            sb.append(String.format("sig %s in %s {}\n",getUniqueName(node),right));
            superType.put(getUniqueName(node),right);
            //_c denoting that is present as a field in alloy.
            if(node.getChild(0) instanceof MethodWontHandleNode)
                sb.append(String.format("fact { %s = %s }", getUniqueName(node), getUniqueName(node.getChild(0))));
            else
                sb.append(String.format("fact { %s = %s.%s_c }", getUniqueName(node), getUniqueName(node.getChild(0)), right));
            lazyGenerates.add(sb.toString());
            columns.add(right+"_c");
            type.put(right+"_c", getUniqueName(node.getChild(1)));
            generateNRA(node, node.getChild(0), columns, extras);
            return getUniqueName(node);

        } else if (node instanceof UnionNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            if(left instanceof ListNode || right instanceof ListNode) {
                ListNode list = null;
                Node table = null;
                if(left instanceof ListNode) {
                    list = (ListNode) left;
                    table = right;
                }
                else if(right instanceof ListNode) {
                    list = (ListNode) right;
                    table = left;
                }
                for(Node column: list.columns) {
                    columns.add(getUniqueName(column));
                }
                generateNRA(node, table, columns, extras);
                String tableSuper = getSuperType(getUniqueName(table));
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("one sig %s in %s {}\n",getUniqueName(list),(tableSuper)));
                superType.put(getUniqueName(node),tableSuper);
                for(int i=0; i<list.getNumChildren(); i++) {
                    Node columnValue = list.getChild(i);
                    Node columnName = list.columns.get(i);
                    String columnValueString = generateNRA(list,columnValue,columns,extras);
                    sb.append(String.format("fact { %s.%s = %s }\n",getUniqueName(list),getUniqueName(columnName),columnValueString));
                }
                lazyGenerates.add(sb.toString());
            }
            else {
                generateNRA(node, node.getChild(1), columns,extras);
                generateNRA(node, node.getChild(0), columns,extras);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("sig %s in %s + %s {}\n", getUniqueName(node),getUniqueName(left),getUniqueName(right)));
            superType.put(getUniqueName(node),getUniqueName(left));
            sb.append(String.format("fact { %s = %s + %s }\n",getUniqueName(node), getUniqueName(left), getUniqueName(right)));
            lazyGenerates.add(sb.toString());
            return getUniqueName(node);
        }
        else if (node instanceof MethodWontHandleNode) {
            if(parent instanceof EqNode || parent instanceof NotEqNode) {
//                generateBoolean = true;
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format("one sig %s in %s {}\n", getUniqueName(node), BooleanName));
//                sb.append(String.format("one sig %s in FieldData {}\n", getUniqueName(node)));
                variables.add(getUniqueName(node));
//                lazyGenerates.add(sb.toString());
//                return String.format("%s = %s",getUniqueName(node), BooleanTrueName);
                return getUniqueName(node);
            }
            else if(childShouldBeInt(parent)) {
                variables.add(getUniqueName(node));
                type.put(getUniqueName(node),"Int");
                return getUniqueName(node);
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("sig %s in univ {}\n",getUniqueName(node)));
                lazyGenerates.add(sb.toString());
                return getUniqueName(node);
//                throw new AlloyGenerationException("Methodwo't handle without comparison");
            }
        }
        else if(node instanceof EmptySetNode) {
            return "none";
        }
        else if(node instanceof RelMinusNode) {
            String leftName = generateNRA(node,node.getChild(0),columns,extras);
            Node right = node.getChild(1);
            String rightName = null;
            StringBuilder sb = new StringBuilder();
            if(right instanceof ListNode) {
                String super_right = getSuperType(leftName);
                rightName = getUniqueName(right);
                sb.append(String.format("one sig %s in %s {}", rightName, super_right));
                superType.put(rightName, super_right);
                Set<String> cols = new HashSet<>();
                for(int i=0; i<right.getNumChildren(); i++) {
                    String value = generateNRA(right, right.getChild(i), columns, extras);
                    String columnName = getUniqueName(((ListNode) right).columns.get(i));
                    cols.add(columnName);
                    sb.append(String.format("fact { %s.%s = %s }\n", rightName, columnName, value));

                    //comun += chiildi column
                    //fact valuei = childi
//                    sb.append(String.format());
                }
                Set<String> superRightCols = tables.get(super_right);
                superRightCols.addAll(cols);
            }
            else {
                rightName = generateNRA(node, node.getChild(1), columns, extras);
            }
            sb.append(String.format("sig %s in %s {}\n",getUniqueName(node),leftName));
            superType.put(getUniqueName(node),leftName);
            sb.append(String.format("fact { %s = %s - %s }",getUniqueName(node),leftName,rightName));
            lazyGenerates.add(sb.toString());
            return getUniqueName(node);
        }
        else if(node instanceof OneNode) {
            if(childShouldBeInt(parent))
                return "1";
            if(parent instanceof TernaryNode && ((TernaryNode) parent).isBooleanTyped)
                return "1=1";
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if(node instanceof ZeroNode) {
            if(childShouldBeInt(parent))
                return "0";
            if(parent instanceof TernaryNode && ((TernaryNode) parent).isBooleanTyped)
                return "0!=0";
            literals.add(getUniqueName(node));
            return getUniqueName(node);
        }
        else if(node instanceof MoreThanEqNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generateNRA(node,left,columns,extras);
            String rightStr = generateNRA(node,right,columns,extras);
            if(left instanceof ValueNode) {
                ValueNode valnode = (ValueNode) left;
                ValueOp vop = (ValueOp) valnode.getOperator();
                if(vop.getValue() instanceof JLengthExpr) {
                    leftStr = leftStr.replaceAll("\\s+", "");
                    lazyGenerates.add("sig " + leftStr + " in Int {}");
                }
            }
            return String.format("%s >= %s",leftStr,rightStr);
        }
        else if(node instanceof MoreThanNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generateNRA(node,left,columns,extras);
            String rightStr = generateNRA(node,right,columns,extras);
            return String.format("%s > %s",leftStr,rightStr);
        }
        else if(node instanceof ArithAddNode) {
            Node left = node.getChild(0);
            Node right = node.getChild(1);
            String leftStr = generateNRA(node,left,columns,extras);
            String rightStr = generateNRA(node,right,columns,extras);
            return String.format("%s + %s",leftStr,rightStr);
        }
        else {
//            for(Node child: node.getChildren()) {
//                generateNRA(node, child, columns,extras);
//            }
            Integer count;
            if(uniqueMethodWontHandleCounterMap.containsKey(node)) {
                count = uniqueMethodWontHandleCounterMap.get(node);
            } else {
                methodWontHandleCounter++;
                count = methodWontHandleCounter;
                uniqueMethodWontHandleCounterMap.put(node, count);
            }
            return generateNRA(parent, new MethodWontHandleNode(Integer.toString(count)), columns, extras);
            //  throw new AlloyGenerationException("New node of type " + node.getClass().getName());
        }
    }

    public String getFieldName(Node node){
        String name = getUniqueName(node);
        if(name.contains("_"))
            name = name.substring(name.lastIndexOf('_'));
        return "u"+name;
    }
    private void addFieldToTable(String EntityClass, String fieldName, String fieldType) {
        if(!tables.containsKey(EntityClass))
            tables.put(EntityClass, new HashSet<String>());
        tables.get(EntityClass).add(fieldName);
        type.put(fieldName, fieldType);
    }


    private boolean childShouldBeInt(Node parent) {
        return (parent instanceof MoreThanEqNode || parent instanceof ArithAddNode || parent instanceof MoreThanNode);
    }

    static boolean isComparison(Node node) {
        return node instanceof EqNode || node instanceof NotEqNode;
    }
    ClassRefNode getRelationForVar(Node varnode) {
        if(!(varnode instanceof VarNode)) throw new AlloyGenerationException("Not a varnode");
        if(varToRelation.containsKey(varnode)) return varToRelation.get(varnode);
        else {
            ClassRefNode rel = new ClassRefNode(varnode.toString());
            varToRelation.put(varnode, rel);
            return rel;
        }
    }
    private Node negation(Node node) {
        Node nodeBar;
        if(node instanceof EqNode) {
            return new NotEqNode(node.getChild(0), node.getChild(1));
        }
        else if(node instanceof NotEqNode) {
            return new EqNode(node.getChild(0), node.getChild(1));
        }
        else if(node instanceof MethodWontHandleNode) {
            return node;
        }
        else {
            throw new AlloyGenerationException("not implemented for "+node.getClass().getName());
        }
    }


    private void write(Object o, Object ... args) {
        printWriter.println(String.format(String.valueOf(o),args));
    }
    private String getSuperType(String node) {
        if(superType.containsKey(node)) {
            return getSuperType(superType.get(node));
        }
        else return node;
    }
    private static String getUniqueName(Node node) {
        String name = "";
        if(node instanceof ClassRefNode) {
            name = ((ClassRefOp)node.getOperator()).getClassName();
        }
        else if(node instanceof FieldRefNode) {
            name = ((FieldRefOp)((FieldRefNode)node).getOperator()).getFieldName();
            name = name.toLowerCase();
        }
        else if(node instanceof VarNode) {
            name = node.toString();
        }
        else if(node instanceof OneNode) {
            name = oneNodeName;
        }
        else if(node instanceof ZeroNode) {
            name = zeroNodeName;
        }
        else if(node instanceof ValueNode) {
            name = node.toString();
        } else if (node instanceof MethodWontHandleNode && ((MethodWontHandleNode) node).callSiteStr != null
                && ((MethodWontHandleNode) node).callSiteStr.contains("principal")
                && ((MethodWontHandleNode) node).callSiteStr.contains("getName")) {
            name = "principalusername";
        }
        else {
            //name = String.format("%.20s",node)+node.hashCode();
            Integer uniqueNumB = uniqueNumOf.get(node);
            int uniqueNum;
            if (uniqueNumB != null){
                uniqueNum = uniqueNumB;
            } else {
                nextUniqueNum++;
                uniqueNum = nextUniqueNum;
                uniqueNumOf.put(node,uniqueNum);
            }
            name = String.format("%.20s",node)+uniqueNum;
            // change above done by @raghavan
        }
        name = "u_"+name
                .replace(' ','_')
                .replace('(','_')
                .replace(')','_')
                .replace('$','_')
                .replace('|','_')
                .replace('\n','_')
                .replace('.','_')
                .replace('?','_')
                .replace('"','_')
                .replace('[', '_')
                .replace(']', '_')
                .replace('<', '_')
                .replace('>', '_')
                .replace(':', '_');
        name = name.replace('-','_');
        return name;
    }
    public void generateCommons() {

        write("sig FieldData {}");
        for(String literal: literals) {
            write("one sig %s extends FieldData {}",literal);
        }
        for(String variable: variables) {
            write("one sig %s in %s {}",variable,type.getOrDefault(variable,"FieldData"));
        }
        for(Map.Entry<String,Set<String>> entry: tables.entrySet()) {
            String table = entry.getKey();
            if(table.length() == 0)
                continue;
            if(superType.containsKey(entry.getKey())) {
                write("sig %s in %s {",(table),(superType.get(entry.getKey())));
            }
            else {
                write("sig %s {", (table));
            }
//            Boolean replaced = alloyExtraEntityInfo(table);
//            if (replaced) {
//                continue;
//            }
            Set<String> columns = entry.getValue();
            if(columns != null) {
                for (String column : columns) {
                    String cType = type.getOrDefault(column, "FieldData");
                    write("%s : %s,", column, cType);
                }
            }
            write("}");
        }

//        try {
//            List<String> lines = Files.readAllLines(Paths.get("AlloyExtraEntityInfo/metadata.csv"));
//            for (String repline : lines) {
//                if (repline.contains(CMDOptions.controllerSig)) {
//                    String[] split = repline.split("\",\"");
//                    String entityName = split[1];
//                    entityName = entityName.replace("\"", "");
//                    Set<String> tablesFound = tables.keySet();
//                    if (tablesFound.contains("u_" + entityName) == false) {
//                        write("sig u_" + entityName + " {");
//                        List<String> entityLines = Files.readAllLines(Paths.get("AlloyExtraEntityInfo/" + entityName + ".txt"));
//                        for (String col : entityLines) {
//                            write("%s : %s,", "u_" + col, "FieldData");
//                        }
//                        write("}");
//                    }
//                }
//            }
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }

        for(String select: selects.values()) {
            write(select);
        }
        for(String s:lazyGenerates) {
            write(s);
        }
        for(Map.Entry<Node,String> entry:modelAttributes.entrySet()) {
            write("sig %s in univ {}",'m'+getUniqueName(entry.getKey()));
            write("fact { %s = %s }",'m'+getUniqueName(entry.getKey()), entry.getValue());
        }
        write("sig BottomNode in FieldData {}");
        if(generateBoolean) {
            write("abstract sig %s {}",BooleanName);
            write("one sig %s extends %s {}",BooleanTrueName, BooleanName);
            write("one sig %s extends %s {}",BooleanFalseName, BooleanName);
        }
    }
    //returns true if replaced and false otherwise
    private Boolean alloyExtraEntityInfo(String table) {
        Boolean ret = false;
        try {
            List<String> lines = Files.readAllLines(Paths.get("AlloyExtraEntityInfo/metadata.csv"));
            for (String repline : lines) {
                if (repline.contains(CMDOptions.controllerSig)) {
                    String[] split = repline.split("\",\"");
                    String entityName = split[1];
                    entityName = entityName.replace("\"", "");
                    if (table.equals("u_" + entityName)) {
                        List<String> entityLines = Files.readAllLines(Paths.get("AlloyExtraEntityInfo/" + entityName + ".txt"));
                        for (String col : entityLines) {
                            String colname = "u_" + col;
                            String typename = "FieldData";
                            if (col.contains(",")) {
                                String[] colspl = col.split(",");
                                colname = "u_" + colspl[0];
                                typename = "u_" + colspl[1];
                            }
                            write("%s : %s,", colname, typename);
                        }
                        write("}");
                        ret = true;
                    }
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


    public String getProjFieldName(Node node){
        String nestFieldName=" ";
        String[] nodeNameSplit = node.toString().split("=");
//        System.out.println(nodeNameSplit[0].split("[.]")[0]);
        nestFieldName = nodeNameSplit[0].split("[.]")[1];
        return nestFieldName;
    }

    private String getShortName(String name){
        return name.substring(name.lastIndexOf(".")+1);
    }

    public boolean isNested(Node node, boolean seenList){
//        return true;
//        if(node instanceof ListNode)
//            seenList = true;
//        if(node instanceof ProjectNode && seenList)
//            return true;
//        for(int i=0; i<node.getNumChildren(); i++){
//            if(isNested(node.getChild(i), seenList))
//                return true;
//        }
//        return false;

        ////////////////////////////////
        if(node.toString().contains("Alpha"))
            return true;
        boolean res = false;
        if(node.getNumChildren() == 0)
            return false;
        for(Node child : node.getChildren()) {
            res = isNested(child, false);
            if(res == true)
                return true;
        }
        return res;
    }

    public void generateNestedJoinSummary(Node node, List<String> relationList, StringBuilder sb, int depth){
        System.out.println("Inside generateNestedJoinSummary");
        if(node instanceof ProjectNode){
            Node piChild1 = node.getChild(0); // Sel Node
            Node piChild2 = node.getChild(1); // List Node
            // Handling Sel Node
            if(piChild1 instanceof SelectNode ){
                sb = processNestedJoinSelNode(piChild1, relationList, sb);
            }
            if(piChild1 instanceof JoinNode && depth == 0){
                System.out.println("Join node encountered ");
                String expandField = "u_" + getProjFieldName(node);
                sb = processNestedJoinDepthZero(piChild1, relationList, sb, expandField);
            }
            // Handling Join Node
            if(piChild1 instanceof JoinNode && depth != 0){
                String expandField = "u_" + getProjFieldName(node);
                relationList = processNestedJoinNode(piChild1, relationList, expandField, sb);
            }

            if(piChild2 instanceof ListNode){
                for(int j=0; j<piChild2.getNumChildren(); j++){
                    Node listChild = piChild2.getChild(j);
                    if(listChild instanceof SelectNode)
                        sb = processNestedJoinSelNode(listChild, relationList, sb);
                    if(listChild instanceof ProjectNode){
                        List<String> relationListCopy = new ArrayList<>(relationList);
                        generateNestedJoinSummary(listChild, relationListCopy, sb, depth+1);
                    }
                }
            }
        }
    }

    private StringBuilder processNestedJoinDepthZero(Node joinNode, List<String> relationList, StringBuilder sb, String expandField) {
        Node relation1 = joinNode.getChild(0);
        Node relation2 = joinNode.getChild(1);
        Node condition = joinNode.getChild(2);
        if(relation1 instanceof SelectNode){
            sb = processNestedJoinSelNode(relation1, relationList, sb);
        }
        processNestedJoinNode(joinNode, relationList, expandField, sb);
        return sb;
    }

    public List<String> processNestedJoinNode(Node node, List<String> relationList, String expandField, StringBuilder sb){
        System.out.println("Inside processNestedJoinNode \n" + sb);
        Node relation1 = node.getChild(0); // Alpha
        Node relation2 = node.getChild(1); // ClassRef(Pet)
        String rel2 = getUniqueNameNRA(relation2); // u_Pet
        Node equals = node.getChild(2);

        String eqChild1 = equals.getChild(0).toString();
        eqChild1 = eqChild1.substring(eqChild1.indexOf('.')+1); // id
        String eqChild2 = equals.getChild(1).toString();
        eqChild2 = "u_" + eqChild2.substring(eqChild2.indexOf('.')+1); // u_owner_id
        sb.append(String.format("\n fact {"));
        String preVar = "", currVar = "", currRelation = "";
        for(int i=0; i<relationList.size(); i++){
            if(i == relationList.size()-1)
                currVar = "alpha";
            else
                currVar = "v" + String.valueOf(i);

            currRelation = relationList.get(i);
            if(preVar == "")
                sb.append(String.format(" all %s : %s | ", currVar, currRelation));
            else
                sb.append(String.format("all %s : %s.%s | ", currVar, preVar, currRelation));

            preVar = currVar;
        }
        currVar = "v" + String.valueOf(relationList.size()+1);  // v3
        sb.append(String.format(" all %s : %s | ", currVar, rel2));
        preVar = "alpha";
        sb.append(String.format("%s.%s = %s.%s <=> %s in %s.%s } \n",
                preVar, eqChild1, currVar, eqChild2, currVar, preVar, expandField));
        relationList.add(expandField);
        return relationList;
    }

    public StringBuilder processNestedJoinSelNode(Node node, List<String> relationList, StringBuilder sb){
        System.out.println("Inside processNestedJoinSelNode node = " +  node.toString());
        Node selChild1 = node.getChild(0);
        Node selChild2 = node.getChild(1);
        String relation1 = "", like1 = "";
        String relation2 = "", like2 = "";
        Node relation;
        if(selChild1 instanceof CartesianProdNode)
            relation = selChild1.getChild(0); // ClassRef(Owner) => Owner
        else
            relation = selChild1;

        relation1 = getUniqueNameNRA(relation);
        relation1 = removeEndString(relation1, "Repository");
        nextUniqueNum++;
        relation2 = relation1 + String.valueOf(nextUniqueNum);
//            System.out.println("Tables are = " + relation1 + ",,, " + relation2);
        sb.append(String.format("\n sig %s in %s {} \n", relation2, relation1));
        if(selChild2 instanceof LikeNode || selChild2 instanceof EqNode){
            Node likeChild1 = selChild2.getChild(0);
            Node likeChild2 = selChild2.getChild(1);
            if(likeChild1 instanceof FieldRefNode) {
                like1 = getUniqueName(likeChild1);
//                like1 = like1.substring(like1.indexOf('_') + 1);
                like1 = getUsabeName(like1, 1, relation1).toLowerCase();
                like2 = likeChild2.toString();
                like2 = getUsabeName(like2, 2, relation1);
            }
            int varNum = 0;
            String currVar = "v" + String.valueOf(varNum);
            sb.append(String.format("fact{all %s : %s | %s.%s = %s <=> %s in %s} \n",
                    currVar, relation1, currVar, like1, like2, currVar, relation2));
            relationList.add(relation2);
        }
        return sb;
    }
    public String getUsabeName(String s, int code, String relation){
        String res = s;
        switch (code){
            case 1:

                if(s.endsWith("id"))
                    res = s.substring(0, s.indexOf("id")) + "Id";
                break;
            case 2:
                if(!s.contains("."))
                    break;
                res = ".u_" + s.substring(s.lastIndexOf(".")+1);
                s = s.substring(0,s.lastIndexOf("."));
                res = "u_" + s.substring(s.lastIndexOf(".")+1) + res;
                break;
        }
        if(res.endsWith(")"))
            res = res.substring(0, res.length()-1);
        return res;
    }

    public String removeEndString(String s1, String s2){
        if(s1.contains(s2)){
            s1 = s1.substring(0, s1.indexOf(s2));
        }
        return s1;
    }

    private static String getUniqueNameNRA(Node node) {
        debug d = new debug("Alloygenerator.java","getUniqueName()");
//        d.dg("node : "+node.toString());
        String name = "";
        if(node instanceof ClassRefNode) {

            name = ((ClassRefOp)node.getOperator()).getClassName();
            name =  name.substring(name.lastIndexOf(".")+1);
        }
        else if(node instanceof FieldRefNode) {
            name = ((FieldRefOp)((FieldRefNode)node).getOperator()).getFieldName();
            name = name.toLowerCase();
        }
        else if(node instanceof VarNode) {
            name = node.toString();
        }
        else if(node instanceof OneNode) {
            name = oneNodeName;
        }
        else if(node instanceof ZeroNode) {
            name = zeroNodeName;
        }
        else if(node instanceof ValueNode) {
            name = node.toString();
        } else if (node instanceof MethodWontHandleNode && ((MethodWontHandleNode) node).callSiteStr != null
                && ((MethodWontHandleNode) node).callSiteStr.contains("principal")
                && ((MethodWontHandleNode) node).callSiteStr.contains("getName")) {
            name = "principalusername";
        }

        //  MyImpl //

//        else if(node instanceof  ProjectNode){
//            name = node;
//            int index = name.lastIndexOf("=");
//            d.dg("name : "+name);
////            name = name.substring(0,index);
//            name = name.substring(name.lastIndexOf(".")+1);
//        }

        else if(node instanceof JoinNode){
            name="Join_";
//            if(node.getChild(0) instanceof SelectNode){
//                name+= node.getChild(0).getChild(0).toString()+"_";
//            }
//            else{
//                name+= node.getChild(0).toString();
//            }
            name += getUniqueName(node.getChild(0))+"_";
            name += getUniqueName(node.getChild(1));

//            if(node.getChild(1) instanceof SelectNode){
//                name+= node.getChild(1).getChild(1).toString()+"_";
//            }
//            else{
//                name+= node.getChild(1).toString();
//            }
        }
        //MyImpl end //

        else {
//            d.dg("case : else");
            //name = String.format("%.20s",node)+node.hashCode();
            Integer uniqueNumB = uniqueNumOf.get(node);
            int uniqueNum;
            if (uniqueNumB != null){
                uniqueNum = uniqueNumB;
            }
            else {
                nextUniqueNum++;
                uniqueNum = nextUniqueNum;
                uniqueNumOf.put(node,uniqueNum);
            }
            name = String.format("%.20s",node.getOperator().getName())+uniqueNum;
            // change above done by @raghavan

            if(name.split("=").length > 1){
                int index = name.lastIndexOf("=");
                String nameSplit = name.split("=")[0];
                name = nameSplit.substring(nameSplit.lastIndexOf(".")+1);
            }
        }
        name = "u_"+name
                .replace(' ','_')
                .replace('(','_')
                .replace(')','_')
                .replace('$','_')
                .replace("|","")
                .replace('\n','_')
                .replace('.','_')
                .replace('?','_')
                .replace('"','_')
                .replace('[', '_')
                .replace(']', '_')
                .replace('<', '_')
                .replace('>', '_')
                .replace(':', '_');
        name = name.replace('-','_');
//        d.dg("Uniquename = "+name);
        return name;
    }

    public boolean isFlatFindAll(Node node, int depth){
        boolean res = true;
        if(!(node instanceof ProjectNode) || !(node.getChild(0) instanceof SelectNode) || !(node.getChild(1) instanceof ListNode))
            return false;
        if(!(node.getChild(0).getChild(1).toString().equals("NullOp")))
            return false;
        Node list = node.getChild(1);
        for(int i=0; i<list.getNumChildren(); i++){
            String op = list.getChild(i).getOperator().toString();
            if(!op.equals("Var"))
                return false;
        }
        return true;
    }

    public String processFlatFindAll(Node node, StringBuilder sb){
        String ret = "";
        sb.append("// " + node.toString());


        return ret;
    }


} // class ends

