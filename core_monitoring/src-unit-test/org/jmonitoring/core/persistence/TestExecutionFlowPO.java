package org.jmonitoring.core.persistence;


import org.jmonitoring.core.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestExecutionFlowPO extends PersistanceTestCase
{
    public void testGetListOfExecutionFlow()  
    {
//        mSession.clear();
//        mSession.flush();
//        List tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
//        assertEquals(0, tResult.size());
        createDataSet("/dataset.xml");
//        tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
//        assertEquals(3, tResult.size());
   }

}
