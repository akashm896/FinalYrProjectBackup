/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql;

import dbridge.analysis.eqsql.expr.DIR;
import dbridge.analysis.eqsql.hibernate.metadata.TableMetaData;
import dbridge.analysis.region.regions.ARegion;
import soot.Local;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;

import java.util.*;

/**
 * Created by ek on 12/5/16.
 * Data structure for moving data between different components of ee dag analysis. Singleton.
 */
public class EqSQLSharedData {
    private HashMap<String, String> insertPoint_StmtChain;

    public HashMap<String, TableMetaData> classToTableMapping;
    private Set<String> func_set;
    public HashMap<String, String> tableAlias;
    public int aliasCounter;
    private int paramCounter;
    HashMap<String, Object> paramReplacement;

    //Tejas
    Map<Value, String> varQuery;

    //for DCE
    Map<String, Local> locals;
    Map<String, SootMethodRef> methodMap;

    Map<ARegion,List<Unit>> lICMStmts = new HashMap<ARegion,List<Unit>>();
	Map<Value, DIR> varAfterReg = new HashMap<Value,DIR>();

    private EqSQLSharedData() {
        initialize();
    }

    /**
     * Resets values of all fields. This method is to be called after an analysis is finished.
     */
    public void reset() {
        initialize();
    }

    private void initialize() {
        insertPoint_StmtChain = new HashMap<String, String>();
        classToTableMapping = new HashMap<String, TableMetaData>();
        func_set = new HashSet<String>();
        tableAlias = new HashMap<String, String>();
        aliasCounter = 1;
        paramCounter = 0;
        paramReplacement = new HashMap<String, Object>();
        varQuery = new HashMap<Value, String>();
        locals = new HashMap<String, Local>();
        methodMap = new HashMap<String, SootMethodRef>();
    }

    private static class EqSQLDataHolder {
        private static final EqSQLSharedData instance = new EqSQLSharedData();
    }

    public static EqSQLSharedData getInstance() {
        return EqSQLDataHolder.instance;
    }
}
