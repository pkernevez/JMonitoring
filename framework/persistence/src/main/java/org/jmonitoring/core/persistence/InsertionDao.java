package org.jmonitoring.core.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class InsertionDao implements IInsertionDao
{
    private static final int BATCH_SIZE = 100;

    private static Logger sLog = LoggerFactory.getLogger(InsertionDao.class);

    private PreparedStatement mMethodCallInsertStatement;

    @Resource(name = "sessionFactory")
    protected SessionFactory mSessionFactory;

    /**
     * Default constructor.
     */
    public InsertionDao()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jmonitoring.core.persistence.IInsertionDao#insertFullExecutionFlow(org.jmonitoring.core.domain.ExecutionFlowPO
     * )
     */
    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        Session tSession = mSessionFactory.getCurrentSession();
        MethodCallPO tMeth = pExecutionFlow.getFirstMethodCall();
        pExecutionFlow.setFirstMethodCall(null);
        tSession.save(pExecutionFlow);
        tSession.flush();
        updatePkMethodCall(tMeth, 1);

        try
        {
            try
            {
                saveAllMethodCall(tMeth, 0, 1);
                mMethodCallInsertStatement.executeBatch();
            } finally
            {
                try
                {
                    if (mMethodCallInsertStatement != null)
                    {
                        mMethodCallInsertStatement.close();
                    }
                } finally
                {
                    mMethodCallInsertStatement = null;
                }
            }
        } catch (SQLException e)
        {
            throw new DataBaseException("Unable to insert METHOD_CALL into DB", e);
        }
        tSession.flush();
        updateExecutionFlowLink(pExecutionFlow);
        pExecutionFlow.setFirstMethodCall(tMeth);
        return pExecutionFlow.getId();
    }

    private static final String UPDATE_FLOW_WITH_FIRST_METHOD_CALL =
        "UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?";

    private void updateExecutionFlowLink(ExecutionFlowPO pExecutionFlow)
    {
        PreparedStatement tStat = null;
        try
        {
            try
            {
                Session tSession = mSessionFactory.getCurrentSession();
                tStat = tSession.connection().prepareStatement(UPDATE_FLOW_WITH_FIRST_METHOD_CALL);
                tStat.setInt(1, 1);
                tStat.setInt(2, pExecutionFlow.getId());
                tStat.execute();
            } finally
            {
                if (tStat != null)
                {
                    tStat.close();
                }
            }
        } catch (SQLException e)
        {
            sLog.error("Unable to UPDATE the link of ExecutionFlowPO in DataBase", e);
            throw new MeasureException("Unable to UPDATE the link of ExecutionFlowPO in DataBase");
        }
    }

    private int updatePkMethodCall(MethodCallPO pMethodCall, final int pCurrentIndex)
    {
        int tCurrentIndex = pCurrentIndex;
        MethodCallPK tPK = new MethodCallPK(pMethodCall.getFlow(), tCurrentIndex);
        pMethodCall.setMethId(tPK);
        for (MethodCallPO tMeth : pMethodCall.getChildren())
        {
            tCurrentIndex = updatePkMethodCall(tMeth, tCurrentIndex + 1);
        }
        return tCurrentIndex;
    }

    private int saveAllMethodCall(MethodCallPO pMethodCall, int pCurrentChildIndex, int pBatchBufferSize)
    {
        int tNewBatchBufferSize = saveMethodCall(pMethodCall, pCurrentChildIndex, pBatchBufferSize++);
        int tChildIndex = 0;
        for (MethodCallPO tMeth : pMethodCall.getChildren())
        {
            tNewBatchBufferSize = saveAllMethodCall(tMeth, tChildIndex++, tNewBatchBufferSize + 1);
        }
        return tNewBatchBufferSize;
    }

    private static final String SQL_INSERT_METHOD_CALL = "INSERT INTO METHOD_CALL "
        + "(FLOW_ID, INDEX_IN_FLOW, PARAMETERS, BEGIN_TIME, END_TIME, FULL_CLASS_NAME, RUNTIME_CLASS_NAME,"
        + "METHOD_NAME, THROWABLE_CLASS_NAME, THROWABLE_MESSAGE, "
        + "RESULT, GROUP_NAME, PARENT_INDEX_IN_FLOW, SUB_METH_INDEX )"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    int saveMethodCall(MethodCallPO pMethodCall, int pCurIndex, int pBatchBufferSize)
    {
        try
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("Inserting MethodCall(FlowId=[" + pMethodCall.getFlow().getId() + "] Index=["
                    + pMethodCall.getPosition() + "] and NewBatchSize=[" + pBatchBufferSize + "]");
            }
            int curIndex = 1;
            if (mMethodCallInsertStatement == null)
            {
                Session tSession = mSessionFactory.getCurrentSession();
                mMethodCallInsertStatement = tSession.connection().prepareStatement(SQL_INSERT_METHOD_CALL);
            }
            int tNewBatchBufferSize = pBatchBufferSize;
            if (tNewBatchBufferSize % BATCH_SIZE == 0)
            {
                tNewBatchBufferSize = 0;
                int tFlushSize = mMethodCallInsertStatement.executeBatch().length;
                if (sLog.isDebugEnabled())
                {
                    sLog.debug("Flush MethodCall, flushsize=[" + tFlushSize + "]");
                }
            }
            mMethodCallInsertStatement.setInt(curIndex++, pMethodCall.getFlow().getId());
            mMethodCallInsertStatement.setInt(curIndex++, pMethodCall.getPosition());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getParams());
            mMethodCallInsertStatement.setLong(curIndex++, pMethodCall.getBeginTime());
            mMethodCallInsertStatement.setLong(curIndex++, pMethodCall.getEndTime());
            mMethodCallInsertStatement.setString(curIndex++, limitString(pMethodCall.getClassName(), 120));
            mMethodCallInsertStatement.setString(curIndex++, limitString(pMethodCall.getRuntimeClassName(), 120));
            mMethodCallInsertStatement.setString(curIndex++, limitString(pMethodCall.getMethodName(), 80));
            mMethodCallInsertStatement.setString(curIndex++, limitString(pMethodCall.getThrowableClass(), 120));
            mMethodCallInsertStatement.setString(curIndex++, limitString(pMethodCall.getThrowableMessage(), 120));
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getReturnValue());
            mMethodCallInsertStatement.setString(curIndex++,limitString(pMethodCall.getGroupName(), 30));
            if (pMethodCall.getParentMethodCall() == null)
            {
                mMethodCallInsertStatement.setObject(curIndex++, null);
            } else
            {
                mMethodCallInsertStatement.setInt(curIndex++, pMethodCall.getParentMethodCall().getPosition());
            }
            mMethodCallInsertStatement.setInt(curIndex++, pCurIndex);
            mMethodCallInsertStatement.addBatch();
            return tNewBatchBufferSize;
        } catch (HibernateException e)
        {
            throw new DataBaseException("Unable to insert METHOD_CALL into DB", e);
        } catch (SQLException e)
        {
            throw new DataBaseException("Unable to insert METHOD_CALL into DB", e);
        }
    }

    private String limitString(String pString, int pMaxSize)
    {
        return (pString==null||pString.length()<=120?pString:pString.substring(0, 117)+"...");
    }

    public int countFlows()
    {
        Session tSession = mSessionFactory.getCurrentSession();
        SQLQuery tQuery = tSession.createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        if (tResult != null)
        {
            return ((Integer) tResult).intValue();
        } else
        {
            return 0;
        }
    }
}
