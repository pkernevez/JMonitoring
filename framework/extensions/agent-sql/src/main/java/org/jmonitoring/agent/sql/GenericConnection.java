package org.jmonitoring.agent.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class GenericConnection implements Connection
{
    private final Connection mRealConnection;

    // private static Signature CREATE_STATEMENT;
    //
    // private static Signature PREPARE_STATEMENT;
    //
    // private static Signature PREPARE_CALL;
    //
    // protected static IResultTracer sResultTracer = new ToStringResultTracer();
    //
    // protected static IThrowableTracer sThrowableTracer = new DefaultExceptionTracer();
    //
    // private static Logger sLog = LoggerFactory.getLogger(GenericConnection.class);

    // {
    // try
    // {
    // Class<JMonitoringPreparedStatement> tClass = JMonitoringPreparedStatement.class;
    // CREATE_STATEMENT = new SqlSignature(PreparedStatement.class, tClass.getMethod("createStatement"));
    // PREPARE_STATEMENT =
    // new SqlSignature(PreparedStatement.class, tClass.getMethod("prepareStatement", String.class));
    // PREPARE_CALL = new SqlSignature(PreparedStatement.class, tClass.getMethod("prepareCall"), String.class));
    // } catch (SecurityException e)
    // {
    // throw new RuntimeException(e);
    // } catch (NoSuchMethodException e)
    // {
    // throw new RuntimeException(e);
    // }
    // }

    public GenericConnection(Connection pRealConnection)
    {
        super();
        mRealConnection = pRealConnection;
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
        // StoreManager tManager = JMonitoringStatement.getStoreManager();
        // tManager.logBeginOfMethod(CREATE_STATEMENT, null, new Object[0], JMonitoringStatement.GROUP_NAME, this);
        // try
        // {
        Statement tResult = new JMonitoringStatement(mRealConnection.createStatement());
        // tManager.logEndOfMethodNormal(sResultTracer, this, "");
        return tResult;
        // } catch (Error e)
        // {
        // tManager.logEndOfMethodWithException(sThrowableTracer, e);
        // throw e;
        // } catch (RuntimeException e)
        // {
        // tManager.logEndOfMethodWithException(sThrowableTracer, e);
        // throw e;
        // }
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
                                                                        pResultSetHoldability));
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
        return new JMonitoringStatement(mRealConnection.createStatement(pResultSetType, pResultSetConcurrency));
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
                                                                            pResultSetHoldability), pSql);
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
        return new JMonitoringCallableStatement(tStat, pSql);
    }

    /**
     * @param pSql
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String pSql) throws SQLException
    {
        return new JMonitoringCallableStatement(mRealConnection.prepareCall(pSql), pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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
        return new JMonitoringPreparedStatement(tStat, pSql);
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

}
