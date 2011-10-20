package org.jmonitoring.console.gwt.server.flow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class InsertionDaoTest extends PersistanceTestCase
{
    @Test
    public void testInsertMethodCall()
    {
        int tOldResult = countMethods();

        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall.setMethId(new MethodCallPK(tFlow, 0));
        getSession().save(tMethodCall);
        getSession().flush();

        int tNewResult = countMethods();
        assertEquals(tOldResult + 1, tNewResult);
        assertStatistics(MethodCallPO.class, 1, 0, 0, 0);
    }

    public void testInsertMethodCalls()
    {
        int tOldResult = countMethods();

        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall1 =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall1.setMethId(new MethodCallPK(tFlow, 1));
        MethodCallPO tMethodCall2 =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall2.setMethId(new MethodCallPK(tFlow, 2));
        MethodCallPO tMethodCall3 =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall3.setMethId(new MethodCallPK(tFlow, 3));
        tMethodCall1.addChildren(tMethodCall2);
        tMethodCall1.addChildren(tMethodCall3);

        getSession().save(tMethodCall1);
        getSession().save(tMethodCall2);
        getSession().save(tMethodCall3);
        getSession().flush();

        int tNewResult = countMethods();
        assertEquals(tOldResult + 3, tNewResult);
        assertEquals(1, tMethodCall1.getPosition());
        assertEquals(2, tMethodCall2.getPosition());
        assertEquals(3, tMethodCall3.getPosition());
        assertStatistics(MethodCallPO.class, 3, 0, 0, 0);
    }

    public void testLinkedMethodCalls()
    {
        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall1 =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall1.setMethId(new MethodCallPK(tFlow, 0));
        getSession().save(tMethodCall1);
        getSession().flush();

        MethodCallPO tMethodCall2 =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall2.setMethId(new MethodCallPK(tFlow, 1));
        getSession().save(tMethodCall2);
        getSession().flush();

        assertNull(tMethodCall1.getParentMethodCall());
        assertNull(tMethodCall2.getParentMethodCall());

        assertEquals(0, tMethodCall1.getPosition());
        assertEquals(1, tMethodCall2.getPosition());
        assertStatistics(MethodCallPO.class, 2, 0, 0, 0);

        assertEquals(0, countMethodsWithParent());
        tMethodCall1.addChildren(tMethodCall2);
        getSession().save(tMethodCall1);
        getSession().save(tMethodCall2);
        getSession().flush();
        // assertStatistics(MethodCallPO.class, 2, 2, 0, 0);

        assertEquals(0, tMethodCall1.getPosition());
        assertEquals(1, tMethodCall2.getPosition());

        assertEquals(1, countMethodsWithParent());

        getSession().clear();
        MethodCallPO tTempMeth =
            (MethodCallPO) getSession().load(MethodCallPO.class, new MethodCallPK(tFlow, tMethodCall1.getPosition()));
        assertNotSame(tMethodCall1, tTempMeth);
        tMethodCall1 = tTempMeth;
        tTempMeth =
            (MethodCallPO) getSession().load(MethodCallPO.class, new MethodCallPK(tFlow, tMethodCall2.getPosition()));
        assertNotSame(tMethodCall2, tTempMeth);
        tMethodCall2 = tTempMeth;
        assertNull(tMethodCall1.getParentMethodCall());
        assertNotNull(tMethodCall2.getParentMethodCall());
        assertSame(tMethodCall1, tMethodCall2.getParentMethodCall());
        assertEquals(1, tMethodCall1.getChildren().size());
        assertSame(tMethodCall2, tMethodCall1.getChild(0));
    }

    private int countMethods()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From METHOD_CALL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    /** @todo Passer les requ�tes en Query externe */
    private int countMethodsWithParent()
    {
        SQLQuery tQuery =
            getSession().createSQLQuery("Select Count(*) as myCount From METHOD_CALL Where PARENT_INDEX_IN_FLOW IS NOT NULL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    public void testBidonPKE() throws SQLException
    {
        int tOldResult = countMethods();

        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall =
            new MethodCallPO(null, InsertionDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tMethodCall.setMethId(new MethodCallPK(tFlow, 0));
        getSession().save(tMethodCall);
        getSession().flush();

        int tNewResult = countMethods();
        assertEquals(tOldResult + 1, tNewResult);
        assertStatistics(MethodCallPO.class, 1, 0, 0, 0);

        @SuppressWarnings("deprecation")
        Statement tStat = getSession().connection().createStatement();
        ResultSet tSet =
            tStat.executeQuery("SELECT CONVERT(123, int),"
                + "DATEDIFF('ss', BEGIN_TIME_AS_DATE, BEGIN_TIME_AS_DATE)+1 " + "As MonCalcul FROM EXECUTION_FLOW ");
        tSet.next();
        assertEquals(123, tSet.getInt(1));
        assertEquals(1, tSet.getInt(2));
    }

}
