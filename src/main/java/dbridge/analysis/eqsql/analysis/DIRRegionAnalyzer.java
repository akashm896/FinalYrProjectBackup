package dbridge.analysis.eqsql.analysis;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.expr.node.*;
import dbridge.analysis.eqsql.hibernate.construct.StmtDIRConstructionHandler;
import dbridge.analysis.eqsql.hibernate.construct.StmtInfo;
import dbridge.analysis.eqsql.utils;
import dbridge.analysis.eqsql.hibernate.construct.Utils;
import exceptions.DIRConstructionException;
import exceptions.UnknownStatementException;
import dbridge.analysis.eqsql.util.VarResolver;
import dbridge.analysis.region.regions.ARegion;
import mytest.debug;
import soot.*;
import soot.JastAddJ.*;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.Block;
import soot.util.Switch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by ek on 4/5/16.
 */
public class DIRRegionAnalyzer extends AbstractDIRRegionAnalyzer {

    /* Singleton */
    private DIRRegionAnalyzer(){};
    public static DIRRegionAnalyzer INSTANCE = new DIRRegionAnalyzer();

    @Override
    public DIR constructDIR(ARegion region) {
        Block basicBlock = region.getHead();
        Iterator<Unit> iterator = basicBlock.iterator();

        DIR dir = new DIR(); //dir for this region

        StmtInfo stmtInfo = StmtInfo.nullInfo;

        while (iterator.hasNext()) {
            Unit curUnit = iterator.next();
            debug.dbg("DIRRegionAnalyzer.java", "constructDIR()", "curUnit = " + curUnit.toString());
            try {

//                if(curUnit instanceof JAssignStmt && ((JAssignStmt) curUnit).rightBox.toString().contains("getBy")) {
//                  //  debug.dbg("DIRRegionAnalyzer.java", "constructDIR(): ",
//                  //          "curunit instanceof jassignstmt and contains getBy");
//                    JAssignStmt getByStatement = (JAssignStmt) curUnit;
//                    System.out.println(getByStatement.leftBox.getValue().getType());
//                    Type typeOfLeft = getByStatement.leftBox.getValue().getType();
//                    Value leftVal = getByStatement.leftBox.getValue();
//                    SootClass classOfLeft = Scene.v().loadClass(typeOfLeft.toString(), 1);
//               //     List <VarNode> fieldAccesses = utils.getVarNodeFieldAccessListOfBaseVar(leftVal);
//                    //Instead of creating a dbridge-fieldrefnode explicitly, maybe a soot-fieldref can be created which
//                    //can be passed on to constructFromValue to get dbridge equivalent. Is it useful?
//                    for(SootField sf : classOfLeft.getFields()) {
//                        VarNode varNode = utils.getFieldAccessVarNode(leftVal, sf);
//                        SQLSelectValueNode rval = new SQLSelectValueNode(leftVal, sf.getName(), "dummy_table");
//                        dir.insert(varNode, rval);
//                    }
//                }
                if(curUnit.toString().contains("addAttribute")) {
                    JInvokeStmt addAttributeInvoke = (JInvokeStmt) curUnit;
                    InvokeExpr addAttributeExpr = addAttributeInvoke.getInvokeExpr();
                    List <Value> args = addAttributeExpr.getArgs();
                    assert args.size() == 2: "too many/few args to attAttribute call";
                    Value attributeName = args.get(0);
                    Value attributeValue = args.get(1);
                    String attributeNameStr = attributeName.toString().substring(1, attributeName.toString().length() - 1);
                    JimpleLocal local = new JimpleLocal("__modelattribute__" + attributeNameStr, attributeValue.getType());
                    JAssignStmt stmt = new JAssignStmt(local, attributeValue);

                    stmtInfo = StmtDIRConstructionHandler.constructDagSS(stmt);
                    if (stmtInfo == StmtInfo.nullInfo) {
                        continue;
                    }
                    VarNode dest = stmtInfo.getDest();
                    Node source = stmtInfo.getSource();

                    VarResolver varResolver = new VarResolver(dir);
                    Node resolvedSource = source.accept(varResolver);

                    dir.insert(dest, resolvedSource);
                }

                else {

                    stmtInfo = StmtDIRConstructionHandler.constructDagSS(curUnit);
                    if (stmtInfo == StmtInfo.nullInfo) {
                        continue;
                    }
                    VarNode dest = stmtInfo.getDest();
                    Node source = stmtInfo.getSource();

                    VarResolver varResolver = new VarResolver(dir);
                    Node resolvedSource = source.accept(varResolver);

                    dir.insert(dest, resolvedSource);
                }
            }
            catch (UnknownStatementException e) {
                //Temporary fix. Todo: pass exception to higher level.
                e.printStackTrace();
                return null;
            }
        }

        return dir;
    }
}
