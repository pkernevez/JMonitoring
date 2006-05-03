package org.jmonitoring.core.dao;

import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class PersitanceHelper
{
    /**
     * Get the list of Flows.
     * 
     * @return The number of flows in database.
     * @throws SQLException If an error occures during DB access.
     */
    public static int countFlows( Session pSession)
    {
        SQLQuery tQuery = pSession.createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        if (tResult != null)
        {
            return ((Integer) tResult).intValue();
        } else
        {
            return 0;
        }
    }


}
