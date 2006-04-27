package org.jmonitoring.core.persistence;

import java.sql.SQLException;
import java.util.List;

import org.jmonitoring.core.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestExecutionFlowPO extends PersistanceTestCase
{
    public void testGetListOfExecutionFlow() throws SQLException 
    {

        createDataSet("/dataset.xml");
        List tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
        assertEquals(3, tResult.size());
   }

}
