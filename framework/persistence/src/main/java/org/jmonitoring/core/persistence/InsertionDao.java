package org.jmonitoring.core.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class InsertionDao
{
    private static final int BATCH_SIZE = 100;

    private static Log sLog = LogFactory.getLog(InsertionDao.class);

    private PreparedStatement mMethodCallInsertStatement;

    private Session mSession;

    /**
     * Default constructor.
     * @todo remove this constuctor
     * @param pSession The hibrnate Session to use for DataBase access.
     */
    public InsertionDao(Session pSession)
    {
        mSession = pSession;
    }

    /**
     * Default constructor.
     * @todo remove this constuctor
     * @param pSession The hibrnate Session to use for DataBase access.
     */
    public InsertionDao()
    {
        mSession = HibernateManager.getSession();
    }

    /**
     * Insert la totalité d'un flux en base.
     * 
     * @param pExecutionFlow The <code>ExecutionFlow</code> to write into the database.
     * @return The primary key of the inserted <code>ExecutionFlow</code>.
     * @todo Revoir les étapes de l'enregistrement
     */
    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        MethodCallPO tMeth = pExecutionFlow.getFirstMethodCall();
        pExecutionFlow.setFirstMethodCall(null);
        mSession.save(pExecutionFlow);
        mSession.flush();
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
        mSession.flush();
        updateExecutionFlowLink(pExecutionFlow);
        pExecutionFlow.setFirstMethodCall(tMeth);
        return pExecutionFlow.getId();
    }

    private static final String UPDATE_FLOW_WITH_FIRST_METHOD_CALL = "UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?";

    private void updateExecutionFlowLink(ExecutionFlowPO pExecutionFlow)
    {
        PreparedStatement tStat = null;
        try
        {
            try
            {
                tStat = mSession.connection().prepareStatement(UPDATE_FLOW_WITH_FIRST_METHOD_CALL);
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
        for (Iterator tChildIt = pMethodCall.getChildren().iterator(); tChildIt.hasNext();)
        {
            tCurrentIndex = updatePkMethodCall((MethodCallPO) tChildIt.next(), tCurrentIndex + 1);
        }
        return tCurrentIndex;
    }

    private int saveAllMethodCall(MethodCallPO pMethodCall, int pCurrentChildIndex, int pBatchBufferSize)
    {
        int tNewBatchBufferSize = saveMethodCall(pMethodCall, pCurrentChildIndex, pBatchBufferSize++);
        int tChildIndex = 0;
        for (Iterator tChildIt = pMethodCall.getChildren().iterator(); tChildIt.hasNext();)
        {
            tNewBatchBufferSize = saveAllMethodCall((MethodCallPO) tChildIt.next(), tChildIndex++,
                tNewBatchBufferSize + 1);
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
                mMethodCallInsertStatement = mSession.connection().prepareStatement(SQL_INSERT_METHOD_CALL);
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
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getClassName());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getRuntimeClassName());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getMethodName());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getThrowableClass());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getThrowableMessage());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getReturnValue());
            mMethodCallInsertStatement.setString(curIndex++, pMethodCall.getGroupName());
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

    public int countFlows()
    {
        SQLQuery tQuery = mSession.createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        if (tResult != null)
        {
            return ((Integer) tResult).intValue();
        } else
        {
            return 0;
        }
    }

    public Session getSession()
    {
        return mSession;
    }

}
