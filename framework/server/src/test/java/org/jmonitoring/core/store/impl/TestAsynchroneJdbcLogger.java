package org.jmonitoring.core.store.impl;


import java.sql.SQLException;

import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.impl.AsynchroneJdbcLogger;
import org.jmonitoring.core.store.impl.MockAbstractAsynchroneLogger;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class TestAsynchroneJdbcLogger extends PersistanceTestCase
{
    private static final int TIME_TO_WAIT = 1500;

    // private int mInitialMaxFlowId;

    // /**
    // * no doc
    // *
    // * @throws Exception no doc
    // */
    // protected void setUp() throws Exception
    // {
    // super.setUp();
    // SQLQuery tQuery = mSession.createSQLQuery("Select Max(id) As MyMax from EXECUTION_FLOW");
    // Object tResult = tQuery.addScalar("MyMax", Hibernate.INTEGER).list().get(0);
    // mInitialMaxFlowId = ((Integer) tResult).intValue();
    // }

    // /**
    // * no doc
    // *
    // * @todo Refactor the connection
    // * @throws Exception no doc
    // */
    // protected void tearDown() throws Exception
    // {
    // Connection tCon = null;
    // Statement tStat = null;
    // try
    // {
    // tCon = mSession.connection();
    // tStat = tCon.createStatement();
    // tStat.executeUpdate("DELETE From METHOD_CALL where EXECUTION_FLOW_ID>" + mInitialMaxFlowId);
    // tStat.executeUpdate("DELETE From EXECUTION_FLOW where ID>" + mInitialMaxFlowId);
    // tCon.commit();
    // } finally
    // {
    // try
    // {
    // if (tStat != null)
    // {
    // tStat.close();
    // }
    // } finally
    // {
    // if (tCon != null)
    // {
    // tCon.close();
    // }
    // }
    //
    // }
    //
    // super.tearDown();
    // }
}
