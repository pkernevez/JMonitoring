package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.Request;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.springframework.stereotype.Service;

@Service
public class ConsoleDao extends InsertionDao
{
    // private static final Logger sLog = LoggerFactory.getLogger(ConsoleDao.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name = "formater")
    private FormaterBean formater;

    int countFlows(Request pRequest)
    {
        Criteria tCrit = createCriteria(pRequest);
        tCrit.setProjection(Projections.rowCount());
        return ((Integer) tCrit.list().get(0)).intValue();
    }

    Criteria createCriteria(Request pRequest)
    {
        Criteria tCrit = sessionFactory.getCurrentSession().createCriteria(ExecutionFlowPO.class);
        for (Filter<?> curFilter : pRequest.getFilters())
        {
            if (HibernateConstant.MIN_DURATION.equals(curFilter.getPropertyName()))
            {
                tCrit.add(Restrictions.gt(curFilter.getPropertyName(), Long.valueOf((String) curFilter.getValue())));
            } else if (HibernateConstant.BEGIN_DATE.equals(curFilter.getPropertyName()))
            {
                long tBeginTime = formater.parseDate((String) curFilter.getValue()).getTime();
                tCrit.add(Restrictions.gt("beginTime", tBeginTime));
                tCrit.add(Restrictions.lt("beginTime", tBeginTime + FormaterBean.ONE_DAY));

            } else
            {
                tCrit.add(Restrictions.like(curFilter.getPropertyName(), curFilter.getValue() + "%"));
            }
        }
        return tCrit;
    }

    @SuppressWarnings("unchecked")
    List<FlowExtractDTO> search(Request pRequest)
    {
        Criteria tCrit = createCriteria(pRequest);
        tCrit.setFirstResult(pRequest.getStartRow() - 1);
        tCrit.setMaxResults(pRequest.getPageSize());

        if (pRequest.getSortingColumn() != null)
        {
            if (pRequest.getSortType() == Column.SORTING_ASC)
            {
                tCrit.addOrder(Order.asc(pRequest.getSortingColumn()));
            } else
            {
                tCrit.addOrder(Order.desc(pRequest.getSortingColumn()));
            }
        }

        List<FlowExtractDTO> tResult = new ArrayList<FlowExtractDTO>();
        for (ExecutionFlowPO curExecFlow : (List<ExecutionFlowPO>) tCrit.list())
        {
            tResult.add(toDto(curExecFlow));
        }
        return tResult;
    }

    ExecutionFlowPO loadFlow(int pId)
    {
        return (ExecutionFlowPO) sessionFactory.getCurrentSession().load(ExecutionFlowPO.class, Integer.valueOf(pId));
    }

    /**
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     */
    ExecutionFlowPO loadFullFlow(int pFlowId)
    {
        Session tSession = sessionFactory.getCurrentSession();
        ExecutionFlowPO tFlow = (ExecutionFlowPO) tSession.get(ExecutionFlowPO.class, new Integer(pFlowId));
        Criteria tCriteria = tSession.createCriteria(MethodCallPO.class).setFetchMode("children", FetchMode.JOIN);
        tCriteria.add(Restrictions.eq("flow.id", new Integer(pFlowId)));
        tCriteria.list();
        return tFlow;
    }

    FlowExtractDTO toDto(ExecutionFlowPO pExecFlow)
    {
        FlowExtractDTO tResult = new FlowExtractDTO();
        tResult.setId(pExecFlow.getId());
        tResult.setThreadName(pExecFlow.getThreadName());
        tResult.setServer(pExecFlow.getJvmIdentifier());
        tResult.setDuration(pExecFlow.getDuration());
        tResult.setBeginTime(formater.formatDateTime(pExecFlow.getBeginTime()));
        tResult.setEndTime(formater.formatDateTime(pExecFlow.getEndTime()));
        tResult.setClassName(pExecFlow.getFirstClassName());
        tResult.setMethodName(pExecFlow.getFirstMethodName());
        return tResult;
    }

}
