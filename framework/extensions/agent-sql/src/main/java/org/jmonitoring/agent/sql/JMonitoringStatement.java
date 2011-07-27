package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.aspectj.lang.Signature;
import org.jmonitoring.agent.info.impl.DefaultExceptionTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringStatement implements Statement, IProxyStatement
{
    public static final String GROUP_NAME = "Jdbc";

    private static Signature EXECUTE_STR;

    private static Signature EXECUTE_STR_INT;

    private static Signature EXECUTE_STR_TINT;

    private static Signature EXECUTE_STR_TSTR;

    private static Signature EXECUTE_BATCH;

    private static Signature EXECUTE_QUERY;

    private static Signature EXECUTE_UPDATE;

    private static Signature EXECUTE_UPDATE_STR_INT;

    private static Signature EXECUTE_UPDATE_STR_TINT;

    private static Signature EXECUTE_UPDATE_STR_TSTR;
    {
        try
        {
            Class<JMonitoringStatement> tClass = JMonitoringStatement.class;
            EXECUTE_STR = new SqlSignature(Statement.class, tClass.getMethod("execute", String.class));
            EXECUTE_STR_INT =
                new SqlSignature(Statement.class, tClass.getMethod("execute", String.class, Integer.TYPE));
            EXECUTE_STR_TINT =
                new SqlSignature(Statement.class, tClass.getMethod("execute", String.class, new int[0].getClass()));
            EXECUTE_STR_TSTR =
                new SqlSignature(Statement.class, tClass.getMethod("execute", String.class, new String[0].getClass()));
            EXECUTE_BATCH = new SqlSignature(Statement.class, tClass.getMethod("executeBatch"));
            EXECUTE_QUERY = new SqlSignature(Statement.class, tClass.getMethod("executeQuery", String.class));
            EXECUTE_UPDATE = new SqlSignature(Statement.class, tClass.getMethod("executeUpdate", String.class));
            EXECUTE_UPDATE_STR_INT =
                new SqlSignature(Statement.class, tClass.getMethod("executeUpdate", String.class, Integer.TYPE));
            EXECUTE_UPDATE_STR_TINT =
                new SqlSignature(Statement.class,
                                 tClass.getMethod("executeUpdate", String.class, new int[0].getClass()));
            EXECUTE_UPDATE_STR_TSTR =
                new SqlSignature(Statement.class, tClass.getMethod("executeUpdate", String.class,
                                                                   new String[0].getClass()));
        } catch (SecurityException e)
        {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected static ThreadLocal<StoreManager> sStoreManager = new ThreadLocal<StoreManager>();

    protected static IResultTracer sResultTracer = new ToStringResultTracer();

    protected static IThrowableTracer sThrowableTracer = new DefaultExceptionTracer();

    private final Statement mRealStatement;

    private static Logger sLog = LoggerFactory.getLogger(JMonitoringStatement.class);

    protected StringBuilder mTrace = new StringBuilder();

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

    protected static StoreManager getStoreManager()
    {
        StoreManager tStoreManager = sStoreManager.get();
        if (tStoreManager == null)
        {
            tStoreManager = (StoreManager) SpringConfigurationUtil.getBean(StoreManager.STORE_MANAGER_NAME);
            sStoreManager.set(tStoreManager);
        }
        return tStoreManager;
    }

    public Statement getRealStatement()
    {
        return mRealStatement;
    }

    public CharSequence getTrace()
    {
        CharSequence tResult = mTrace;
        mTrace = new StringBuilder();
        return tResult;
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
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_STR, null, new Object[0], GROUP_NAME, this);
        try
        {
            boolean tResult = mRealStatement.execute(pSql);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public boolean execute(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_STR_INT, null, new Object[0], GROUP_NAME, this);
        try
        {
            boolean tResult = mRealStatement.execute(pSql, pAutoGeneratedKeys);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public boolean execute(String pSql, int[] pColumnIndexes) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_STR_TINT, null, new Object[0], GROUP_NAME, this);
        try
        {
            boolean tResult = mRealStatement.execute(pSql, pColumnIndexes);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public boolean execute(String pSql, String[] pColumnNames) throws SQLException
    {
        mTrace.append("Sql=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_STR_TSTR, null, new Object[0], GROUP_NAME, this);
        try
        {
            boolean tResult = mRealStatement.execute(pSql, pColumnNames);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public int[] executeBatch() throws SQLException
    {
        mTrace.append("Batch executed\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_BATCH, null, new Object[0], GROUP_NAME, this);
        try
        {
            int[] tResult = mRealStatement.executeBatch();
            mTrace.append("Return array size=[").append(tResult.length).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public ResultSet executeQuery(String pSql) throws SQLException
    {
        mTrace.append("Query executed=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_QUERY, null, new Object[0], GROUP_NAME, this);
        try
        {
            ResultSet tResult = mRealStatement.executeQuery(pSql);
            mTrace.append("ResultSet=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return new JMonitoringResultSet(tResult);
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public int executeUpdate(String pSql) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_UPDATE, null, new Object[0], GROUP_NAME, this);
        try
        {
            int tResult = mRealStatement.executeUpdate(pSql);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public int executeUpdate(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_UPDATE_STR_INT, null, new Object[0], GROUP_NAME, this);
        try
        {
            int tResult = mRealStatement.executeUpdate(pSql, pAutoGeneratedKeys);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public int executeUpdate(String pSql, int[] pColumnIndexes) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_UPDATE_STR_TINT, null, new Object[0], GROUP_NAME, this);
        try
        {
            int tResult = mRealStatement.executeUpdate(pSql, pColumnIndexes);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
    }

    public int executeUpdate(String pSql, String[] pColumnNames) throws SQLException
    {
        mTrace.append("Execute Update=[").append(pSql).append("]\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_UPDATE_STR_TSTR, null, new Object[0], GROUP_NAME, this);
        try
        {
            int tResult = mRealStatement.executeUpdate(pSql, pColumnNames);
            mTrace.append("Result=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (SQLException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace);
            throw e;
        } finally
        {
            mTrace = new StringBuilder();
        }
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
        return new JMonitoringResultSet(mRealStatement.getGeneratedKeys());
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
        ResultSet tResultSet = mRealStatement.getResultSet();
        return (tResultSet == null ? null : new JMonitoringResultSet(tResultSet));
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

    //TODO Mettre en place un bus 'configurationchangenotification'
    public static void refresh()
    {
        sStoreManager = new ThreadLocal<StoreManager>();
        
    }
}