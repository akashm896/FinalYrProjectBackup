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
import mytest.debug;
import soot.jimple.internal.JLengthExpr;

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

public class AlloyGeneratorNRA implements AlloyGen {
    Map<Node, ClassRefNode> varToRelation = new HashMap<>();
    Map<String, String> superType = new HashMap<>();
    static String nullNodeName = "NullNode";
    static String bottomNodeName = "BottomNode";
    Map<VarNode, Node> veMap;
    //    String attribute = "post.user.email";
    String attribute = "";
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
    public AlloyGeneratorNRA(Map<VarNode, Node> veMap) throws IOException {
        fileWriter = new FileWriter(CMDOptions.outfile != null ? CMDOptions.outfile : "outputs/Alloy/akash.als");
        printWriter = new PrintWriter(fileWriter);
//        printWriter.println("Akash Testing write()");
        this.veMap = veMap;
        System.out.println("Akash 1 \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        Object[] keySet = veMap.keySet().toArray();
        Arrays.sort(keySet);
        //for(VarNode node : veMap.keySet()) {
        Boolean keypresent = false;
        sop("Tables and Fields :\n");
        for(String  sc:tableAndFields.keySet()){
            String sig = "";
            sig += "u_" + getShortName(sc) + "{\n";
            Map<String, String> fields = tableAndFields.get(sc);
            for(String k : fields.keySet()) {
                if(fields.get(k).equals("FieldData"))
                    sig += k + " : " + "FieldData\n";
                else
                    sig += k + " : u_" + fields.get(k) + "\n";
            }
            sig += "}";
            lazyGenerates.add(sig);
            sop(getShortName(sc)+" fields = ");
            sop(tableAndFields.get(sc)+"\n");

        }

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
                System.out.println("Akash Analysis1 = "+primary);
//                write(primary);
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
        printWriter.close();
    }

    private String getShortName(String name){
        return name.substring(name.lastIndexOf(".")+1);
    }

    private boolean isDbNode(VarNode node) {
        return !(node.toString().startsWith("__model"));
    }
    public String getProjFieldName(Node node){
        String nestFieldName=" ";
        String[] nodeNameSplit = node.toString().split("=");
//        System.out.println(nodeNameSplit[0].split("[.]")[0]);
        nestFieldName = nodeNameSplit[0].split("[.]")[1];
        return nestFieldName;
    }

    public Node getJionedTable(Node relation){
//        String joinedTable = "";
//        sop("relation = "+relation.toString());
//        sop("Number of children = "+ relation.getNumChildren());
        if(relation instanceof JoinNode)
            return relation.getChild(1);
        else
            return relation.getChild(0).getChild(1);

    }

    public String getFieldTableName(Node node){
        String fieldTable=" ";
        String[] nodeNameSplit = node.toString().split("=");
//        System.out.println(nodeNameSplit[0].split("[.]")[0]);
        fieldTable = nodeNameSplit[0].split("[.]")[0];
        return fieldTable;
    }

    public void sop(Object o){
        System.out.println(o);
    }

    public void processNestField(Node relation,Node parent, Node node, Set<String> columns, Map<String, String> extras){
        String field = "";
        field="u_"+getProjFieldName(node);
        String fieldType = "u_"+getFieldTableName(node);
        String tableSuper = getSuperType(fieldType);
//        sop(field +" "+tableSuper);
        if (tables.containsKey(tableSuper)) {
            tables.get(tableSuper).add(field);
        } else {
            tables.put(tableSuper, new HashSet<>());
            tables.get(tableSuper).add(field);
        }
//        String fieldType = getJionedTableName(relation);
        Node joinedTableNode = getJionedTable(relation);
//        sop(field + " "+getUniqueName(joinedTableNode));
        type.put(field,getUniqueName(joinedTableNode));

    }

    public int count=1;
    public String generate(Node parent, Node node, Set<String> columns, Map<String, String> extras) {
//        System.out.println("Akash.node = " + node.toString());
        boolean isNRA = isNested(node, false);
        if(isNRA){ // checks if there is Project as child of List which means NRA
//            return "";
             System.out.println(" Node contains nested fields");
            StringBuilder sb = new StringBuilder();
//            sb.append("Alloy Summary by Akash code starts \n \n ");
//            sb.append("\n////////////////////////////////////////////////////////////////////////\n " + node.toString());
            generateNestedJoinSummary(node, new ArrayList<String>(), sb, 0);
//            sb.append("\n Alloy Summary by Akash code ends \n \n ");
//            System.out.println("Final Summary = \n" + sb);
            lazyGenerates.add(sb.toString());
//            System.out.println("Done\n");
//            write(sb.toString());
            return sb.toString();
        }
        sop((count++) +" "+ node+"\n\n");
        if(node instanceof ProjectNode) {
            Node relation = node.getChild(0);
            if(relation instanceof VarNode) relation = getRelationForVar((VarNode) relation);
            if((node.getOperator().getName().split("[=]")).length > 1 && (relation instanceof SelectNode || relation instanceof  JoinNode)){
                processNestField(relation,parent,node,columns,extras);
            }
            Node project = node.getChild(1);
//            ArrayList<Node> projectFields = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            if(project instanceof ListNode) {
                StringBuilder sb1 = new StringBuilder();
                for(int i=0; i<project.getNumChildren(); i++) {
                    Node child = project.getChild(i);
                    if(child instanceof ProjectNode) {
                        Node fieldOfChild = ((ListNode) project).columns.get(i);
                        String fieldName = fieldOfChild.toString();
//                        columns.add(fieldOfChild.toString());
                        String colName =  fieldName.substring(fieldName.lastIndexOf(".")+1);
//                        columns.add("u_"+colName.substring(0,colName.length()-1));
                        generate(project,child,new HashSet<>(),extras);
//                        String fieldOfChildName = generate(project,fieldOfChild, columns, extras);
//                        String childName = generate(project,child,columns,extras);
//                        sb1.append(String.format("p.%s = %s\n",fieldOfChildName,childName));
                    }
                    else {

//                        String childName = generate(project, child, columns, extras);
                        String childName = child.toString();
//                        sb1.append(String.format("p.%s = s.%s\n",childName,childName));
                        columns.add("u_"+childName.substring(childName.lastIndexOf(".")+1));
                    }
                }
//                sb.append("fact {\n");
//                sb.append(String.format("all s:%s|some p:%s {\n",getUniqueName(relation),getUniqueName(node)));
////                sb.append(String.format("all p:%s,s:%s {\n",getUniqueName(node),getUniqueName(relation)));
//                sb.append(sb1.toString());
//                sb.append("}\n");
//                sb.append(String.format("all p:%s|some s:%s {\n",getUniqueName(node),getUniqueName(relation)));
//                sb.append(sb1.toString());
//                sb.append("}}\n");
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
//            sop("supertype = "+tableSuper);
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
        }
        else if (node instanceof JoinNode) {
//            sop("JoinNode = "+node);
            StringBuilder sb = new StringBuilder();
            String right = generate(node, node.getChild(1), columns, extras);

            String lhs = node.getChild(2).getChild(0).toString();
            String leftEnt = lhs.substring(0,lhs.lastIndexOf("."));
            lhs = lhs.substring(lhs.lastIndexOf(".")+1);


            String rhs = node.getChild(2).getChild(1).toString();
            String rightEnt = rhs.substring(0,rhs.lastIndexOf("."));
            rhs = rhs.substring(rhs.lastIndexOf(".")+1);
//            sop(leftEnt+" : "+tables.get("u_"+leftEnt));
//            sop(rightEnt+" : "+tables.get("u_"+rightEnt));

            if(((JoinNode) node).fieldType.equals("OneToMany")){
                if(tables.containsKey("u_"+rightEnt)){
                    tables.get("u_"+rightEnt).add(rhs);
                }
                else{
                    Set<String> cols= new HashSet<>();
                    cols.add(rhs);
                    tables.put("u_"+rightEnt,cols);
                }
            }
            else if(((JoinNode) node).fieldType.equals("ManyToOne")){
                if(tables.containsKey("u_"+leftEnt)){
                    tables.get("u_"+leftEnt).add(lhs);
                }
                else{
                    Set<String> cols= new HashSet<>();
                    cols.add(lhs);
                    tables.put("u_"+leftEnt,cols);
                }

            }
            else if(((JoinNode) node).fieldType.equals("ManyToMany")){

            }


            sb.append(String.format("sig %s in %s {}\n",getUniqueName(node),right));
            superType.put(getUniqueName(node),right);
            //_c denoting that is present as a field in alloy.
            if(node.getChild(0) instanceof MethodWontHandleNode)
                sb.append(String.format("fact { %s = %s }\n", getUniqueName(node), getUniqueName(node.getChild(0))));
            else
//                sb.append(String.format("fact { %s = %s.%s_c }\n", getUniqueName(node), getUniqueName(node.getChild(0)), right));
                sb.append(String.format("fact { all x:u_%s | all y:u_%s | x.%s = y.%s <=> y in %s}\n",leftEnt,rightEnt, lhs, rhs,getUniqueName(node)));
            lazyGenerates.add(sb.toString());
//            columns.add(right+"_c");
//            type.put(right+"_c", getUniqueName(node.getChild(1)));
            generate(node, node.getChild(0), columns, extras);
            return getUniqueName(node);

        }
        else if (node instanceof UnionNode) {
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
            debug.dbg("check = "+rel);
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
//        printWriter.println("Akash Testing write()");
        printWriter.println(String.format(String.valueOf(o),args));
    }
    private String getSuperType(String node) {
        if(superType.containsKey(node)) {
            return getSuperType(superType.get(node));
        }
        else return node;
    }
    private static String getUniqueName(Node node) {
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

    public boolean isNested(Node node, boolean seenList){
//        return true;
        if(node instanceof ListNode)
            seenList = true;
        if(node instanceof ProjectNode && seenList)
            return true;
        for(int i=0; i<node.getNumChildren(); i++){
            if(isNested(node.getChild(i), seenList))
                return true;
        }
        return false;
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
        String rel2 = getUniqueName(relation2); // u_Pet
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

//    public StringBuilder processNestedJoinJoinNode(Node node, List<String> relationList, StringBuilder sb, String expandFeld){
//        System.out.println("Inside processNestedJoinJoinNode ");
//        Node joinChild1 = node.getChild(0); // Sel
//        Node joinChild2 = node.getChild(1); // ClassRef(Pet)
//        Node selChild1 = joinChild1.getChild(0);
//        Node selChild2 = joinChild1.getChild(1);
//        String relation1 = "", like1 = "";
//        String relation2 = "", like2 = "";
//        if(selChild1 instanceof CartesianProdNode){
//            // ClassRef(Owner) => Owner
//            Node relation = selChild1.getChild(0);
//            relation1 = getUniqueName(relation);
//            nextUniqueNum++;
//            relation2 = getUniqueName(relation) + String.valueOf(nextUniqueNum);
////            System.out.println("Tables are = " + relation1 + ",,, " + relation2);
//            sb.append(String.format("\n sig %s in %s {} \n", relation2, relation1));
//            if(selChild2 instanceof LikeNode || selChild2 instanceof EqNode){
//                Node likeChild1 = selChild2.getChild(0);
//                Node likeChild2 = selChild2.getChild(1);
//                if(likeChild1 instanceof FieldRefNode) {
//                    like1 = getUniqueName(likeChild1);
//                    like1 = like1.substring(like1.indexOf('_') + 1);
//                    like2 = likeChild2.toString();
//                }
//                int varNum = 0;
//                String currVar = "v" + String.valueOf(varNum);
//                sb.append(String.format("fact{all %s : %s | %s.%s = %s <=> %s in %s } \n",
//                        currVar, relation1, currVar, like1, like2, currVar, relation2));
//                relationList.add(relation2);
//            }
//        }
//
//        if(joinChild2 instanceof ClassRefNode){
//            String rel2 = getUniqueName(joinChild2); // u_Pet
//            Node equals = node.getChild(2);
//
//            String eqChild1 = equals.getChild(0).toString();
//            eqChild1 = eqChild1.substring(eqChild1.indexOf('.')+1); // id
//            String eqChild2 = equals.getChild(1).toString();
//            eqChild2 = "u_" + eqChild2.substring(eqChild2.indexOf('.')+1); // u_owner_id
//            sb.append(String.format("\n fact {"));
//            sb.append(String.format(" all v0 : %s | all v1 : %s | v0.%s = v1.%s <=> v1 in v0.%s }",
//                    relation2, rel2, eqChild1, eqChild2, expandFeld));
//
//        }
//
//        return sb;
//    }

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

        relation1 = getUniqueName(relation);
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
                like1 = getUsabeName(like1, 1);
                like2 = likeChild2.toString();
                like2 = getUsabeName(like2, 2);
            }
            int varNum = 0;
            String currVar = "v" + String.valueOf(varNum);
            sb.append(String.format("fact{all %s : %s | %s.%s = %s <=> %s in %s} \n",
                    currVar, relation1, currVar, like1, like2, currVar, relation2));
            relationList.add(relation2);
        }
        return sb;
    }

    public String getUsabeName(String s, int code){
        String res = "";
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
}