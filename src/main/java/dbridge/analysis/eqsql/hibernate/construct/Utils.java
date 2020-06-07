package dbridge.analysis.eqsql.hibernate.construct;

import com.geetam.formalToActualVisitor.FormalToActual;
import com.geetam.hqlparser.CommonTreeWalk;
import dbridge.analysis.eqsql.expr.node.*;

import java.util.Collection;
import java.util.List;

import exceptions.UnknownStatementException;
import mytest.debug;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.hibernate.hql.ast.origin.hql.parse.HQLLexer;
import org.hibernate.hql.ast.origin.hql.parse.HQLParser;
import org.objectweb.asm.attrs.Annotation;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.*;

/**
 * Created by ek on 24/10/16.
 */
public class Utils {
    static VarNode fetchBase(Value source)  {
        Value base = null;
        if(source instanceof VirtualInvokeExpr)
            base = ((VirtualInvokeExpr)(source)).getBase();
        else if(source instanceof InterfaceInvokeExpr)
            base = ((InterfaceInvokeExpr)(source)).getBase();

        assert base instanceof JimpleLocal;
        Node var = NodeFactory.constructFromValue(base);
        assert var instanceof VarNode;
        return (VarNode) var;
    }

    public static VarNode getVarNode(ValueBox valueBox) throws UnknownStatementException {
        Value value = valueBox.getValue();
        debug.dbg("Utils.java", "getVarNode","value: ");
        System.out.println(value);
        debug.dbg("Utils.java", "getVarNode","Value Box: ");
        System.out.println(valueBox);
//        if(!(value instanceof JimpleLocal)){
//            throw new UnknownStatementException(value + " is not JimpleLocal");
//        }
        Node var = NodeFactory.constructFromValue2(value);
        assert var instanceof VarNode;
        return (VarNode) var;
    }

    public static VarNode getVarNode(Value value) throws UnknownStatementException {

//        if(!(value instanceof JimpleLocal)){
//            throw new UnknownStatementException(value + " is not JimpleLocal");
//        }
        Node var = NodeFactory.constructFromValue(value);
        assert var instanceof VarNode;
        return (VarNode) var;
    }


    private static  Node[] makeNodeArray(List<Value> valueList){
        Node[] valueNodes = new Node[valueList.size()];
        int i = 0;
        for (Value value : valueList) {
            Node v = NodeFactory.constructFromValue(value);
            valueNodes[i] = v;
            i++;
        }
        return valueNodes;
    }

    /**
     * Create and return a Node by parsing InvokeExpr
     */
    static Node parseInvokeExpr(InvokeExpr invokeExpr){
        String methodName = invokeExpr.getMethod().getName();
        String methodSignature = trim(invokeExpr.getMethod().toString());

        if(invokeExpr instanceof JStaticInvokeExpr){
            return parseStaticInvoke(invokeExpr, methodName);
        }
        else{
            return parseObjectInvoke(invokeExpr, methodName, methodSignature);
        }
    }

    private static Node parseStaticInvoke(InvokeExpr invokeExpr, String methodName)
    {
        System.out.println("parseStaticInvoke: name: " + methodName);
        Node retNode;
        assert methodName.equals("valueOf") || methodName.equals("unmodifiableList");
        List<Value> args = invokeExpr.getArgs();
        assert args.size() == 1;

        retNode = NodeFactory.constructFromValue(args.get(0));
        return retNode;
    }

    private static Node parseObjectInvoke(InvokeExpr invokeExpr, String methodName, String methodSignature) {
        Node[] args;
        MethodRef methodNode;
        FuncParamsNode funcParamsNode;
        VarNode baseObj;

        baseObj = fetchBase(invokeExpr);
        switch (methodName) {

            case "equals":
                args = makeNodeArray(invokeExpr.getArgs());
                assert args.length == 1;
                return new EqNode(baseObj, args[0]); //Note the return here

            case "iterator":
                methodNode = new MethodIteratorNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                break;

            case "next":
                methodNode = new MethodNextNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                break;

            case "hasNext":
                methodNode = new MethodHasNextNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                break;

            case "getHibernateTemplate":
                methodNode = new MethodGetHibernateTemplateNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                break;

            case "booleanValue":
                methodNode = new MethodBooleanValueNode();
                funcParamsNode = FuncParamsNode.getEmptyParams();
                break;

            case "add":
                args = makeNodeArray(invokeExpr.getArgs());
                funcParamsNode = new FuncParamsNode(args);
                methodNode = new MethodInsertNode();
                break;

            case "put":
                args = makeNodeArray(invokeExpr.getArgs());
                funcParamsNode = new FuncParamsNode(args);
                methodNode = new MethodMapPutNode();
                break;
            //TODO: Remove this hardcoding
            case "getPets":
//                String table = "pets";
//                Node cond = new EqNode(new VarNode("ownerId"), new VarNode("owner.id"));
//                SelectNode selectNode = new SelectNode(new ClassRefNode(table), cond);
//                return selectNode;
                return new CartesianProdNode(new ClassRefNode("pets"));
            case "loadAll":
                args = makeNodeArray(invokeExpr.getArgs());
                assert args.length == 1;
                assert args[0] instanceof ClassRefNode;
                return new CartesianProdNode((ClassRefNode)args[0]); //note the return here
            case "findOne":
                VarNode projEl = new VarNode("id");
                String tableName = invokeExpr.getMethodRef().declaringClass().toString();
                Node eqCondition = new EqNode(new VarNode("id"), new PlaceholderVarNode());
                SelectNode relation = new SelectNode(new ClassRefNode(tableName), eqCondition);
                ProjectNode projectNode = new ProjectNode(relation, projEl);
                return projectNode;
            case "findAll":
                String table = invokeExpr.getMethodRef().declaringClass().toString();
                return new CartesianProdNode(new ClassRefNode(table)); //note the return here
            default:
                if(methodName.startsWith("find")) { //TODO: could replace this check with checking if body is empty and if there is @Query annotation
                    Node relExp = getRelExpForMethod(invokeExpr);
                    if(relExp != null)
                        return relExp;
                    else {
                        String attName = methodName.substring(6);
                        tableName = invokeExpr.getMethodRef().declaringClass().toString();
                        List <Value> arglist = invokeExpr.getArgs();
                        assert arglist.size() == 1;
                        Value arg = arglist.get(0);
                        Node actualParam = NodeFactory.constructFromValue(arg);
                        Node condition = new EqNode(new VarNode(attName), actualParam);
                        SelectNode select = new SelectNode(new ClassRefNode(tableName), condition);
                        System.out.println("@Query not present, relnode = " + select);
                        return select;
                    }
                }

                else if(methodName.startsWith("add")) { //union to a field which is a collection
                    String attName = methodName.substring(3).toLowerCase();
                    System.out.println("baseObj: " + baseObj);
                    String keyStr = baseObj.toString() + "." + attName;
                    VarNode key = new VarNode(keyStr);
                    VarNode oldFieldCollection = new VarNode(keyStr);
                    Node argNode = NodeFactory.constructFromValue(invokeExpr.getArg(0));
                    UnionNode unionNode = new UnionNode(oldFieldCollection, argNode);
                    return unionNode;
                }


                args = makeNodeArray(invokeExpr.getArgs());
                funcParamsNode = new FuncParamsNode(args);
                methodNode = new MethodRefNode(methodSignature);
                break;
        }
        return new InvokeMethodNode(baseObj, methodNode, funcParamsNode);
    }

    /** Remove the angular brackets appended by SootMethod.toString() to the method signature at the beginning and
     * the end
     */
    private static String trim(String methodSign){
        return methodSign.substring(1, methodSign.length() - 1);
    }

    public static Node getRelExpForMethod(InvokeExpr invokeExpr) {
        System.out.println("getRelExpForMethod: " + invokeExpr);
        SootMethod methodInvoked = invokeExpr.getMethod();
        List <Value> args = invokeExpr.getArgs();

        System.out.println("actualargs = " + args);
        System.out.println("methodInvoked = " + methodInvoked);
        List <Tag> tagList = methodInvoked.getTags();
        System.out.println("taglist: \n" + tagList);
        /*TODO: For now assuming that the query has only one param
         */
        String paramName = getQueryParamNameFromTagList(tagList);
        String query = getQueryFromTagList(tagList);
        if(query == null) {
            return null;
        }
        CommonTree parsedTree = getParsedTree(query);
        CommonTreeWalk.postOrder(parsedTree, 0);
        System.out.println("Info collected by walk: ");
        CommonTreeWalk.printInfo();
        SelectNode relExp = CommonTreeWalk.getRelNode();
        Value actualArg = args.get(0);
        Node actaulArgDBNode = NodeFactory.constructFromValue(actualArg);
        FormalToActual formalToActualVisitor = new FormalToActual(new VarNode(paramName), actaulArgDBNode);
        relExp.accept(formalToActualVisitor);
        return relExp;
    }

    public static String getCorrespondingPlaceholderForIthParam(String methodName, int paramNumber) {
        return null;
    }

    public static CommonTree getParsedTree(String query) {
        ANTLRStringStream antlrStream = new ANTLRStringStream(query);
        HQLLexer lexer = new HQLLexer( antlrStream );
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        HQLParser parser = new HQLParser( tokens );
        HQLParser.statement_return statement = null;
        try {
            statement = parser.statement();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        // System.out.println( tokens.getTokens() );
        CommonTree tree = (CommonTree) statement.getTree();
        return tree;
    }

    public static String getQueryParamNameFromTagList(List <Tag> tagListOfMethod) {
        for(Tag tag : tagListOfMethod) {
            if (tag instanceof VisibilityParameterAnnotationTag) {
                VisibilityParameterAnnotationTag parameterAnnotationTag = (VisibilityParameterAnnotationTag) tag;
                List<VisibilityAnnotationTag> annotationList = parameterAnnotationTag.getVisibilityAnnotations();
                for(VisibilityAnnotationTag paramTag : annotationList) {
                    List <AnnotationTag> paramTagAnnotations = paramTag.getAnnotations();
                    for(AnnotationTag annotation : paramTagAnnotations) {
                        Collection<AnnotationElem> elems = annotation.getElems();
                        for(AnnotationElem elem : elems) {
                            String param = ((AnnotationStringElem)elem).getValue();
                            return param;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getQueryFromTagList(List <Tag> tagListOfMethod) {
        for(Tag tag : tagListOfMethod) {
            if(tag instanceof VisibilityAnnotationTag) {
                VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
                List <AnnotationTag> annotationList = visibilityAnnotationTag.getAnnotations();

                for(AnnotationTag annotation : annotationList) {
                     if(annotation.getType().equals("Lorg/springframework/data/jpa/repository/Query;")) {
                        for(AnnotationElem annotationElem : annotation.getElems()) {
                            if(annotationElem instanceof AnnotationStringElem) {
                                AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
                                String query = annotationStringElem.getValue();
                                return query;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
