package org.jmonitoring.agent.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.aspectj.lang.Signature;
import org.jmonitoring.agent.store.StoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringPreparedStatement extends JMonitoringStatement implements PreparedStatement
{
    private static Signature EXECUTE;

    private static Signature EXECUTE_QUERY;

    private static Signature EXECUTE_UPDATE;

    {
        try
        {
            Class<JMonitoringPreparedStatement> tClass = JMonitoringPreparedStatement.class;
            EXECUTE = new SqlSignature(PreparedStatement.class, tClass.getMethod("execute"));
            EXECUTE_QUERY = new SqlSignature(PreparedStatement.class, tClass.getMethod("executeQuery"));
            EXECUTE_UPDATE = new SqlSignature(PreparedStatement.class, tClass.getMethod("executeUpdate"));
        } catch (SecurityException e)
        {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    private final PreparedStatement mRealPreparedStat;

    private final String mSql;

    private static Logger sLog = LoggerFactory.getLogger(JMonitoringPreparedStatement.class);

    public JMonitoringPreparedStatement(PreparedStatement pRealPreparedStat, String pSql, String pGroupName)
    {
        this("PrepareStatement", pRealPreparedStat, pSql, pGroupName);
    }

    protected JMonitoringPreparedStatement(String pStatementType, PreparedStatement pRealPreparedStat, String pSql,
        String pGroupName)
    {
        super(pRealPreparedStat, pGroupName, false);
        if (sLog.isDebugEnabled())
        {
            sLog.debug(pStatementType + " detected and Weaved");
        }
        mSql = pStatementType + " with Sql=[" + pSql + "]\n";
        mTrace.append(mSql);
        mRealPreparedStat = pRealPreparedStat;
    }

    public void addBatch() throws SQLException
    {
        mTrace.append("Add PreparedBatch\n");
        mRealPreparedStat.addBatch();
    }

    public void clearParameters() throws SQLException
    {
        mTrace = new StringBuilder().append(mSql);
        mRealPreparedStat.clearParameters();

    }

    public boolean execute() throws SQLException
    {
        mTrace.append("Execute \n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE, null, new Object[0], groupName, this);
        try
        {
            boolean tResult = mRealPreparedStat.execute();
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
            mTrace = new StringBuilder().append(mSql);
        }
    }

    public ResultSet executeQuery() throws SQLException
    {
        mTrace.append("Execute query\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_QUERY, null, new Object[0], groupName, this);
        try
        {
            ResultSet tResult = mRealPreparedStat.executeQuery();
            mTrace.append("ResultSet=[").append(tResult).append("]\n");
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return new JMonitoringResultSet(tResult, groupName);
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
            mTrace = new StringBuilder().append(mSql);
        }
    }

    public int executeUpdate() throws SQLException
    {
        mTrace.append("Execute update\n");
        StoreManager tManager = getStoreManager();
        tManager.logBeginOfMethod(EXECUTE_UPDATE, null, new Object[0], groupName, this);
        try
        {
            int tResult = mRealPreparedStat.executeUpdate();
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
            mTrace = new StringBuilder().append(mSql);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        return mRealPreparedStat.getMetaData();
    }

    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        return mRealPreparedStat.getParameterMetaData();
    }

    public void setArray(int pParameterIndex, Array pX) throws SQLException
    {
        mTrace.append("Add ARRAY, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setArray(pParameterIndex, pX);
    }

    public void setAsciiStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add AsciiStream, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength + "]\n");
        mRealPreparedStat.setAsciiStream(pParameterIndex, pX, pLength);
    }

    public void setBigDecimal(int pParameterIndex, BigDecimal pX) throws SQLException
    {
        mTrace.append("Add BigDecimal, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBigDecimal(pParameterIndex, pX);
    }

    public void setBinaryStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add BinaryStream, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength + "]\n");
        mRealPreparedStat.setBinaryStream(pParameterIndex, pX, pLength);
    }

    public void setBlob(int pI, Blob pX) throws SQLException
    {
        mTrace.append("Add Blob, pos=[" + pI + "], value not traced");
        mRealPreparedStat.setBlob(pI, pX);

    }

    public void setBoolean(int pParameterIndex, boolean pX) throws SQLException
    {
        mTrace.append("Add Boolean, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBoolean(pParameterIndex, pX);
    }

    public void setByte(int pParameterIndex, byte pX) throws SQLException
    {
        mTrace.append("Add Byte, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setByte(pParameterIndex, pX);
    }

    public void setBytes(int pPrameterIndex, byte[] pX) throws SQLException
    {
        mTrace.append("Add Bytes, pos=[" + pPrameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBytes(pPrameterIndex, pX);
    }

    public void setCharacterStream(int pParameterIndex, Reader pReader, int pLength) throws SQLException
    {
        mTrace.append("Add CharacterStream, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength + "]");
        mRealPreparedStat.setCharacterStream(pParameterIndex, pReader, pLength);

    }

    public void setClob(int pI, Clob pX) throws SQLException
    {
        mTrace.append("Add Clob, pos=[" + pI + "], value not traced");
        mRealPreparedStat.setClob(pI, pX);
    }

    public void setDate(int pParameterIndex, Date pX) throws SQLException
    {
        mTrace.append("Add Date, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX);
    }

    public void setDate(int pParameterIndex, Date pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Date, pos=[" + pParameterIndex + "], value=[" + pX + ", calendar=[" + pCal + "]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX, pCal);
    }

    public void setDouble(int pParameterIndex, double pX) throws SQLException
    {
        mTrace.append("Add Double, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setDouble(pParameterIndex, pX);
    }

    public void setFloat(int pParameterIndex, float pX) throws SQLException
    {
        mTrace.append("Add Float, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setFloat(pParameterIndex, pX);
    }

    public void setInt(int pParameterIndex, int pX) throws SQLException
    {
        mTrace.append("Add Int, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setInt(pParameterIndex, pX);
    }

    public void setLong(int pParameterIndex, long pX) throws SQLException
    {
        mTrace.append("Add Long, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setLong(pParameterIndex, pX);
    }

    public void setNull(int pParameterIndex, int pSqlType) throws SQLException
    {
        mTrace.append("Add NULL, pos=[" + pParameterIndex + "], type=[" + pSqlType + "]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType);
    }

    public void setNull(int pParameterIndex, int pSqlType, String pTypeName) throws SQLException
    {
        mTrace.append("Add NULL, pos=[" + pParameterIndex + "], type=[" + pSqlType + "], type name=[" + pTypeName
            + "]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType, pTypeName);
    }

    public void setObject(int pParameterIndex, Object pX) throws SQLException
    {
        mTrace.append("Add Object, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType) throws SQLException
    {
        mTrace.append("Add Object, pos=[" + pParameterIndex + "], value=[" + pX + "], targetTyp=[" + pTargetSqlType
            + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType, int pScale) throws SQLException
    {
        mTrace.append("Add Object, pos=[" + pParameterIndex + "], value=[" + pX + "], targetTyp=[" + pTargetSqlType
            + "], scale=[" + pScale + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType, pScale);
    }

    public void setRef(int pParameterIndex, Ref pX) throws SQLException
    {
        mTrace.append("Add Ref, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setRef(pParameterIndex, pX);
    }

    public void setShort(int pParameterIndex, short pX) throws SQLException
    {
        mTrace.append("Add Short, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setShort(pParameterIndex, pX);

    }

    public void setString(int pParameterIndex, String pX) throws SQLException
    {
        mTrace.append("Add String, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setString(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX) throws SQLException
    {
        mTrace.append("Add Time, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Time, pos=[" + pParameterIndex + "], value=[" + pX + "], Calendar=[" + pCal + "]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX, pCal);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX) throws SQLException
    {
        mTrace.append("Add Timestamp, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Timestamp, pos=[" + pParameterIndex + "], value=[" + pX + "], Calendar=[" + pCal + "]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX, pCal);
    }

    public void setURL(int pParameterIndex, URL pX) throws SQLException
    {
        mTrace.append("Add URL, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setURL(pParameterIndex, pX);
    }

    @SuppressWarnings("deprecation")
    public void setUnicodeStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add UnicodeStream, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength + "]\n");
        mRealPreparedStat.setUnicodeStream(pParameterIndex, pX, pLength);
    }

}
