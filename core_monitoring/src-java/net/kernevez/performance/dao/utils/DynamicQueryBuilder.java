package net.kernevez.performance.dao.utils;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;

import net.kernevez.performance.measure.MeasureException;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class DynamicQueryBuilder
{
    private static final int TYPE_INT = 0;

    private static final int TYPE_STRING = 1;

    private static final int TYPE_DATE = 2;

    private static final int TYPE_LONG = 3;

    private static final int TYPE_TIME = 4;

    private static final int TYPE_NO_PARAM = 5;

    private StringBuffer mSqlQuery = new StringBuffer();

    private LinkedList mParamList = new LinkedList();

    private static class ParamEntry
    {

        public ParamEntry(int pType, Object pValue)
        {
            mParamType = pType;
            mValue = pValue;
        }

        private int mParamType;

        private Object mValue;
    }

    private static final String SQL_SELECT_EXEC_FLOW_AND = " AND ";

    private static final String SQL_SELECT_EXEC_FLOW_WHERE = " WHERE ";

    /**
     * Permet d'ajouter un morceau une requête SQL.
     * 
     * @param pSql Le morceaux de SQL a ajouter.
     */
    public void addQueryPart(String pSql)
    {
        mSqlQuery.append(pSql);
    }

    /**
     * Permet d'ajouter un morceau une requête SQL mais qui contient une clause WHERE.
     * 
     * @param pSqlClause Le morceaux de SQL a ajouter.
     */
    public void addQueryPartParam(String pSqlClause)
    {
        mParamList.add(new ParamEntry(TYPE_NO_PARAM, null));
        if (mParamList.size() > 1)
        {
            mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
        } else
        {
            mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

        }
    }

    /**
     * Add an Sql close to the current Sql Request and the corresponding <code>Integer</code> parameter if the param
     * is not null, do nothing if <code>pInt</code> is null.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pInt The parameter to use for the Sql request.
     */
    public void addIntParam(String pSqlClause, Integer pInt)
    {
        if (pInt != null)
        {
            mParamList.add(new ParamEntry(TYPE_INT, pInt));
            if (mParamList.size() > 1)
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
            } else
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

            }
        }
    }

    /**
     * Add an Sql close to the current Sql Request and the corresponding <code>String</code> parameter if the param is
     * not null, do nothing if <code>pString</code> is null.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pString The parameter to use for the Sql request.
     */
    public void addStringParam(String pSqlClause, String pString)
    {
        if ((pString != null) && (pString.length() != 0))
        {
            mParamList.add(new ParamEntry(TYPE_STRING, pString));
            if (mParamList.size() > 1)
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
            } else
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

            }
        }
    }

    /**
     * Same than addStringParam but and a caracter '%' if there isn't one already into the criter.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pString The parameter to use for the Sql request.
     * @see DynamicQueryBuilder.addStringParam(String, String)
     */
    public void addStringLikeParam(String pSqlClause, String pString)
    {
        if ((pString != null) && (pString.length() != 0))
        {
            if (pString.indexOf("%") == -1)
            {
                addStringParam(pSqlClause, pString + "%");
            } else
            {
                addStringParam(pSqlClause, pString);
            }
        }
    }

    /**
     * Add an Sql close to the current Sql Request and the corresponding <code>Long</code> parameter if the param is
     * not null, do nothing if <code>pLong</code> is null.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pLong The parameter to use for the Sql request.
     */
    public void addLongParam(String pSqlClause, Long pLong)
    {
        if (pLong != null)
        {
            mParamList.add(new ParamEntry(TYPE_LONG, pLong));
            if (mParamList.size() > 1)
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
            } else
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

            }
        }
    }

    /**
     * Add an Sql close to the current Sql Request and the corresponding <code>Date</code> parameter if the param is
     * not null, do nothing if <code>pDate</code> is null.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pDate The parameter to use for the Sql request.
     */
    public void addDateParam(String pSqlClause, java.util.Date pDate)
    {
        if (pDate != null)
        {
            mParamList.add(new ParamEntry(TYPE_DATE, pDate));
            if (mParamList.size() > 1)
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
            } else
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

            }
        }
    }

    /**
     * Add an Sql close to the current Sql Request and the corresponding <code>Time</code> parameter if the param is
     * not null, do nothing if <code>pTime</code> is null.
     * 
     * @param pSqlClause The SqlClose to add to the current request.
     * @param pTime The parameter to use for the Sql request.
     */
    public void addTimeParam(String pSqlClause, java.util.Date pTime)
    {
        if (pTime != null)
        {
            mParamList.add(new ParamEntry(TYPE_TIME, pTime));
            if (mParamList.size() > 1)
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_AND + pSqlClause);
            } else
            {
                mSqlQuery.append(SQL_SELECT_EXEC_FLOW_WHERE + pSqlClause);

            }
        }
    }

    /**
     * Execute the dynamic query and return the <code>PreparedStatement</code>.
     * 
     * @param pConnection The connection to use for the query execution.
     * @return The resulting <code>ResultSet</code>.
     * @throws SQLException If an exception occures.
     */
    public ResultSet executeQuery(Connection pConnection) throws SQLException
    {
        String tQuery = mSqlQuery.toString();
        PreparedStatement tPStat = pConnection.prepareStatement(tQuery);
        fillParam(tPStat);
        tPStat.execute();
        return tPStat.getResultSet();
    }

    /**
     * Accessor to the SqlQuery. Defined for testing purpose.
     * 
     * @param pStatement The statement to fill with parameters.
     * @throws SQLException If an error occures.
     */
    protected void fillParam(PreparedStatement pStatement) throws SQLException
    {
        ParamEntry tEntry;
        int curPos = 0;
        for (int i = 0; i < mParamList.size(); i++)
        {
            tEntry = (ParamEntry) mParamList.get(i);
            switch (tEntry.mParamType)
            {
            case TYPE_INT:
                pStatement.setInt(++curPos, ((Integer) tEntry.mValue).intValue());
                break;
            case TYPE_STRING:
                pStatement.setString(++curPos, (String) tEntry.mValue);
                break;
            case TYPE_DATE:
                long tTimeAsLong = ((Date) tEntry.mValue).getTime();
                java.sql.Date tDate = new java.sql.Date(tTimeAsLong);
                pStatement.setDate(++curPos, tDate);
                break;
            case TYPE_LONG:
                pStatement.setLong(++curPos, ((Long) tEntry.mValue).longValue());
                break;
            case TYPE_TIME:
                tTimeAsLong = ((Date) tEntry.mValue).getTime();
                Time tTime = new Time(tTimeAsLong);
                pStatement.setTime(++curPos, tTime);
                break;
            case TYPE_NO_PARAM:
                // Nothing to do 
                break;
            default:
                throw new MeasureException("Type invalid:" + tEntry);
            }

        }
    }

    /**
     * Accessor to the SqlQuery. Defined for testing purpose.
     * 
     * @return The SqlQuery.
     */
    protected String getSqlQuery()
    {
        return mSqlQuery.toString();
    }
}
