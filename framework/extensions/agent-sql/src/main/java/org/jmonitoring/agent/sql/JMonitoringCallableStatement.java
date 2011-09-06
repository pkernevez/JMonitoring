package org.jmonitoring.agent.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringCallableStatement extends JMonitoringPreparedStatement implements java.sql.CallableStatement
{
    private final CallableStatement mRealCallStat;

    public JMonitoringCallableStatement(CallableStatement pCallStatement, String pSqlRequest, String pGroupName)
    {
        super("CallableStatement", pCallStatement, pSqlRequest, pGroupName);
        mRealCallStat = pCallStatement;
    }

    public Array getArray(int pI) throws SQLException
    {
        return mRealCallStat.getArray(pI);
    }

    public Array getArray(String parameterName) throws SQLException
    {
        return mRealCallStat.getArray(parameterName);
    }

    public BigDecimal getBigDecimal(int parameterIndex, int pScale) throws SQLException
    {
        return mRealCallStat.getBigDecimal(parameterIndex, pScale);
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getBigDecimal(parameterIndex);
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException
    {
        return mRealCallStat.getBigDecimal(parameterName);
    }

    public Blob getBlob(int pI) throws SQLException
    {
        return mRealCallStat.getBlob(pI);
    }

    public Blob getBlob(String parameterName) throws SQLException
    {
        return mRealCallStat.getBlob(parameterName);
    }

    public boolean getBoolean(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getBoolean(parameterIndex);
    }

    public boolean getBoolean(String parameterName) throws SQLException
    {
        return mRealCallStat.getBoolean(parameterName);
    }

    public byte getByte(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getByte(parameterIndex);
    }

    public byte getByte(String parameterName) throws SQLException
    {
        return mRealCallStat.getByte(parameterName);
    }

    public byte[] getBytes(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getBytes(parameterIndex);
    }

    public byte[] getBytes(String parameterName) throws SQLException
    {
        return mRealCallStat.getBytes(parameterName);
    }

    public Clob getClob(int pI) throws SQLException
    {
        return mRealCallStat.getClob(pI);
    }

    public Clob getClob(String parameterName) throws SQLException
    {
        return mRealCallStat.getClob(parameterName);
    }

    public Date getDate(int parameterIndex, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getDate(parameterIndex, pCal);
    }

    public Date getDate(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getDate(parameterIndex);
    }

    public Date getDate(String parameterName, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getDate(parameterName, pCal);
    }

    public Date getDate(String parameterName) throws SQLException
    {
        return mRealCallStat.getDate(parameterName);
    }

    public double getDouble(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getDouble(parameterIndex);
    }

    public double getDouble(String parameterName) throws SQLException
    {
        return mRealCallStat.getDouble(parameterName);
    }

    public float getFloat(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getFloat(parameterIndex);
    }

    public float getFloat(String parameterName) throws SQLException
    {
        return mRealCallStat.getFloat(parameterName);
    }

    public int getInt(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getInt(parameterIndex);
    }

    public int getInt(String parameterName) throws SQLException
    {
        return mRealCallStat.getInt(parameterName);
    }

    public long getLong(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getLong(parameterIndex);
    }

    public long getLong(String parameterName) throws SQLException
    {
        return mRealCallStat.getLong(parameterName);
    }

    @SuppressWarnings("unchecked")
    public Object getObject(int pI, Map pMap) throws SQLException
    {
        return mRealCallStat.getObject(pI, pMap);
    }

    public Object getObject(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getObject(parameterIndex);
    }

    @SuppressWarnings("unchecked")
    public Object getObject(String parameterName, Map pMap) throws SQLException
    {
        return mRealCallStat.getObject(parameterName, pMap);
    }

    public Object getObject(String parameterName) throws SQLException
    {
        return mRealCallStat.getObject(parameterName);
    }

    public Ref getRef(int pI) throws SQLException
    {
        return mRealCallStat.getRef(pI);
    }

    public Ref getRef(String parameterName) throws SQLException
    {
        return mRealCallStat.getRef(parameterName);
    }

    public short getShort(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getShort(parameterIndex);
    }

    public short getShort(String parameterName) throws SQLException
    {
        return mRealCallStat.getShort(parameterName);
    }

    public String getString(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getString(parameterIndex);
    }

    public String getString(String parameterName) throws SQLException
    {
        return mRealCallStat.getString(parameterName);
    }

    public Time getTime(int parameterIndex, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getTime(parameterIndex, pCal);
    }

    public Time getTime(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getTime(parameterIndex);
    }

    public Time getTime(String parameterName, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getTime(parameterName, pCal);
    }

    public Time getTime(String parameterName) throws SQLException
    {
        return mRealCallStat.getTime(parameterName);
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getTimestamp(parameterIndex, pCal);
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getTimestamp(parameterIndex);
    }

    public Timestamp getTimestamp(String parameterName, Calendar pCal) throws SQLException
    {
        return mRealCallStat.getTimestamp(parameterName, pCal);
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException
    {
        return mRealCallStat.getTimestamp(parameterName);
    }

    public URL getURL(int parameterIndex) throws SQLException
    {
        return mRealCallStat.getURL(parameterIndex);
    }

    public URL getURL(String parameterName) throws SQLException
    {
        return mRealCallStat.getURL(parameterName);
    }

    public void registerOutParameter(int pParameterIndex, int pSqlType) throws SQLException
    {
        mRealCallStat.registerOutParameter(pParameterIndex, pSqlType);

    }

    public void registerOutParameter(int parameterIndex, int pSqlType, int pScale) throws SQLException
    {
        mRealCallStat.registerOutParameter(parameterIndex, pSqlType, pScale);
    }

    public void registerOutParameter(int paramIndex, int pSqlType, String pTypeName) throws SQLException
    {
        mRealCallStat.registerOutParameter(paramIndex, pSqlType, pTypeName);
    }

    public void registerOutParameter(String parameterName, int pSqlType, int pScale) throws SQLException
    {
        mRealCallStat.registerOutParameter(parameterName, pSqlType, pScale);
    }

    public void registerOutParameter(String parameterName, int pSqlType, String pTypeName) throws SQLException
    {
        mRealCallStat.registerOutParameter(parameterName, pSqlType, pTypeName);
    }

    public void registerOutParameter(String parameterName, int pSqlType) throws SQLException
    {
        mRealCallStat.registerOutParameter(parameterName, pSqlType);
    }

    public void setAsciiStream(String parameterName, InputStream pX, int pLength) throws SQLException
    {
        mRealCallStat.setAsciiStream(parameterName, pX, pLength);
    }

    public void setBigDecimal(String parameterName, BigDecimal pX) throws SQLException
    {
        mRealCallStat.setBigDecimal(parameterName, pX);
    }

    public void setBinaryStream(String parameterName, InputStream pX, int pLength) throws SQLException
    {
        mRealCallStat.setBinaryStream(parameterName, pX, pLength);
    }

    public void setBoolean(String parameterName, boolean pX) throws SQLException
    {
        mRealCallStat.setBoolean(parameterName, pX);
    }

    public void setByte(String parameterName, byte pX) throws SQLException
    {
        mRealCallStat.setByte(parameterName, pX);
    }

    public void setBytes(String parameterName, byte[] pX) throws SQLException
    {
        mRealCallStat.setBytes(parameterName, pX);
    }

    public void setCharacterStream(String parameterName, Reader pReader, int pLength) throws SQLException
    {
        mRealCallStat.setCharacterStream(parameterName, pReader, pLength);
    }

    public void setDate(String parameterName, Date pX, Calendar pCal) throws SQLException
    {
        mRealCallStat.setDate(parameterName, pX, pCal);
    }

    public void setDate(String parameterName, Date pX) throws SQLException
    {
        mRealCallStat.setDate(parameterName, pX);
    }

    public void setDouble(String parameterName, double pX) throws SQLException
    {
        mRealCallStat.setDouble(parameterName, pX);
    }

    public void setFloat(String parameterName, float pX) throws SQLException
    {
        mRealCallStat.setFloat(parameterName, pX);
    }

    public void setInt(String parameterName, int pX) throws SQLException
    {
        mRealCallStat.setInt(parameterName, pX);
    }

    public void setLong(String parameterName, long pX) throws SQLException
    {
        mRealCallStat.setLong(parameterName, pX);
    }

    public void setNull(String parameterName, int pSqlType) throws SQLException
    {
        mRealCallStat.setNull(parameterName, pSqlType);
    }

    public void setNull(String parameterName, int pSqlType, String pTypeName) throws SQLException
    {
        mRealCallStat.setNull(parameterName, pSqlType, pTypeName);
    }

    public void setObject(String parameterName, Object pX, int pTargetSqlType, int pScale) throws SQLException
    {
        mRealCallStat.setObject(parameterName, pX, pTargetSqlType, pScale);
    }

    public void setObject(String parameterName, Object pX, int pTargetSqlType) throws SQLException
    {
        mRealCallStat.setObject(parameterName, pX, pTargetSqlType);
    }

    public void setObject(String parameterName, Object pX) throws SQLException
    {
        mRealCallStat.setObject(parameterName, pX);
    }

    public void setShort(String parameterName, short pX) throws SQLException
    {
        mRealCallStat.setShort(parameterName, pX);
    }

    public void setString(String parameterName, String pX) throws SQLException
    {
        mRealCallStat.setString(parameterName, pX);
    }

    public void setTime(String parameterName, Time pX, Calendar pCal) throws SQLException
    {
        mRealCallStat.setTime(parameterName, pX, pCal);
    }

    public void setTime(String parameterName, Time pX) throws SQLException
    {
        mRealCallStat.setTime(parameterName, pX);
    }

    public void setTimestamp(String parameterName, Timestamp pX, Calendar pCal) throws SQLException
    {
        mRealCallStat.setTimestamp(parameterName, pX, pCal);
    }

    public void setTimestamp(String parameterName, Timestamp pX) throws SQLException
    {
        mRealCallStat.setTimestamp(parameterName, pX);
    }

    public void setURL(String parameterName, URL pVal) throws SQLException
    {
        mRealCallStat.setURL(parameterName, pVal);
    }

    public boolean wasNull() throws SQLException
    {
        return mRealCallStat.wasNull();
    }
}
