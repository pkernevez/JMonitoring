package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.common.DataBaseException;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPK;
import org.jmonitoring.core.persistence.MethodCallPO;

/**
 * Manage the persistance
 * 
 * @author pke
 */
public class ExecutionFlowDAO implements IExecutionFlowDAO
{

    private static Log sLog = LogFactory.getLog(ExecutionFlowDAO.class);

    private PreparedStatement mMethodCallInsertStatement;

    private static final int BATCH_SIZE = 100;

    /**
     * Default constructor.
     * 
     * @param pSession The hibrnate Session to use for DataBase access.
     */
    public ExecutionFlowDAO(Session pSession)
    {
        mSession = pSession;
    }

    private Session mSession;

    private static final String UPDATE_FLOW_WITH_FIRST_METHOD_CALL = "UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?";

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#insertFullExecutionFlow(org.jmonitoring.core.persistence.ExecutionFlowPO)
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
                saveAllMethodCall(tMeth, 0, 0);
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
                sLog.debug("Interting MethodCall(FlowId=[" + pMethodCall.getFlow().getId() + "] Index=["
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#getListOfExecutionFlowPO(org.jmonitoring.core.dao.FlowSearchCriterion)
     */
    public List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion)
    { // On construit la requête

        Criteria tCriteria = mSession.createCriteria(ExecutionFlowPO.class);
        if (pCriterion.getThreadName() != null && pCriterion.getThreadName().length() > 0)
        {
            tCriteria = tCriteria.add(Restrictions.like("threadName", pCriterion.getThreadName() + "%"));
        }
        if (pCriterion.getDurationMin() != null)
        {
            tCriteria = tCriteria.add(Restrictions.gt("duration", pCriterion.getDurationMin()));
        }
        if (pCriterion.getBeginDate() != null)
        {
            long tBeginTime = pCriterion.getBeginDate().getTime();
            tCriteria = tCriteria.add(Restrictions.gt("beginTime", new Long(tBeginTime)));
            tCriteria = tCriteria.add(Restrictions.lt("beginTime", new Long(tBeginTime + ONE_DAY)));
        }

        // Search with join on METHOD_CALL if class_name, group_name or method_name of the first method_call
        String curString = pCriterion.getClassName();
        boolean tNeedJoinOnMeasure = ((curString != null) && (curString.length() != 0));
        curString = pCriterion.getGroupName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        curString = pCriterion.getMethodName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        if (tNeedJoinOnMeasure)
        {
            Criteria tMethodCallCriteria = tCriteria.createCriteria("firstMethodCall");
            String curCriteriaValue = pCriterion.getClassName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("className", curCriteriaValue + "%"));
            }
            curCriteriaValue = pCriterion.getMethodName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("methodName", curCriteriaValue + "%"));
            }
            curCriteriaValue = pCriterion.getGroupName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("groupName", curCriteriaValue + "%"));
            }
        }
        return tCriteria.list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#readExecutionFlow(int)
     */
    public ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        ExecutionFlowPO tFlow = (ExecutionFlowPO) mSession.load(ExecutionFlowPO.class, new Integer(pFlowId));
        Criteria tCriteria = mSession.createCriteria(MethodCallPO.class).setFetchMode("children", FetchMode.JOIN);
        tCriteria.add(Restrictions.eq("flow.id", new Integer(pFlowId)));
        tCriteria.list();
        return tFlow;
    }

    // /**
    // * @param flowId
    // * @return
    // * @throws SQLException
    // */
    // private ExecutionFlowPO readExecutionFlow(int pFlowId)
    // {
    // return (ExecutionFlowPO) mSession.load(ExecutionFlowPO.class, new Integer(pFlowId));
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#deleteAllFlows()
     */
    public void deleteAllFlows()
    {
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.drop(true, true);
        tDdlexport.create(true, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#deleteFlow(int)
     */
    public void deleteFlow(int pId) throws UnknownFlowException
    {
        PreparedStatement tStat = null;
        try
        {
            try
            {
                tStat = mSession.connection().prepareStatement(
                    "UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = mSession.connection().prepareStatement(
                    "UPDATE METHOD_CALL set PARENT_INDEX_IN_FLOW=? Where FLOW_ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = mSession.connection().prepareStatement("DELETE FROM METHOD_CALL Where FLOW_ID=?");
                tStat.setInt(1, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = mSession.connection().prepareStatement("DELETE FROM EXECUTION_FLOW Where ID=?");
                tStat.setInt(1, pId);
                tStat.execute();
                int tResultCount = tStat.getUpdateCount();
                tStat.close();
                tStat = null;
                if (tResultCount != 1)
                {
                    throw new UnknownFlowException("Flow with Id=" + pId
                        + " could not be retreive from database, and can't be delete");
                }

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
            throw new MeasureException("Unable to delete the ExecutionFlowPO in DataBase");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#getListOfMethodCall(java.lang.String, java.lang.String)
     */
    public List getListOfMethodCall(String pClassName, String pMethodName)
    {
        Criteria tCriteria = mSession.createCriteria(MethodCallPO.class);
        tCriteria.add(Restrictions.like("className", pClassName + "%"));
        tCriteria.add(Restrictions.like("methodName", pMethodName + "%"));
        return tCriteria.list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#readMethodCall(int, int)
     */
    public MethodCallPO readMethodCall(int pFlowId, int pMethodId)
    {
        Query tQuery = mSession
            .createQuery("from MethodCallPO m where m.methId.flow.id=:flowId and m.methId.position=:pid");
        tQuery.setInteger("flowId", pFlowId);
        tQuery.setInteger("pid", pMethodId);
        MethodCallPO tMeth = (MethodCallPO) tQuery.uniqueResult();
        if (tMeth == null)
        {
            throw new ObjectNotFoundException("FlowId=" + pFlowId + " and Position=" + pMethodId, MethodCallPO.class
                .getName());
        } else
        {
            return tMeth;
        }
    }

    private static final String SELECT_LIST_OF_MEASURE = "SELECT MethodCallPO.className,  MethodCallPO.methodName ,"
        + " MethodCallPO.groupName, COUNT(MethodCallPO.id.position) As NB" + " FROM MethodCallPO MethodCallPO "
        + "GROUP BY MethodCallPO.className, MethodCallPO.methodName, MethodCallPO.groupName "
        + "ORDER BY MethodCallPO.className  || '.' || MethodCallPO.methodName";

    private static final int EXTRACT_NB_POS = 3;

    private static final int EXTRACT_GROUPNAME_POS = 2;

    private static final int EXTRACT_METHODNAME_POS = 1;

    private static final int EXTRACT_CLASSNAME_POS = 0;

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#getListOfMethodCallExtract()
     */
    public List getListOfMethodCallExtract()
    {
        // List tResult = new ArrayList();
        Query tQuery = mSession.createQuery(SELECT_LIST_OF_MEASURE);
        List tResult = new ArrayList();
        Object[] tExtract;
        for (Iterator tIt = tQuery.list().iterator(); tIt.hasNext();)
        {
            tExtract = (Object[]) tIt.next();
            tResult.add(new MethodCallExtractDTO((String) tExtract[EXTRACT_CLASSNAME_POS],
                (String) tExtract[EXTRACT_METHODNAME_POS], (String) tExtract[EXTRACT_GROUPNAME_POS],
                (Integer) tExtract[EXTRACT_NB_POS]));
        }
        return tResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#countFlows()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#createDataBase()
     */
    public void createDataBase()
    {
        sLog.info("Creating new Schema for the DataBase");
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);
        tDdlexport.create(true, true);
        sLog.info("End of the Schema creation for the DataBase");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IExecutionFlowDao#getMethodCallList(java.lang.String, java.lang.String, long, long)
     */
    public List getMethodCallList(String pClassName, String pMethodName, long pDurationMin, long pDurationMax)
    {
        Query tQuery = mSession.getNamedQuery("GetMethodCallList");
        tQuery.setString("className", pClassName);
        tQuery.setString("methodName", pMethodName);
        tQuery.setLong("durationMin", pDurationMin);
        tQuery.setLong("durationMax", pDurationMax);
        return tQuery.list();
    }

}