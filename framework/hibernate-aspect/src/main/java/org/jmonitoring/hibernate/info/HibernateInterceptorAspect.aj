package org.jmonitoring.hibernate.info;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public aspect HibernateInterceptorAspect
{
    pointcut jHIAmonitoring() : execution( * junit.framework.TestCase+.setUp())
        || execution( * junit.framework.TestCase+.tearDown())
        || execution( * junit.framework.TestCase+.check*(..))
        || execution( * junit.framework.TestCase+.define*(..))
        || execution( * org.jmonitoring.hibernate.dao.ExecutionFlowHibernateDAO.*(..));

    // || execution(* org.jmonitoring.sample.testtreetracer.TestTreeTracer.*(..));

    pointcut callStat() : call(java.sql.Statement java.sql.Connection.createStatement(..))
        && !cflow( jHIAmonitoring() );

    Object around() : callStat() {
        Object tResult = proceed();
        Statement tStat = new JMonitoringStatement((Statement) tResult);
        return tStat;
    }

    pointcut callPreparedStat() : call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String,..))
        && !cflow( jHIAmonitoring() );

    Object around() : callPreparedStat() {
        Object tResult = proceed();
        Object[] tParams = thisJoinPoint.getArgs();
        String tSqlRequest = (String) tParams[0];
        PreparedStatement tStat = new JMonitoringPreparedStatement((PreparedStatement) tResult, tSqlRequest);
        return tStat;
    }

    pointcut callPreparedCall() : call(java.sql.CallableStatement java.sql.Connection.prepareCall(String,..))
        && !cflow( jHIAmonitoring() );

    Object around() : callPreparedCall() {
        Object tResult = proceed();
        Object[] tParams = thisJoinPoint.getArgs();
        String tSqlRequest = (String) tParams[0];
        CallableStatement tStat = new JMonitoringCallableStatement((CallableStatement) tResult, tSqlRequest);
        return tStat;
    }

}
