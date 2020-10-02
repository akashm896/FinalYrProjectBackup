package dbridge.analysis.eqsql.hibernate.construct;

import com.geetam.accesspath.AccessPath;
import com.geetam.accesspath.Flatten;
import dbridge.analysis.eqsql.expr.node.*;
import exceptions.UnknownStatementException;
import mytest.debug;
import soot.*;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;

import java.util.List;

/**
 * Created by ek on 18/5/16.
 */
public class JAssignStmtCons implements StmtDIRConstructor {

    @Override
    public StmtInfo construct(Unit stmt) throws UnknownStatementException {
        debug.dbg("JAssignStmtCons.java", "construct()", "Statement = " + stmt.toString() + "\n\n");
        assert (stmt instanceof JAssignStmt);
        JAssignStmt assignStmt = (JAssignStmt) stmt;

        ValueBox leftOprnd = assignStmt.leftBox;
        Value rightOprnd = assignStmt.getRightOp();
  //      debug.dbg("JAssignStmtCons.java", "construct", "leftop = " + leftOprnd.toString());
    //    debug.dbg("JAssignStmtCons.java", "construct", "leftop value = " + leftOprnd.getValue().toString());
    //    debug.dbg("JAssignStmtCons.java", "construct", "leftop value class = " + leftOprnd.getValue().getClass().toString());

//        debug.dbg("rightOp = " + rightOprnd.toString());



        if(leftOprnd instanceof VariableBox) {
            debug.dbg("leftOperand instanceof VariableBox!!!");
            VariableBox vBox = (VariableBox) leftOprnd;
            debug.dbg(vBox.getValue().toString());

            if(vBox.getValue() instanceof FieldRef) {
                debug.dbg("Field Ref!!!!\n");
            }
        }
        List<AccessPath> accessPaths = new Flatten(1).flatten(leftOprnd.getValue());
        System.out.println("Flatten: jassignstmtcons: " + accessPaths);




        /* By default source and destination are leftOprnd and rightOprnd respectively.
        Depending on the type of rightOprnd, source may be reassigned.
         */
        Node sourceNode = NodeFactory.constructFromValue(rightOprnd);
        VarNode destNode = Utils.getVarNode(leftOprnd);

        if(rightOprnd instanceof JCastExpr){
            sourceNode = NodeFactory.constructFromValue(((JCastExpr)rightOprnd).getOpBox().getValue());
        }
        else if (rightOprnd instanceof InvokeExpr){
            InvokeExpr expr = (InvokeExpr) (rightOprnd);
            debug.dbg("JAssignStmtCons.java", "construct", "Invoke Expr: ");
         //   if(expr.toString().contains("getBy")) {
                debug.dbg("left operand class = " + leftOprnd.getClass());
           // }

            System.out.println(expr);
            sourceNode = Utils.parseInvokeExpr(expr);
        }
        else if(rightOprnd instanceof JAddExpr){
            JAddExpr expr = (JAddExpr)rightOprnd;
            Node op1 = NodeFactory.constructFromValue(expr.getOp1());
            Node op2 = NodeFactory.constructFromValue(expr.getOp2());
            sourceNode = new ArithAddNode(op1, op2);
        }
        else if (rightOprnd instanceof JInstanceFieldRef){
            //TODO This has become complex. Simplify this.
            JInstanceFieldRef ifr = (JInstanceFieldRef) rightOprnd;
            if(isDao(ifr)){
                sourceNode = new DaoNode();
            }
            else{
                String baseClass = ifr.getField().getDeclaringClass().getShortName();
                String fieldName = ifr.getField().getName();
                assert ifr.getField().getType() instanceof RefType;
                Type fieldType = ifr.getField().getType();
                String typeClass;
                if(fieldType instanceof  RefType) {
                    RefType fieldRefType = (RefType) fieldType;
                    typeClass = fieldRefType.getSootClass().getShortName();
                }
                else {
                    typeClass = fieldType.getClass().toString();
                    //For e.g. in case of i0 = this.id where int is an integer, typeClass will be set to
                    //class.soot.IntType
                }
                sourceNode = isLazy(baseClass, fieldName) ?
                        new LazyFetchNode(new FieldRefNode(baseClass, fieldName, typeClass)) :
                        new FieldRefNode(baseClass, fieldName, typeClass);
            }
        }
        return new StmtInfo(destNode, sourceNode);
    }

    /**
     * Consult the schema and check whether a particular attribute is
     * lazily fetched or not
     * @param baseClass
     * @param fieldName
     * @return true if fieldName in baseClass is specified as lazy fetch,
     * false, otherwise.
     */
    private boolean isLazy(String baseClass, String fieldName) {
        if(baseClass.equals("Order") && fieldName.equals("date")){
            return true;
        }
        return false;

        //Hardcoded implementation. Yet to implement properly.
    }

    /**
     * @return true if the field is a DAO field, false otherwise
     */
    private boolean isDao(JInstanceFieldRef ifr) {
        /* Currently, we just check if the field name ends in "Dao" to
        see if it is a Dao field.
        //TODO The correct way to implement this is to check if the type of the field
        is an instance of HibernateDaoSupport
         */
        return ifr.getField().getName().endsWith("Dao");
    }

}
























