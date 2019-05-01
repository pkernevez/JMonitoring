package org.jmonitoring.agent.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

import org.aspectj.lang.Signature;
import org.jmonitoring.agent.info.impl.DefaultExceptionTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;
import org.jmonitoring.agent.store.Filter;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringResultSet implements ResultSet
{
    protected static IResultTracer sResultTracer = new ToStringResultTracer();

    protected static IThrowableTracer sThrowableTracer = new DefaultExceptionTracer();

    private final String groupName;

    private int mStatInsert = 0;

    private int mStatDelete = 0;

    private int mStatUpdated = 0;

    private int mStatNext = 0;

    private int mStatPrevious = 0;

    private final ResultSet mInternalRS;

    private static Signature CLOSE;

    private Filter mFilter;

    {
        try
        {
            Class<JMonitoringStatement> tClass = JMonitoringStatement.class;
            CLOSE = new SqlSignature(ResultSet.class, tClass.getMethod("close"));
        } catch (SecurityException e)
        {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    public JMonitoringResultSet(ResultSet pInternalRS, String pGroupName, Filter pFilter)
    {
        super();
        mInternalRS = pInternalRS;
        groupName = pGroupName + "CloseRS";
        mFilter = pFilter;
    }

    public boolean absolute(int pRow) throws SQLException
    {
        return mInternalRS.absolute(pRow);
    }

    public void afterLast() throws SQLException
    {
        mInternalRS.afterLast();
    }

    public void beforeFirst() throws SQLException
    {
        mInternalRS.beforeFirst();
    }

    public void cancelRowUpdates() throws SQLException
    {
        mInternalRS.cancelRowUpdates();
    }

    public void clearWarnings() throws SQLException
    {
        mInternalRS.clearWarnings();
    }

    public void close() throws SQLException
    {
        StringBuilder tTrace = new StringBuilder();
        tTrace.append("Statistics of resultSet :\n");

        StoreManager tManager = JMonitoringStatement.getStoreManager();
        tManager.logBeginOfMethod(CLOSE, null, new Object[0], groupName, this);
        try
        {
            mInternalRS.close();
            tTrace.append("Inserted=[").append(mStatInsert).append("]\n");
            tTrace.append("Updated=[").append(mStatUpdated).append("]\n");
            tTrace.append("Delete=[").append(mStatDelete).append("]\n");
            tTrace.append("Previous=[").append(mStatPrevious).append("]\n");
            tTrace.append("Next=[").append(mStatNext).append("]");
            tManager.logEndOfMethodNormal(sResultTracer, this, tTrace, mFilter);
        } catch (Error | RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e, tTrace, mFilter);
            throw e;
        }
    }

    public void deleteRow() throws SQLException
    {
        mStatDelete++;
        mInternalRS.deleteRow();
    }

    public int findColumn(String pColumnName) throws SQLException
    {
        return mInternalRS.findColumn(pColumnName);
    }

    public boolean first() throws SQLException
    {
        return mInternalRS.first();
    }

    public Array getArray(int pI) throws SQLException
    {
        return mInternalRS.getArray(pI);
    }

    public Array getArray(String pColName) throws SQLException
    {
        return mInternalRS.getArray(pColName);
    }

    public InputStream getAsciiStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getAsciiStream(pColumnIndex);
    }

    public InputStream getAsciiStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getAsciiStream(pColumnName);
    }

    @Deprecated
    public BigDecimal getBigDecimal(int pColumnIndex, int pScale) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnIndex, pScale);
    }

    public BigDecimal getBigDecimal(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnIndex);
    }

    @Deprecated
    public BigDecimal getBigDecimal(String pColumnName, int pScale) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnName, pScale);
    }

    public BigDecimal getBigDecimal(String pColumnName) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnName);
    }

    public InputStream getBinaryStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBinaryStream(pColumnIndex);
    }

    public InputStream getBinaryStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getBinaryStream(pColumnName);
    }

    public Blob getBlob(int pI) throws SQLException
    {
        return mInternalRS.getBlob(pI);
    }

    public Blob getBlob(String pColName) throws SQLException
    {
        return mInternalRS.getBlob(pColName);
    }

    public boolean getBoolean(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBoolean(pColumnIndex);
    }

    public boolean getBoolean(String pColumnName) throws SQLException
    {
        return mInternalRS.getBoolean(pColumnName);
    }

    public byte getByte(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getByte(pColumnIndex);
    }

    public byte getByte(String pColumnName) throws SQLException
    {
        return mInternalRS.getByte(pColumnName);
    }

    public byte[] getBytes(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBytes(pColumnIndex);
    }

    public byte[] getBytes(String pColumnName) throws SQLException
    {
        return mInternalRS.getBytes(pColumnName);
    }

    public Reader getCharacterStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getCharacterStream(pColumnIndex);
    }

    public Reader getCharacterStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getCharacterStream(pColumnName);
    }

    public Clob getClob(int pI) throws SQLException
    {
        return mInternalRS.getClob(pI);
    }

    public Clob getClob(String pColName) throws SQLException
    {
        return mInternalRS.getClob(pColName);
    }

    public int getConcurrency() throws SQLException
    {
        return mInternalRS.getConcurrency();
    }

    public String getCursorName() throws SQLException
    {
        return mInternalRS.getCursorName();
    }

    public Date getDate(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getDate(pColumnIndex, pCal);
    }

    public Date getDate(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getDate(pColumnIndex);
    }

    public Date getDate(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getDate(pColumnName, pCal);
    }

    public Date getDate(String pColumnName) throws SQLException
    {
        return mInternalRS.getDate(pColumnName);
    }

    public double getDouble(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getDouble(pColumnIndex);
    }

    public double getDouble(String pColumnName) throws SQLException
    {
        return mInternalRS.getDouble(pColumnName);
    }

    public int getFetchDirection() throws SQLException
    {
        return mInternalRS.getFetchDirection();
    }

    public int getFetchSize() throws SQLException
    {
        return mInternalRS.getFetchSize();
    }

    public float getFloat(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getFloat(pColumnIndex);
    }

    public float getFloat(String pColumnName) throws SQLException
    {
        return mInternalRS.getFloat(pColumnName);
    }

    public int getInt(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getInt(pColumnIndex);
    }

    public int getInt(String pColumnName) throws SQLException
    {
        return mInternalRS.getInt(pColumnName);
    }

    public long getLong(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getLong(pColumnIndex);
    }

    public long getLong(String pColumnName) throws SQLException
    {
        return mInternalRS.getLong(pColumnName);
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        return mInternalRS.getMetaData();
    }

    public Object getObject(int pI, Map<String, Class<?>> pMap) throws SQLException
    {
        return mInternalRS.getObject(pI, pMap);
    }

    public Object getObject(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getObject(pColumnIndex);
    }

    public Object getObject(String pColName, Map<String, Class<?>> pMap) throws SQLException
    {
        return mInternalRS.getObject(pColName, pMap);
    }

    public Object getObject(String pColumnName) throws SQLException
    {
        return mInternalRS.getObject(pColumnName);
    }

    public Ref getRef(int pI) throws SQLException
    {
        return mInternalRS.getRef(pI);
    }

    public Ref getRef(String pColName) throws SQLException
    {
        return mInternalRS.getRef(pColName);
    }

    public int getRow() throws SQLException
    {
        return mInternalRS.getRow();
    }

    public short getShort(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getShort(pColumnIndex);
    }

    public short getShort(String pColumnName) throws SQLException
    {
        return mInternalRS.getShort(pColumnName);
    }

    public Statement getStatement() throws SQLException
    {
        return mInternalRS.getStatement();
    }

    public String getString(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getString(pColumnIndex);
    }

    public String getString(String pColumnName) throws SQLException
    {
        return mInternalRS.getString(pColumnName);
    }

    public Time getTime(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTime(pColumnIndex, pCal);
    }

    public Time getTime(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getTime(pColumnIndex);
    }

    public Time getTime(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTime(pColumnName, pCal);
    }

    public Time getTime(String pColumnName) throws SQLException
    {
        return mInternalRS.getTime(pColumnName);
    }

    public Timestamp getTimestamp(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnIndex, pCal);
    }

    public Timestamp getTimestamp(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnIndex);
    }

    public Timestamp getTimestamp(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnName, pCal);
    }

    public Timestamp getTimestamp(String pColumnName) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnName);
    }

    public int getType() throws SQLException
    {
        return mInternalRS.getType();
    }

    @Deprecated
    public InputStream getUnicodeStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getUnicodeStream(pColumnIndex);
    }

    @Deprecated
    public InputStream getUnicodeStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getUnicodeStream(pColumnName);
    }

    public URL getURL(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getURL(pColumnIndex);
    }

    public URL getURL(String pColumnName) throws SQLException
    {
        return mInternalRS.getURL(pColumnName);
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return mInternalRS.getWarnings();
    }

    public void insertRow() throws SQLException
    {
        mStatInsert++;
        mInternalRS.insertRow();
    }

    public boolean isAfterLast() throws SQLException
    {
        return mInternalRS.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException
    {
        return mInternalRS.isBeforeFirst();
    }

    public boolean isFirst() throws SQLException
    {
        return mInternalRS.isFirst();
    }

    public boolean isLast() throws SQLException
    {
        return mInternalRS.isLast();
    }

    public boolean last() throws SQLException
    {
        return mInternalRS.last();
    }

    public void moveToCurrentRow() throws SQLException
    {
        mInternalRS.moveToCurrentRow();
    }

    public void moveToInsertRow() throws SQLException
    {
        mInternalRS.moveToInsertRow();
    }

    public boolean next() throws SQLException
    {
        mStatNext++;
        return mInternalRS.next();
    }

    public boolean previous() throws SQLException
    {
        mStatPrevious++;
        return mInternalRS.previous();
    }

    public void refreshRow() throws SQLException
    {
        mInternalRS.refreshRow();
    }

    public boolean relative(int pRows) throws SQLException
    {
        return mInternalRS.relative(pRows);
    }

    public boolean rowDeleted() throws SQLException
    {
        return mInternalRS.rowDeleted();
    }

    public boolean rowInserted() throws SQLException
    {
        return mInternalRS.rowInserted();
    }

    public boolean rowUpdated() throws SQLException
    {
        return mInternalRS.rowUpdated();
    }

    public void setFetchDirection(int pDirection) throws SQLException
    {
        mInternalRS.setFetchDirection(pDirection);
    }

    public void setFetchSize(int pRows) throws SQLException
    {
        mInternalRS.setFetchSize(pRows);
    }

    public void updateArray(int pColumnIndex, Array pX) throws SQLException
    {
        mInternalRS.updateArray(pColumnIndex, pX);
    }

    public void updateArray(String pColumnName, Array pX) throws SQLException
    {
        mInternalRS.updateArray(pColumnName, pX);
    }

    public void updateAsciiStream(int pColumnIndex, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateAsciiStream(pColumnIndex, pX, pLength);
    }

    public void updateAsciiStream(String pColumnName, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateAsciiStream(pColumnName, pX, pLength);
    }

    public void updateBigDecimal(int pColumnIndex, BigDecimal pX) throws SQLException
    {
        mInternalRS.updateBigDecimal(pColumnIndex, pX);
    }

    public void updateBigDecimal(String pColumnName, BigDecimal pX) throws SQLException
    {
        mInternalRS.updateBigDecimal(pColumnName, pX);
    }

    public void updateBinaryStream(int pColumnIndex, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateBinaryStream(pColumnIndex, pX, pLength);
    }

    public void updateBinaryStream(String pColumnName, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateBinaryStream(pColumnName, pX, pLength);
    }

    public void updateBlob(int pColumnIndex, Blob pX) throws SQLException
    {
        mInternalRS.updateBlob(pColumnIndex, pX);
    }

    public void updateBlob(String pColumnName, Blob pX) throws SQLException
    {
        mInternalRS.updateBlob(pColumnName, pX);
    }

    public void updateBoolean(int pColumnIndex, boolean pX) throws SQLException
    {
        mInternalRS.updateBoolean(pColumnIndex, pX);
    }

    public void updateBoolean(String pColumnName, boolean pX) throws SQLException
    {
        mInternalRS.updateBoolean(pColumnName, pX);
    }

    public void updateByte(int pColumnIndex, byte pX) throws SQLException
    {
        mInternalRS.updateByte(pColumnIndex, pX);
    }

    public void updateByte(String pColumnName, byte pX) throws SQLException
    {
        mInternalRS.updateByte(pColumnName, pX);
    }

    public void updateBytes(int pColumnIndex, byte[] pX) throws SQLException
    {
        mInternalRS.updateBytes(pColumnIndex, pX);
    }

    public void updateBytes(String pColumnName, byte[] pX) throws SQLException
    {
        mInternalRS.updateBytes(pColumnName, pX);
    }

    public void updateCharacterStream(int pColumnIndex, Reader pX, int pLength) throws SQLException
    {
        mInternalRS.updateCharacterStream(pColumnIndex, pX, pLength);
    }

    public void updateCharacterStream(String pColumnName, Reader pReader, int pLength) throws SQLException
    {
        mInternalRS.updateCharacterStream(pColumnName, pReader, pLength);
    }

    public void updateClob(int pColumnIndex, Clob pX) throws SQLException
    {
        mInternalRS.updateClob(pColumnIndex, pX);
    }

    public void updateClob(String pColumnName, Clob pX) throws SQLException
    {
        mInternalRS.updateClob(pColumnName, pX);
    }

    public void updateDate(int pColumnIndex, Date pX) throws SQLException
    {
        mInternalRS.updateDate(pColumnIndex, pX);
    }

    public void updateDate(String pColumnName, Date pX) throws SQLException
    {
        mInternalRS.updateDate(pColumnName, pX);
    }

    public void updateDouble(int pColumnIndex, double pX) throws SQLException
    {
        mInternalRS.updateDouble(pColumnIndex, pX);
    }

    public void updateDouble(String pColumnName, double pX) throws SQLException
    {
        mInternalRS.updateDouble(pColumnName, pX);
    }

    public void updateFloat(int pColumnIndex, float pX) throws SQLException
    {
        mInternalRS.updateFloat(pColumnIndex, pX);
    }

    public void updateFloat(String pColumnName, float pX) throws SQLException
    {
        mInternalRS.updateFloat(pColumnName, pX);
    }

    public void updateInt(int pColumnIndex, int pX) throws SQLException
    {
        mInternalRS.updateInt(pColumnIndex, pX);
    }

    public void updateInt(String pColumnName, int pX) throws SQLException
    {
        mInternalRS.updateInt(pColumnName, pX);
    }

    public void updateLong(int pColumnIndex, long pX) throws SQLException
    {
        mInternalRS.updateLong(pColumnIndex, pX);
    }

    public void updateLong(String pColumnName, long pX) throws SQLException
    {
        mInternalRS.updateLong(pColumnName, pX);
    }

    public void updateNull(int pColumnIndex) throws SQLException
    {
        mInternalRS.updateNull(pColumnIndex);
    }

    public void updateNull(String pColumnName) throws SQLException
    {
        mInternalRS.updateNull(pColumnName);
    }

    public void updateObject(int pColumnIndex, Object pX, int pScale) throws SQLException
    {
        mInternalRS.updateObject(pColumnIndex, pX, pScale);
    }

    public void updateObject(int pColumnIndex, Object pX) throws SQLException
    {
        mInternalRS.updateObject(pColumnIndex, pX);
    }

    public void updateObject(String pColumnName, Object pX, int pScale) throws SQLException
    {
        mInternalRS.updateObject(pColumnName, pX, pScale);
    }

    public void updateObject(String pColumnName, Object pX) throws SQLException
    {
        mInternalRS.updateObject(pColumnName, pX);
    }

    public void updateRef(int pColumnIndex, Ref pX) throws SQLException
    {
        mInternalRS.updateRef(pColumnIndex, pX);
    }

    public void updateRef(String pColumnName, Ref pX) throws SQLException
    {
        mInternalRS.updateRef(pColumnName, pX);
    }

    public void updateRow() throws SQLException
    {
        mStatUpdated++;
        mInternalRS.updateRow();
    }

    public void updateShort(int pColumnIndex, short pX) throws SQLException
    {
        mInternalRS.updateShort(pColumnIndex, pX);
    }

    public void updateShort(String pColumnName, short pX) throws SQLException
    {
        mInternalRS.updateShort(pColumnName, pX);
    }

    public void updateString(int pColumnIndex, String pX) throws SQLException
    {
        mInternalRS.updateString(pColumnIndex, pX);
    }

    public void updateString(String pColumnName, String pX) throws SQLException
    {
        mInternalRS.updateString(pColumnName, pX);
    }

    public void updateTime(int pColumnIndex, Time pX) throws SQLException
    {
        mInternalRS.updateTime(pColumnIndex, pX);
    }

    public void updateTime(String pColumnName, Time pX) throws SQLException
    {
        mInternalRS.updateTime(pColumnName, pX);
    }

    public void updateTimestamp(int pColumnIndex, Timestamp pX) throws SQLException
    {
        mInternalRS.updateTimestamp(pColumnIndex, pX);
    }

    public void updateTimestamp(String pColumnName, Timestamp pX) throws SQLException
    {
        mInternalRS.updateTimestamp(pColumnName, pX);
    }

    public boolean wasNull() throws SQLException
    {
        return mInternalRS.wasNull();
    }

    //new
    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return mInternalRS.getRowId(columnIndex);
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return mInternalRS.getRowId(columnLabel);
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        mInternalRS.updateRowId(columnIndex, x);
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        mInternalRS.updateRowId(columnLabel, x);
    }

    @Override
    public int getHoldability() throws SQLException {
        return mInternalRS.getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return mInternalRS.isClosed();
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        mInternalRS.updateNString(columnIndex, nString);
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        mInternalRS.updateNString(columnLabel, nString);
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        mInternalRS.updateNClob(columnIndex, nClob);
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        mInternalRS.updateNClob(columnLabel, nClob);
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return mInternalRS.getNClob(columnIndex);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return mInternalRS.getNClob(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return mInternalRS.getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return mInternalRS.getSQLXML(columnLabel);
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        mInternalRS.updateSQLXML(columnIndex, xmlObject);
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        mInternalRS.updateSQLXML(columnLabel, xmlObject);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return mInternalRS.getNString(columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return mInternalRS.getNString(columnLabel);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return mInternalRS.getNCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return mInternalRS.getNCharacterStream(columnLabel);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        mInternalRS.updateNCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        mInternalRS.updateNCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        mInternalRS.updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        mInternalRS.updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        mInternalRS.updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        mInternalRS.updateAsciiStream(columnLabel, x, length);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        mInternalRS.updateBinaryStream(columnLabel, x, length);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        mInternalRS.updateCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        mInternalRS.updateBlob(columnIndex, inputStream, length);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        mInternalRS.updateBlob(columnLabel, inputStream, length);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        mInternalRS.updateClob(columnIndex, reader, length);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        mInternalRS.updateClob(columnLabel, reader, length);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        mInternalRS.updateNClob(columnIndex, reader, length);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        mInternalRS.updateNClob(columnLabel, reader, length);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        mInternalRS.updateNCharacterStream(columnIndex, x);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        mInternalRS.updateNCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        mInternalRS.updateAsciiStream(columnIndex, x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        mInternalRS.updateBinaryStream(columnIndex, x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        mInternalRS.updateCharacterStream(columnIndex, x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        mInternalRS.updateAsciiStream(columnLabel, x);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        mInternalRS.updateBinaryStream(columnLabel, x);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        mInternalRS.updateCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        mInternalRS.updateBlob(columnIndex, inputStream);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        mInternalRS.updateBlob(columnLabel, inputStream);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        mInternalRS.updateClob(columnIndex, reader);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        mInternalRS.updateClob(columnLabel, reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        mInternalRS.updateNClob(columnIndex, reader);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        mInternalRS.updateNClob(columnLabel, reader);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return mInternalRS.getObject(columnIndex, type);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return mInternalRS.getObject(columnLabel, type);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return mInternalRS.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return mInternalRS.isWrapperFor(iface);
    }
}
