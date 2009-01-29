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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;

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
            EXECUTE = new SqlSignature(tClass.getMethod("execute"));
            EXECUTE_QUERY = new SqlSignature(tClass.getMethod("executeQuery"));
            EXECUTE_UPDATE = new SqlSignature(tClass.getMethod("executeUpdate"));
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

    private static Log sLog = LogFactory.getLog(JMonitoringPreparedStatement.class);

    public JMonitoringPreparedStatement(PreparedStatement pRealPreparedStat, String pSql)
    {
        this("PrepareStatement", pRealPreparedStat, pSql);
    }

    protected JMonitoringPreparedStatement(String pStatementType, PreparedStatement pRealPreparedStat, String pSql)
    {
        super(pRealPreparedStat, false);
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
        mManager.logBeginOfMethod(EXECUTE, null, new Object[0], "Sql", this);
        try
        {
            boolean tResult = mRealPreparedStat.execute();
            mTrace.append("Result=[").append(tResult).append("]\n");
            mManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } catch (RuntimeException e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } finally
        {
            mTrace = new StringBuilder().append(mSql);
        }
    }

    public ResultSet executeQuery() throws SQLException
    {
        mTrace.append("Execute query\n");
        mManager.logBeginOfMethod(EXECUTE_QUERY, null, new Object[0], "Sql", this);
        try
        {
            ResultSet tResult = mRealPreparedStat.executeQuery();
            mTrace.append("ResultSet=[").append(tResult).append("]\n");
            mManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } catch (RuntimeException e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } finally
        {
            mTrace = new StringBuilder().append(mSql);
        }
    }

    public int executeUpdate() throws SQLException
    {
        mTrace.append("Execute update\n");
        mManager.logBeginOfMethod(EXECUTE_UPDATE, null, new Object[0], "Sql", this);
        try
        {
            int tResult = mRealPreparedStat.executeUpdate();
            mTrace.append("Result=[").append(tResult).append("]\n");
            mManager.logEndOfMethodNormal(sResultTracer, this, mTrace);
            return tResult;
        } catch (Error e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } catch (RuntimeException e)
        {
            mManager.logEndOfMethodWithException(sThrowableTracer, e);
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
        mTrace.append("Add ARRAY parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setArray(pParameterIndex, pX);
    }

    public void setAsciiStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add AsciiStream parameter, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength
            + "]\n");
        mRealPreparedStat.setAsciiStream(pParameterIndex, pX, pLength);
    }

    public void setBigDecimal(int pParameterIndex, BigDecimal pX) throws SQLException
    {
        mTrace.append("Add BigDecimal parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBigDecimal(pParameterIndex, pX);
    }

    public void setBinaryStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add BinaryStream parameter, pos=[" + pParameterIndex + "], value not traced, length=[" + pLength
            + "]\n");
        mRealPreparedStat.setBinaryStream(pParameterIndex, pX, pLength);
    }

    public void setBlob(int pI, Blob pX) throws SQLException
    {
        mTrace.append("Add Blob parameter, pos=[" + pI + "], value not traced");
        mRealPreparedStat.setBlob(pI, pX);

    }

    public void setBoolean(int pParameterIndex, boolean pX) throws SQLException
    {
        mTrace.append("Add Boolean parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBoolean(pParameterIndex, pX);
    }

    public void setByte(int pParameterIndex, byte pX) throws SQLException
    {
        mTrace.append("Add Byte parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setByte(pParameterIndex, pX);
    }

    public void setBytes(int pPrameterIndex, byte[] pX) throws SQLException
    {
        mTrace.append("Add Bytes parameter, pos=[" + pPrameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setBytes(pPrameterIndex, pX);
    }

    public void setCharacterStream(int pParameterIndex, Reader pReader, int pLength) throws SQLException
    {
        mTrace.append("Add CharacterStream parameter, pos=[" + pParameterIndex + "], value not traced, length=["
            + pLength + "]");
        mRealPreparedStat.setCharacterStream(pParameterIndex, pReader, pLength);

    }

    public void setClob(int pI, Clob pX) throws SQLException
    {
        mTrace.append("Add Clob parameter, pos=[" + pI + "], value not traced");
        mRealPreparedStat.setClob(pI, pX);
    }

    public void setDate(int pParameterIndex, Date pX) throws SQLException
    {
        mTrace.append("Add Date parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX);
    }

    public void setDate(int pParameterIndex, Date pX, Calendar pCal) throws SQLException
    {
        mTrace
              .append("Add Date parameter, pos=[" + pParameterIndex + "], value=[" + pX + ", calendar=[" + pCal + "]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX, pCal);
    }

    public void setDouble(int pParameterIndex, double pX) throws SQLException
    {
        mTrace.append("Add Double parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setDouble(pParameterIndex, pX);
    }

    public void setFloat(int pParameterIndex, float pX) throws SQLException
    {
        mTrace.append("Add Float parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setFloat(pParameterIndex, pX);
    }

    public void setInt(int pParameterIndex, int pX) throws SQLException
    {
        mTrace.append("Add Int parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setInt(pParameterIndex, pX);
    }

    public void setLong(int pParameterIndex, long pX) throws SQLException
    {
        mTrace.append("Add Long parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setLong(pParameterIndex, pX);
    }

    public void setNull(int pParameterIndex, int pSqlType) throws SQLException
    {
        mTrace.append("Add NULL parameter, pos=[" + pParameterIndex + "], type=[" + pSqlType + "]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType);
    }

    public void setNull(int pParameterIndex, int pSqlType, String pTypeName) throws SQLException
    {
        mTrace.append("Add NULL parameter, pos=[" + pParameterIndex + "], type=[" + pSqlType + "], type name=["
            + pTypeName + "]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType, pTypeName);
    }

    public void setObject(int pParameterIndex, Object pX) throws SQLException
    {
        mTrace.append("Add Object parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType) throws SQLException
    {
        mTrace.append("Add Object parameter, pos=[" + pParameterIndex + "], value=[" + pX + "], targetTyp=["
            + pTargetSqlType + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType, int pScale) throws SQLException
    {
        mTrace.append("Add Object parameter, pos=[" + pParameterIndex + "], value=[" + pX + "], targetTyp=["
            + pTargetSqlType + "], scale=[" + pScale + "]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType, pScale);
    }

    public void setRef(int pParameterIndex, Ref pX) throws SQLException
    {
        mTrace.append("Add Ref parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setRef(pParameterIndex, pX);
    }

    public void setShort(int pParameterIndex, short pX) throws SQLException
    {
        mTrace.append("Add Short parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setShort(pParameterIndex, pX);

    }

    public void setString(int pParameterIndex, String pX) throws SQLException
    {
        mTrace.append("Add String parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setString(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX) throws SQLException
    {
        mTrace.append("Add Time parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Time parameter, pos=[" + pParameterIndex + "], value=[" + pX + "], Calendar=[" + pCal
            + "]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX, pCal);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX) throws SQLException
    {
        mTrace.append("Add Timestamp parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Timestamp parameter, pos=[" + pParameterIndex + "], value=[" + pX + "], Calendar=[" + pCal
            + "]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX, pCal);
    }

    public void setURL(int pParameterIndex, URL pX) throws SQLException
    {
        mTrace.append("Add URL parameter, pos=[" + pParameterIndex + "], value=[" + pX + "]\n");
        mRealPreparedStat.setURL(pParameterIndex, pX);
    }

    @SuppressWarnings("deprecation")
    public void setUnicodeStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add UnicodeStream parameter, pos=[" + pParameterIndex + "], value not traced, length=["
            + pLength + "]\n");
        mRealPreparedStat.setUnicodeStream(pParameterIndex, pX, pLength);
    }

}
