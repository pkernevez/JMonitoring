package org.jmonitoring.agent.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.aspectj.lang.Signature;
import org.jmonitoring.agent.info.impl.DefaultExceptionTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;
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

    private int mStatInsert = 0;

    private int mStatDelete = 0;

    private int mStatUpdated = 0;

    private int mStatNext = 0;

    private int mStatPrevious = 0;

    private final ResultSet mInternalRS;

    private static Signature CLOSE;

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

    public JMonitoringResultSet(ResultSet pInternalRS)
    {
        super();
        mInternalRS = pInternalRS;
    }

    /**
     * @param pRow
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#absolute(int)
     */
    public boolean absolute(int pRow) throws SQLException
    {
        return mInternalRS.absolute(pRow);
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#afterLast()
     */
    public void afterLast() throws SQLException
    {
        mInternalRS.afterLast();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#beforeFirst()
     */
    public void beforeFirst() throws SQLException
    {
        mInternalRS.beforeFirst();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#cancelRowUpdates()
     */
    public void cancelRowUpdates() throws SQLException
    {
        mInternalRS.cancelRowUpdates();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#clearWarnings()
     */
    public void clearWarnings() throws SQLException
    {
        mInternalRS.clearWarnings();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#close()
     */
    public void close() throws SQLException
    {
        StringBuilder tTrace = new StringBuilder();
        tTrace.append("Statistics of resultSet :\n");

        StoreManager tManager = JMonitoringStatement.getStoreManager();
        tManager.logBeginOfMethod(CLOSE, null, new Object[0], "Jdbc", this);
        try
        {
            mInternalRS.close();
            tTrace.append("Inserted=[" + mStatInsert + "]\n");
            tTrace.append("Updated=[" + mStatUpdated + "]\n");
            tTrace.append("Delete=[" + mStatDelete + "]\n");
            tTrace.append("Previous=[" + mStatPrevious + "]\n");
            tTrace.append("Next=[" + mStatNext + "]");
            tManager.logEndOfMethodNormal(sResultTracer, this, tTrace);
        } catch (Error e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        } catch (RuntimeException e)
        {
            tManager.logEndOfMethodWithException(sThrowableTracer, e);
            throw e;
        }
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#deleteRow()
     */
    public void deleteRow() throws SQLException
    {
        mStatDelete++;
        mInternalRS.deleteRow();
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#findColumn(java.lang.String)
     */
    public int findColumn(String pColumnName) throws SQLException
    {
        return mInternalRS.findColumn(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#first()
     */
    public boolean first() throws SQLException
    {
        return mInternalRS.first();
    }

    /**
     * @param pI
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getArray(int)
     */
    public Array getArray(int pI) throws SQLException
    {
        return mInternalRS.getArray(pI);
    }

    /**
     * @param pColName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getArray(java.lang.String)
     */
    public Array getArray(String pColName) throws SQLException
    {
        return mInternalRS.getArray(pColName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getAsciiStream(int)
     */
    public InputStream getAsciiStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getAsciiStream(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
     */
    public InputStream getAsciiStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getAsciiStream(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @param pScale
     * @return
     * @throws SQLException
     * @deprecated
     * @see java.sql.ResultSet#getBigDecimal(int, int)
     */
    @Deprecated
    public BigDecimal getBigDecimal(int pColumnIndex, int pScale) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnIndex, pScale);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @param pScale
     * @return
     * @throws SQLException
     * @deprecated
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
     */
    @Deprecated
    public BigDecimal getBigDecimal(String pColumnName, int pScale) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnName, pScale);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(String pColumnName) throws SQLException
    {
        return mInternalRS.getBigDecimal(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBinaryStream(int)
     */
    public InputStream getBinaryStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBinaryStream(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
     */
    public InputStream getBinaryStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getBinaryStream(pColumnName);
    }

    /**
     * @param pI
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBlob(int)
     */
    public Blob getBlob(int pI) throws SQLException
    {
        return mInternalRS.getBlob(pI);
    }

    /**
     * @param pColName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBlob(java.lang.String)
     */
    public Blob getBlob(String pColName) throws SQLException
    {
        return mInternalRS.getBlob(pColName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBoolean(int)
     */
    public boolean getBoolean(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBoolean(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String pColumnName) throws SQLException
    {
        return mInternalRS.getBoolean(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getByte(int)
     */
    public byte getByte(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getByte(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getByte(java.lang.String)
     */
    public byte getByte(String pColumnName) throws SQLException
    {
        return mInternalRS.getByte(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBytes(int)
     */
    public byte[] getBytes(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getBytes(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getBytes(java.lang.String)
     */
    public byte[] getBytes(String pColumnName) throws SQLException
    {
        return mInternalRS.getBytes(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getCharacterStream(int)
     */
    public Reader getCharacterStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getCharacterStream(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
     */
    public Reader getCharacterStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getCharacterStream(pColumnName);
    }

    /**
     * @param pI
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getClob(int)
     */
    public Clob getClob(int pI) throws SQLException
    {
        return mInternalRS.getClob(pI);
    }

    /**
     * @param pColName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getClob(java.lang.String)
     */
    public Clob getClob(String pColName) throws SQLException
    {
        return mInternalRS.getClob(pColName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getConcurrency()
     */
    public int getConcurrency() throws SQLException
    {
        return mInternalRS.getConcurrency();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getCursorName()
     */
    public String getCursorName() throws SQLException
    {
        return mInternalRS.getCursorName();
    }

    /**
     * @param pColumnIndex
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
     */
    public Date getDate(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getDate(pColumnIndex, pCal);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDate(int)
     */
    public Date getDate(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getDate(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
     */
    public Date getDate(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getDate(pColumnName, pCal);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDate(java.lang.String)
     */
    public Date getDate(String pColumnName) throws SQLException
    {
        return mInternalRS.getDate(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDouble(int)
     */
    public double getDouble(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getDouble(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getDouble(java.lang.String)
     */
    public double getDouble(String pColumnName) throws SQLException
    {
        return mInternalRS.getDouble(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException
    {
        return mInternalRS.getFetchDirection();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getFetchSize()
     */
    public int getFetchSize() throws SQLException
    {
        return mInternalRS.getFetchSize();
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getFloat(int)
     */
    public float getFloat(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getFloat(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getFloat(java.lang.String)
     */
    public float getFloat(String pColumnName) throws SQLException
    {
        return mInternalRS.getFloat(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getInt(int)
     */
    public int getInt(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getInt(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getInt(java.lang.String)
     */
    public int getInt(String pColumnName) throws SQLException
    {
        return mInternalRS.getInt(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getLong(int)
     */
    public long getLong(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getLong(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getLong(java.lang.String)
     */
    public long getLong(String pColumnName) throws SQLException
    {
        return mInternalRS.getLong(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException
    {
        return mInternalRS.getMetaData();
    }

    /**
     * @param pI
     * @param pMap
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getObject(int, java.util.Map)
     */
    public Object getObject(int pI, Map<String, Class<?>> pMap) throws SQLException
    {
        return mInternalRS.getObject(pI, pMap);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getObject(int)
     */
    public Object getObject(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getObject(pColumnIndex);
    }

    /**
     * @param pColName
     * @param pMap
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
     */
    public Object getObject(String pColName, Map<String, Class<?>> pMap) throws SQLException
    {
        return mInternalRS.getObject(pColName, pMap);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getObject(java.lang.String)
     */
    public Object getObject(String pColumnName) throws SQLException
    {
        return mInternalRS.getObject(pColumnName);
    }

    /**
     * @param pI
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getRef(int)
     */
    public Ref getRef(int pI) throws SQLException
    {
        return mInternalRS.getRef(pI);
    }

    /**
     * @param pColName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getRef(java.lang.String)
     */
    public Ref getRef(String pColName) throws SQLException
    {
        return mInternalRS.getRef(pColName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getRow()
     */
    public int getRow() throws SQLException
    {
        return mInternalRS.getRow();
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getShort(int)
     */
    public short getShort(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getShort(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getShort(java.lang.String)
     */
    public short getShort(String pColumnName) throws SQLException
    {
        return mInternalRS.getShort(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getStatement()
     */
    public Statement getStatement() throws SQLException
    {
        return mInternalRS.getStatement();
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getString(int)
     */
    public String getString(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getString(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getString(java.lang.String)
     */
    public String getString(String pColumnName) throws SQLException
    {
        return mInternalRS.getString(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
     */
    public Time getTime(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTime(pColumnIndex, pCal);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTime(int)
     */
    public Time getTime(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getTime(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
     */
    public Time getTime(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTime(pColumnName, pCal);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTime(java.lang.String)
     */
    public Time getTime(String pColumnName) throws SQLException
    {
        return mInternalRS.getTime(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(int pColumnIndex, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnIndex, pCal);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTimestamp(int)
     */
    public Timestamp getTimestamp(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @param pCal
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
     */
    public Timestamp getTimestamp(String pColumnName, Calendar pCal) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnName, pCal);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(String pColumnName) throws SQLException
    {
        return mInternalRS.getTimestamp(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getType()
     */
    public int getType() throws SQLException
    {
        return mInternalRS.getType();
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @deprecated
     * @see java.sql.ResultSet#getUnicodeStream(int)
     */
    @Deprecated
    public InputStream getUnicodeStream(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getUnicodeStream(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @deprecated
     * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
     */
    @Deprecated
    public InputStream getUnicodeStream(String pColumnName) throws SQLException
    {
        return mInternalRS.getUnicodeStream(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getURL(int)
     */
    public URL getURL(int pColumnIndex) throws SQLException
    {
        return mInternalRS.getURL(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getURL(java.lang.String)
     */
    public URL getURL(String pColumnName) throws SQLException
    {
        return mInternalRS.getURL(pColumnName);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException
    {
        return mInternalRS.getWarnings();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#insertRow()
     */
    public void insertRow() throws SQLException
    {
        mStatInsert++;
        mInternalRS.insertRow();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#isAfterLast()
     */
    public boolean isAfterLast() throws SQLException
    {
        return mInternalRS.isAfterLast();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#isBeforeFirst()
     */
    public boolean isBeforeFirst() throws SQLException
    {
        return mInternalRS.isBeforeFirst();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#isFirst()
     */
    public boolean isFirst() throws SQLException
    {
        return mInternalRS.isFirst();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#isLast()
     */
    public boolean isLast() throws SQLException
    {
        return mInternalRS.isLast();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#last()
     */
    public boolean last() throws SQLException
    {
        return mInternalRS.last();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#moveToCurrentRow()
     */
    public void moveToCurrentRow() throws SQLException
    {
        mInternalRS.moveToCurrentRow();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#moveToInsertRow()
     */
    public void moveToInsertRow() throws SQLException
    {
        mInternalRS.moveToInsertRow();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#next()
     */
    public boolean next() throws SQLException
    {
        mStatNext++;
        return mInternalRS.next();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#previous()
     */
    public boolean previous() throws SQLException
    {
        mStatPrevious++;
        return mInternalRS.previous();
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#refreshRow()
     */
    public void refreshRow() throws SQLException
    {
        mInternalRS.refreshRow();
    }

    /**
     * @param pRows
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#relative(int)
     */
    public boolean relative(int pRows) throws SQLException
    {
        return mInternalRS.relative(pRows);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#rowDeleted()
     */
    public boolean rowDeleted() throws SQLException
    {
        return mInternalRS.rowDeleted();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#rowInserted()
     */
    public boolean rowInserted() throws SQLException
    {
        return mInternalRS.rowInserted();
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#rowUpdated()
     */
    public boolean rowUpdated() throws SQLException
    {
        return mInternalRS.rowUpdated();
    }

    /**
     * @param pDirection
     * @throws SQLException
     * @see java.sql.ResultSet#setFetchDirection(int)
     */
    public void setFetchDirection(int pDirection) throws SQLException
    {
        mInternalRS.setFetchDirection(pDirection);
    }

    /**
     * @param pRows
     * @throws SQLException
     * @see java.sql.ResultSet#setFetchSize(int)
     */
    public void setFetchSize(int pRows) throws SQLException
    {
        mInternalRS.setFetchSize(pRows);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
     */
    public void updateArray(int pColumnIndex, Array pX) throws SQLException
    {
        mInternalRS.updateArray(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
     */
    public void updateArray(String pColumnName, Array pX) throws SQLException
    {
        mInternalRS.updateArray(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
     */
    public void updateAsciiStream(int pColumnIndex, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateAsciiStream(pColumnIndex, pX, pLength);
    }

    /**
     * @param pColumnName
     * @param pX
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateAsciiStream(String pColumnName, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateAsciiStream(pColumnName, pX, pLength);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
     */
    public void updateBigDecimal(int pColumnIndex, BigDecimal pX) throws SQLException
    {
        mInternalRS.updateBigDecimal(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    public void updateBigDecimal(String pColumnName, BigDecimal pX) throws SQLException
    {
        mInternalRS.updateBigDecimal(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
     */
    public void updateBinaryStream(int pColumnIndex, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateBinaryStream(pColumnIndex, pX, pLength);
    }

    /**
     * @param pColumnName
     * @param pX
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateBinaryStream(String pColumnName, InputStream pX, int pLength) throws SQLException
    {
        mInternalRS.updateBinaryStream(pColumnName, pX, pLength);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
     */
    public void updateBlob(int pColumnIndex, Blob pX) throws SQLException
    {
        mInternalRS.updateBlob(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
     */
    public void updateBlob(String pColumnName, Blob pX) throws SQLException
    {
        mInternalRS.updateBlob(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBoolean(int, boolean)
     */
    public void updateBoolean(int pColumnIndex, boolean pX) throws SQLException
    {
        mInternalRS.updateBoolean(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
     */
    public void updateBoolean(String pColumnName, boolean pX) throws SQLException
    {
        mInternalRS.updateBoolean(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateByte(int, byte)
     */
    public void updateByte(int pColumnIndex, byte pX) throws SQLException
    {
        mInternalRS.updateByte(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
     */
    public void updateByte(String pColumnName, byte pX) throws SQLException
    {
        mInternalRS.updateByte(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBytes(int, byte[])
     */
    public void updateBytes(int pColumnIndex, byte[] pX) throws SQLException
    {
        mInternalRS.updateBytes(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
     */
    public void updateBytes(String pColumnName, byte[] pX) throws SQLException
    {
        mInternalRS.updateBytes(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
     */
    public void updateCharacterStream(int pColumnIndex, Reader pX, int pLength) throws SQLException
    {
        mInternalRS.updateCharacterStream(pColumnIndex, pX, pLength);
    }

    /**
     * @param pColumnName
     * @param pReader
     * @param pLength
     * @throws SQLException
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
     */
    public void updateCharacterStream(String pColumnName, Reader pReader, int pLength) throws SQLException
    {
        mInternalRS.updateCharacterStream(pColumnName, pReader, pLength);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
     */
    public void updateClob(int pColumnIndex, Clob pX) throws SQLException
    {
        mInternalRS.updateClob(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
     */
    public void updateClob(String pColumnName, Clob pX) throws SQLException
    {
        mInternalRS.updateClob(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
     */
    public void updateDate(int pColumnIndex, Date pX) throws SQLException
    {
        mInternalRS.updateDate(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
     */
    public void updateDate(String pColumnName, Date pX) throws SQLException
    {
        mInternalRS.updateDate(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateDouble(int, double)
     */
    public void updateDouble(int pColumnIndex, double pX) throws SQLException
    {
        mInternalRS.updateDouble(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
     */
    public void updateDouble(String pColumnName, double pX) throws SQLException
    {
        mInternalRS.updateDouble(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateFloat(int, float)
     */
    public void updateFloat(int pColumnIndex, float pX) throws SQLException
    {
        mInternalRS.updateFloat(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
     */
    public void updateFloat(String pColumnName, float pX) throws SQLException
    {
        mInternalRS.updateFloat(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateInt(int, int)
     */
    public void updateInt(int pColumnIndex, int pX) throws SQLException
    {
        mInternalRS.updateInt(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateInt(java.lang.String, int)
     */
    public void updateInt(String pColumnName, int pX) throws SQLException
    {
        mInternalRS.updateInt(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateLong(int, long)
     */
    public void updateLong(int pColumnIndex, long pX) throws SQLException
    {
        mInternalRS.updateLong(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateLong(java.lang.String, long)
     */
    public void updateLong(String pColumnName, long pX) throws SQLException
    {
        mInternalRS.updateLong(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @throws SQLException
     * @see java.sql.ResultSet#updateNull(int)
     */
    public void updateNull(int pColumnIndex) throws SQLException
    {
        mInternalRS.updateNull(pColumnIndex);
    }

    /**
     * @param pColumnName
     * @throws SQLException
     * @see java.sql.ResultSet#updateNull(java.lang.String)
     */
    public void updateNull(String pColumnName) throws SQLException
    {
        mInternalRS.updateNull(pColumnName);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @param pScale
     * @throws SQLException
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
     */
    public void updateObject(int pColumnIndex, Object pX, int pScale) throws SQLException
    {
        mInternalRS.updateObject(pColumnIndex, pX, pScale);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
     */
    public void updateObject(int pColumnIndex, Object pX) throws SQLException
    {
        mInternalRS.updateObject(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @param pScale
     * @throws SQLException
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
     */
    public void updateObject(String pColumnName, Object pX, int pScale) throws SQLException
    {
        mInternalRS.updateObject(pColumnName, pX, pScale);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
     */
    public void updateObject(String pColumnName, Object pX) throws SQLException
    {
        mInternalRS.updateObject(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
     */
    public void updateRef(int pColumnIndex, Ref pX) throws SQLException
    {
        mInternalRS.updateRef(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
     */
    public void updateRef(String pColumnName, Ref pX) throws SQLException
    {
        mInternalRS.updateRef(pColumnName, pX);
    }

    /**
     * @throws SQLException
     * @see java.sql.ResultSet#updateRow()
     */
    public void updateRow() throws SQLException
    {
        mStatUpdated++;
        mInternalRS.updateRow();
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateShort(int, short)
     */
    public void updateShort(int pColumnIndex, short pX) throws SQLException
    {
        mInternalRS.updateShort(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateShort(java.lang.String, short)
     */
    public void updateShort(String pColumnName, short pX) throws SQLException
    {
        mInternalRS.updateShort(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateString(int, java.lang.String)
     */
    public void updateString(int pColumnIndex, String pX) throws SQLException
    {
        mInternalRS.updateString(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
     */
    public void updateString(String pColumnName, String pX) throws SQLException
    {
        mInternalRS.updateString(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
     */
    public void updateTime(int pColumnIndex, Time pX) throws SQLException
    {
        mInternalRS.updateTime(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
     */
    public void updateTime(String pColumnName, Time pX) throws SQLException
    {
        mInternalRS.updateTime(pColumnName, pX);
    }

    /**
     * @param pColumnIndex
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
     */
    public void updateTimestamp(int pColumnIndex, Timestamp pX) throws SQLException
    {
        mInternalRS.updateTimestamp(pColumnIndex, pX);
    }

    /**
     * @param pColumnName
     * @param pX
     * @throws SQLException
     * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
     */
    public void updateTimestamp(String pColumnName, Timestamp pX) throws SQLException
    {
        mInternalRS.updateTimestamp(pColumnName, pX);
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.ResultSet#wasNull()
     */
    public boolean wasNull() throws SQLException
    {
        return mInternalRS.wasNull();
    }

}
