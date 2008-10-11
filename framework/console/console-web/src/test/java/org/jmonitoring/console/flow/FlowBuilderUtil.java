package org.jmonitoring.console.flow;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowBuilderUtil {
    public void clear() {
        HibernateManager.getSession().clear();
    }

    public void createSchema() {
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.create(true, true);

    }

    public ExecutionFlowDTO buildAndSaveNewDto(int pNbMethods) {
        ExecutionFlowPO tExecPO = buildNewFullFlow(pNbMethods);
        ConsoleDao tDao = new ConsoleDao(HibernateManager.getSession());
        tDao.insertFullExecutionFlow(tExecPO);
        return DtoHelper.getDeepCopy(tExecPO);
    }

    public int countFlows() {
        SQLQuery tQuery = HibernateManager.getSession().createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        if (tResult != null) {
            return ((Integer) tResult).intValue();
        } else {
            return 0;
        }
    }

    public ExecutionFlowPO buildNewFullFlow(int pNbMethods) {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long currentTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, FlowBuilderUtil.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(currentTime);

        for (int i = 0; i < pNbMethods; i++) {
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
