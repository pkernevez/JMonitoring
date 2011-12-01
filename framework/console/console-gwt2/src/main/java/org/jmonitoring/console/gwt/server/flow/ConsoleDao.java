package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsoleDao extends InsertionDao
{
    Logger sLog = LoggerFactory.getLogger(ConsoleDao.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name = "formater")
    private FormaterBean formater;

    public int countFlows(Request pRequest)
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

    public Session getSession()
    {
        return mSessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public List<FlowExtractDTO> search(Request pRequest)
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

    public ExecutionFlowPO loadFlow(int pId)
    {
        return (ExecutionFlowPO) sessionFactory.getCurrentSession().load(ExecutionFlowPO.class, Integer.valueOf(pId));
    }

    /**
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     */
    public ExecutionFlowPO loadFullFlow(int pFlowId) throws UnknownEntity
    {
        Session tSession = sessionFactory.getCurrentSession();
        ExecutionFlowPO tFlow = (ExecutionFlowPO) tSession.get(ExecutionFlowPO.class, new Integer(pFlowId));
        if (tFlow == null)
        {
            throw new UnknownEntity("Unable to find ExecFlow with id = " + pFlowId);
        }
        Criteria tCriteria = tSession.createCriteria(MethodCallPO.class).setFetchMode("children", FetchMode.JOIN);
        tCriteria.add(Restrictions.eq("flow.id", new Integer(pFlowId)));
        tCriteria.list();
        // Evict to avoid unusefull updates into DB
        tSession.evict(tFlow);
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

    public MethodCallPO loadMethodCall(int pFlowId, int pPosition)
    {
        Session tSession = sessionFactory.getCurrentSession();
        ExecutionFlowPO tFlow = loadFlow(pFlowId);
        return (MethodCallPO) tSession.get(MethodCallPO.class, new MethodCallPK(tFlow, pPosition));
    }

    /** Return the previous <code>MethodCall</code>'s position from the same <code>ExecutionFlow</code> and group */
    public int getPrevInGroup(int pFlowId, int pPosition, String pGroupName)
    {
        Criteria tCrit = mSessionFactory.getCurrentSession().createCriteria(MethodCallPO.class);
        tCrit.add(Restrictions.eq("groupName", pGroupName));
        tCrit.add(Restrictions.eq("flow.id", pFlowId));
        tCrit.add(Restrictions.lt("id.position", pPosition));
        tCrit.addOrder(Order.desc("id.position"));
        tCrit.setMaxResults(1);
        MethodCallPO tUniqueResult = (MethodCallPO) tCrit.uniqueResult();
        return (tUniqueResult == null ? -1 : tUniqueResult.getPosition());

    }

    /** Return the next <code>MethodCall</code>'s position from the same <code>ExecutionFlow</code> and group */
    public int getNextInGroup(int pFlowId, int pPosition, String pGroupName)
    {
        Criteria tCrit = mSessionFactory.getCurrentSession().createCriteria(MethodCallPO.class);
        tCrit.add(Restrictions.eq("groupName", pGroupName));
        tCrit.add(Restrictions.eq("flow.id", pFlowId));
        tCrit.add(Restrictions.gt("id.position", pPosition));
        tCrit.setMaxResults(1);
        tCrit.addOrder(Order.asc("id.position"));
        MethodCallPO tUniqueResult = (MethodCallPO) tCrit.uniqueResult();
        return (tUniqueResult == null ? -1 : tUniqueResult.getPosition());
    }

    // /**
    // * Delete all flows and linked objects. This method, drop and recreate the schema that is faster than the deletion
    // * of all instances.
    // */
    // @SuppressWarnings("deprecation")
    // public void deleteAllFlows()
    // {
    // Session tSession = mSessionFactory.getCurrentSession();
    // Connection tCon = tSession.connection();
    // try
    // {
    // SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
    // tDdlexport.drop(true, true);
    // tSession.flush();
    // } finally
    // {
    // try
    // {
    // tCon.close();
    // } catch (SQLException e)
    // {
    // sLog.error("Unable to release resources", e);
    // }
    // }
    // try
    // {
    // tCon = tSession.connection();
    // SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
    // tDdlexport.create(true, true);
    // } finally
    // {
    // try
    // {
    // tCon.close();
    // } catch (SQLException e)
    // {
    // sLog.error("Unable to release resources", e);
    // }
    // }
    // }

    /**
     * Delete an <code>ExcecutionFlow</code> an its nested <code>MethodCallDTO</code>.
     * 
     * @param pId The <code>ExecutionFlowDTO</code> identifier.
     * @throws UnknownFlowException If the flow can't be find in db.
     */
    @SuppressWarnings("deprecation")
    public void deleteFlow(int pId)
    {
        PreparedStatement tStat = null;
        try
        {
            try
            {
                Session tSession = mSessionFactory.getCurrentSession();
                Connection tCon = tSession.connection();
                tStat = tCon.prepareStatement("UPDATE EXECUTION_FLOW set FIRST_METHOD_CALL_INDEX_IN_FLOW=? where ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = tCon.prepareStatement("UPDATE METHOD_CALL set PARENT_INDEX_IN_FLOW=? Where FLOW_ID=?");
                tStat.setNull(1, Types.INTEGER);
                tStat.setInt(2, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = tCon.prepareStatement("DELETE FROM METHOD_CALL Where FLOW_ID=?");
                tStat.setInt(1, pId);
                tStat.execute();
                tStat.close();
                tStat = null;

                tStat = tCon.prepareStatement("DELETE FROM EXECUTION_FLOW Where ID=?");
                tStat.setInt(1, pId);
                tStat.execute();
                int tResultCount = tStat.getUpdateCount();
                tStat.close();
                tStat = null;
                if (tResultCount != 1)
                {
                    throw new RuntimeException("Flow with Id=" + pId
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

    public List<Distribution> getDistribution(String pClassName, String pMethodName, long pGapDuration)
    {
        // TODO Find a better way for the gapDuration
        String tHql =
            "select new org.jmonitoring.console.gwt.server.flow.Distribution( count(*) as n , round(((meth.endTime-meth.beginTime) / "
                + pGapDuration
                + ")-0.49, 0 )) from MethodCallPO meth "
                + "WHERE meth.className = :className AND meth.methodName = :methodName "
                + "group by  round(((meth.endTime-meth.beginTime) / "
                + pGapDuration
                + ")-0.49, 0 )"
                + " order by  round(((meth.endTime-meth.beginTime) / " + pGapDuration + ")-0.49, 0 )";
        Query tQuery = getSession().createQuery(tHql);
        tQuery.setString("className", pClassName);
        tQuery.setString("methodName", pMethodName);
        // tQuery.setInteger("gapDuration", pGapDuration);
        @SuppressWarnings("unchecked")
        List<Distribution> tList = tQuery.list();

        long tFinalSize = (tList.size() == 0 ? 0 : tList.get(tList.size() - 1).duration - tList.get(0).duration + 1);
        List<Distribution> tResult = new ArrayList<Distribution>((int) tFinalSize);
        long tDurationPosition = 0;
        for (Distribution tDist : tList)
        {
            for (long i = tDurationPosition; i < tDist.duration; i++)
            {
                tResult.add(new Distribution(0, i * pGapDuration));
            }
            tDurationPosition = tDist.duration + 1;
            tDist.duration = tDist.duration * pGapDuration;
            tResult.add(tDist);
        }
        return tResult;
    }

    public Stats getDurationStats(String pClassName, String pMethodName)
    {
        String tHql =
            "select new org.jmonitoring.console.gwt.server.flow.Stats(min(endTime-beginTime), max(endTime-beginTime), "
                + "avg(endTime-beginTime), count(*)) from MethodCallPO "
                + "WHERE className = :className AND methodName = :methodName ";
        Query tQuery = getSession().createQuery(tHql);
        tQuery.setString("className", pClassName);
        tQuery.setString("methodName", pMethodName);
        Stats tResult = (Stats) tQuery.list().get(0);

        tHql =
            "select sum((:average - (endTime-beginTime))*(:average - (endTime-beginTime))) from MethodCallPO "
                + "WHERE className = :className AND methodName = :methodName ";
        tQuery = getSession().createQuery(tHql);
        tQuery.setString("className", pClassName);
        tQuery.setString("methodName", pMethodName);
        tQuery.setDouble("average", tResult.average);
        tResult.stdDeviation = Math.sqrt((Long) tQuery.list().get(0) / tResult.nbOccurence);
        return tResult;
    }

    @SuppressWarnings("unchecked")
    public List<MethodCallSearchExtractDTO> searchMethodCall(Request pRequest,
        MethodCallSearchCriterion pSearchCriterion)
    {
        Criteria tCrit = createMethodCallSearchCriteria(pSearchCriterion);
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

        List<MethodCallSearchExtractDTO> tResult = new ArrayList<MethodCallSearchExtractDTO>();
        for (MethodCallPO curExecFlow : (List<MethodCallPO>) tCrit.list())
        {
            tResult.add(toDto(curExecFlow));
        }
        return tResult;
    }

    MethodCallSearchExtractDTO toDto(MethodCallPO pMethodCall)
    {
        MethodCallSearchExtractDTO tResult = new MethodCallSearchExtractDTO();
        tResult.setFlowId(String.valueOf(pMethodCall.getFlow().getId()));
        tResult.setFlowBeginDate(formater.formatDateTime(pMethodCall.getFlow().getBeginTime()));
        tResult.setFlowServer(pMethodCall.getFlow().getJvmIdentifier());
        tResult.setFlowDuration(String.valueOf(pMethodCall.getFlow().getDuration()));
        tResult.setFlowThread(pMethodCall.getFlow().getThreadName());
        tResult.setPosition(String.valueOf(pMethodCall.getPosition()));
        tResult.setDuration(String.valueOf(pMethodCall.getDuration()));
        tResult.setClassName(pMethodCall.getClassName());
        tResult.setMethodName(pMethodCall.getMethodName());
        tResult.setGroup(pMethodCall.getGroupName());
        tResult.setHasException(pMethodCall.getThrowableClass() == null ? "" : "yes");
        return tResult;
    }

    static boolean hasValue(String pString)
    {
        return (pString != null) && (pString.length() > 0);
    }

    Criteria createMethodCallSearchCriteria(MethodCallSearchCriterion pCriterion)
    {
        Criteria tCrit = sessionFactory.getCurrentSession().createCriteria(MethodCallPO.class);
        tCrit.setFetchMode("flow", FetchMode.JOIN);
        if (hasValue(pCriterion.getMethodName()))
        {
            tCrit.add(Restrictions.like("methodName", pCriterion.getMethodName() + "%"));
        }
        if (hasValue(pCriterion.getClassName()))
        {
            tCrit.add(Restrictions.like("className", pCriterion.getClassName() + "%"));
        }
        if (hasValue(pCriterion.getParameters()))
        {
            tCrit.add(Restrictions.like("params", pCriterion.getParameters() + "%"));
        }
        if (hasValue(pCriterion.getPosition()))
        {
            tCrit.add(Restrictions.eq("id.position", Integer.parseInt(pCriterion.getPosition())));
        }
        if (hasValue(pCriterion.getParameters()))
        {
            tCrit.add(Restrictions.like("params", pCriterion.getParameters() + "%"));
        }
        if (hasValue(pCriterion.getReturnValue()))
        {
            tCrit.add(Restrictions.like("returnValue", pCriterion.getReturnValue() + "%"));
        }
        if (hasValue(pCriterion.getThrownExceptionClass()))
        {
            tCrit.add(Restrictions.like("throwableClass", pCriterion.getThrownExceptionClass() + "%"));
        }
        if (hasValue(pCriterion.getThrownExceptionMessage()))
        {
            tCrit.add(Restrictions.like("throwableMessage", pCriterion.getThrownExceptionMessage() + "%"));
        }

        // Parent properties
        if (hasValue(pCriterion.getParentPosition()))
        {
            tCrit.add(Restrictions.eq("parent.id.position", Integer.parseInt(pCriterion.getParentPosition())));
        }

        // ExecutionFlow properties
        Criteria tFlowCrit = null;
        if (pCriterion.getFlowBeginDate() != null)
        { // Check this part for non exact day
            tFlowCrit = (tFlowCrit == null ? tCrit.createCriteria("flow") : tFlowCrit);
            tFlowCrit.add(Restrictions.ge("beginTime", pCriterion.getFlowBeginDate().getTime()));
        }
        if (hasValue(pCriterion.getFlowMinDuration()))
        {
            tFlowCrit = (tFlowCrit == null ? tCrit.createCriteria("flow") : tFlowCrit);
            tFlowCrit.add(Restrictions.ge("duration", Long.parseLong(pCriterion.getFlowMinDuration())));
        }
        if (hasValue(pCriterion.getFlowServer()))
        {
            tFlowCrit = (tFlowCrit == null ? tCrit.createCriteria("flow") : tFlowCrit);
            tFlowCrit.add(Restrictions.eq("jvmIdentifier", pCriterion.getFlowServer()));
        }
        if (hasValue(pCriterion.getFlowThread()))
        {
            tFlowCrit = (tFlowCrit == null ? tCrit.createCriteria("flow") : tFlowCrit);
            tFlowCrit.add(Restrictions.eq("threadName", pCriterion.getFlowThread()));
        }
        return tCrit;
    }

    public int countMethodCall(MethodCallSearchCriterion pCrit)
    {
        Criteria tCrit = createMethodCallSearchCriteria(pCrit);
        tCrit.setProjection(Projections.rowCount());
        return ((Integer) tCrit.list().get(0)).intValue();
    }

}
