package org.jmonitoring.agent.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;

import org.aspectj.lang.Signature;
import org.jmonitoring.agent.store.Filter;
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
        } catch (SecurityException | NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    private final PreparedStatement mRealPreparedStat;

    private final String mSql;

    private static Logger sLog = LoggerFactory.getLogger(JMonitoringPreparedStatement.class);

    public JMonitoringPreparedStatement(PreparedStatement pRealPreparedStat, String pSql, String pGroupName,
        Filter pFilter)
    {
        this("PrepareStatement", pRealPreparedStat, pSql, pGroupName, pFilter);
    }

    protected JMonitoringPreparedStatement(String pStatementType, PreparedStatement pRealPreparedStat, String pSql,
        String pGroupName, Filter pFilter)
    {
        super(pRealPreparedStat, pGroupName, false, pFilter);
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
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace, mFilter);
            return tResult;
        } catch (Error | SQLException | RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace, mFilter);
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
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace,mFilter);
            return new JMonitoringResultSet(tResult, groupName,mFilter);
        } catch (Error | SQLException | RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace,mFilter);
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
            tManager.logEndOfMethodNormal(sResultTracer, this, mTrace,mFilter);
            return tResult;
        } catch (Error | SQLException | RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, mTrace,mFilter);
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
        mTrace.append("Add ARRAY, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setArray(pParameterIndex, pX);
    }

    public void setAsciiStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add AsciiStream, pos=[").append(pParameterIndex).append("], value not traced, length=[").append(pLength)
              .append("]\n");
        mRealPreparedStat.setAsciiStream(pParameterIndex, pX, pLength);
    }

    public void setBigDecimal(int pParameterIndex, BigDecimal pX) throws SQLException
    {
        mTrace.append("Add BigDecimal, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setBigDecimal(pParameterIndex, pX);
    }

    public void setBinaryStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add BinaryStream, pos=[").append(pParameterIndex).append("], value not traced, length=[").append(pLength)
              .append("]\n");
        mRealPreparedStat.setBinaryStream(pParameterIndex, pX, pLength);
    }

    public void setBlob(int pI, Blob pX) throws SQLException
    {
        mTrace.append("Add Blob, pos=[").append(pI).append("], value not traced");
        mRealPreparedStat.setBlob(pI, pX);

    }

    public void setBoolean(int pParameterIndex, boolean pX) throws SQLException
    {
        mTrace.append("Add Boolean, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setBoolean(pParameterIndex, pX);
    }

    public void setByte(int pParameterIndex, byte pX) throws SQLException
    {
        mTrace.append("Add Byte, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setByte(pParameterIndex, pX);
    }

    public void setBytes(int pPrameterIndex, byte[] pX) throws SQLException
    {
        mTrace.append("Add Bytes, pos=[").append(pPrameterIndex).append("], value=[").append(Arrays.toString(pX)).append("]\n");
        mRealPreparedStat.setBytes(pPrameterIndex, pX);
    }

    public void setCharacterStream(int pParameterIndex, Reader pReader, int pLength) throws SQLException
    {
        mTrace.append("Add CharacterStream, pos=[").append(pParameterIndex).append("], value not traced, length=[").append(pLength)
              .append("]");
        mRealPreparedStat.setCharacterStream(pParameterIndex, pReader, pLength);

    }

    public void setClob(int pI, Clob pX) throws SQLException
    {
        mTrace.append("Add Clob, pos=[").append(pI).append("], value not traced");
        mRealPreparedStat.setClob(pI, pX);
    }

    public void setDate(int pParameterIndex, Date pX) throws SQLException
    {
        mTrace.append("Add Date, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX);
    }

    public void setDate(int pParameterIndex, Date pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Date, pos=[").append(pParameterIndex).append("], value=[").append(pX).append(", calendar=[").append(pCal)
              .append("]\n");
        mRealPreparedStat.setDate(pParameterIndex, pX, pCal);
    }

    public void setDouble(int pParameterIndex, double pX) throws SQLException
    {
        mTrace.append("Add Double, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setDouble(pParameterIndex, pX);
    }

    public void setFloat(int pParameterIndex, float pX) throws SQLException
    {
        mTrace.append("Add Float, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setFloat(pParameterIndex, pX);
    }

    public void setInt(int pParameterIndex, int pX) throws SQLException
    {
        mTrace.append("Add Int, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setInt(pParameterIndex, pX);
    }

    public void setLong(int pParameterIndex, long pX) throws SQLException
    {
        mTrace.append("Add Long, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setLong(pParameterIndex, pX);
    }

    public void setNull(int pParameterIndex, int pSqlType) throws SQLException
    {
        mTrace.append("Add NULL, pos=[").append(pParameterIndex).append("], type=[").append(pSqlType).append("]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType);
    }

    public void setNull(int pParameterIndex, int pSqlType, String pTypeName) throws SQLException
    {
        mTrace.append("Add NULL, pos=[").append(pParameterIndex).append("], type=[").append(pSqlType).append("], type name=[")
              .append(pTypeName).append("]\n");
        mRealPreparedStat.setNull(pParameterIndex, pSqlType, pTypeName);
    }

    public void setObject(int pParameterIndex, Object pX) throws SQLException
    {
        mTrace.append("Add Object, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType) throws SQLException
    {
        mTrace.append("Add Object, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("], targetTyp=[")
              .append(pTargetSqlType).append("]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType);
    }

    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType, int pScale) throws SQLException
    {
        mTrace.append("Add Object, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("], targetTyp=[")
              .append(pTargetSqlType).append("], scale=[").append(pScale).append("]\n");
        mRealPreparedStat.setObject(pParameterIndex, pX, pTargetSqlType, pScale);
    }

    public void setRef(int pParameterIndex, Ref pX) throws SQLException
    {
        mTrace.append("Add Ref, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setRef(pParameterIndex, pX);
    }

    public void setShort(int pParameterIndex, short pX) throws SQLException
    {
        mTrace.append("Add Short, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setShort(pParameterIndex, pX);

    }

    public void setString(int pParameterIndex, String pX) throws SQLException
    {
        mTrace.append("Add String, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setString(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX) throws SQLException
    {
        mTrace.append("Add Time, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX);
    }

    public void setTime(int pParameterIndex, Time pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Time, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("], Calendar=[").append(pCal)
              .append("]\n");
        mRealPreparedStat.setTime(pParameterIndex, pX, pCal);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX) throws SQLException
    {
        mTrace.append("Add Timestamp, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX);
    }

    public void setTimestamp(int pParameterIndex, Timestamp pX, Calendar pCal) throws SQLException
    {
        mTrace.append("Add Timestamp, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("], Calendar=[").append(pCal)
              .append("]\n");
        mRealPreparedStat.setTimestamp(pParameterIndex, pX, pCal);
    }

    public void setURL(int pParameterIndex, URL pX) throws SQLException
    {
        mTrace.append("Add URL, pos=[").append(pParameterIndex).append("], value=[").append(pX).append("]\n");
        mRealPreparedStat.setURL(pParameterIndex, pX);
    }

    @SuppressWarnings("deprecation")
    public void setUnicodeStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        mTrace.append("Add UnicodeStream, pos=[").append(pParameterIndex).append("], value not traced, length=[").append(pLength)
              .append("]\n");
        mRealPreparedStat.setUnicodeStream(pParameterIndex, pX, pLength);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        mTrace.append("Add RowId, pos=[").append(parameterIndex).append("], value=[").append(x).append("]\n");
        mRealPreparedStat.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        mTrace.append("Add NString, pos=[").append(parameterIndex).append("], value=[").append(value).append("]\n");
        mRealPreparedStat.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        mTrace.append("Add NCharacterStream, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length)
              .append("]\n");
        mRealPreparedStat.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        mTrace.append("Add NClob, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        mTrace.append("Add Clob, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length).append("]\n");
        mRealPreparedStat.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        mTrace.append("Add Blob, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length).append("]\n");
        mRealPreparedStat.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        mTrace.append("Add NClob, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length).append("]\n");
        mRealPreparedStat.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        mTrace.append("Add SQLXML, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        mTrace.append("Add AsciiStream, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length).append("]\n");
        mRealPreparedStat.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        mTrace.append("Add BinaryStream, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length)
              .append("]\n");
        mRealPreparedStat.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        mTrace.append("Add CharacterStream, pos=[").append(parameterIndex).append("], value not traced, length=[").append(length)
              .append("]\n");
        mRealPreparedStat.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        mTrace.append("Add AsciiStream, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        mTrace.append("Add BinaryStream, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        mTrace.append("Add CharacterStream, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        mTrace.append("Add NCharacterStream, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        mTrace.append("Add Clob, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        mTrace.append("Add Blob, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        mTrace.append("Add NClob, pos=[").append(parameterIndex).append("], value not traced\n");
        mRealPreparedStat.setNClob(parameterIndex, reader);
    }
}
