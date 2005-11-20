package org.jmonitoring.sample.driver;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;


/**
 * @author pke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MockPreparedStatement implements PreparedStatement
{

    private static final int TEMPO2 = 17;
    private static final int TEMPO = 13;
    /**
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String pSql) throws SQLException
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Can't sleep in MockConnection", e);
        }
        
        return null;
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String pSql) throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int pMax) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int pMax) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean pEnable) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int pSeconds) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException
    {
        
        return null;
    }

    /**
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String pName) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String pSql) throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException
    {
        
        return null;
    }

    /**
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int pDirection) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int pRows) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String pSql) throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException
    {
        

    }

    /**
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException
    {
        
        return new int[0];
    }

    /**
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException
    {
        
        return null;
    }

    /**
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int pCurrent) throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException
    {
        
        return null;
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(String pSql, int[] pColumnIndexes) throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(String pSql, String[] pColumnNames) throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(String pSql, int pAutoGeneratedKeys) throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(String pSql, int[] pColumnIndexes) throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(String pSql, String[] pColumnNames) throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException
    {
        try
        {
            Thread.sleep(TEMPO2);
        } catch (InterruptedException e)
        {
            throw new RuntimeException("Can't sleep in MockConnection", e);
        }
        return null;
    }

    /**
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException
    {
        
        return 0;
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int pParameterIndex, int pSqlType) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int pParameterIndex, boolean pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int pParameterIndex, byte pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(int pParameterIndex, short pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int pParameterIndex, int pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int pParameterIndex, long pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int pParameterIndex, float pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int pParameterIndex, double pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int pParameterIndex, BigDecimal pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int pParameterIndex, String pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int pParameterIndex, byte[] pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int pParameterIndex, Date pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int pParameterIndex, Time pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int pParameterIndex, Timestamp pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
     */
    public void setUnicodeStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(int pParameterIndex, InputStream pX, int pLength) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType, int pScale) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int pParameterIndex, Object pX, int pTargetSqlType) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int pParameterIndex, Object pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException
    {
        
        return false;
    }

    /**
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(int pParameterIndex, Reader pReader, int pLength) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(int pIndex, Ref pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int pIndex, Blob pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int pIndex, Clob pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int pIndex, Array pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException
    {
        
        return null;
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(int pParameterIndex, Date pX, Calendar pCal) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(int pParameterIndex, Time pX, Calendar pCal) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(int pParameterIndex, Timestamp pX, Calendar pCal) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, pIndexnt, java.lang.String)
     */
    public void setNull(int pParamIndex, int pSqlType, String pTypeName) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(int pParameterIndex, URL pX) throws SQLException
    {
        
        
    }

    /**
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        
        return null;
    }

}
