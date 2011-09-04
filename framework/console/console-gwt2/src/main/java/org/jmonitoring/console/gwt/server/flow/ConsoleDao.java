package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Request;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsoleDao
{
    private static final Logger sLog = LoggerFactory.getLogger(ConsoleDao.class);

    @Resource(name = "sessionFactory")
    protected SessionFactory mSessionFactory;

    int countFlows(Request pRequest)
    {
        createCriteria(pRequest);
        return 0;
    }

    Criteria createCriteria(Request pRequest)
    {
        Criteria tCrit = mSessionFactory.getCurrentSession().createCriteria(ExecutionFlowPO.class);
        
        return tCrit;
    }

    List<FlowExtractDTO> search(Request pRequest)
    {

        return null;
    }

}
