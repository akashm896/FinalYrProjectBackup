package dbridge.analysis.eqsql.hibernate.construct;

import dbridge.analysis.eqsql.expr.node.*;

import java.util.List;

import exceptions.UnknownStatementException;
import mytest.debug;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JimpleLocal;

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
        Node retNode;
        assert methodName.equals("valueOf");
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
            default:
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
}
