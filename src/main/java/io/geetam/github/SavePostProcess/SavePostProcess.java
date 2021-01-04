package io.geetam.github.SavePostProcess;

import dbridge.analysis.eqsql.expr.node.*;
import dbridge.visitor.NodeVisitor;

import java.util.List;

public class SavePostProcess implements NodeVisitor {

    public List<String> uniqueFields;
    public VarNode repoVN;
    public CartesianProdNode repoCPN;

    public SavePostProcess(VarNode repoVN, List <String> uniqueFields) {
        this.repoVN = repoVN;
        this.uniqueFields = uniqueFields;
        this.repoCPN = new CartesianProdNode(repoVN);
    }

    public Node transformSave(SaveNode saveNode) {
        ListNode list = (ListNode) saveNode.getChild(0);
        List <VarNode> fieldVarNodes = list.fieldRefNodeList;
        int idInd = -1;
        for(VarNode vn : fieldVarNodes) {
            if(vn.toString().endsWith("id")) {
                idInd = fieldVarNodes.indexOf(vn);
                break;
            }
        }
        Node idField = list.getChild(idInd);
        SelectNode tuplesWithMatchingId = new SelectNode(repoCPN, new EqNode(new FieldRefNode(repoVN.toString(), "id", repoVN.toString()), idField));
        //TODO handle uniqueFields.
        UnionNode inserted = new UnionNode(repoVN, list);
        RelMinusNode minusNode = new RelMinusNode(repoCPN, tuplesWithMatchingId);
        UnionNode updated = new UnionNode(minusNode, list);
        TernaryNode root = new TernaryNode(new EqNode(tuplesWithMatchingId, new EmptySetNode()), inserted, updated);
        return root;
    }

    @Override
    public Node visit(Node node) {
        if(node instanceof SaveNode) {
            return transformSave((SaveNode) node);
        }

        for(int i = 0; i < node.getNumChildren(); i++) {
            Node child = node.getChild(i);
            if(child instanceof SaveNode) {
                Node childT = transformSave((SaveNode) child);
                node.setChild(i, childT);
            }
        }
        return node;
    }
}
