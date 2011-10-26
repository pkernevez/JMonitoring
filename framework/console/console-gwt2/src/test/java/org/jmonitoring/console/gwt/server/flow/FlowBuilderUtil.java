package org.jmonitoring.console.gwt.server.flow;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
public class FlowBuilderUtil
{
    @Resource(name = "sessionFactory")
    protected SessionFactory mSessionFactory;

    @Resource(name = "consoleDao")
    private ConsoleDao mDao;

    @Autowired
    private FlowServiceImpl mFlowService;

    public ExecutionFlowDTO buildAndSaveNewDto(int pNbMethods)
    {
        ExecutionFlowPO tExecPO = buildNewFullFlow(pNbMethods);

        mDao.insertFullExecutionFlow(tExecPO);
        return mFlowService.convertToDtoDeeply(tExecPO);
    }

    public int countFlows()
    {
        SQLQuery tQuery =
            mSessionFactory.getCurrentSession().createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        if (tResult != null)
        {
            return ((Integer) tResult).intValue();
        } else
        {
            return 0;
        }
    }

    public static ExecutionFlowPO buildNewFullFlow(int pNbMethods)
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long currentTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, FlowBuilderUtil.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(currentTime);

        for (int i = 0; i < pNbMethods; i++)
        {
            tSubPoint = new MethodCallPO(tPoint, FlowBuilderUtil.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
            tSubPoint.setBeginTime(currentTime + 1);
            currentTime = currentTime + 5;
            tSubPoint.setEndTime(currentTime);
        }
        tPoint.setEndTime(currentTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

}
