package io.geetam.github.accesspath;

import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import mytest.debug;
import soot.*;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;

import java.util.*;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.*;


//Myimpl
public class NRA implements Cloneable{

    public static Set<String> visited=new HashSet<>();
    //This method will generate nested Relational expr

    public static HashSet<String> cloned(HashSet<String> visited) throws CloneNotSupportedException {
        HashSet<String> newVisited = new HashSet<>();
        newVisited = (HashSet<String>) visited.clone();
        return newVisited;
    }
    public static Node genExprNra(SootField nestedField,String base_Entity, Node base_Entity_dag,Set<String> visited,Map<VarNode,Node> calleeVEMap) throws CloneNotSupportedException {

        debug d=new debug("NRA.java","genExprNra()");
        d.dg("baseEntity type : "+ base_Entity);
        String base_EntityName= base_Entity.substring(base_Entity.lastIndexOf(".")+1);
        d.dg("baseEntity NAme="+base_EntityName);
        d.dg("nested field name= "+nestedField.getName());
        String nestEntityName= bcelActualCollectionFieldType(base_Entity,nestedField.getName());
        d.dg("nestedField Entity= "+nestEntityName);

        SootClass nestClass= Scene.v().getSootClass(nestEntityName);
        d.dg("From soot typeclass= "+ nestClass);

        List<SootField> allFields= Flatten.getAllFields(nestClass);


        d.dg(nestClass.getType()+" fields= "+allFields);
        int nestedEntity_field_count=0;
        for(SootField sf:allFields){
            if(isVisited(visited,nestClass+"~"+sf.getName())){
                d.dg(sf+" Field is visited skipping count");
                continue;
            }
            nestedEntity_field_count++;
        }

        ListNode cols=new ListNode(new Node[nestedEntity_field_count]);
        d.dg("new ListNode = "+cols.columns.size());
        int index=0;
        for(SootField sf : allFields) {
            d.dg("visited= "+visited);
            d.dg("field ="+sf);
            d.dg("tags="+sf.getTags());
            d.dg("isStarToManyField(sf) = "+isStarToManyField(sf));
//            d.dg("isTransientField(sf) = "+isTransientField(sf));
            d.dg("isStarToOne field = "+ isAStarToOneField(sf));

//            if(isTransientField(sf)){
//                if(isVisited(visited,nestClass+"~"+sf.getName())){
//                    d.dg("Field is visited skipping");
//                    continue;
//                }
//                String sfEntity="";
//                sfEntity=bcelActualCollectionFieldType(nestClass.toString(),sf.getName());
//                SootClass sfEntityName= Scene.v().getSootClass(sfEntity);
//                VarNode col= new VarNode(sf.getName());
//                cols.columns.add(new FieldRefNode(nestClass.getName(),sf.getName(),sfEntityName.getName()));
//                cols.setChild(index++,col);
//                continue;
//            }
            if(isStarToManyField(sf)  || isAStarToOneField(sf) || isTransientField(sf)){
                String sfEntity="";

                d.dg("sf.getname="+sf.getName());
                d.dg("visited String=  "+nestClass+"~"+sf.getName());
                if(isVisited(visited,nestClass+"~"+sf.getName())){
                    d.dg("Field is visited skipping");
                    continue;
                }

//                Set<String> newVisited = new HashSet<>();
//                newVisited = cloned((HashSet<String>) visited);
//                newVisited.add(nestClass+"~"+sf.getName());
                visited.add(nestClass+"~"+sf.getName());

                d.dg("visited = "+visited);
//                d.dg("newVisited = "+newVisited);
                sfEntity=bcelActualCollectionFieldType(nestClass.toString(),sf.getName());



                d.dg("sfEntity=" + sfEntity);
                SootClass sfEntityName= Scene.v().getSootClass(sfEntity);

                String baseClass = nestClass.getName().substring(nestClass.getName().lastIndexOf(".")+1);
                d.dg("baseclas = "+baseClass);
                String fieldClass = sfEntityName.getName().substring(sfEntityName.getName().lastIndexOf(".")+1);



                String lhs= getJoinedColumn(sf.getTags());
                if(lhs==null ){
                    lhs="lhs";
                }
                String primAttr= getPrimaryField( Flatten.getAllFields(sfEntityName));
                String rhs=fieldClass+"."+primAttr;

                EqNode condition= new EqNode(new VarNode(lhs),new VarNode(rhs));
                JoinNode j=new JoinNode(new AlphaNode(new ClassRefNode(baseClass)), new ClassRefNode(fieldClass),condition);
//                SelectNode select=new SelectNode(j,condition);
                cols.columns.add(new FieldRefNode(nestClass.getName(),sf.getName(),sfEntityName.getName()));
                if(isTransientField(sf)){
                    VarNode col= new VarNode(sf.getName());
                    cols.setChild(index++,col);
                }
                else{
                    Node nestExpr;
//                    nestExpr = genExprNra(sf,nestClass.toString(),select,newVisited,calleeVEMap);
//                    nestExpr= genExprNra(sf,nestClass.toString(),select,visited,calleeVEMap);
                    nestExpr= genExprNra(sf,nestClass.toString(),j,visited,calleeVEMap);
                    d.dg("nestexpr= "+nestExpr );
                    cols.setChild(index++,nestExpr);

                }


            }
            else{
                VarNode col= new VarNode(sf.getName());
                cols.columns.add(new FieldRefNode(nestClass.getName(),sf.getName(),nestClass.getName()));
                cols.setChild(index++,col);
            }

        }

//    if(varRefType.toString().equals("java.util.Optional")) {
//        d.dg("OptionalTypeInfo.typeMap: " + OptionalTypeInfo.typeMap);
//        String actualType = OptionalTypeInfo.typeMap.get(ap.toString());
//        d.dg("optional vars actual type: " + actualType);
//        typeClass = Scene.v().getSootClass(actualType);
//    }

        d.dg("ListNode columns="+ cols.columns);
        ProjectNode p=new ProjectNode(base_Entity_dag,cols);
        p.getOperator().setName(base_EntityName + "." + nestedField.getName() +"=Pi");
        d.dg("projectNode name: "+p.getOperator());
        return p;
    }

    public static String getPrimaryField(List<SootField> allfields) {
        debug d = new debug("NRA.java", "getPrimaryField()");
        for (SootField f : allfields) {
            List<Tag> tags = f.getTags();
//        d.dg("field= " + f);
//        d.dg(" tags = " + tags);
            List<AnnotationTag> anns = Utils.getAnnotationTags(tags);

            for(AnnotationTag an:anns){
//            d.dg("Anntn type= "+ an.getType());
                if(an.getType().equals("Ljavax/persistence/Id;")){
                    return f.getName();
                }



            }
        }

        return null;

    }

    public static String getClassName(String className){
        return "";
    }

    public static String getJoinedColumn(List<Tag> tags) {
        debug d = new debug("NRA.java", "getJoinedColumn()");
//        for (SootField f : allfields) {
//            List<Tag> tags = f.getTags();
//        d.dg("field= " + f);
//        d.dg(" tags = " + tags);
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);

        for(AnnotationTag an:anns){
//            d.dg("Anntn type= "+ an.getType());
            if(an.getType().equals("Ljavax/persistence/JoinColumn;")){
                Collection<AnnotationElem> anElems= an.getElems();
                for(AnnotationElem ae:anElems){
                    d.dg("joined Column value= "+ae.toString().substring(ae.toString().lastIndexOf(": ")+1));
                    return ae.toString().substring(ae.toString().lastIndexOf(": ")+1);
                }
            }



        }


        return null;

    }

    public static boolean isVisited(Set<String> visited,String field){

        if(visited.contains(field))return true;
        else return false;
    }

    public static SelectNode getSelNode(Type entityType,SootField id){
        CartesianProdNode relation=new CartesianProdNode(new VarNode(entityType.toString()));

//    List<SootField> leftFields=Flatten.getAllFields(((RefType) entityType).getSootClass());

//    VarNode id=new VarNode();
        Flatten.getAccp((Value) entityType,id);
        EqNode equals=new EqNode(new VarNode(id.getName()),new VarNode(""));
        SelectNode select=new SelectNode(relation,new VarNode("condition"));

        return select;
    }

    public static SootField getPrimaryField(SootClass nestEntityClass){
        List<SootField> fields=Flatten.getAllFields(nestEntityClass);

        for(SootField sf:fields){
            List<AnnotationTag> annTags=Utils.getAnnotationTags(sf.getTags());
            for(AnnotationTag tg:annTags){
                if(tg.getType().toString().equals("Ljavax/persistence/Id;")){
                    return sf;
                }
            }
        }


        return null;
    }

    public static Node getJoinNode(){
        JoinNode join=new JoinNode(new VarNode("left"),new VarNode("right"),new NullNode());

        return join;
    }


}