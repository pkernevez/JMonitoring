package org.jmonitoring.agent.sql;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.jmonitoring.agent.store.Filter;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class GenericConnection implements Connection
{
    private final Connection mRealConnection;

    private final Filter filter;
    
    private final String groupName;

    public GenericConnection(Connection pRealConnection, String pGroupName,Filter pFiler)
    {
        super();
        mRealConnection = pRealConnection;
        groupName = pGroupName;
        filter = pFiler;
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException
    {
        mRealConnection.clearWarnings();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException
    {
        mRealConnection.close();
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException
    {
        mRealConnection.commit();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException
    {
        return new JMonitoringStatement(mRealConnection.createStatement(), groupName, filter);
    }

    /**
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @param pResultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int pResultSetType, int pResultSetConcurrency, int pResultSetHoldability)
        throws SQLException
    {
        return new JMonitoringStatement(mRealConnection.createStatement(pResultSetType, pResultSetConcurrency,
                                                                        pResultSetHoldability), groupName, filter);
    }

    /**
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int pResultSetType, int pResultSetConcurrency) throws SQLException
    {
        return new JMonitoringStatement(mRealConnection.createStatement(pResultSetType, pResultSetConcurrency),
                                        groupName, filter);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException
    {
        return mRealConnection.getAutoCommit();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog() throws SQLException
    {
        return mRealConnection.getCatalog();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws SQLException
    {
        return mRealConnection.getHoldability();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException
    {
        return mRealConnection.getMetaData();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException
    {
        return mRealConnection.getTransactionIsolation();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTypeMap()
     */
    public Map<String, Class<?>> getTypeMap() throws SQLException
    {
        return mRealConnection.getTypeMap();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException
    {
        return mRealConnection.getWarnings();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException
    {
        return mRealConnection.isClosed();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException
    {
        return mRealConnection.isReadOnly();
    }

    /**
     * @param pSql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(String pSql) throws SQLException
    {
        return mRealConnection.nativeSQL(pSql);
    }

    /**
     * @param pSql
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @param pResultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(String pSql, int pResultSetType, int pResultSetConcurrency,
        int pResultSetHoldability) throws SQLException
    {
        return new JMonitoringCallableStatement(mRealConnection.prepareCall(pSql, pResultSetType,
                                                                            pResultSetConcurrency,
                                                                            pResultSetHoldability), pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(String pSql, int pResultSetType, int pResultSetConcurrency)
        throws SQLException
    {
        CallableStatement tStat = mRealConnection.prepareCall(pSql, pResultSetType, pResultSetConcurrency);
        return new JMonitoringCallableStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String pSql) throws SQLException
    {
        return new JMonitoringCallableStatement(mRealConnection.prepareCall(pSql), pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @param pResultSetHoldability
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pResultSetType, int pResultSetConcurrency,
        int pResultSetHoldability) throws SQLException
    {
        PreparedStatement tStat =
            mRealConnection.prepareStatement(pSql, pResultSetType, pResultSetConcurrency, pResultSetHoldability);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pResultSetType
     * @param pResultSetConcurrency
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pResultSetType, int pResultSetConcurrency)
        throws SQLException
    {
        PreparedStatement tStat = mRealConnection.prepareStatement(pSql, pResultSetType, pResultSetConcurrency);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pAutoGeneratedKeys
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        PreparedStatement tStat = mRealConnection.prepareStatement(pSql, pAutoGeneratedKeys);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pColumnIndexes
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(String pSql, int[] pColumnIndexes) throws SQLException
    {
        PreparedStatement tStat = mRealConnection.prepareStatement(pSql, pColumnIndexes);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @param pColumnNames
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(String pSql, String[] pColumnNames) throws SQLException
    {
        PreparedStatement tStat = mRealConnection.prepareStatement(pSql, pColumnNames);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(String pSql) throws SQLException
    {
        PreparedStatement tStat = mRealConnection.prepareStatement(pSql);
        return new JMonitoringPreparedStatement(tStat, pSql, groupName, filter);
    }

    /**
     * @param pSavepoint
     * @throws SQLException
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(Savepoint pSavepoint) throws SQLException
    {
        mRealConnection.releaseSavepoint(pSavepoint);
    }

    /**
     * @throws SQLException
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException
    {
        mRealConnection.rollback();
    }

    /**
     * @param pSavepoint
     * @throws SQLException
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(Savepoint pSavepoint) throws SQLException
    {
        mRealConnection.rollback(pSavepoint);
    }

    /**
     * @param pAutoCommit
     * @throws SQLException
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean pAutoCommit) throws SQLException
    {
        mRealConnection.setAutoCommit(pAutoCommit);
    }

    /**
     * @param pCatalog
     * @throws SQLException
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(String pCatalog) throws SQLException
    {
        mRealConnection.setCatalog(pCatalog);
    }

    /**
     * @param pHoldability
     * @throws SQLException
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int pHoldability) throws SQLException
    {
        mRealConnection.setHoldability(pHoldability);
    }

    /**
     * @param pReadOnly
     * @throws SQLException
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean pReadOnly) throws SQLException
    {
        mRealConnection.setReadOnly(pReadOnly);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Connection#setSavepoint()
     */
    public Savepoint setSavepoint() throws SQLException
    {
        return mRealConnection.setSavepoint();
    }

    /**
     * @param pName
     * @return
     * @throws SQLException
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public Savepoint setSavepoint(String pName) throws SQLException
    {
        return mRealConnection.setSavepoint(pName);
    }

    /**
     * @param pLevel
     * @throws SQLException
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int pLevel) throws SQLException
    {
        mRealConnection.setTransactionIsolation(pLevel);
    }

    /**
     * @param pMap
     * @throws SQLException
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    public void setTypeMap(Map<String, Class<?>> pMap) throws SQLException
    {
        mRealConnection.setTypeMap(pMap);
    }

    public Clob createClob() throws SQLException {
        return mRealConnection.createClob();
    }

    public Blob createBlob() throws SQLException {
        return mRealConnection.createBlob();
    }

    public NClob createNClob() throws SQLException {
        return mRealConnection.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return mRealConnection.createSQLXML();
    }

    public boolean isValid(int timeout) throws SQLException {
        return mRealConnection.isValid(timeout);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        mRealConnection.setClientInfo(name, value);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        mRealConnection.setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return mRealConnection.getClientInfo(name);
    }

    public Properties getClientInfo() throws SQLException {
        return mRealConnection.getClientInfo();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return mRealConnection.createArrayOf(typeName, elements);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return mRealConnection.createStruct(typeName, attributes);
    }

    public void setSchema(String schema) throws SQLException {
        mRealConnection.setSchema(schema);
    }

    public String getSchema() throws SQLException {
        return mRealConnection.getSchema();
    }

    public void abort(Executor executor) throws SQLException {
        mRealConnection.abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        mRealConnection.setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return mRealConnection.getNetworkTimeout();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return mRealConnection.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return mRealConnection.isWrapperFor(iface);
    }
}
