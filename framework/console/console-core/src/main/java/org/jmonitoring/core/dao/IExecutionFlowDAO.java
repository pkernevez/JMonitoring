package org.jmonitoring.core.dao;

import java.util.List;

import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public interface IExecutionFlowDAO
{

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;


//    int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow);

    /**
     * Return the database <code>ExecutionFlowDTO</code>s.
     * 
     * @param pCriterion The criterions for the search.
     * @return The <code>ExecutionFlowDTO</code> list matching the criterion.
     */
    List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion);

    /**
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     */
    ExecutionFlowPO readExecutionFlow(int pFlowId);

    /**
     * Delete all flows and linked objects. This method, drop and recreate the schema that is faster than the deletion
     * of all instances.
     */
    void deleteAllFlows();

    /**
     * Delete an <code>ExcecutionFlow</code> an its nested <code>MethodCallDTO</code>.
     * 
     * @param pId The <code>ExecutionFlowDTO</code> identifier.
     * @throws UnknownFlowException If the flow can't be find in db.
     */
    void deleteFlow(int pId) throws UnknownFlowException;

    /**
     * Get the list of <code>MethodCallDTO</code> with the same classname and methodname.
     * 
     * @param pClassName The classname mask.
     * @param pMethodName The methodname mask.
     * @return The list of <code>MethodCallPO</code> that math the criteria.
     */
    List getListOfMethodCall(String pClassName, String pMethodName);

    /**
     * Read a single <code>MethodCallDTO</code>.
     * 
     * @param pFlow
     * 
     * @param pFlow The ExecutionFlow of this <code>MethodCall</code>.
     * @param pMethodId The flow identifier.
     * @return The <code>MethodCallDTO</code>.
     */
    MethodCallPO readMethodCall(int pFlowId, int pMethodId);

    /**
     * Find the <code>List</code> of Measure from the database.
     * 
     * @return The <code>List</code> of all Measure.
     */
    List getListOfMethodCallExtract();

    int countFlows();

    void createDataBase();

    /**
     * 
     * @param pClassName The matching classname
     * @param pMethodName The mathing method name
     * @param pDurationMin The minimum duration of <code>MethodCall</code>
     * @param pDurationMax The maximimu duration of the <code>MethodCall</code>
     * @return La liste d'objet <code>MethodCallFullExtractPO</code> correspondant aux critères.
     */
    List getMethodCallList(String pClassName, String pMethodName, long pDurationMin, long pDurationMax);

}