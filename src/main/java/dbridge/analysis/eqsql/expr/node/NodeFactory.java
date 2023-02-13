/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.expr.operator.Operator;
import dbridge.analysis.eqsql.expr.operator.SelectOp;
import soot.Value;
import soot.jimple.ClassConstant;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JimpleLocal;

/**
 * Created by ek on 26/10/16.
 * Factory method with limited use cases as of now. Currently, it is used to
 * create a VarNode if a Value is JimpleLocal or a ValueNode otherwise.
 */
public class NodeFactory {
    public static Node constructFromValue(Value value){

        if(value instanceof JimpleLocal){
            return new VarNode((JimpleLocal)value);
        }
        else if (value instanceof JNewExpr){
            return BottomNode.v();
        }
        else if (value instanceof IntConstant && ((IntConstant) value).value == 0){
            return new ZeroNode();
        }
        else if (value instanceof IntConstant && ((IntConstant) value).value == 1){
            return new OneNode();
        }
        else if(value instanceof NullConstant) {
            return new NullNode();
        }
        /* Creating special separate nodes for constants is fine
         * for a few constants. But we have to come up with a better
         * approach for representing all constants using a single
         * operator in a way that can be used in transformations as well.
         * This is TODO */
        else if (value instanceof ClassConstant){
            ClassConstant classConstant = (ClassConstant)value;
            return new ClassRefNode(classConstant);
        }
        else {
            return new ValueNode(value);
        }
    }

    public static Node constructFromValue2(Value value){
        return new VarNode(value);
    }


    /**
     * Construct a new Node depending on the type of passed operator. This function is intended for use in
     * transformation rules.
     * @param opType Type of the operator
     * @param children Child nodes, if any. If operator has 0 arity, this argument should be null.
     * @return a new Node depending on the type of passed operator
     */
    public static Node constructFromOp(OpType opType, Object... children){
        Node node;

        switch (opType) {
            case And:
                node = consAnd(children);
                break;
            case Bottom:
                node = BottomNode.v();
                break;
            case ClassRef:
                node = consClassRef(children);
                break;
            case Eq:
                node = consEq(children);
                break;
            case Gt:
                node = consGt(children);
                break;
            case Lt:
                node = consLt(children);
                break;
            case Exist:
                node = consExist(children);
                break;
            case FieldRef:
                node = consFieldRef(children);
                break;
            case Fold:
                node = consFold(children);
                break;
            case FuncParams:
                node = consFuncParams(children);
                break;
            case MethodHasNext:
                node = consHasNext();
                break;
            case MethodBooleanValue:
                node = consBooleanValue();
                break;
            case If:
                node = consIf(children);
                break;
            case InvokeMethod:
                node = consInvMethod(children);
                break;
            case MethodInsert:
                node = consMethodInsert();
                break;
            case MethodIterator:
                node = consMethodIterator();
                break;
            case MethodNext:
                node = consMethodNext();
                break;
            case NotEq:
                node = consNotEq(children);
                break;
            case NotExist:
                node = consNotExists(children);
                break;
            case Or:
                node = consOr(children);
                break;
            case Param:
                node = consParam(children);
                break;
            case Project:
                node = consProject(children);
                break;
            case GroupBy:
                node = consGroupBy(children);
                break;
            case Select:
                node = consSelect(children);
                break;
            case SelfRef:
                node = consSelfRef();
                break;
            case StringConst:
                node = consStringConst(children);
                break;
            case Ternary:
                node = consTernary(children);
                break;
            case Value:
            case Var:
                assert children != null;
                assert children[0] instanceof Value;
                node = constructFromValue((Value)children[0]);
                break;
            case Zero:
                node = new ZeroNode();
                break;
            case CountStar:
                node = new CountStarNode();
                break;
            case FuncExpr:
                node = consFuncExpr(children);
                break;
            case CartesianProd:
                node = consCartesianProd(children);
                break;
            case Seq:
                node = consSeq(children);
                break;
            case Union:
                node = new UnionNode((Node) children[0], (Node) children[1]);
                break;
            case Join:
                node = new JoinNode((Node) children[0], (Node) children[1],new NullNode());
                break;
            case In:
               node = new InNode((Node) children[0], (Node) children[1]);
                break;
            case ArrayRef:
                node = new ArrayRefNode((Node) children[0], (Node) children[1]);
                break;
            case AggSum:
                node = new AggSumNode((Node) children[0], (Node) children[1]);
                break;
            case Id:
                node = new IdNode();
                break;
            case ArithAdd:
                node = new ArithAddNode((Node) children[0], (Node) children[1]);
                break;
            case Save:
                node = new SaveNode((Node) children[0], (Node) children[1]);
                break;
            case RelMinus:
                node = new RelMinusNode((Node) children[0], (Node) children[1]);
                break;
            case MoreThan:
                node = new MoreThanNode((Node) children[0], (Node) children[1]);
                break;
            default:
                System.err.println("DEBUG Unknown operator. Returning null");
                node = null;
                System.exit(-1);
        }
        return node;
    }

    private static Node consSeq(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        return new SeqNode((Node)children[0],
                            (Node)children[1]);
    }

    private static Node consCartesianProd(Object[] children) {
        for (Object child : children) {
            assert child instanceof Node;
        }
        Node[] childNodes = new Node[children.length];
        for (int i = 0; i < children.length; i++) {
            childNodes[i] = (Node) children[i];
        }

        return new CartesianProdNode(childNodes);
    }

    private static Node consGroupBy(Object[] children) {
        assert children[0] instanceof ProjectNode;
        assert children[1] instanceof FieldRefNode;

        return new GroupByNode((ProjectNode)children[0],
                (FieldRefNode)children[1]);
    }

    private static Node consFuncExpr(Object[] children) {
        assert children[0] instanceof Node;

        return new FuncExprNode((Node)children[0]);
    }

    private static Node consTernary(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        assert children[2] instanceof Node;

        return new TernaryNode((Node)children[0], (Node)children[1], (Node)children[2]);
    }

    private static Node consStringConst(Object[] children) {
        assert children[0] instanceof String;
        return new StringConstNode((String)children[0]);
    }

    private static Node consSelfRef() {
        return new SelfRefNode();
    }

    private static Node consSelect(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        return new SelectNode((Node)children[0], (Node)children[1]);
    }

    private static Node consProject(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;

        return new ProjectNode((Node)children[0], (Node)children[1]);
    }

    private static Node consParam(Object[] children) {
        assert (children[0] instanceof Integer);
        return new ParamNode((Integer)children[0]);
    }

    private static Node consOr(Object[] children) {
        Node node;
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        Node cond1 = (Node) children[0];
        Node cond2 = (Node) children[1];

        node = new OrNode(cond1, cond2);
        return node;

    }

    private static Node consNotExists(Object[] children) {
        assert children[0] instanceof ProjectNode;
        return new NotExistNode((ProjectNode)children[0]);

    }

    private static Node consNotEq(Object[] children) {
        Object arg1 = children[0];
        Object arg2 = children[1];

        assert arg1 instanceof Node;
        assert arg2 instanceof Node;

        return new NotEqNode((Node)arg1, (Node)arg2);
    }

    private static Node consMethodNext() {
        return new MethodNextNode();
    }

    private static Node consMethodIterator() {
        return new MethodIteratorNode();
    }

    private static Node consMethodInsert() {
        return new MethodInsertNode();
    }

    private static Node consInvMethod(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof MethodRef;
        assert children[2] instanceof FuncParamsNode;

        return new InvokeMethodNode((Node)children[0],
                (MethodRef)children[1],
                (FuncParamsNode)children[2]);
    }

    private static Node consIf(Object[] children) {
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        return new IfNode((Node)children[0], (Node)children[1]);
    }

    private static Node consHasNext() {
        return new MethodHasNextNode();
    }

    private static Node consBooleanValue() {
        return new MethodBooleanValueNode();
    }

    private static Node consFuncParams(Object[] children) {
        assert children[0] instanceof Node[];
        return new FuncParamsNode((Node[])children);
    }

    private static Node consFold(Object[] children) {
        assert children[0] instanceof FuncExprNode;
        assert children[1] instanceof Node;
        assert children[2] instanceof Node;
        assert children[3] instanceof Node;
        return new FoldNode((FuncExprNode)children[0],(Node)children[1],(Node)children[2], (Node)children[3]);
    }

    private static Node consFieldRef(Object[] children) {
        Object baseClass = children[0];
        Object fieldName = children[1];
        Object typeClass = children[2];

        assert baseClass instanceof String;
        assert fieldName instanceof String;
        assert typeClass instanceof String;

        return new FieldRefNode((String)baseClass, (String)fieldName, (String)typeClass);
    }

    private static Node consExist(Object[] children) {
        assert children[0] instanceof ProjectNode;
        return new ExistNode((ProjectNode)children[0]);
    }

    private static Node consGt(Object[] children) {
        Object arg1 = children[0];
        Object arg2 = children[1];

        assert arg1 instanceof Node;
        assert arg2 instanceof Node;

        return new GtNode((Node)arg1, (Node)arg2);
    }

    private static Node consLt(Object[] children) {
        Object arg1 = children[0];
        Object arg2 = children[1];

        assert arg1 instanceof Node;
        assert arg2 instanceof Node;

        return new LtNode((Node)arg1, (Node)arg2);
    }


    private static Node consEq(Object[] children) {
        Object arg1 = children[0];
        Object arg2 = children[1];

        assert arg1 instanceof Node;
        assert arg2 instanceof Node;

        return new EqNode((Node)arg1, (Node)arg2);
    }

    private static Node consClassRef(Object[] children) {
        Object arg1 = children[0];
        assert (arg1 instanceof String);

        return new ClassRefNode((String)arg1);
    }

    private static Node consAnd(Object[] children) {
        Node node;
        assert children[0] instanceof Node;
        assert children[1] instanceof Node;
        Node cond1 = (Node) children[0];
        Node cond2 = (Node) children[1];

        node = new AndNode(cond1, cond2);
        return node;
    }
}
