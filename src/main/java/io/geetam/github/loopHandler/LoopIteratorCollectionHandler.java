package io.geetam.github.loopHandler;

import com.iisc.pav.AlloyGenerator;
import dbridge.analysis.eqsql.FuncStackAnalyzer;
import dbridge.analysis.eqsql.analysis.DIRSequentialRegionAnalyzerN;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.VarNode;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import dbridge.analysis.region.regions.ARegion;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.toolkits.graph.Block;

import java.util.*;

import static dbridge.analysis.eqsql.analysis.Utils.getFlattenedTree;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.fetchBaseValue;

public class LoopIteratorCollectionHandler {
    public static Map<Node, Node> changedLoopPrimitiveFieldsMap = new HashMap();
    public static Map<Node, Node> changedLoopEntityFieldsMap = new HashMap();
//    public static Map<VarNode, Node> summarizedLoopVEMap = new HashMap<>();
//    public static Map<VarNode, Node> unknownLoopVEMap = new HashMap<>();
    public static Set<Node> loopPatternSummarizedKeys = new HashSet<>();

    public static Set<String> collectionVariable = new HashSet<>();
    public static Map<VarNode, Node> loopPatternSummarizedVEMaps = new HashMap();
    public static boolean isNRAProperty = false;
    public static void replacePrimitives(Node toReplaceVeMap, Node changedKey, Node changedVEMap) {
        String opShortName = getShortName(changedVEMap.getOperator().toString());
        changedVEMap.getOperator().setName(opShortName);
        String k = changedVEMap.toString();
        if(toReplaceVeMap == null || toReplaceVeMap.getNumChildren() < 2)
            return;
        Node listNode = toReplaceVeMap.getChild(1);
        int cnum = 0;
        for(Node child : listNode.getChildren()){
            String childstr = child.toString();
            if(changedKey.toString().contains(child.toString())){
                listNode.setChild(cnum, changedVEMap);
            }
            cnum++;
        }
//        System.out.println(listNode);


    }



    public static void replaceEntity(Node toReplaceVeMap, Node changedKey, Node toInlineVEMap) {
        if(toReplaceVeMap == null || toReplaceVeMap.getNumChildren() < 2)
            return;
        if(!canReplace(toInlineVEMap))
            return;
        Node listNode = toReplaceVeMap.getChild(1);
        int cnum = 0;
        for(Node child : listNode.getChildren()){
//            if(changedKey.toString().contains(child.toString())){
            if(changedKey.toString().contains(child.toString().toLowerCase()) || child.toString().toLowerCase().contains(changedKey.toString())){
                listNode.setChild(cnum, toInlineVEMap);
            }
            cnum++;
        }

//        System.out.println(toReplaceVeMap + " " + changedKey + " " + toInlineVEMap);
    }

    public static boolean canReplace(Node toInlineVEMap){
        List<Node> leaves = new ArrayList<>(getFlattenedTree(toInlineVEMap));
        for(Node leaf : leaves){
            String s = leaf.toString();
            if(s.contains("NextOp") || s.contains("NextOp"))
                return false;
        }
        return true;
    }

    public void printJimpleLHSRHS(ARegion region){
        Block basicBlock = region.getHead();
        Iterator<Unit> iterator = basicBlock.iterator();
        while(iterator.hasNext()) {
            Unit curUnit = iterator.next();
            if (curUnit instanceof JAssignStmt ) {
                JAssignStmt stmt = (JAssignStmt) curUnit;
                Value leftVal = stmt.leftBox.getValue();
                Value rhsVal = stmt.rightBox.getValue();
                System.out.println("lhs in loop is = " + leftVal.toString());
                if(rhsVal instanceof InvokeExpr) {
                    InvokeExpr invokeExpr = (InvokeExpr) rhsVal;
                    Value base = fetchBaseValue(invokeExpr);
                    System.out.println("Invoked rhs base = " + base);
                }
            }

        }
    }
    public void inLineCollectionIteratorToCollection(List<Node> iteratorEntityFields, Map<VarNode, Node> bodyVEMap, ARegion region, DIR loopDIR){
        Map<String, DIR> prevMethodsVEMap = FuncStackAnalyzer.funcDIRMap;
        System.out.println("iteratorEntityFields \n" + iteratorEntityFields);
        System.out.println("received VEMap = \n" + bodyVEMap);
        System.out.println("previousMethodsVEMap are \n" + prevMethodsVEMap);


        List<String> changedEntityInLoop = new ArrayList<>();
        for(Node node : iteratorEntityFields){
            changedEntityInLoop.add(node.toString().substring(node.toString().lastIndexOf('.')+1));
        }

        List<Node> toInline = new ArrayList<>();
        for(Node node : bodyVEMap.keySet()) {
            List<Node> leaves = new ArrayList<>(getFlattenedTree(bodyVEMap.get(node)));
            boolean canInLine = true;
            if(leaves.get(0).toString().contains("FieldRef"))
                continue;
            for(Node leaf : leaves){
                String s = leaf.toString();
                if(s.contains("NextOp") || s.contains("NextOp")){
                    canInLine = false;
                    break;
                }
            }
            if(canInLine)
                toInline.add(node);
            System.out.println("Child of bodyVEMap node " + node.toString() + " is : \n" + getFlattenedTree(bodyVEMap.get(node)));
        }

        System.out.println("ToInLne nodes are = " + toInline);
//        DIR dir = new DAGTillNow().getDag();
        DIR dir = DIRSequentialRegionAnalyzerN.INSTANCE.getMergedDIR();
        System.out.println("merged dir are : " + dir);
        for(Node key : toInline) {
            Node valueToInLine = bodyVEMap.get(key);
            String name = getFieldName(key.toString()); // will have visits
            System.out.println("Value to inline = \n" + bodyVEMap.get(key));
            // find the owner vemap where visits is present as child
//            for(String method : FuncStackAnalyzer.funcDIRMap.keySet()){
//                DIR dir = FuncStackAnalyzer.funcDIRMap.get(method); // contains entire veMap for each methods executed earlier

//            for(DIR dir : )
            for(Map.Entry<VarNode, Node> veMap : dir.getVeMap().entrySet()){
                if(needsReplacement(veMap.getValue(), name, valueToInLine, null, 0)) {
                    System.out.println("Value to Inline = " + valueToInLine.toString());
                    System.out.println("Replacement reuired for " + veMap.getKey() + " in field = " + name);
                    System.out.println("New Replaced veMap = " + veMap.getValue().toString());
                    loopDIR.insert(veMap.getKey(), veMap.getValue());
                }
            }

        }
        System.out.println("dagTillNow = \n" + dir);
    }

    public boolean needsReplacement(Node veMap, String name, Node valueToInLine, Node prev, int idx){
        if(veMap == null)
            return false;
        if(veMap.isLeaf() && veMap.toString().contains(name)) {
//            putNewValue(veMap, valueToInLine);
//            String uniqName = getUniqueName(veMap);
//            System.out.println("curr uniqName = " + uniqName);
            if(prev!= null) {
                System.out.println("Setting child of " + prev.toString() + " to " + valueToInLine.toString());
                Node cpy = valueToInLine;
                prev.setChild(idx, cpy);
            }
//            System.out.println("found in "+idx+" th child of parent = " + prev.toString());
            return true;
        }
        boolean res = false;
        Node[] childs = veMap.getChildren();
        if(childs != null) {
            for (int i=0; i<childs.length; i++) {
                Node child = childs[i];
                res = res || needsReplacement(child, name, valueToInLine, veMap, i);
            }
        }
        System.out.println("Result of replacement is " + res);
        return res;
    }

//    public void putNewValue(Node veMap, Node valueToInLine){
//        for(int i=0; i<veMap.getNumChildren(); i++)
//            veMap.setChild(i, new Node());
//    }

    public static String getShortName(String name){
        if(name.indexOf('.') == -1)
            return name;
        name  = name.substring(name.lastIndexOf('.')+1);
        return name.substring(0, name.length()-1);
    }

    public String getFieldName(String name){
        if(name.contains("."))
            name = name.substring(name.lastIndexOf('.')+1);
        return name;
    }

    public static void replaceNodeWithNode(Node parent, Node toReplaceNode, Node newNode){
        if(parent == null)
            return;
        for(int i=0; i<parent.getNumChildren(); i++){
            if(parent.getChild(i).toString().equals(toReplaceNode.toString())) // compare node condition
                parent.setChild(i, newNode);
            else
                replaceNodeWithNode(parent.getChild(i), toReplaceNode, newNode);
        }
    }

} // class ends


