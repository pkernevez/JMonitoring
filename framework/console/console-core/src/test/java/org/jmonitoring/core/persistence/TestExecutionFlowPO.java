package org.jmonitoring.core.persistence;

import PersistanceTestCase;

import org.jmonitoring.core.dao.TestExecutionFlowDAO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestExecutionFlowPO extends PersistanceTestCase
{
    public void testGetListOfExecutionFlow()
    {
        // mSession.clear();
        // mSession.flush();
        // List tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
        // assertEquals(0, tResult.size());
        createDataSet("/dataset.xml");
        // tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
        // assertEquals(3, tResult.size());
    }

    public void testToString()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        assertEquals("ExecutionFlowPO FlowId=[-1]", tFlow.toString());
    }

}
