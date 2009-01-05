package org.jmonitoring.test.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

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
    public void testTransaction() throws Exception
    {
        getSession().close();
        tearDown();
    }
}
