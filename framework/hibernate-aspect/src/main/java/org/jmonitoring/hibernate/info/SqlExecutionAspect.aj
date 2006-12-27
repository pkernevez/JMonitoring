package org.jmonitoring.hibernate.info;

import org.jmonitoring.core.aspects.PerformanceAspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public aspect SqlExecutionAspect extends PerformanceAspect
{

    private pointcut jmonitoring() :  execution( * junit.framework.TestCase+.setUp())
    || execution( * junit.framework.TestCase+.tearDown())
    || execution( * junit.framework.TestCase+.check*(..))
    || execution( * org.jmonitoring.hibernate.dao.ExecutionFlowHibernateDAO.*(..));
    
//    private pointcut jmonitoring() : execution( * org.jmonitoring.core.*.*.*(..))
//    || execution(* org.jmonitoring.hibernate.info.JMonitoringPreparedStatement.*(..));
    
    public SqlExecutionAspect()
    {
        mResultTracer = new SqlStatementTracer();
    }
    
    public pointcut executionToLog() : (call( * java.sql.Statement+.execute*(..) ) 
        || call( * java.sql.PreparedStatement+.executeQuery())
        || call( * java.sql.PreparedStatement+.executeUpdate())) 
        && !within(org.jmonitoring.hibernate.info.JMonitoringStatement+)
        && !cflow(jmonitoring());

}
