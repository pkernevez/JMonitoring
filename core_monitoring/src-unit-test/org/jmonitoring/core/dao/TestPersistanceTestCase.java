package org.jmonitoring.core.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestPersistanceTestCase extends PersistanceTestCase
{

    public void testRequestExecution() throws SQLException
    {
        PreparedStatement tStat = mSession.connection().prepareStatement("select 1 from METHOD_CALL");
        assertTrue(tStat.execute());
        ResultSet tResult = tStat.getResultSet();
        assertFalse(tResult.next());
    }
}
