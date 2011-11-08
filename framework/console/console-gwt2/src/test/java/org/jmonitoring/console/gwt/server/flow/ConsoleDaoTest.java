package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.StringFilter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.jmonitoring.console.gwt.server.common.ExecutionFlowBuilder;
import org.jmonitoring.console.gwt.server.common.MethodCallBuilder;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;

public class ConsoleDaoTest extends PersistanceTestCase
{

    @Resource(name = "consoleDao")
    private ConsoleDao dao;

    @Test
    public void testCreateCriteriaWithoutParam()
    {

        Request tRequest = new Request();
        List<Filter<?>> tFilter = new ArrayList<Filter<?>>();
        tRequest.setFilters(tFilter);

        assertEquals(5, dao.createCriteria(tRequest).list().size());
    }

    @Test
    public void testCreateCriteriaWithParam()
    {
        checkFilter(HibernateConstant.THREAD, "Speci", 2);
        checkFilter(HibernateConstant.THREAD, "Spice", 0);
        checkFilter(HibernateConstant.THREAD, "Speci%", 2);
        checkFilter(HibernateConstant.MIN_DURATION, "200", 1);
        checkFilter(HibernateConstant.MIN_DURATION, "260", 0);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass", 5);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass%", 5);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClazz", 0);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass3", 1);
        checkFilter(HibernateConstant.FIRST_MEASURE_METHOD_NAME, "main2", 1);
        checkFilter(HibernateConstant.FIRST_MEASURE_METHOD_NAME, "main", 5);
        checkFilter(HibernateConstant.FIRST_MEASURE_METHOD_NAME, "main%", 5);
        checkFilter(HibernateConstant.BEGIN_DATE, "12/01/70", 1);
        checkFilter(HibernateConstant.BEGIN_DATE, "15/01/70", 0);
    }

    @Test
    public void testCountFlows()
    {
        Request tRequest = createRequestWithAllRows();
        assertEquals(5, dao.countFlows(tRequest));
    }

    @Test
    public void testSearchSimple()
    {
        Request tRequest = createRequestWithAllRows();
        tRequest.setSortingColumn(HibernateConstant.ID);
        tRequest.setSortType(Column.SORTING_ASC);
        List<FlowExtractDTO> tResult = dao.search(tRequest);
        assertEquals(2, tResult.size());
        assertEquals("MainClass2", tResult.get(0).getClassName());
        assertEquals("MainClass3", tResult.get(1).getClassName());
    }

    @Test
    public void testSearchOrderByDurationDesc()
    {
        Request tRequest = createRequestWithAllRows();
        tRequest.setSortingColumn(HibernateConstant.MIN_DURATION);
        tRequest.setSortType(Column.SORTING_DESC);
        List<FlowExtractDTO> tResult = dao.search(tRequest);
        assertEquals(2, tResult.size());
        assertEquals(110, tResult.get(0).getDuration());
        assertEquals(100, tResult.get(1).getDuration());
    }

    private Request createRequestWithAllRows()
    {
        Request tRequest = new Request();
        tRequest.setPageSize(2);
        tRequest.setStartRow(3);
        List<Filter<?>> tFilter = new ArrayList<Filter<?>>();
        StringFilter curFilter = new StringFilter();
        curFilter.setPropertyName(HibernateConstant.FIRST_MEASURE_CLASS_NAME);
        curFilter.setValue("MainClass");
        tFilter.add(curFilter);
        tRequest.setFilters(tFilter);
        return tRequest;
    }

    private void checkFilter(String pPropertyName, String pValue, int pExceptedLines)
    {
        Request tRequest = new Request();
        tRequest.setPageSize(20);
        List<Filter<?>> tFilter = new ArrayList<Filter<?>>();
        StringFilter curFilter = new StringFilter();
        curFilter.setPropertyName(pPropertyName);
        curFilter.setValue(pValue);
        tFilter.add(curFilter);
        tRequest.setFilters(tFilter);
        assertEquals(pExceptedLines, dao.createCriteria(tRequest).list().size());
    }

    private int countMethods()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From METHOD_CALL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    @Test
    public void testInsertNewFlows()
    {
        int tOldNbFlow = dao.countFlows();
        int tOldNbMeth = countMethods();

        ExecutionFlowPO tFlow = PersistanceTestCase.buildNewFullFlow();
        dao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = dao.countFlows();
        int tNewNbMeth = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMeth + 6, tNewNbMeth);

        MethodCallPO curMeth = tFlow.getFirstMethodCall();
        assertEquals(tFlow.getId(), curMeth.getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(0).getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(1).getFlow().getId());
    }

    @Test
    public void testLoadFullFlow() throws UnknownEntity
    {
        dao.loadFullFlow(1);
        getSession().flush();
        assertEquals("We don not need to update the execution flow", 0, getStats().getEntityUpdateCount());
    }

    @Test
    public void testLoadFullFlowNotFound()
    {
        try
        {
            dao.loadFullFlow(314159265);
            fail("Should not pass !");
        } catch (UnknownEntity e)
        {
            assertTrue(e.getMessage().contains("314159265"));
        }
    }

    @Test
    public void testGetNextInGroup()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        dao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tFlowId = tFlow.getId();
        assertEquals(1, dao.getNextInGroup(tFlowId, 0, "GrDefault"));
        assertEquals(-1, dao.getNextInGroup(tFlowId, 1, "GrDefault"));

        assertEquals(2, dao.getNextInGroup(tFlowId, 0, "GrChild1"));
        assertEquals(-1, dao.getNextInGroup(tFlowId, 2, "GrChild1"));

        assertEquals(3, dao.getNextInGroup(tFlowId, 0, "GrChild2"));
        assertEquals(4, dao.getNextInGroup(tFlowId, 3, "GrChild2"));
        assertEquals(5, dao.getNextInGroup(tFlowId, 4, "GrChild2"));
        assertEquals(6, dao.getNextInGroup(tFlowId, 5, "GrChild2"));
        assertEquals(-1, dao.getNextInGroup(tFlowId, 6, "GrChild2"));
    }

    @Test
    public void testGetPrevInGroup()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        dao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tFlowId = tFlow.getId();

        assertEquals(6, dao.getPrevInGroup(tFlowId, 7, "GrChild2"));
        assertEquals(5, dao.getPrevInGroup(tFlowId, 6, "GrChild2"));
        assertEquals(4, dao.getPrevInGroup(tFlowId, 5, "GrChild2"));
        assertEquals(3, dao.getPrevInGroup(tFlowId, 4, "GrChild2"));
        assertEquals(-1, dao.getPrevInGroup(tFlowId, 3, "GrChild2"));

        assertEquals(2, dao.getPrevInGroup(tFlowId, 7, "GrChild1"));
        assertEquals(-1, dao.getPrevInGroup(tFlowId, 2, "GrChild1"));

        assertEquals(1, dao.getPrevInGroup(tFlowId, 7, "GrDefault"));
        assertEquals(-1, dao.getPrevInGroup(tFlowId, 1, "GrDefault"));
    }

    @Test
    public void testGetDistribution() throws UnknownEntity
    {
        MethodCallBuilder tBuilder =
            ExecutionFlowBuilder.create(1000000000L).createMethodCall("MainClass", "main", "grp", 200);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 0, 15);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 17, 13);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 42, 12);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 56, 12);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 70, 11);
        tBuilder.getAndSave(dao);
        tBuilder = ExecutionFlowBuilder.create(1000000000L).createMethodCall("MainClass3", "main22", "grp", 33);
        tBuilder.addSubMethod("MainClass", "method1", "grp1", 2, 7);
        tBuilder.getAndSave(dao);

        assertEquals(0, dao.getDistribution("Unkmown", "Unknown", 10).size());
        assertEquals(0, dao.getDistribution("Unkmown", "Unknown", 1).size());
        assertEquals(0, dao.getDistribution("Unkmown", "Unknown", 20).size());

        assertEquals(0, dao.getDistribution("Unkmown", "method1", 10).size());
        assertEquals(0, dao.getDistribution("MainClass", "Unknown", 10).size());

        List<Distribution> tDistribution = dao.getDistribution("MainClass", "method1", 10);
        assertEquals(2, tDistribution.size());
        assertEquals(0, tDistribution.get(0).duration);
        assertEquals(1, tDistribution.get(0).numberOfOccurence);
        assertEquals(10, tDistribution.get(1).duration);
        assertEquals(5, tDistribution.get(1).numberOfOccurence);

        tDistribution = dao.getDistribution("MainClass", "method1", 5);
        assertEquals(3, tDistribution.size());
        assertEquals(5, tDistribution.get(0).duration);
        assertEquals(1, tDistribution.get(0).numberOfOccurence);
        assertEquals(10, tDistribution.get(1).duration);
        assertEquals(4, tDistribution.get(1).numberOfOccurence);
        assertEquals(15, tDistribution.get(2).duration);
        assertEquals(1, tDistribution.get(2).numberOfOccurence);

        tDistribution = dao.getDistribution("MainClass", "method1", 2);
        assertEquals(5, tDistribution.size());
        assertEquals(6, tDistribution.get(0).duration);
        assertEquals(1, tDistribution.get(0).numberOfOccurence);
        assertEquals(8, tDistribution.get(1).duration);
        assertEquals(0, tDistribution.get(1).numberOfOccurence);
        assertEquals(10, tDistribution.get(2).duration);
        assertEquals(1, tDistribution.get(2).numberOfOccurence);
        assertEquals(12, tDistribution.get(3).duration);
        assertEquals(3, tDistribution.get(3).numberOfOccurence);
        assertEquals(14, tDistribution.get(4).duration);
        assertEquals(1, tDistribution.get(4).numberOfOccurence);

    }
}
