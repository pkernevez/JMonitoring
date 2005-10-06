package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.utils.DynamicQueryBuilder;
import org.jmonitoring.core.measure.ExecutionFlow;
import org.jmonitoring.core.measure.MeasureException;
import org.jmonitoring.core.measure.MeasurePoint;

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
     * @param pConnection The connection to use for all the Database access.
     */
    public ExecutionFlowMySqlDAO(Connection pConnection)
    {
        mConnection = pConnection;
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
    }

    private Connection mConnection;

    private static final String SQL_INSERT_EXECUTION_FLOW = "INSERT INTO EXECUTION_FLOW (THREAD_NAME, DURATION,"
                    + "  BEGIN_TIME_AS_DATE, BEGIN_TIME, END_TIME, JVM) VALUES "
                    + "(?, ?, STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s.%f'), " + " ?, ?, ?);";

    private static final String SQL_LAST_ID = "SELECT LAST_INSERT_ID();";

    private static final String SQL_INSERT_METHOD_EXECUTION = "INSERT INTO METHOD_EXECUTION "
                    + "(FLOW_ID, SEQUENCE_ID, FULL_CLASS_NAME,"
                    + "METHOD_NAME, DURATION, BEGIN_TIME, END_TIME, PARAMETERS,"
                    + "RESULT, THROWABLE_CLASS_NAME, THROWABLE_MESSAGE, PARENT_ID, RETURN_TYPE, GROUP_NAME)"
                    + " VALUES (?, ?, ?," + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?);";

    /**
     * Insert la totalité d'un flux en base.
     * 
     * @param pExecutionFlow The <code>ExecutionFlow</code> to write into the database.
     * @return The primary key of the inserted <code>ExecutionFlow</code>.
     * @throws SQLException If an exception occures.
     */
    public int insertFullExecutionFlow(ExecutionFlow pExecutionFlow) throws SQLException
    {
        int tThreadId = insertExecutionFlow(pExecutionFlow);

        insertMeasurePointGraph(pExecutionFlow.getFirstMeasure(), tThreadId);

        return tThreadId;
    }

    /**
     * Permet d'insérer en base un nouveau flux d'exécution.
     * 
     * @param pExecutionFlow Le flux d'exécution associé à loguer.
     * @param pConnection La connexion à la base données à utiliser pour les requètes.
     * @return La clé technique associée au flux que l'on vient d'insérer.
     * @throws SQLException
     */
    private int insertExecutionFlow(ExecutionFlow pExecutionFlow) throws SQLException
    {
        Statement tStatement = null;
        PreparedStatement tPStatement = null;
        int tThreadId = -1;

        try
        {
            // Insertion du thread la base de données
            tPStatement = mConnection.prepareStatement(SQL_INSERT_EXECUTION_FLOW);
            int curIndex = 1;
            // THREAD_NAME
            tPStatement.setString(curIndex++, pExecutionFlow.getThreadName());
            // DURATION
            tPStatement.setLong(curIndex++, (pExecutionFlow.getEndTime() - pExecutionFlow.getBeginDate()));
            // BEGIN_TIME_AS_DATE
            DateFormat tFormat = org.jmonitoring.core.configuration.Configuration.getInstance().getDataBaseDateTimeFormater();
            tPStatement.setString(curIndex++, tFormat.format(new Date(pExecutionFlow.getBeginDate())));
            // BEGIN_TIME
            tPStatement.setLong(curIndex++, pExecutionFlow.getBeginDate());
            // END_TIME
            tPStatement.setLong(curIndex++, pExecutionFlow.getEndTime());
            // JVM
            tPStatement.setString(curIndex++, pExecutionFlow.getJvmIdentifier());
            tPStatement.executeUpdate();
        } finally
        {
            if (tPStatement != null)
            {
                tPStatement.close();
            }
        }
        try
        {
            tStatement = mConnection.createStatement();
            tStatement.execute(SQL_LAST_ID);
            ResultSet tResultSet = tStatement.getResultSet();
            tResultSet.next();
            tThreadId = tResultSet.getInt(1);
        } finally
        {
            if (tStatement != null)
            {
                tStatement.close();
            }
        }
        return tThreadId;
    }

    /**
     * Insert la liste des points de mesure en base.
     * 
     * @param pCurrentMeasurePoint La racine courante de l'arbre à logger.
     * @param pThreadId L'identifiant du Thread à utiliser pour la clé technique.
     * @throws SQLException
     */
    private void insertMeasurePointGraph(MeasurePoint pCurrentMeasurePoint, int pThreadId) throws SQLException
    {
        PreparedStatement tPStatement = null;
        try
        {
            tPStatement = mConnection.prepareStatement(SQL_INSERT_METHOD_EXECUTION);
            addBatchStatementWithMeasurePoint(pCurrentMeasurePoint, tPStatement, pThreadId, -1, 0);
            //tPStatement.execute();
            tPStatement.executeBatch();
        } finally
        {
            if (tPStatement != null)
            {
                tPStatement.close();
            }
        }

    }

    /**
     * Ecrit le log dans le Buffer pour éviter les probmèmes de multithreading si on écrit dans le même fichier. Cette
     * méthode est recursive.
     * 
     * @param pCurrentMeasurePoint La racine courante de l'arbre à logger.
     * @param pPStatement Le Statement courant à utiliser pour le batch.
     * @param pThreadId L'identifiant du Thread à utiliser pour la clé technique.
     * @param pParentSequence L'identifiant de la séquence pour créer la hierachie des appels.
     * @param pLastSequence La dernière séquence utilisée pour un insert.
     * @return La valeur de la dernière séquence mise à jour par les appels récursifs.
     * @throws SQLException S'il n'est pas possible d'exécuter la requète sur la base.
     */
    private int addBatchStatementWithMeasurePoint(MeasurePoint pCurrentMeasurePoint,
                    PreparedStatement pPStatement, int pThreadId, int pParentSequence, int pLastSequence)
                    throws SQLException
    {

        int curIndex = 1;
        // THREAD_ID
        pPStatement.setInt(curIndex++, pThreadId);
        // SEQUENCE_ID
        pPStatement.setInt(curIndex++, pLastSequence + 1);
        pCurrentMeasurePoint.setSequenceId(pLastSequence + 1);
        // FULL_CLASS_NAME
        pPStatement.setString(curIndex++, pCurrentMeasurePoint.getClassName());
        // METHOD_NAME
        pPStatement.setString(curIndex++, pCurrentMeasurePoint.getMethodName());
        // DURATION
        pPStatement.setLong(curIndex++, (pCurrentMeasurePoint.getEndTime() - pCurrentMeasurePoint.getBeginTime()));
        // BEGIN_TIME
        pPStatement.setLong(curIndex++, pCurrentMeasurePoint.getBeginTime());
        // END_TIME
        pPStatement.setLong(curIndex++, pCurrentMeasurePoint.getEndTime());
        String tParams, tResult, tThrowableClassName, tThrowableMsg, tReturnType;
        if (Configuration.getInstance().getLogMethodParameter())
        { // On log tous les paramètres
            tParams = pCurrentMeasurePoint.getParams();

            // Maintenant on log le type de retour
            if (pCurrentMeasurePoint.getThrowableClassName() == null)
            { // Le retour de cette méthode s'est bien passé
                tThrowableClassName = null;
                tThrowableMsg = null;
                if (pCurrentMeasurePoint.getReturnValue() == null)
                { // Méthode de type 'void'
                    tResult = "void";
                } else
                { // On log le retour
                    tResult = pCurrentMeasurePoint.getReturnValue();
                }
                tReturnType = "Ok";
            } else
            { // On log l'exception
                tResult = null;
                tThrowableClassName = pCurrentMeasurePoint.getThrowableClassName();
                tThrowableMsg = pCurrentMeasurePoint.getThrowableMessage();
                tReturnType = "Throwable";
            }
        } else
        { // On log que le type de retour
            tParams = null;
            tResult = null;
            tThrowableMsg = null;
            tThrowableClassName = null;
            if (pCurrentMeasurePoint.getThrowableClassName() == null)
            { // Le retour de cette méthode s'est bien passé
                tReturnType = "Ok";
            } else
            {
                tReturnType = "Throwable";
            }
        }
        // PARAMETERS
        pPStatement.setString(curIndex++, tParams);
        // RESULT
        pPStatement.setString(curIndex++, tResult);
        // THROWABLE_CLASS_NAME
        pPStatement.setString(curIndex++, tThrowableClassName);
        // THROWABLE_MESSAGE
        pPStatement.setString(curIndex++, tThrowableMsg);

        // PARENT_ID
        if (pParentSequence != -1)
        {
            pPStatement.setInt(curIndex++, pParentSequence);
        } else
        { // Premier appel, pas de parent
            pPStatement.setObject(curIndex++, null);
        }
        // RETURN_TYPE
        pPStatement.setString(curIndex++, tReturnType);
        // GROUP_NAME
        pPStatement.setString(curIndex++, pCurrentMeasurePoint.getGroupName());
        // On fait le recursif sur les children
        MeasurePoint curChild;
        int curSequence = pLastSequence;
        //        pPStatement.execute();
        pPStatement.addBatch();
        for (Iterator tChildIterator = pCurrentMeasurePoint.getChildren().iterator(); tChildIterator.hasNext();)
        {
            curChild = (MeasurePoint) tChildIterator.next();
            curSequence = addBatchStatementWithMeasurePoint(curChild, pPStatement, pThreadId, pLastSequence + 1,
                            curSequence + 1);
        }
        return curSequence;
    }

    private static final String SQL_SELECT_EXEC_FLOW_SELECT = "SELECT E.ID, E.THREAD_NAME, E.JVM, E.DURATION, "
                    + "E.BEGIN_TIME, E.END_TIME, M.FULL_CLASS_NAME, M.METHOD_NAME, "
                    + "M.GROUP_NAME FROM EXECUTION_FLOW E, METHOD_EXECUTION M ";

    private static final String SQL_SELECT_EXEC_MEASURE_SELECT_JOIN_CLAUSE = "M.FLOW_ID = E.ID AND M.SEQUENCE_ID = 1 ";

    private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_THREAD = "E.THREAD_NAME LIKE ? ";

    // private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_DATE =
    // "BEGIN_DATE = ? ";

    private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_DURATION_MIN = "E.DURATION >= ? ";

    //private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_DURATION_MAX = "DURATION < ? ";

    private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME = "E.BEGIN_TIME_AS_DATE >= ? ";

    private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME_BIS = "E.BEGIN_TIME_AS_DATE < ? ";

    private static final String SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME_MIN = "E.BEGIN_TIME_AS_DATE >= ? ";

    private static final String SQL_SELECT_EXEC_FLOW_ORDER_BY = "ORDER BY E.DATE_BEGIN";

    private static final String SQL_SELECT_EXEC_FLOW_CLASSNAME = "M.FULL_CLASS_NAME LIKE ? ";

    private static final String SQL_SELECT_EXEC_FLOW_METHOD_NAME = "M.METHOD_NAME LIKE ? ";

    private static final String SQL_SELECT_EXEC_FLOW_GROUP_NAME = "M.GROUP_NAME LIKE ? ";

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * Return the database <code>ExecutionFlow</code>s.
     * 
     * @param pCriterion The criterions for the search.
     * @return The <code>ExecutionFlow</code> list matching the criterion.
     * @throws SQLException If an exception occures.
     */
    public ExecutionFlow[] getListOfExecutionFlow(FlowSearchCriterion pCriterion) throws SQLException
    { // On construit la requête

        // Statement tStat = mConnection.prepareStatement();

        DynamicQueryBuilder tBuilder = new DynamicQueryBuilder();
        tBuilder.addQueryPart(SQL_SELECT_EXEC_FLOW_SELECT);
        String curString = pCriterion.getFirstMeasureClassName();
        boolean tNeedJoinOnMeasure = ((curString != null) && (curString.length() != 0));
        curString = pCriterion.getFirstMeasureGroupName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        curString = pCriterion.getFirstMeasureMethodName();
        tNeedJoinOnMeasure = (tNeedJoinOnMeasure || ((curString != null) && (curString.length() != 0)));
        tBuilder.addQueryPartParam(SQL_SELECT_EXEC_MEASURE_SELECT_JOIN_CLAUSE);
        if (tNeedJoinOnMeasure)
        {
            tBuilder.addStringLikeParam(SQL_SELECT_EXEC_FLOW_CLASSNAME, pCriterion.getFirstMeasureClassName());
            tBuilder.addStringLikeParam(SQL_SELECT_EXEC_FLOW_GROUP_NAME, pCriterion.getFirstMeasureGroupName());
            tBuilder.addStringLikeParam(SQL_SELECT_EXEC_FLOW_METHOD_NAME, pCriterion.getFirstMeasureMethodName());
        }

        tBuilder.addStringLikeParam(SQL_SELECT_EXEC_FLOW_CLAUSE_THREAD, pCriterion.getThreadName());
        tBuilder.addLongParam(SQL_SELECT_EXEC_FLOW_CLAUSE_DURATION_MIN, pCriterion.getDurationMin());

        Date tBeginDate = pCriterion.getBeginDate();
        if (tBeginDate != null)
        {
            tBuilder.addDateParam(SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME, tBeginDate);
            tBeginDate = new Date(tBeginDate.getTime() + ONE_DAY);
            tBuilder.addDateParam(SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME_BIS, tBeginDate);
        }
        Date tBeginTimeMin = pCriterion.getBeginTimeMin();
        if (tBeginTimeMin != null)
        {
            tBuilder.addTimeParam(SQL_SELECT_EXEC_FLOW_CLAUSE_BEGIN_TIME_MIN, tBeginTimeMin);
        }
        ResultSet tResult = tBuilder.executeQuery(mConnection);
        LinkedList tList = new LinkedList();
        ExecutionFlow curFlow;
        while (tResult.next())
        {
            curFlow = fillFlowExtractWithThisResultSet(tResult);
            tList.add(curFlow);
        }

        return (ExecutionFlow[]) tList.toArray(new ExecutionFlow[tList.size()]);
    }

    /**
     * Get the list of measure points for the specified <code>ExecutionFlow</code>.
     * 
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlow.
     * @throws SQLException If an exception occures.
     */
    public ExecutionFlow readFullExecutionFlow(int pFlowId) throws SQLException
    {
        sLog.debug("Read Flow");
        ExecutionFlow tResultFlow = readExecutionFlow(pFlowId);
        sLog.debug("Read MeasurePoints of the Flow");
        tResultFlow = readMeasurePointOfThisFlow(tResultFlow);
        return tResultFlow;
    }

    private static final String SQL_SELECT_METHOD_EXECUTION = "SELECT FLOW_ID, SEQUENCE_ID, FULL_CLASS_NAME, "
                    + "METHOD_NAME, DURATION, BEGIN_TIME, " + "END_TIME, PARAMETERS, RESULT, "
                    + "THROWABLE_CLASS_NAME, THROWABLE_MESSAGE, PARENT_ID, "
                    + "RETURN_TYPE, GROUP_NAME From method_execution";

    private static final String SQL_WHERE_CLAUSE_METHOD_EXECUTION = " WHERE FLOW_ID = ? " + "ORDER BY SEQUENCE_ID";

    /**
     * @param resultFlow
     * @return
     * @throws SQLException
     */
    private ExecutionFlow readMeasurePointOfThisFlow(ExecutionFlow pResultFlow) throws SQLException
    {

        String tSQL = SQL_SELECT_METHOD_EXECUTION + SQL_WHERE_CLAUSE_METHOD_EXECUTION;

        PreparedStatement tStat = null;
        ResultSet tResultSet = null;

        try
        {
            tStat = mConnection.prepareStatement(tSQL);
            tStat.setInt(1, pResultFlow.getId());

            tResultSet = tStat.executeQuery();
            sLog.debug("Execute query for MeasurePoint");
            Map tListOfMeasurePoint = new HashMap();
            MeasurePoint curMeasure;
            boolean tFirstTime = true;
            while (tResultSet.next())
            {
                curMeasure = fillMeasureWithThisResultSet(tResultSet, tListOfMeasurePoint);
                if (tFirstTime)
                {
                    pResultFlow.setFirstMeasure(curMeasure);
                    tFirstTime = false;
                }
            }

        } finally
        {
            try
            {
                if (tResultSet != null)
                {
                    tResultSet.close();
                }
            } finally
            {
                if (tStat != null)
                {
                    tStat.close();
                }
            }
        }

        return pResultFlow;
    }

    private MeasurePoint fillMeasureWithThisResultSet(ResultSet pResultSet, Map pListOfMeasurePoint)
                    throws SQLException
    {
        int tParentId = pResultSet.getInt("PARENT_ID");
        MeasurePoint tParent = (MeasurePoint) pListOfMeasurePoint.get(new Integer(tParentId));
        String tClassName = pResultSet.getString("FULL_CLASS_NAME");
        String tMethodName = pResultSet.getString("METHOD_NAME");
        String tParams = pResultSet.getString("PARAMETERS");
        String tGroupName = pResultSet.getString("GROUP_NAME");

        MeasurePoint tMeasure = new MeasurePoint(tParent, tClassName, tMethodName, tGroupName, tParams);
        tMeasure.setFlowId(pResultSet.getInt("FLOW_ID"));
        tMeasure.setSequenceId(pResultSet.getInt("SEQUENCE_ID"));
        sLog.debug("Flush MeasurePoint FlowId=[" + tMeasure.getFlowId() + "] Sequence=["
                        + tMeasure.getSequenceId() + "]");
        pListOfMeasurePoint.put(new Integer(tMeasure.getSequenceId()), tMeasure);
        // DURATION
        tMeasure.setBeginTime(pResultSet.getLong("BEGIN_TIME"));
        tMeasure.setEndTime(pResultSet.getLong("END_TIME"));
        tMeasure.setThrowableClass(pResultSet.getString("THROWABLE_CLASS_NAME"));
        tMeasure.setThrowableMessage(pResultSet.getString("THROWABLE_MESSAGE"));
        tMeasure.setReturnValue(pResultSet.getString("RESULT"));
        tMeasure.setSequenceId(pResultSet.getInt("SEQUENCE_ID"));
        // RETURN_TYPE

        return tMeasure;
    }

    private static final String SQL_SELECT_EXEC_WHERE_CLAUSE = " WHERE M.FLOW_ID = E.ID AND E.ID = ?";

    /**
     * @param flowId
     * @return
     * @throws SQLException
     */
    private ExecutionFlow readExecutionFlow(int pFlowId) throws SQLException
    {
        String tSQL = SQL_SELECT_EXEC_FLOW_SELECT + SQL_SELECT_EXEC_WHERE_CLAUSE;
        PreparedStatement tStat = null;
        ResultSet tResultSet = null;
        try
        {
            sLog.debug("Prepare Statement for Flow");
            tStat = mConnection.prepareStatement(tSQL);
            tStat.setInt(1, pFlowId);
            sLog.debug("Execute Query for Flow SQL=[" + tSQL + "] and Id=" + pFlowId + "]");
            tResultSet = tStat.executeQuery();
            tResultSet.next();
            sLog.debug("Fetch Flow");
            return fillFlowWithThisResultSet(tResultSet);
        } finally
        {
            try
            {
                if (tResultSet != null)
                {
                    tResultSet.close();
                }
            } finally
            {

                if (tStat != null)
                {
                    tStat.close();
                }
            }

        }
    }

    /**
     * @param resultSet
     * @throws SQLException
     */
    private ExecutionFlow fillFlowWithThisResultSet(ResultSet pResultSet) throws SQLException
    {
        int tId = pResultSet.getInt("ID");
        String tThreadName = pResultSet.getString("THREAD_NAME");
        String tJvm = pResultSet.getString("JVM");
        long tBeginTime = pResultSet.getLong("BEGIN_TIME");
        long tEndTime = pResultSet.getLong("END_TIME");
        return new ExecutionFlow(tId, tThreadName, tJvm, tBeginTime, tEndTime);
    }

    private ExecutionFlow fillFlowExtractWithThisResultSet(ResultSet pResultSet) throws SQLException
    {
        int tId = pResultSet.getInt("ID");
        String tThreadName = pResultSet.getString("THREAD_NAME");
        String tJvm = pResultSet.getString("JVM");
        long tBeginTime = pResultSet.getLong("BEGIN_TIME");
        long tEndTime = pResultSet.getLong("END_TIME");
        MeasurePoint tFirstMeasure = new MeasurePoint(null, pResultSet.getString("FULL_CLASS_NAME"), pResultSet
                        .getString("METHOD_NAME"), pResultSet.getString("GROUP_NAME"), (String) null);
        ExecutionFlow tFlow = new ExecutionFlow(tId, tThreadName, tJvm, tBeginTime, tEndTime);
        tFlow.setFirstMeasure(tFirstMeasure);
        return tFlow;
    }

    private static final String SQL_DELETE_LINK = "Update `method_execution` set PARENT_ID=NULL";

    private static final String SQL_DELETE_METHODE_EXECUTION = "Delete from `method_execution`";

    private static final String SQL_DELETE_EXECUTION_FLOW = "delete FROM `execution_flow`";

    private static final String SQL_DELETE_WHERE_FLOW = " where FLOW_ID = ?";

    private static final String SQL_DELETE_WHERE = " where ID = ?";

    private static final String SQL_TRUNCATE_METHODE_EXECUTION = "TRUNCATE TABLE method_execution";

    private static final String SQL_TRUNCATE_EXECUTION_FLOW = "TRUNCATE TABLE execution_flow";

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
     * Delete an <code>ExcecutionFlow</code> an its nested <code>MeasurePoint</code>.
     * 
     * @param pId The <code>ExecutionFlow</code> identifier.
     * @throws SQLException If an exception occures.
     */
    public void deleteFlow(int pId) throws SQLException
    {
        PreparedStatement tPStat = null;
        try
        {

            tPStat = mConnection.prepareStatement(SQL_DELETE_LINK + SQL_DELETE_WHERE_FLOW);
            tPStat.setInt(1, pId);
            tPStat.execute();
            tPStat.close();

            tPStat = mConnection.prepareStatement(SQL_DELETE_METHODE_EXECUTION + SQL_DELETE_WHERE_FLOW);
            tPStat.setInt(1, pId);
            tPStat.execute();
            tPStat.close();

            tPStat = mConnection.prepareStatement(SQL_DELETE_EXECUTION_FLOW + SQL_DELETE_WHERE);
            tPStat.setInt(1, pId);
            tPStat.execute();
            tPStat.close();

        } finally
        {
            if (tPStat != null)

            {
                tPStat.close();
            }
        }

    }

    private static final String SELECT_LIST_OF_MEASURE_POINT = "select *"
                    + " from method_execution where full_class_name = ? And method_name = ?";

    /**
     * Get the list of <code>MeasurePoint</code> with the same classname and methodname.
     * 
     * @param pClassName The classname mask.
     * @param pMethodName The methodname mask.
     * @return The result list.
     * @throws SQLException If DataBase access fail.
     */
    public MeasurePoint[] getListOfMeasurePoint(String pClassName, String pMethodName) throws SQLException
    {
        PreparedStatement tPStat = null;
        try
        {
            tPStat = mConnection.prepareStatement(SELECT_LIST_OF_MEASURE_POINT);

            tPStat.setString(1, pClassName);
            tPStat.setString(2, pMethodName);
            ResultSet tResultSet = tPStat.executeQuery();
            List tList = new ArrayList();
            while (tResultSet.next())
            {
                tList.add(fillMeasureWithThisResultSet(tResultSet, new HashMap()));
            }
            MeasurePoint[] tResult = (MeasurePoint[]) tList.toArray(new MeasurePoint[0]);
            return tResult;
        } finally
        {
            if (tPStat != null)
            {
                tPStat.close();
            }
        }
    }

    private static final String SELECT_MEASURE_POINT = "select *"
                    + " from method_execution where flow_id = ? And sequence_id = ?";

    /**
     * Read a single <code>MeasurePoint</code>.
     * 
     * @param pFlowId The flow identifier.
     * @param pSequenceId The sequence identifier.
     * @return The <code>MeasurePoint</code>.
     * @throws SQLException If DataBase access fail.
     */
    public MeasurePoint readMeasurePoint(int pFlowId, int pSequenceId) throws SQLException
    {
        PreparedStatement tPStat = null;
        try
        {
            tPStat = mConnection.prepareStatement(SELECT_MEASURE_POINT);
            tPStat.setInt(1, pFlowId);
            tPStat.setInt(2, pSequenceId);
            ResultSet tResultSet = tPStat.executeQuery();
            if (!tResultSet.next())
            {
                throw new MeasureException("Unvalid parameter, MeasurePoint doesn't exist... FlowId=[" + pFlowId
                                + "] & SequenceId=[" + pSequenceId + "].");
            }
            return fillMeasureWithThisResultSet(tResultSet, new HashMap());
        } finally
        {
            if (tPStat != null)
            {
                tPStat.close();
            }
        }
    }

    private static final String SQL_SELECT_FULL_MEASURE_POINT = "SELECT Parent.* "
                    + "FROM `method_execution` Parent, `method_execution` m "
                    + "where Parent.FLOW_ID=m.FLOW_ID And m.FLOW_ID = ? "
                    + "And Parent.SEQUENCE_ID = m.PARENT_ID " + "And m.SEQUENCE_ID=? " + "UNION ALL "
                    + "SELECT * FROM `method_execution` m " + "where m.FLOW_ID = ? " + "And m.SEQUENCE_ID=? "
                    + "UNION ALL " + "SELECT * FROM `method_execution` Child " + "where Child.FLOW_ID = ? "
                    + "And Child.PARENT_ID=?";

    /**
     * Get <code>MeasurePoint</code> with its parent and chidren.
     * 
     * @param pFlowId The flow identifier.
     * @param pSequenceId The sequence identifier.
     * @return The <code>MeasurePoint</code>.
     * @throws SQLException If DataBase access fail.
     */
    public MeasurePoint readFullMeasurePoint(int pFlowId, int pSequenceId) throws SQLException
    {
        PreparedStatement tPStat = null;
        try
        {
            tPStat = mConnection.prepareStatement(SQL_SELECT_FULL_MEASURE_POINT);
            int curIndex = 1;
            tPStat.setInt(curIndex++, pFlowId);
            tPStat.setInt(curIndex++, pSequenceId);
            tPStat.setInt(curIndex++, pFlowId);
            tPStat.setInt(curIndex++, pSequenceId);
            tPStat.setInt(curIndex++, pFlowId);
            tPStat.setInt(curIndex++, pSequenceId);
            ResultSet tResultSet = tPStat.executeQuery();
            Map tListOfMeasure = new HashMap();
            while (tResultSet.next())
            {
                fillMeasureWithThisResultSet(tResultSet, tListOfMeasure);
            }

            // The Parent has only one child
            return (MeasurePoint) tListOfMeasure.get(new Integer(pSequenceId));
        } finally
        {
            if (tPStat != null)
            {
                tPStat.close();
            }
        }
    }

    private static final String SELECT_LIST_OF_MEASURE = "SELECT CONCAT(FULL_CLASS_NAME, '.', METHOD_NAME)"
                    + " AS MEASURE_NAME, GROUP_NAME, COUNT(*) As NB" + " FROM `method_execution` m "
                    + "GROUP BY FULL_CLASS_NAME, METHOD_NAME, GROUP_NAME ORDER BY MEASURE_NAME";

    /**
     * Find the <code>List</code> of Measure from the database.
     * 
     * @return The <code>List</code> of all Measure.
     * @throws SQLException If the database is not available.
     */
    public List getListOfMeasure() throws SQLException
    {
        List tResult = new ArrayList();
        PreparedStatement tPStat = null;
        try
        {
            tPStat = mConnection.prepareStatement(SELECT_LIST_OF_MEASURE);
            ResultSet tRSet = tPStat.executeQuery();
            while (tRSet.next())
            {
                tResult.add(new MeasureExtract(tRSet.getString("MEASURE_NAME"), tRSet.getString("GROUP_NAME"),
                                tRSet.getInt("NB")));
            }
            return tResult;
        } finally
        {
            if (tPStat != null)
            {
                tPStat.close();
            }
        }
    }

}