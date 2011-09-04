package org.jmonitoring.test.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/persistence-test.xml" })
public class PersistanceTestCaseTest extends PersistanceTestCase
{
    
    @Test
    public void testRequestExecution() throws SQLException
    {
        assertNotNull(applicationContext);
        assertNotNull(getSession());
        assertNotNull(getSession().connection());
        PreparedStatement tStat = getSession().connection().prepareStatement("select * from METHOD_CALL");
        assertTrue(tStat.execute());
        ResultSet tResult = tStat.getResultSet();
        assertFalse(tResult.next());
    }

    @Test
        public void testCommitAndRestartSession() throws Exception
        {
            Criteria tCrit = getSession().createCriteria(MethodCallPO.class);
            tCrit.setProjection(Projections.rowCount());
            int totalResults = ((Integer) tCrit.list().get(0)).intValue();
            commitAndRestartSession();
            tCrit = getSession().createCriteria(MethodCallPO.class);
            tCrit.setProjection(Projections.rowCount());
            int totalResultsBis = ((Integer) tCrit.list().get(0)).intValue();
            assertEquals(totalResults, totalResultsBis);
        }

}
