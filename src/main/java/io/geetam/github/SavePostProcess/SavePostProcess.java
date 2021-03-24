package io.geetam.github.SavePostProcess;

import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.expr.operator.FieldRefOp;
import dbridge.visitor.NodeVisitor;
import io.geetam.github.RepoToEntity.RepoToEntity;
import mytest.debug;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.util.Chain;

import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.getAnnotationTags;

public class SavePostProcess implements NodeVisitor {

    public List<String> uniqueFields;
    public VarNode repoVN;
    public CartesianProdNode repoCPN;
    private String idfieldname = "id";

    public SavePostProcess(VarNode repoVN, List <String> uniqueFields) {
        debug d = new debug("SavePostProcess.java", "SavePostProcess()");
        d.dg("repo varnode: " + repoVN);
        this.repoVN = repoVN;
        this.uniqueFields = uniqueFields;
        this.repoCPN = new CartesianProdNode(repoVN);
        d.dg("repo type = " + repoVN.repoType);
        if(repoVN.repoType != null) {
            initializeIdFieldName(repoVN);
        } else {
            d.dg("repository type is null, as a consequence, cannot find id field");
        }
    }

    private void initializeIdFieldName(VarNode repoVN) {
        String entity = RepoToEntity.getEntityForRepo(repoVN.repoType.toString());
        System.out.println("entity = " + entity);
        SootClass entitycls = Scene.v().forceResolve(entity, 1);
        System.out.println("entity soot cls: " + entitycls);
        Chain<SootField> entityfields = entitycls.getFields();
        for (SootField sf : entityfields) {
            System.out.println("field: " + sf);
            List<Tag> fieldtags = sf.getTags();
            List<AnnotationTag> annotations = getAnnotationTags(fieldtags);
            for (AnnotationTag at : annotations) {
                System.out.println("annotation tag: " + at);
                if (at.getType().equals("Ljavax/persistence/Id;")) {
                    System.out.println("id field = " + sf);
                    System.out.println("id field name = " + sf.getName());
                    idfieldname = sf.getName();
                }
            }
            System.out.println("field tags: " + sf.getTags());
        }
    }

    public Node transformSave(SaveNode saveNode) {
        debug d = new debug("SavePostProcess.java", "transformSave()");
        d.dg("saveNode = " + saveNode);
        ListNode list = (ListNode) saveNode.getChild(0);
        List <FieldRefNode> columns = list.columns;
        d.dg("columns: " + columns);
        int idInd = -1;
        d.dg("idfieldname: " + idfieldname);
        for(FieldRefNode frn : columns) {
            FieldRefOp frnOp = (FieldRefOp) frn.getOperator();
            if(frnOp.getFieldName().equals(idfieldname)) {
                idInd = columns.indexOf(frn);
                break;
            }
        }
        d.dg("idInd = " + idInd);
        Node idField = list.getChild(idInd);
        d.dg("idField: " + idField);
        SelectNode tuplesWithMatchingId = new SelectNode(repoCPN, new EqNode(new FieldRefNode(repoVN.toString(), idfieldname, repoVN.toString()), idField));
        //TODO handle uniqueFields.
        UnionNode inserted = new UnionNode(repoVN, list);
        RelMinusNode minusNode = new RelMinusNode(repoCPN, tuplesWithMatchingId);
        UnionNode updated = new UnionNode(minusNode, list);
        TernaryNode root = new TernaryNode(new EqNode(tuplesWithMatchingId, new EmptySetNode()), inserted, updated);
        d.dg("transformed save: " + root);
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
