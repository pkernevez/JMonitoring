package org.jmonitoring.sample.driver;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class MockConnection implements Connection {

    /**
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(String pSql) throws SQLException {

        return new MockPreparedStatement();
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String pSql) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(String pSql) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean pAutoCommit) throws SQLException {

    }

    /**
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {

        return false;
    }

    /**
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {

    }

    /**
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {

    }

    /**
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {

    }

    /**
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {

        return false;
    }

    /**
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean pReadOnly) throws SQLException {

    }

    /**
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {

        return false;
    }

    /**
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(String pCatalog) throws SQLException {

    }

    /**
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int pLevel) throws SQLException {

    }

    /**
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {

        return 0;
    }

    /**
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException {

    }

    /**
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int pResultSetType, int pResultSetConcurrency) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pResultSetType, int pResultSetConcurrency)
            throws SQLException {
        return null;
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(String pSql, int pResultSetType, int pResultSetConcurrency)
            throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#getTypeMap()
     */
    public Map getTypeMap() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    public void setTypeMap(Map pArg0) throws SQLException {

    }

    /**
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int pHoldability) throws SQLException {

    }

    /**
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws SQLException {

        return 0;
    }

    /**
     * @see java.sql.Connection#setSavepoint()
     */
    public Savepoint setSavepoint() throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public Savepoint setSavepoint(String pName) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(Savepoint pSavepoint) throws SQLException {

    }

    /**
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(Savepoint pSavepoint) throws SQLException {

    }

    /**
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int pResultSetType, int pResultSetConcurrency, int pResultSetHoldability)
            throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pResultSetType, int pResultSetConcurrency,
            int pResultSetHoldability) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(String pSql, int pResultSetType, int pResultSetConcurrency,
            int pResultSetHoldability) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pAutoGeneratedKeys) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(String pSql, int[] pColumnIndexes) throws SQLException {

        return null;
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(String pSql, String[] pColumnNames) throws SQLException {

        return null;
    }

}
