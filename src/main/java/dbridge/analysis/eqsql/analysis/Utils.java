package dbridge.analysis.eqsql.analysis;

import com.rits.cloning.Cloner;
import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import javafx.util.Pair;
import mytest.debug;

import java.util.*;

/**
 * Created by ek on 24/10/16.
 * Package local class.
 */
class Utils {

    static DIR mergeSeqDirs(DIR precedingDIR, DIR followingDIR)  {
        debug d = new debug("analysis/Utils.java", "mergeSeqDirs()");
        /* Resolve variable references in followingDIR with their expressions from precedingDIR */
        d.dg("following dir domain: " + followingDIR.getVeMap().keySet());
        d.dg("following dir: " + followingDIR);
        d.dg("following dir vemap: " + followingDIR.getVeMap());
        Cloner cloner = new Cloner();
        DIR mergedDIR = new DIR();
        mergedDIR.getVeMap().putAll(precedingDIR.getVeMap());
        // Map <VarNode, Node> folMapClone = cloner.deepClone(followingDIR.getVeMap());
        for(VarNode k : followingDIR.getVeMap().keySet()) {
            d.dg("key: " + k);

            //  Node val = folMapClone.get(k);
            Node val2 = followingDIR.getVeMap().get(k);
          //  List <VarNode> varNodes = getVarNodes(val2);
         //   d.dg("varNodes: " + varNodes);


           // d.dg(val2);
            d.dg("before resolution: " + val2);

           // d.dg("before resolution flattened " + getFlattenedTree(val2));
            //  Node resolved = val2.accept(resolver);
            d.dg("precedingDIR: " + precedingDIR);
            Node resolved = resolveTree(val2, precedingDIR);
            d.dg("after resolution: " + resolved);

            mergedDIR.insert(k, resolved);

        }

//        for (Map.Entry<VarNode, Node> d2Entry : followingDIR.getVeMap().entrySet()) {
//            d.dg("following DIR key: " + d2Entry.getKey());
////            if(d2Entry.getKey().toString().equals("__modelattribute__selections")) {
////                d.dg("break");
////            }
//           // d.dg("followingDIR (key,val) = " + d2Entry.getKey() + " -> " + d2Entry.getValue());
//            Node node = cloner.deepClone(d2Entry.getValue());
//
//            d.dg("node: " + node);
//            Node resolvedNode = node.accept(resolver);
//            mergedDIR.insert(d2Entry.getKey(), resolvedNode);
//          //  d2Entry.setValue(resolvedNode);
//        }
        d.dg("mergedDIR: " + mergedDIR);

        /* Now merge precedingDIR and followingDIR. If followingDIR is a loop region, then
         * some variables may be marked not algebrizable. This information propagates
          * during merging. */
//        for (Map.Entry<VarNode, Node> d2entry : followingDIR.getVeMap().entrySet()) {
//            VarNode key = d2entry.getKey();
//            if(UnAlgNode.isUnAlgNode(precedingDIR.find(key))){
//                continue;//do not insert entry if it is already marked as an UnAlgNode
//            }
//            precedingDIR.insert(key, d2entry.getValue());
//        }

        /* The two loops above cannot be merged. Although they iterate on the same set of values,
        * the entries from followingDIR get modified in the first loop BEFORE they are inserted
        * into precedingDIR in the second loop */

        d.dg("return");
        return mergedDIR;
    }

    static Node extractCondition(DIR dir){
        debug d = new debug("analysis/Utils.java", "extractCondition()");
        VarNode condVar = VarNode.getACondVar();
        d.dg("condVar: " + condVar);
        d.dg("input dir (of headRegion): " + dir);
        assert dir.contains(condVar);
        Node cond = dir.find(condVar);
        return cond;
    }

    public static Node invertCondition(Node cond) {
        try {
            if (cond instanceof EqNode) {
                return new NotEqNode(cond.getChild(0), cond.getChild(1));
            } else if (cond instanceof NotEqNode) {
                return new EqNode(cond.getChild(0), cond.getChild(1));
            } else if (cond instanceof LessThanEqNode) {
                return new MoreThanNode(cond.getChild(0), cond.getChild(1));
            }
            else {
                throw new RuntimeException("condition operator: " + cond + " not supported");
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return null;
    }

    public static List<VarNode> getVarNodes(Node root) {
        List <VarNode> ret = new ArrayList<>();
        Queue <Node> q = new LinkedList<>();
        q.add(root);
        while (q.isEmpty() == false) {
            Node top = q.poll();
            if(top.getNumChildren() > 0)
                q.addAll(Arrays.asList(top.getChildren()));
            else if(top instanceof VarNode) {
                ret.add((VarNode) top);
            }
        }
        return ret;
    }

    public static List<Node> getFlattenedTree(Node root) {
        List <Node> ret = new ArrayList<>();
        Queue <Node> q = new LinkedList<>();
        q.add(root);
        while (q.isEmpty() == false) {
            Node top = q.poll();
            if(top.getNumChildren() > 0)
                q.addAll(Arrays.asList(top.getChildren()));
            else
                ret.add(top);
        }
        return ret;
    }

    public static boolean dirContainsNode(Node node, DIR dir) {
        return node instanceof VarNode && dir.getVeMap().containsKey((VarNode)node);
    }

    public static Node resolveTree(Node root, DIR dir) {
        debug d = new debug("analysis/Utils.java", "resolveTree()");
        Cloner cloner = new Cloner();
  //      d.dg("in: " + root);
      //  d.dg("dir: " + dir);
        Node ret = root;
        if(root.getNumChildren() == 0) {
            if(root instanceof VarNode && dir.getVeMap().containsKey((VarNode)root)) {
                ret = dir.getVeMap().get((VarNode)root);
            }
        }
        else {
            Stack<Node> stack = new Stack();
            stack.add(root);
            while (stack.isEmpty() == false) {
                Node top = stack.pop();
                d.dg("stack pop");
             //   d.dg("top: " + top);
            //    d.dg("top num children: " + top.getNumChildren());
                if (top.getNumChildren() > 0) {
                    List <Pair<Integer, Node>> newChildValueList = new LinkedList<>();
                    for (int i = 0; i < top.getChildren().length; i++) {
                        Node child = top.getChildren()[i];
                        d.dg("i = " + i + " child of top:" + child);
                        if (dirContainsNode(child, dir)) {

                            Node resolvedValChild = cloner.deepClone(dir.getVeMap().get(child));
                            d.dg("top: " + top);
                            d.dg("resolvedValChild: " + resolvedValChild);
                            top.getChildren()[i] = resolvedValChild;
                            d.dg("new top: " + top);

                            newChildValueList.add(new Pair<>(i, resolvedValChild));
                        } else {
                            d.dg("stack add");
                            stack.add(child);
                        }
                    }
                    d.dg("newChildValueList.size(): " + newChildValueList.size());
                    d.dg("newChildValueList: " + newChildValueList);
//                    for(Pair <Integer, Node> ncv : newChildValueList) {
//                        top.setChild(ncv.getKey(), ncv.getValue());
//                    }
                }
            }
        }
     //   d.dg("out: " + ret);
        d.dg("return");
        return ret;
    }




}
