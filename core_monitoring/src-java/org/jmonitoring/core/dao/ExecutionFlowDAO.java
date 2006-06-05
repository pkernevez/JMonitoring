package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPO;

/**
 * Manage the persistance
 * 
 * @author pke
 */
public class ExecutionFlowDAO
{

    private static Log sLog;

    /**
     * Default constructor.
     * 
     * @param pPersistenceManager
     */
    public ExecutionFlowDAO(Session pPersistenceManager)
    {
        mPersistenceManager = pPersistenceManager;
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
    }

    private Session mPersistenceManager;

    /**
     * Insert la totalité d'un flux en base.
     * 
     * @param pExecutionFlow The <code>ExecutionFlow</code> to write into the database.
     * @return The primary key of the inserted <code>ExecutionFlow</code>.
     */
    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        int tThreadId = insertExecutionFlow(pExecutionFlow);

        return tThreadId;
    }

    /**
     * Permet d'insérer en base un nouveau flux d'exécution.
     * 
     * @param pExecutionFlow Le flux d'exécution associé à loguer.
     * @return La clé technique associée au flux que l'on vient d'insérer.
     * @todo menage
     */
    private int insertExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        // ExecutionFlowPO tPo = new ExecutionFlowPO(pExecutionFlowDTO);
        mPersistenceManager.save(pExecutionFlow);
        // pExecutionFlowDTO.setId( tPo.getId() );
        return pExecutionFlow.getId();
    }

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    /**
     * Return the database <code>ExecutionFlowDTO</code>s.
     * 
     * @param pCriterion The criterions for the search.
     * @return The <code>ExecutionFlowDTO</code> list matching the criterion.
     * @throws SQLException If an exception occures.
     */
    public List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion)
    { // On construit la requête

        Criteria tCriteria = mPersistenceManager.createCriteria(ExecutionFlowPO.class);
        if (pCriterion.getThreadName() != null && pCriterion.getThreadName().length()>0)
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
     * @todo Be sure that all MethodCall are already loaded after this call...
     */
    public ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        return (ExecutionFlowPO) mPersistenceManager.load(ExecutionFlowPO.class, new Integer(pFlowId));
    }

    // /**
    // * @param flowId
    // * @return
    // * @throws SQLException
    // */
    // private ExecutionFlowPO readExecutionFlow(int pFlowId)
    // {
    // return (ExecutionFlowPO) mPersistenceManager.load(ExecutionFlowPO.class, new Integer(pFlowId));
    // }

    /**
     * Delete all flows and linked objects. This method, drop and recreate the schema that is faster than the deletion
     * of all instances.
     * 
     * @throws SQLException If an exception occures.
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
     * @todo menage
     */
    public void deleteFlow(int pId) throws UnknownFlowException
    {
        ExecutionFlowPO tFlow = (ExecutionFlowPO) mPersistenceManager.get(ExecutionFlowPO.class, new Integer(pId));
        if (tFlow == null)
        {
            throw new UnknownFlowException("Flow with Id=" + pId
                            + " could not be retreive from database, and can't be delete");
        } else
        {
            mPersistenceManager.delete(tFlow);
        }
    }

    /**
     * Get the list of <code>MethodCallDTO</code> with the same classname and methodname.
     * 
     * @param pClassName The classname mask.
     * @param pMethodName The methodname mask.
     * @return The list of <code>MethodCallPO</code> that math the criteria.
     * @throws SQLException If DataBase access fail.
     */
    public List getListOfMethodCall(String pClassName, String pMethodName)
    {
        Criteria tCriteria = mPersistenceManager.createCriteria(MethodCallPO.class);
        tCriteria.add(Restrictions.like("className", pClassName + "%"));
        tCriteria.add(Restrictions.like("methodName", pMethodName + "%"));
        return tCriteria.list();
    }

    /**
     * Read a single <code>MethodCallDTO</code>.
     * 
     * @param pMethodId The flow identifier.
     * @return The <code>MethodCallDTO</code>.
     * @throws SQLException If DataBase access fail.
     */
    public MethodCallPO readMethodCall(int pMethodId)
    {
        Query tQuery = mPersistenceManager.createQuery("from MethodCallPO m where m.id=:pid");
        tQuery.setInteger("pid", pMethodId);
        return (MethodCallPO) tQuery.uniqueResult();
    }

    private static final String SELECT_LIST_OF_MEASURE = "SELECT MethodCallPO.className  || '.' || MethodCallPO.methodName ," + " MethodCallPO.groupName, COUNT(MethodCallPO) As NB"
                    + " FROM MethodCallPO MethodCallPO "
                    + "GROUP BY MethodCallPO.className, MethodCallPO.methodName, MethodCallPO.groupName "
                    + "ORDER BY MethodCallPO.className  || '.' || MethodCallPO.methodName";

    /**
     * Find the <code>List</code> of Measure from the database.
     * 
     * @return The <code>List</code> of all Measure.
     * @throws SQLException If the database is not available.
     */
    public List getListOfMethodCallExtract() 
    {
        // List tResult = new ArrayList();
        Query tQuery = mPersistenceManager.createQuery(SELECT_LIST_OF_MEASURE);
        List tResult = new ArrayList();
        Object[] tExtract;
        for (Iterator tIt = tQuery.list().iterator(); tIt.hasNext();)
        {
            tExtract = (Object[]) tIt.next();
            tResult.add(new MethodCallExtract((String) tExtract[0], (String) tExtract[1], (Integer) tExtract[2]));
        }
        return tResult;
    }

    public int countFlows()
    {
            SQLQuery tQuery = mPersistenceManager.createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
            Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
            if (tResult != null)
            {
                return ((Integer) tResult).intValue();
            } else
            {
                return 0;
            }

    }

    public void createDataBase()
    {
        sLog.info("Creating new Schema for the DataBase");
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);
        tDdlexport.create(true, true);
        sLog.info("End of the Schema creation for the DataBase");
    }

}