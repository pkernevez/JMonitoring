package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;

/**
 * Manage the persistance
 * 
 * @author pke
 */
public class ExecutionFlowMySqlDAO
{

    private static Log sLog;

    /**
     * Default constructor.
     * 
     * @param pPersistenceManager
     */
    public ExecutionFlowMySqlDAO(Session pPersistenceManager)
    {
        mPersistenceManager = pPersistenceManager;
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
    }

    private Session mPersistenceManager;

    private Connection mConnection;

    /**
     * Insert la totalité d'un flux en base.
     * 
     * @param pExecutionFlow The <code>ExecutionFlow</code> to write into the database.
     * @return The primary key of the inserted <code>ExecutionFlow</code>.
     */
    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        int tThreadId = insertExecutionFlowDTO(pExecutionFlow);

        // insertMethodCallGraph(pExecutionFlowDTO.getFirstMeasure(), tThreadId);

        return tThreadId;
    }

    /**
     * Permet d'insérer en base un nouveau flux d'exécution.
     * 
     * @param pExecutionFlow Le flux d'exécution associé à loguer.
     * @return La clé technique associée au flux que l'on vient d'insérer.
     * @todo menage
     */
    private int insertExecutionFlowDTO(ExecutionFlowPO pExecutionFlow)
    {
        // ExecutionFlowPO tPo = new ExecutionFlowPO(pExecutionFlowDTO);
        mPersistenceManager.save(pExecutionFlow);
        // pExecutionFlowDTO.setId( tPo.getId() );
        return pExecutionFlow.getId();
    }

    // /**
    // * Insert la liste des points de mesure en base.
    // *
    // * @param pCurrentMethodCall La racine courante de l'arbre à logger.
    // * @param pThreadId L'identifiant du Thread à utiliser pour la clé technique.
    // * @throws SQLException
    // * @todo menage
    // */
    // private void insertMethodCallGraph(MethodCallPO pCurrentMethodCall, int pThreadId) throws SQLException
    // {
    // PreparedStatement tPStatement = null;
    // try
    // {
    // pCurrentMethodCall
    // //tPStatement = mConnection.prepareStatement(SQL_INSERT_METHOD_CALL);
    // //addBatchStatementWithMethodCall(pCurrentMethodCall, tPStatement, pThreadId, -1, 0);
    // // tPStatement.execute();
    // //tPStatement.executeBatch();
    // } finally
    // {
    // if (tPStatement != null)
    // {
    // tPStatement.close();
    // }
    // }
    //
    // }

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    /**
     * Return the database <code>ExecutionFlowDTO</code>s.
     * 
     * @param pCriterion The criterions for the search.
     * @return The <code>ExecutionFlowDTO</code> list matching the criterion.
     * @throws SQLException If an exception occures.
     */
    public List getListOfExecutionFlowDTO(FlowSearchCriterion pCriterion)
    { // On construit la requête

        // Statement tStat = mConnection.prepareStatement();

        //
        // Date tBeginTimeMin = pCriterion.getBeginTimeMin();
        // if (tBeginTimeMin != null)
        // {
        // tBuilder.addTimeParam(SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME_MIN, tBeginTimeMin);
        // }
        // String tSql = tBuilder.getSqlQuery();
        // SQLQuery tQuery = mPersistenceManager.createSQLQuery(tSql);
        // //tQuery.
        // List tList = tQuery.addEntity().list();
        // return (ExecutionFlowDTO[]) tList.toArray(new ExecutionFlowDTO[tList.size()]);

        Criteria tCriteria = mPersistenceManager.createCriteria(ExecutionFlowPO.class);
        if (pCriterion.getThreadName() != null)
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
        String curString = pCriterion.getFirstMeasureClassName();
        boolean tNeedJoinOnMeasure = ((curString != null) && (curString.length() != 0));
        curString = pCriterion.getFirstMeasureGroupName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        curString = pCriterion.getFirstMeasureMethodName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        if (tNeedJoinOnMeasure)
        {
            Criteria tMethodCallCriteria = tCriteria.createCriteria("firstMethodCall");
            String curCriteriaValue = pCriterion.getFirstMeasureClassName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("className", curCriteriaValue + "%"));
            }
            curCriteriaValue = pCriterion.getFirstMeasureMethodName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("methodName", curCriteriaValue + "%"));
            }
            curCriteriaValue = pCriterion.getFirstMeasureGroupName();
            if (curCriteriaValue != null)
            {
                tMethodCallCriteria = tMethodCallCriteria.add(Restrictions.like("groupName", curCriteriaValue + "%"));
            }
        }
        return tCriteria.list();
    }

    /**
     * Get the list of measure points for the specified <code>ExecutionFlowDTO</code>.
     * 
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     * @todo Be sure that all MethodCall are already loaded after this call...
     */
    public ExecutionFlowPO readFullExecutionFlow(int pFlowId)
    {
        sLog.debug("Read Flow");
        ExecutionFlowPO tResultFlow = readExecutionFlow(pFlowId);
        sLog.debug("Read MethodCalls of the Flow");
        // tResultFlow = readMethodCallOfThisFlow(tResultFlow);
        return tResultFlow;
    }

    private static final String SQL_SELECT_METHOD_CALL = "SELECT EXECUTION_FLOW_ID, SEQUENCE_ID, FULL_CLASS_NAME, " + "METHOD_NAME, DURATION, BEGIN_TIME, END_TIME, PARAMETERS, RESULT, "
                    + "THROWABLE_CLASS_NAME, THROWABLE_MESSAGE, PARENT_ID, "
                    + "RETURN_TYPE, GROUP_NAME From method_call";

    private static final String SQL_WHERE_CLAUSE_METHOD_CALL = " WHERE EXECUTION_FLOW_ID = ? " + "ORDER BY SEQUENCE_ID";

    // /**
    // * @param resultFlow
    // * @return
    // * @throws SQLException
    // */
    // private ExecutionFlowDTO readMethodCallOfThisFlow(ExecutionFlowDTO pResultFlow) throws SQLException
    // {
    //
    // String tSQL = SQL_SELECT_METHOD_CALL + SQL_WHERE_CLAUSE_METHOD_CALL;
    //
    // PreparedStatement tStat = null;
    // ResultSet tResultSet = null;
    //
    // try
    // {
    // tStat = mConnection.prepareStatement(tSQL);
    // tStat.setInt(1, pResultFlow.getId());
    //
    // tResultSet = tStat.executeQuery();
    // sLog.debug("Execute query for MethodCallDTO");
    // Map tListOfMethodCall = new HashMap();
    // MethodCallDTO curMeasure;
    // boolean tFirstTime = true;
    // while (tResultSet.next())
    // {
    // curMeasure = fillMeasureWithThisResultSet(tResultSet, tListOfMethodCall);
    // if (tFirstTime)
    // {
    // pResultFlow.setFirstMeasure(curMeasure);
    // tFirstTime = false;
    // }
    // }
    //
    // } finally
    // {
    // try
    // {
    // if (tResultSet != null)
    // {
    // tResultSet.close();
    // }
    // } finally
    // {
    // if (tStat != null)
    // {
    // tStat.close();
    // }
    // }
    // }
    //
    // return pResultFlow;
    // }

    /**
     * @param flowId
     * @return
     * @throws SQLException
     */
    private ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        return (ExecutionFlowPO) mPersistenceManager.load(ExecutionFlowPO.class, new Integer(pFlowId));
    }

    private static final String SQL_TRUNCATE_METHODE_EXECUTION = "TRUNCATE TABLE METHOD_CALL";

    private static final String SQL_TRUNCATE_EXECUTION_FLOW = "TRUNCATE TABLE EXECUTION_FLOW";

    /**
     * Delete all flows and linked objects.
     * 
     * @throws SQLException If an exception occures.
     */
    public void deleteAllFlows() throws SQLException
    {
        Statement tStat = null;
        try
        {
            tStat = mConnection.createStatement();

            tStat.execute(SQL_TRUNCATE_METHODE_EXECUTION);
            tStat.execute(SQL_TRUNCATE_EXECUTION_FLOW);
        } finally
        {
            if (tStat != null)

            {
                tStat.close();
            }
        }

    }

    /**
     * Delete an <code>ExcecutionFlow</code> an its nested <code>MethodCallDTO</code>.
     * 
     * @param pId The <code>ExecutionFlowDTO</code> identifier.
     * @throws SQLException If an exception occures.
     * @todo menage
     */
    public void deleteFlow(int pId) throws SQLException
    {
        ExecutionFlowPO tFlow = (ExecutionFlowPO) mPersistenceManager.get(ExecutionFlowPO.class, new Integer(pId));
        mPersistenceManager.delete(tFlow);

        // PreparedStatement tPStat = null;
        // try
        // {
        //
        // tPStat = mConnection.prepareStatement(SQL_DELETE_LINK + SQL_DELETE_WHERE_FLOW);
        // tPStat.setInt(1, pId);
        // tPStat.execute();
        // tPStat.close();
        //
        // tPStat = mConnection.prepareStatement(SQL_DELETE_METHODE_EXECUTION + SQL_DELETE_WHERE_FLOW);
        // tPStat.setInt(1, pId);
        // tPStat.execute();
        // tPStat.close();
        //
        // tPStat = mConnection.prepareStatement(SQL_DELETE_EXECUTION_FLOW + SQL_DELETE_WHERE);
        // tPStat.setInt(1, pId);
        // tPStat.execute();
        // tPStat.close();
        //
        // } finally
        // {
        // if (tPStat != null)
        //
        // {
        // tPStat.close();
        // }
        // }
        //
    }

    private static final String SELECT_LIST_OF_MEASURE_POINT = "select *" + " from method_call where full_class_name = ? And method_name = ?";

    /**
     * Get the list of <code>MethodCallDTO</code> with the same classname and methodname.
     * 
     * @param pClassName The classname mask.
     * @param pMethodName The methodname mask.
     * @return The result list.
     * @throws SQLException If DataBase access fail.
     */
    public MethodCallDTO[] getListOfMethodCall(String pClassName, String pMethodName) throws SQLException
    {
        // PreparedStatement tPStat = null;
        // try
        // {
        // tPStat = mConnection.prepareStatement(SELECT_LIST_OF_MEASURE_POINT);
        //
        // tPStat.setString(1, pClassName);
        // tPStat.setString(2, pMethodName);
        // ResultSet tResultSet = tPStat.executeQuery();
        // List tList = new ArrayList();
        // while (tResultSet.next())
        // {
        // tList.add(fillMeasureWithThisResultSet(tResultSet, new HashMap()));
        // }
        // MethodCallDTO[] tResult = (MethodCallDTO[]) tList.toArray(new MethodCallDTO[0]);
        // return tResult;
        // } finally
        // {
        // if (tPStat != null)
        // {
        // tPStat.close();
        // }
        // }
        return null;
    }

    private static final String SELECT_MEASURE_POINT = "select *" + " from method_call where EXECUTION_FLOW_ID = ? And sequence_id = ?";

    /**
     * Read a single <code>MethodCallDTO</code>.
     * 
     * @param pFlowId The flow identifier.
     * @param pSequenceId The sequence identifier.
     * @return The <code>MethodCallDTO</code>.
     * @throws SQLException If DataBase access fail.
     */
    public MethodCallDTO readMethodCall(int pFlowId, int pSequenceId) throws SQLException
    {
        // PreparedStatement tPStat = null;
        // try
        // {
        // tPStat = mConnection.prepareStatement(SELECT_MEASURE_POINT);
        // tPStat.setInt(1, pFlowId);
        // tPStat.setInt(2, pSequenceId);
        // ResultSet tResultSet = tPStat.executeQuery();
        // if (!tResultSet.next())
        // {
        // throw new MeasureException("Unvalid parameter, MethodCallDTO doesn't exist... FlowId=[" + pFlowId
        // + "] & SequenceId=["
        // + pSequenceId
        // + "].");
        // }
        // return fillMeasureWithThisResultSet(tResultSet, new HashMap());
        // } finally
        // {
        // if (tPStat != null)
        // {
        // tPStat.close();
        // }
        // }
        return null;
    }

    // private static final String SQL_SELECT_FULL_MEASURE_POINT = "SELECT Parent.* " + "FROM `method_call` Parent,
    // `method_call` m "
    // + "where Parent.EXECUTION_FLOW_ID=m.EXECUTION_FLOW_ID And m.EXECUTION_FLOW_ID = ? "
    // + "And Parent.SEQUENCE_ID = m.PARENT_ID "
    // + "And m.SEQUENCE_ID=? "
    // + "UNION ALL "
    // + "SELECT * FROM `method_call` m "
    // + "where m.EXECUTION_FLOW_ID = ? "
    // + "And m.SEQUENCE_ID=? "
    // + "UNION ALL "
    // + "SELECT * FROM `method_call` Child "
    // + "where Child.EXECUTION_FLOW_ID = ? "
    // + "And Child.PARENT_ID=?";

    /**
     * Get <code>MethodCallDTO</code> with its parent and chidren.
     * 
     * @param pFlowId The flow identifier.
     * @param pSequenceId The sequence identifier.
     * @return The <code>MethodCallDTO</code>.
     * @throws SQLException If DataBase access fail.
     */
    public MethodCallDTO readFullMethodCall(int pFlowId, int pSequenceId) throws SQLException
    {
        // PreparedStatement tPStat = null;
        // try
        // {
        // tPStat = mConnection.prepareStatement(SQL_SELECT_FULL_MEASURE_POINT);
        // int curIndex = 1;
        // tPStat.setInt(curIndex++, pFlowId);
        // tPStat.setInt(curIndex++, pSequenceId);
        // tPStat.setInt(curIndex++, pFlowId);
        // tPStat.setInt(curIndex++, pSequenceId);
        // tPStat.setInt(curIndex++, pFlowId);
        // tPStat.setInt(curIndex++, pSequenceId);
        // ResultSet tResultSet = tPStat.executeQuery();
        // Map tListOfMeasure = new HashMap();
        // while (tResultSet.next())
        // {
        // fillMeasureWithThisResultSet(tResultSet, tListOfMeasure);
        // }
        //
        // // The Parent has only one child
        // return (MethodCallDTO) tListOfMeasure.get(new Integer(pSequenceId));
        // } finally
        // {
        // if (tPStat != null)
        // {
        // tPStat.close();
        // }
        // }
        return null;
    }

//    private static final String SELECT_LIST_OF_MEASURE = "SELECT CONCAT(FULL_CLASS_NAME, '.', METHOD_NAME)" + " AS MEASURE_NAME, GROUP_NAME, COUNT(*) As NB"
//    + " FROM `method_call` m "
//    + "GROUP BY FULL_CLASS_NAME, METHOD_NAME, GROUP_NAME ORDER BY MEASURE_NAME";

    private static final String SELECT_LIST_OF_MEASURE = "SELECT MethodCallPO.className  || '.' || MethodCallPO.methodName ,"+
    " MethodCallPO.groupName, COUNT(MethodCallPO) As NB"
    + " FROM MethodCallPO MethodCallPO "
    + "GROUP BY MethodCallPO.className, MethodCallPO.methodName, MethodCallPO.groupName "+
    "ORDER BY MethodCallPO.className  || '.' || MethodCallPO.methodName";

    /**
     * Find the <code>List</code> of Measure from the database.
     * 
     * @return The <code>List</code> of all Measure.
     * @throws SQLException If the database is not available.
     */
    public List getListOfMeasure() throws SQLException
    {
        //List tResult = new ArrayList();
        Query tQuery = mPersistenceManager.createQuery(SELECT_LIST_OF_MEASURE);
        List tResult = new ArrayList();
        MeasureExtract curMeasure;
        Object[] tExtract;
        for (Iterator tIt = tQuery.list().iterator();tIt.hasNext();)
        {
            tExtract = (Object[]) tIt.next();
            tResult.add( new MeasureExtract((String)tExtract[0], (String)tExtract[1], (Integer)tExtract[2]));
        }
        return tResult;        
//        PreparedStatement tPStat = null;
//        try
//        {
//            tPStat = mConnection.prepareStatement(SELECT_LIST_OF_MEASURE);
//            ResultSet tRSet = tPStat.executeQuery();
//            while (tRSet.next())
//            {
//                tResult.add(new MeasureExtract(tRSet.getString("MEASURE_NAME"), tRSet.getString("GROUP_NAME"), tRSet
//                                .getInt("NB")));
//            }
//            return tResult;
//        } finally
//        {
//            if (tPStat != null)
//            {
//                tPStat.close();
//            }
//        }
    }

}