package io.geetam.github.accesspath;

import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import io.geetam.github.loopHandler.LoopIteratorCollectionHandler;
import mytest.debug;
import soot.*;
import soot.tagkit.*;

import java.util.*;
import com.iisc.pav.AlloyGenerator;
import static dbridge.analysis.eqsql.hibernate.construct.Utils.*;


//Myimpl
public class NRA implements Cloneable{
    public static int NRA_BOUND=4;

    public static Set<String> visited=new HashSet<>();
    //This method will generate nested Relational expr

    public static HashSet<String> cloned(HashSet<String> visited) throws CloneNotSupportedException {
        HashSet<String> newVisited = new HashSet<>();
        newVisited = (HashSet<String>) visited.clone();
        return newVisited;
    }
    public static Node genExprNra(SootField nestedField,Type nested_Entity,Type base_Entity, Node base_Entity_dag,int depth) throws CloneNotSupportedException {
        LoopIteratorCollectionHandler.isNRAProperty = true;
        System.out.println("This is NRA case");
        debug d=new debug("NRA.java","genExprNra()");
        d.dg("baseEntity : "+ nested_Entity);
        if(depth==NRA_BOUND)return new NullNode();

        if(nestedField!=null)
            d.dg("nested field name= "+nestedField.getName());
//        String nestEntityName= bcelActualCollectionFieldType(base_Entity.toString(),nestedField.getName());
//        d.dg("nestedField Entity= "+nestEntityName);
        d.dg("nested Entity = "+ nested_Entity.toString());
        SootClass nestClass= Scene.v().getSootClass(nested_Entity.toString());
        AlloyGenerator.tableSignatures.add(nestClass);
        if(!AlloyGenerator.tableAndFields.containsKey(nestClass))
            processTableandFields(nestClass);
//        d.dg("classes = "+Scene.v().getClasses());
//        d.dg("methods = "+nestClass.getMethods());
        d.dg("From soot typeclass= "+ nestClass);
//        d.dg(Flatten.flattenEntityClass(nestClass));
//        d.dg("AllFields = "+(nestClass).getFields());
        List<SootField> allFields= Flatten.getAllFields(nestClass);
        d.dg(nestClass.getType()+" fields= "+allFields);
        d.dg("fields count= "+allFields.size());

        int nestedEntity_field_count=allFields.size();
//        int nestedEntity_field_count=0;
//        for(SootField sf:allFields){
//            if(isVisited(visited,nestClass+"~"+sf.getName())){
//                d.dg(sf+" Field is visited skipping count");
//                continue;
//            }
//            nestedEntity_field_count++;
//        }
//        d.dg("children count = "+nestedEntity_field_count);

        ListNode cols=new ListNode(new Node[nestedEntity_field_count]);
        for(int i=0;i<nestedEntity_field_count;i++){
            cols.setChild(i,new NullNode());
        }
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
//                SootClass sfEntityClass= Scene.v().getSootClass(sfEntity);
//                VarNode col= new VarNode(sf.getName());
//                cols.columns.add(new FieldRefNode(nestClass.getName(),sf.getName(),sfEntityClass.getName()));
//                cols.setChild(index++,col);
//                continue;
//            }
            if(isStarToManyField(sf)  || isAStarToOneField(sf) || isTransientField(sf)){
                String sfEntity="";

                d.dg("sf.getname="+sf.getName());
//                d.dg("visited String=  "+nestClass+"~"+sf.getName());
//                if(isVisited(visited,nestClass+"~"+sf.getName())){
//                    d.dg("Field is visited skipping");
//                    continue;
//                }

//                Set<String> newVisited = new HashSet<>();
//                newVisited = cloned((HashSet<String>) visited);
//                newVisited.add(nestClass+"~"+sf.getName());
//                visited.add(nestClass+"~"+sf.getName());

//                d.dg("visited = "+visited);
//                d.dg("newVisited = "+newVisited);
                if(sf.toString().contains("BaseUGC: "))
                    System.out.println("breakpoint");
                sfEntity=bcelActualCollectionFieldType(nestClass.toString(),sf.getName());
                d.dg("sfEntity=" + sfEntity);
//                if(sfEntity == null) {
//                    System.exit(0);
//                }
                SootClass sfEntityClass= Scene.v().getSootClass(sfEntity);

//                String baseClass = nestClass.getName().substring(nestClass.getName().lastIndexOf(".")+1);
                String baseClass = nestClass.getName();
                d.dg("baseclass = "+baseClass);
//                String fieldClass = sfEntityClass.getName().substring(sfEntityClass.getName().lastIndexOf(".")+1);
                String fieldClass = sfEntityClass.getName();
                d.dg("fieldclass = "+fieldClass);


                String lhs= getJoinedColumn(sf.getTags());
                if(lhs==null ){
                    lhs="lhs";
                }
                String primAttr= getPrimaryField( Flatten.getAllFields(sfEntityClass));
                String rhs=fieldClass+"."+primAttr;

                EqNode cond=getJoinCondFromField(sf,nestClass,sfEntityClass);

//                if(isOneToManyField(sf)){
//                    d.dg("OneToMany Field");
//                    cond=getCondFromOneToMany(sf.getTags(),nestClass);
//                }
//                else if(isManyToOneField(sf)){
//                    d.dg("ManyToOne Field");
//                    cond=getCondFromManyToOne(sf.getTags(),nestClass,sfEntityClass);
//                }
//                else if(isManyToManyField(sf)){
//                    d.dg("ManyToMany Field");
//                    cond=getCondFromManyToMany(sf.getTags(),nestClass,sfEntityClass);
//                }
//                else{
//                    cond= new EqNode(new VarNode(lhs),new VarNode(rhs));
//                }
//                EqNode condition= new EqNode(new VarNode(lhs),new VarNode(rhs));
                JoinNode j=new JoinNode(new AlphaNode(new ClassRefNode(baseClass)), new ClassRefNode(fieldClass),cond);
//                JoinNode j=new JoinNode(new ClassRefNode(baseClass), new ClassRefNode(fieldClass),cond);
                if(isOneToManyField(sf)){
                    d.dg("OneToMany Field");
                    j.fieldType="OneToMany";
                }
                else if(isManyToOneField(sf)){
                    d.dg("ManyToOne Field");
                    j.fieldType="ManyToOne";
                }
                else if(isManyToManyField(sf)){
                    d.dg("ManyToMany Field");
                    j.fieldType="ManyToMany";
                }
                else if(isOneToOneField(sf)){
                    d.dg("OneToOne Field");
                    d.dg("onetoone field tags = "+sf.getTags());
                    j.fieldType="OneToOne";
                }
//                SelectNode select=new SelectNode(j,condition);
                cols.columns.add(new FieldRefNode(nestClass.getName(),sf.getName(),sfEntityClass.getName()));
                if(isTransientField(sf)){ // Akash added
                    VarNode col= new VarNode(sf.getName());
//                    nestExpr = genExprNra(sf,nestClass.toString(),select,newVisited,calleeVEMap);
//                    nestExpr= genExprNra(sf,nestClass.toString(),select,visited,calleeVEMap);
                    cols.setChild(index++,col);
                }
                else{
                    if(depth>NRA_BOUND)
                        continue;

                    Node nestExpr;
//                    nestExpr = genExprNra(sf,nestClass.toString(),select,newVisited,calleeVEMap);
//                    nestExpr= genExprNra(sf,nestClass.toString(),select,visited,calleeVEMap);
                    nestExpr= genExprNra(sf,sfEntityClass.getType(),nested_Entity,j,depth+1);
                    d.dg("nestexpr= "+nestExpr );
                    cols.setChild(index++,nestExpr);

                }


            }
            else{
                d.dg("Primitive field ");
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
        if(nestedField!=null){
            String baseEntityName = base_Entity.toString().substring(base_Entity.toString().lastIndexOf(".")+1);
            p.getOperator().setName(baseEntityName + "." + nestedField.getName() +"=Pi");
        }
        d.dg("projectNode name: "+p.getOperator());
        return p;
    }

    public static EqNode getJoinCondFromField(SootField sf,SootClass nestClass,SootClass sfEntityClass){
        debug d = new debug("NRA.java", "getJoinCondFromField()");
        EqNode cond=new EqNode(new VarNode("lhs.lhs"),new VarNode("rhs.rhs"));

        if(isOneToManyField(sf)){
            d.dg("OneToMany Field");
            cond=getCondFromOneToMany(sf,nestClass,sfEntityClass);
        }
        else if(isManyToOneField(sf)){
            d.dg("ManyToOne Field");
            cond=getCondFromManyToOne(sf,nestClass,sfEntityClass);
        }
        else if(isManyToManyField(sf)){
            d.dg("ManyToMany Field");
            cond=getCondFromManyToMany(sf,nestClass,sfEntityClass);
        }
        else if(isOneToOneField(sf)){
//            cond = NullNode;
//            cond= new EqNode(new VarNode("lhs"),new VarNode("rhs"));
            cond = getCondFromOneToOne(sf,nestClass,sfEntityClass);
        }
        return cond;
    }

    public static EqNode getCondFromOneToOne(SootField sf,SootClass entity,SootClass sfEntity){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromOneToOne()");
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy="";
        for(AnnotationTag an:anns){
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems= an.getElems();
            for(AnnotationElem ae:anElems){
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if(ae.getName().equals("mappedBy")){
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem)ae).getValue();
                    d.dg("mappedBy= "+mappedBy);
                    break;
                }
            }
        }

        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol="";


        for(SootField f:fields){
            if(f.getName().equals(mappedBy)){
                joinCol = getJoinedColumn(f.getTags());
                break;
            }
        }

//        String primField=getShortName(entity.getName())+"."+getPrimaryField(Flatten.getAllFields(entity));
        String primField="Alpha."+getPrimaryField(Flatten.getAllFields(entity));
//        d.dg("Primary field of "+entity.getName()+" : "+primField);


        EqNode cond=new EqNode(new VarNode("lhs.lhs"),new VarNode("rhs.rhs"));;
        if(joinCol !="")
            cond=new EqNode(new VarNode(primField),new VarNode(getShortName(sfEntity.getName())+"."+joinCol));
        d.dg("join condition = "+cond);
        return cond;
    }

    public static EqNode getCondFromOneToMany(SootField sf,SootClass entity,SootClass sfEntity){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromOneToMany()");
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy="";
        for(AnnotationTag an:anns){
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems= an.getElems();
            for(AnnotationElem ae:anElems){
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if(ae.getName().equals("mappedBy")){
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem)ae).getValue();
                    d.dg("mappedBy= "+mappedBy);
                    break;
                }
            }
        }

        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol="";
        for(SootField f:fields){
            if(f.getName().equals(mappedBy)){
                joinCol = getJoinedColumn(f.getTags());
                break;
            }
        }

//        String primField=getShortName(entity.getName())+"."+getPrimaryField(Flatten.getAllFields(entity));
        String primField="Alpha."+getPrimaryField(Flatten.getAllFields(entity));
//        d.dg("Primary field of "+entity.getName()+" : "+primField);


        EqNode cond=new EqNode(new VarNode("lhs.lhs"),new VarNode("rhs.rhs"));;
        if(joinCol !="")
            cond=new EqNode(new VarNode(primField),new VarNode(getShortName(sfEntity.getName())+"."+joinCol));
        d.dg("join condition = "+cond);
        return cond;
    }

    public static EqNode getCondFromManyToOne(SootField sf,SootClass entity,SootClass sfEntity){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromManyToOne()");

        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String joinedCol="";
        for(AnnotationTag an:anns) {
            if (an.getType().equals("Ljavax/persistence/JoinColumn;")) {
                Collection<AnnotationElem> anElems = an.getElems();
                d.dg("anElems = "+anElems);
                for (AnnotationElem ae : anElems) {
//                    d.dg("joined Column value =" + ae.toString().substring(ae.toString().lastIndexOf(": ") + 1));
                    joinedCol= ((AnnotationStringElem)ae).getValue();
                    break;
                }
            }
        }


        String primField=getShortName(sfEntity.getName())+"."+getPrimaryField(Flatten.getAllFields(entity));
//            d.dg("Primary field of "+entity.getName()+" : "+primField);


//        EqNode cond=new EqNode(new VarNode(getShortName(entity.getName())+"."+joinedCol),new VarNode(primField));
        EqNode cond = cond=new EqNode(new VarNode("lhs.lhs"),new VarNode("rhs.rhs"));
        if(joinedCol !="")
            cond=new EqNode(new VarNode("Alpha."+joinedCol),new VarNode(primField));
        d.dg("cond = "+cond);
        return cond;
    }

    public static EqNode getCondFromManyToMany(SootField sf,SootClass entity,SootClass sfEntity) {
        List<Tag> tags = sf.getTags();
        debug d = new debug("NRA.java", "getCondFromManyToMany()");
        d.dg("tags = " + tags);
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy = "";
        for (AnnotationTag an : anns) {
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems = an.getElems();
            for (AnnotationElem ae : anElems) {
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if (ae.getName().equals("mappedBy")) {
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem) ae).getValue();
                    d.dg("mappedBy= " + mappedBy);
                    break;
                }
            }
        }

//        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol = "";
        String invJoinCol = "";

        if (!mappedBy.equals("")) {
            for (SootField f : Flatten.getAllFields(sfEntity)) {
                if (f.getName().equals(mappedBy)) {
                    sf = f;
                    break;
                }
            }
        }


        tags = sf.getTags();
        anns = Utils.getAnnotationTags(tags);
        for (AnnotationTag an : anns) {
            if (an.getType().equals("Ljavax/persistence/JoinTable;")) {
                Collection<AnnotationElem> anElems = an.getElems();
//                d.dg("anElems = "+anElems);
//                d.dg("anElems.size  = "+anElems.size());
                for (AnnotationElem ae : anElems) {
//                    d.dg("joined Column value =" + ae.toString().substring(ae.toString().lastIndexOf(": ") + 1));
//                    d.dg("ae type =  "+ae.getClass().getTypeName()+" ::");
                    if (ae.getClass().getTypeName().equals("soot.tagkit.AnnotationArrayElem")) {
//                        d.dg("AnnotationArrayElem values ="+((AnnotationArrayElem)ae).getName());

                        if (((AnnotationArrayElem) ae).getName().equals("joinColumns")) {
//                            d.dg("ae values = "+((AnnotationArrayElem)ae).getValues());
                            for (AnnotationElem a : ((AnnotationArrayElem) ae).getValues()) {
//                                d.dg("Value = "+((AnnotationAnnotationElem)a).getValue().getElems());
                                for (AnnotationElem at : ((AnnotationAnnotationElem) a).getValue().getElems()) {
                                    d.dg(((AnnotationStringElem) at).getValue());
                                    joinCol = ((AnnotationStringElem) at).getValue();
                                }
                            }
                        }

                        if (((AnnotationArrayElem) ae).getName().equals("inverseJoinColumns")) {
//                            d.dg("ae values = "+((AnnotationArrayElem)ae).getValues());
                            for (AnnotationElem a : ((AnnotationArrayElem) ae).getValues()) {
//                                d.dg("Value = "+((AnnotationAnnotationElem)a).getValue().getElems());
                                for (AnnotationElem at : ((AnnotationAnnotationElem) a).getValue().getElems()) {
                                    d.dg(((AnnotationStringElem) at).getValue());
                                    invJoinCol = ((AnnotationStringElem) at).getValue();
                                }
                            }
                        }

                    }

                }
            }
        }

//        String primField=getShortName(entity.getName())+"."+getPrimaryField(Flatten.getAllFields(entity));
//        d.dg("Primary field of "+entity.getName()+" : "+primField);


        EqNode cond = new EqNode(new VarNode("lhs.lhs"), new VarNode("rhs.rhs"));
        ;
        if (joinCol != "" && invJoinCol != "") {

            if (!mappedBy.equals("")) {
//            cond=new EqNode(new VarNode(getShortName(entity.getName())+"."+joinCol),new VarNode(getShortName(sfEntity.getName())+"."+invJoinCol));
                cond = new EqNode(new VarNode("Alpha." + joinCol), new VarNode(getShortName(sfEntity.getName()) + "." + invJoinCol));
            } else {
//            cond=new EqNode(new VarNode(getShortName(entity.getName())+"."+invJoinCol),new VarNode(getShortName(sfEntity.getName())+"."+joinCol));
                cond = new EqNode(new VarNode("Alpha." + invJoinCol), new VarNode(getShortName(sfEntity.getName()) + "." + joinCol));
            }
        }

        d.dg("join condition = "+cond);
        return cond;
    }

    public static String getShortName(String name){
        return (name.substring(name.lastIndexOf(".")+1));
//        return name;
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

//    public static String getClassName(String className){
//        return "";
//    }

    public static String getJoinedColumn(List<Tag> tags) {
        debug d = new debug("NRA.java", "getJoinedColumn()");
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);

        for(AnnotationTag an:anns){
            if(an.getType().equals("Ljavax/persistence/JoinColumn;")){
                Collection<AnnotationElem> anElems= an.getElems();
                for(AnnotationElem ae:anElems){
                    d.dg("joined Column value= "+ae.toString().substring(ae.toString().lastIndexOf(": ")+1));
//                    return ae.toString().substring(ae.toString().lastIndexOf(": ")+1);
                    return ((AnnotationStringElem)ae).getValue();
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



    public static void processTableandFields(SootClass entity){
        debug d=new debug("NRA.java","processTableandFields");
        if(entity.getName().contains("Pair"))return;
        d.dg(entity.getName()+" fields = "+Flatten.getAllFields(entity));
        HashMap<String,String> fieldsAndType = new HashMap<>();
        for(SootField sf:Flatten.getAllFields(entity)){
            String type="FieldData";
            if(AccessPath.isPrimitiveType(sf.getType())){
                type="FieldData";
            }
            if(!AccessPath.isTerminalType(sf.getType())){
                type = getShortName(sf.getType().toString());
            }
            else if(AccessPath.isCollectionType(sf.getType())){
                type = AccessPath.getCollectionType(sf);
                if((type.contains("lang") || type.contains("math") || type.contains("util"))){
                    type = "FieldData";
                }
                else{
                    type = getShortName(type);
                }
            }
            d.dg("sf "+sf+"  type="+type);
//            fieldsAndType.put(sf.getName(),getShortName(type));
            fieldsAndType.put(sf.getName(),type);
            if(isOneToOneField(sf) ){
                SootClass sfentity = Scene.v().getSootClass(sf.getType().toString());
                if(AccessPath.isCollectionType(sf.getType())){
                    sfentity = Scene.v().getSootClass(AccessPath.getCollectionType(sf));

                }
                getAnnotatedOneToOneColumns(sf,entity,sfentity,fieldsAndType);
            }
            else if(isManyToManyField(sf)){
                SootClass sfentity = Scene.v().getSootClass(sf.getType().toString());
                if(AccessPath.isCollectionType(sf.getType())){
                    sfentity = Scene.v().getSootClass(AccessPath.getCollectionType(sf));

                }
                getAnnotatedManyToManyColumns(sf,entity,sfentity,fieldsAndType);
            }
            else if(isManyToOneField(sf)){
                SootClass sfentity = Scene.v().getSootClass(sf.getType().toString());
                if(AccessPath.isCollectionType(sf.getType())){
                    sfentity = Scene.v().getSootClass(AccessPath.getCollectionType(sf));

                }
                getAnnotatedManyToOneColumns(sf,entity,sfentity,fieldsAndType);
            }
            else if(isOneToManyField(sf)){
                SootClass sfentity = Scene.v().getSootClass(sf.getType().toString());
                if(AccessPath.isCollectionType(sf.getType())){
                    sfentity = Scene.v().getSootClass(AccessPath.getCollectionType(sf));

                }
                getAnnotatedOneToManyColumns(sf,entity,sfentity,fieldsAndType);
            }
        }
        AlloyGenerator.tableAndFields.put(getShortName(entity.getName()),fieldsAndType);

    }

    public static void processAnnotatedColumns(SootClass entity,SootField sf, String tableName){
        HashMap<String,String> fieldsAndType = AlloyGenerator.tableAndFields.get(tableName);
        String type = "";

//            if()
    }

    public static void getAnnotatedManyToManyColumns(SootField sf,SootClass entity,SootClass sfEntity,HashMap<String,String> fieldsAndType) {
        List<Tag> tags = sf.getTags();
        debug d = new debug("NRA.java", "getCondFromManyToMany()");
        d.dg("tags = " + tags);
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy = "";
        for (AnnotationTag an : anns) {
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems = an.getElems();
            for (AnnotationElem ae : anElems) {
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if (ae.getName().equals("mappedBy")) {
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem) ae).getValue();
                    d.dg("mappedBy= " + mappedBy);
                    break;
                }
            }
        }

//        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol = "";
        String invJoinCol = "";

        if (!mappedBy.equals("")) {
            for (SootField f : Flatten.getAllFields(sfEntity)) {
                if (f.getName().equals(mappedBy)) {
                    sf = f;
                    break;
                }
            }
        }


        tags = sf.getTags();
        anns = Utils.getAnnotationTags(tags);
        for (AnnotationTag an : anns) {
            if (an.getType().equals("Ljavax/persistence/JoinTable;")) {
                Collection<AnnotationElem> anElems = an.getElems();
//                d.dg("anElems = "+anElems);
//                d.dg("anElems.size  = "+anElems.size());
                for (AnnotationElem ae : anElems) {
//                    d.dg("joined Column value =" + ae.toString().substring(ae.toString().lastIndexOf(": ") + 1));
//                    d.dg("ae type =  "+ae.getClass().getTypeName()+" ::");
                    if (ae.getClass().getTypeName().equals("soot.tagkit.AnnotationArrayElem")) {
//                        d.dg("AnnotationArrayElem values ="+((AnnotationArrayElem)ae).getName());

                        if (((AnnotationArrayElem) ae).getName().equals("joinColumns")) {
//                            d.dg("ae values = "+((AnnotationArrayElem)ae).getValues());
                            for (AnnotationElem a : ((AnnotationArrayElem) ae).getValues()) {
//                                d.dg("Value = "+((AnnotationAnnotationElem)a).getValue().getElems());
                                for (AnnotationElem at : ((AnnotationAnnotationElem) a).getValue().getElems()) {
                                    d.dg(((AnnotationStringElem) at).getValue());
                                    joinCol = ((AnnotationStringElem) at).getValue();
                                }
                            }
                        }

                        if (((AnnotationArrayElem) ae).getName().equals("inverseJoinColumns")) {
//                            d.dg("ae values = "+((AnnotationArrayElem)ae).getValues());
                            for (AnnotationElem a : ((AnnotationArrayElem) ae).getValues()) {
//                                d.dg("Value = "+((AnnotationAnnotationElem)a).getValue().getElems());
                                for (AnnotationElem at : ((AnnotationAnnotationElem) a).getValue().getElems()) {
                                    d.dg(((AnnotationStringElem) at).getValue());
                                    invJoinCol = ((AnnotationStringElem) at).getValue();
                                }
                            }
                        }

                    }

                }
            }
        }

        if (joinCol != "" && invJoinCol != "") {
            fieldsAndType.put(joinCol,"FieldData");
            fieldsAndType.put(invJoinCol,"FieldData");

        }

    }

    public static void getAnnotatedManyToOneColumns(SootField sf,SootClass entity,SootClass sfEntity,HashMap<String,String> fieldsAndType){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromManyToOne()");

        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String joinCol="";
        for(AnnotationTag an:anns) {
            if (an.getType().equals("Ljavax/persistence/JoinColumn;")) {
                Collection<AnnotationElem> anElems = an.getElems();
                d.dg("anElems = "+anElems);
                for (AnnotationElem ae : anElems) {
//                    d.dg("joined Column value =" + ae.toString().substring(ae.toString().lastIndexOf(": ") + 1));
                    joinCol= ((AnnotationStringElem)ae).getValue();
                    break;
                }
            }
        }

        if(joinCol !="")
            fieldsAndType.put(joinCol,"FieldData");
    }

    public static void getAnnotatedOneToManyColumns(SootField sf,SootClass entity,SootClass sfEntity,HashMap<String,String> fieldsAndType){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromOneToMany()");
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy="";
        for(AnnotationTag an:anns){
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems= an.getElems();
            for(AnnotationElem ae:anElems){
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if(ae.getName().equals("mappedBy")){
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem)ae).getValue();
                    d.dg("mappedBy= "+mappedBy);
                    break;
                }
            }
        }

        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol="";
        for(SootField f:fields){
            if(f.getName().equals(mappedBy)){
                joinCol = getJoinedColumn(f.getTags());
                break;
            }
        }



        EqNode cond=new EqNode(new VarNode("lhs.lhs"),new VarNode("rhs.rhs"));;
        if(joinCol !="")
            fieldsAndType.put(joinCol,"FieldData");
//        return cond;
    }

    public static void getAnnotatedOneToOneColumns(SootField sf,SootClass entity,SootClass sfEntity,HashMap<String,String> fieldsAndType){
        List<Tag> tags=sf.getTags();
        debug d = new debug("NRA.java", "getCondFromOneToOne()");
        List<AnnotationTag> anns = Utils.getAnnotationTags(tags);
        String mappedBy="";
        for(AnnotationTag an:anns){
//            d.dg("ann = "+an);
            Collection<AnnotationElem> anElems= an.getElems();
            for(AnnotationElem ae:anElems){
//                d.dg("ae type ="+ae.getClass());
//                d.dg("ae name ="+ae.getName());
                if(ae.getName().equals("mappedBy")){
//                    d.dg("anElem = "+ae);
                    mappedBy = ((AnnotationStringElem)ae).getValue();
                    d.dg("mappedBy= "+mappedBy);
                    break;
                }
            }
        }

        List<SootField> fields= Flatten.getAllFields(sfEntity);
        String joinCol="";


        for(SootField f:fields){
            if(f.getName().equals(mappedBy)){
                joinCol = getJoinedColumn(f.getTags());
                break;
            }
        }

        if(joinCol !="")
            fieldsAndType.put(joinCol,"FieldData");
    }




}