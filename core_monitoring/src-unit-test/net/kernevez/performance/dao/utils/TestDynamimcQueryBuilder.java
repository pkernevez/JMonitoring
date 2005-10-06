package net.kernevez.performance.dao.utils;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmonitoring.core.dao.utils.DynamicQueryBuilder;

/**
 * Unit test class for DynamicQueryBuilder.
 * 
 * @author pke
 */
public class TestDynamimcQueryBuilder extends MockObjectTestCase
{
    private static final String TEST_SELECT = "SELECT * FROM TOTO";

    private static final String TEST_PARAM1 = "MON_PARAM1 = ? ";

    private static final String TEST_PARAM2 = "MON_PARAM2 = ? ";

    public void setUp() throws Exception
    {
        super.setUp();
    }

    
    /**
     * Test without param.
     */
    public void testNoParam()
    {
        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(TEST_SELECT);
        assertEquals(TEST_SELECT, tBuilder.getSqlQuery());
    }

    /**
     * Test with only Int Param.
     * 
     * @throws SQLException
     */
    public void testAddIntParam() throws SQLException
    {
        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(TEST_SELECT);
        tBuilder.addIntParam(TEST_PARAM1, new Integer(4));
        tBuilder.addIntParam(TEST_PARAM2, null);
        assertEquals(tBuilder.getSqlQuery(), "SELECT * FROM TOTO WHERE MON_PARAM1 = ? ");

        Mock tMock = new Mock(PreparedStatement.class);
        tMock.expects(once()).method("setInt").with(eq(new Integer(1)), eq(new Integer(4)));

        tBuilder.fillParam((PreparedStatement) tMock.proxy());
        tMock.verify();

    }

    /**
     * Test with only String Param.
     * 
     * @throws SQLException
     */
    public void testAddStringParam() throws SQLException
    {
        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(TEST_SELECT);
        tBuilder.addStringParam(TEST_PARAM1, "toto");
        tBuilder.addStringParam(TEST_PARAM2, null);
        assertEquals(tBuilder.getSqlQuery(), "SELECT * FROM TOTO WHERE MON_PARAM1 = ? ");

        Mock tMock = new Mock(PreparedStatement.class);
        tMock.expects(once()).method("setString").with(eq(new Integer(1)), eq("toto"));

        tBuilder.fillParam((PreparedStatement) tMock.proxy());
        tMock.verify();

    }

    /**
     * Test with only Long Param.
     * 
     * @throws SQLException
     */
    public void testAddLongParam() throws SQLException
    {
        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(TEST_SELECT);
        tBuilder.addLongParam(TEST_PARAM1, new Long(4));
        tBuilder.addLongParam(TEST_PARAM2, null);
        assertEquals(tBuilder.getSqlQuery(), "SELECT * FROM TOTO WHERE MON_PARAM1 = ? ");

        Mock tMock = new Mock(PreparedStatement.class);
        tMock.expects(once()).method("setLong").with(eq(new Integer(1)), eq(new Long(4)));

        tBuilder.fillParam((PreparedStatement) tMock.proxy());
        tMock.verify();

    }

    /**
     * Test with only Date Param.
     * 
     * @throws SQLException
     */
    public void testAddDateParam() throws SQLException
    {
        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(TEST_SELECT);
        Date tDate = new Date();
        tBuilder.addDateParam(TEST_PARAM1, tDate);
        tBuilder.addDateParam(TEST_PARAM2, null);
        assertEquals(tBuilder.getSqlQuery(), "SELECT * FROM TOTO WHERE MON_PARAM1 = ? ");

        Mock tMock = new Mock(PreparedStatement.class);
        tMock.expects(once()).method("setDate").with(eq(new Integer(1)), eq(tDate));

        tBuilder.fillParam((PreparedStatement) tMock.proxy());
        tMock.verify();

    }

}
