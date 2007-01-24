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
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.persistence.InsertionDao;

/**
 * Manage the persistance
 * 
 * @author pke
 */
public class ConsoleDao extends InsertionDao
{
    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    private static Log sLog = LogFactory.getLog(ConsoleDao.class);

    /**
     * Default constructor.
     * 
     * @param pSession The hibrnate Session to use for DataBase access.
     */
    public ConsoleDao(Session pSession)
    {
        super(pSession);
    }

 
    /**
     * Return the database <code>ExecutionFlowDTO</code>s.
     * 
     * @param pCriterion The criterions for the search.
     * @return The <code>ExecutionFlowDTO</code> list matching the criterion.
     */
    public List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion)
    { // On construit la requête

        Criteria tCriteria = getSession().createCriteria(ExecutionFlowPO.class);
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

    /**
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     */
    public ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        ExecutionFlowPO tFlow = (ExecutionFlowPO) getSession().load(ExecutionFlowPO.class, new Integer(pFlowId));
        Criteria tCriteria = getSession().createCriteria(MethodCallPO.class).setFetchMode("children", FetchMode.JOIN);
        tCriteria.add(Restrictions.eq("flow.id", new Integer(pFlowId)));
        tCriteria.list();
        return tFlow;
    }

    /**
     * Delete all flows and linked objects. This method, drop and recreate the schema that is faster than the deletion
     * of all instances.
     */
    public void deleteAllFlows()
    {
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.drop(true, true);
        tDdlexport.create(true, true);
    }

    /**
     * Delete an <code>ExcecutionFlow</code> an its nested <code>MethodCallDTO</code>.
     * 
     * @param pId The <code>ExecutionFlowDTO</code> identifier.
     * @throws UnknownFlowException If the flow can't be find in db.
     */
    public void deleteFlow(int pId) throws UnknownFlowException
    {
        PreparedStatement tStat = null;
        try
        {
            try
            {
                tStat = getSession().connection().prepareStatement(
                    "UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = getSession().connection().prepareStatement(
                    "UPDATE METHOD_CALL set PARENT_INDEX_IN_FLOW=? Where FLOW_ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = getSession().connection().prepareStatement("DELETE FROM METHOD_CALL Where FLOW_ID=?");
                tStat.setInt(1, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = getSession().connection().prepareStatement("DELETE FROM EXECUTION_FLOW Where ID=?");
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

    /**
     * Get the list of <code>MethodCallDTO</code> with the same classname and methodname.
     * 
     * @param pClassName The classname mask.
     * @param pMethodName The methodname mask.
     * @return The list of <code>MethodCallPO</code> that math the criteria.
     */
    public List getListOfMethodCall(String pClassName, String pMethodName)
    {
        Criteria tCriteria = getSession().createCriteria(MethodCallPO.class);
        tCriteria.add(Restrictions.like("className", pClassName + "%"));
        tCriteria.add(Restrictions.like("methodName", pMethodName + "%"));
        return tCriteria.list();
    }

    /**
     * Read a single <code>MethodCallDTO</code>.
     * 
     * @param pFlow
     * 
     * @param pFlow The ExecutionFlow of this <code>MethodCall</code>.
     * @param pMethodId The flow identifier.
     * @return The <code>MethodCallDTO</code>.
     */
    public MethodCallPO readMethodCall(int pFlowId, int pMethodId)
    {
        Query tQuery = getSession()
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

    /**
     * Find the <code>List</code> of Measure from the database.
     * 
     * @return The <code>List</code> of all Measure.
     */
    public List getListOfMethodCallExtract()
    {
        // List tResult = new ArrayList();
        Query tQuery = getSession().createQuery(SELECT_LIST_OF_MEASURE);
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

    public void createDataBase()
    {
        sLog.info("Creating new Schema for the DataBase");
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);
        tDdlexport.create(true, true);
        sLog.info("End of the Schema creation for the DataBase");
    }

    /**
     * 
     * @param pClassName The matching classname
     * @param pMethodName The mathing method name
     * @param pDurationMin The minimum duration of <code>MethodCall</code>
     * @param pDurationMax The maximimu duration of the <code>MethodCall</code>
     * @return La liste d'objet <code>MethodCallFullExtractPO</code> correspondant aux critères.
     */
    public List getMethodCallList(String pClassName, String pMethodName, long pDurationMin, long pDurationMax)
    {
        Query tQuery = getSession().getNamedQuery("GetMethodCallList");
        tQuery.setString("className", pClassName);
        tQuery.setString("methodName", pMethodName);
        tQuery.setLong("durationMin", pDurationMin);
        tQuery.setLong("durationMax", pDurationMax);
        return tQuery.list();
    }

}