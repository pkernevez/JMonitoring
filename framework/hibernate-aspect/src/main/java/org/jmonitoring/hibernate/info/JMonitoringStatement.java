package org.jmonitoring.hibernate.info;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringStatement implements Statement, IProxyStatement
{
    private Statement mRealStatement;

    private static Log sLog = LogFactory.getLog(JMonitoringStatement.class);

    protected StringBuffer mTrace = new StringBuffer();

    public JMonitoringStatement(Statement pRealStatement)
    {
        this(pRealStatement, true);
    }

    public JMonitoringStatement(Statement pRealPreparedStat, boolean pLog)
    {
        if (pLog)
        {
            sLog.debug("callStatement detected and Weaved");
        }
        mRealStatement = pRealPreparedStat;
    }

    public Statement getRealStatement()
    {
        return mRealStatement;
    }

    public String getTrace()
    {
        return mTrace.toString();
    }

    public void addBatch(String pSql) throws SQLException
    {
        mTrace.append("Add Batch=[").append(pSql).append("]\n");
        mRealStatement.addBatch(pSql);
    }

    public void cancel() throws SQLException
    {
        mRealStatement.cancel();
    }

    public void clearBatch() throws SQLException
    {
        mTrace.append("Clear Batch\n");
        mRealStatement.clearBatch();
    }

    public void clearWarnings() throws SQLException
    {
        mRealStatement.clearWarnings();
    }

    public void close() throws SQLException
    {
        mTrace = null;
        mRealStatement.close();

    }

    public boolean execute(String pSql) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        return mRealStatement.execute(pSql);
    }

    public boolean execute(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        return mRealStatement.execute(pSql, pAutoGeneratedKeys);
    }

    public boolean execute(String pSql, int[] pColumnIndexes) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        return mRealStatement.execute(pSql, pColumnIndexes);
    }

    public boolean execute(String pSql, String[] pColumnNames) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        return mRealStatement.execute(pSql, pColumnNames);
    }

    public int[] executeBatch() throws SQLException
    {
        mTrace.append("Batch Executed\n");
        return mRealStatement.executeBatch();
    }

    public ResultSet executeQuery(String pSql) throws SQLException
    {
        mTrace.append("Query Executed=[").append(pSql).append("]\n");
        return mRealStatement.executeQuery(pSql);
    }

    public int executeUpdate(String pSql) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        return mRealStatement.executeUpdate(pSql);
    }

    public int executeUpdate(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        return mRealStatement.executeUpdate(pSql, pAutoGeneratedKeys);
    }

    public int executeUpdate(String pSql, int[] pColumnIndexes) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        return mRealStatement.executeUpdate(pSql, pColumnIndexes);
    }

    public int executeUpdate(String pSql, String[] pColumnNames) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        return mRealStatement.executeUpdate(pSql, pColumnNames);
    }

    public Connection getConnection() throws SQLException
    {
        return mRealStatement.getConnection();
    }

    public int getFetchDirection() throws SQLException
    {
        return mRealStatement.getFetchDirection();
    }

    public int getFetchSize() throws SQLException
    {
        return mRealStatement.getFetchSize();
    }

    public ResultSet getGeneratedKeys() throws SQLException
    {
        return mRealStatement.getGeneratedKeys();
    }

    public int getMaxFieldSize() throws SQLException
    {
        return mRealStatement.getMaxFieldSize();
    }

    public int getMaxRows() throws SQLException
    {
        return mRealStatement.getMaxRows();
    }

    public boolean getMoreResults() throws SQLException
    {
        return mRealStatement.getMoreResults();
    }

    public boolean getMoreResults(int pCurrent) throws SQLException
    {
        return mRealStatement.getMoreResults(pCurrent);
    }

    public int getQueryTimeout() throws SQLException
    {
        return mRealStatement.getQueryTimeout();
    }

    public ResultSet getResultSet() throws SQLException
    {
        return mRealStatement.getResultSet();
    }

    public int getResultSetConcurrency() throws SQLException
    {
        return mRealStatement.getResultSetConcurrency();
    }

    public int getResultSetHoldability() throws SQLException
    {
        return mRealStatement.getResultSetHoldability();
    }

    public int getResultSetType() throws SQLException
    {
        return mRealStatement.getResultSetType();
    }

    public int getUpdateCount() throws SQLException
    {
        return mRealStatement.getUpdateCount();
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return mRealStatement.getWarnings();
    }

    public void setCursorName(String pName) throws SQLException
    {
        mRealStatement.setCursorName(pName);

    }

    public void setEscapeProcessing(boolean pEnable) throws SQLException
    {
        mRealStatement.setEscapeProcessing(pEnable);

    }

    public void setFetchDirection(int pDirection) throws SQLException
    {
        mRealStatement.setFetchDirection(pDirection);

    }

    public void setFetchSize(int pRows) throws SQLException
    {
        mRealStatement.setFetchSize(pRows);

    }

    public void setMaxFieldSize(int pMax) throws SQLException
    {
        mRealStatement.setMaxFieldSize(pMax);

    }

    public void setMaxRows(int pMax) throws SQLException
    {
        mRealStatement.setMaxRows(pMax);

    }

    public void setQueryTimeout(int pSeconds) throws SQLException
    {
        mRealStatement.setQueryTimeout(pSeconds);
    }

}
