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
package io.geetam.github.SavePostProcess;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.expr.operator.FieldRefOp;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import dbridge.visitor.NodeVisitor;
import io.geetam.github.RepoToEntity.RepoToEntity;
import io.geetam.github.accesspath.AccessPath;
import mytest.debug;
import soot.*;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.util.Chain;

import java.util.*;
import java.util.stream.Collectors;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.getAnnotationTags;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.mapDBFetchAccessGraph;

public class SavePostProcess implements NodeVisitor {

    public List<String> uniqueFields;
    public VarNode repoVN;
    public CartesianProdNode repoCPN;
    private String idfieldname = "id";
    private DIR dir;
    public Map <VarNode, Node> cascadedEntries;

    public SavePostProcess(VarNode repoVN, List <String> uniqueFields, DIR dir) {
        debug d = new debug("SavePostProcess.java", "SavePostProcess()");
        d.dg("repo varnode: " + repoVN);
        this.repoVN = repoVN;
        this.uniqueFields = uniqueFields;
        this.repoCPN = new CartesianProdNode(repoVN);
        d.dg("repo type = " + repoVN.repoType);
        if(repoVN.repoType != null) {
            initializeIdFieldName(repoVN);
        } else {
            d.wrn("repository type is null, as a consequence, cannot find id field");
        }
        this.dir = dir;
        this.cascadedEntries = new HashMap<>();
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
        TupleNode tuple = (TupleNode) saveNode.getChild(1);
        ListNode list = (ListNode) tuple.getChild(1);
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
        if(idInd == -1) {
            d.dg("break");
            return saveNode;
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
            SaveNode savenode = (SaveNode) node;
            handleCascading(dir, savenode);
            Node ret = transformSave(savenode);
            return ret;
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

    private void handleCascading(DIR dir, SaveNode savenode) {
        debug d = new debug("SavePostProcess.java", "handleCascading()");
        VarNode argsave = savenode.getArgumentToSave();
        Node argmapping = dir.find(argsave);
        String argsavestr = argsave.toString();
        Set<VarNode> accesspathsofarg = dir.getVeMap().keySet().stream()
                .filter(accp -> accp.toString().startsWith(argsavestr + "."))
                .collect(Collectors.toSet());
        d.dg("accesspathsofarg: " + accesspathsofarg);
        Value local = argsave.jimpleVar;
        if(local == null) {
            d.wrn("arg varnode does cont contain jimple local");
            d.wrn("not handling cascading");
            return;
        }
        RefType rtArg = (RefType) local.getType();
        Collection<SootField> collFs = Utils.collectionFields(rtArg.getSootClass());
        Map<VarNode, Node> auxVEMap = new HashMap<>();
        d.dg("check mapDBFetchAccessGraph in savepostProcess");
        mapDBFetchAccessGraph(auxVEMap, new AccessPath(argsave.toString()), dir.find(argsave), rtArg.getSootClass(), 0);
        for(SootField sf : collFs) {
            d.dg("coll f: " + sf.getName());
            AccessPath basedotf = new AccessPath(argsave.toString() + "." + sf.getName());
            Node incomingMappingField = dir.find(basedotf.toVarNode());
            //no update
            if(incomingMappingField == null)
                continue;
            Node unmutMapping = auxVEMap.get(basedotf.toVarNode());
            if(!unmutMapping.toString().equals(incomingMappingField.toString())) {
                VarNode implicitTable = new VarNode(sf.getName() + "Repo");
                Node ascRem = new RelMinusNode(implicitTable, unmutMapping);
                Node newVal = new UnionNode(ascRem, incomingMappingField);
                Node tern = new TernaryNode(new EqNode(argmapping, new NullNode()), implicitTable, newVal);
                cascadedEntries.put(implicitTable, tern);
            }
        }
    }
}
