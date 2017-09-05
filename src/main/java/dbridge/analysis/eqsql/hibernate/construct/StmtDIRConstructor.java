package dbridge.analysis.eqsql.hibernate.construct;

import exceptions.UnknownStatementException;
import soot.Unit;

/**
 * Created by ek on 18/5/16.
 */
public interface StmtDIRConstructor {
    StmtInfo construct(Unit stmt) throws UnknownStatementException;
}
