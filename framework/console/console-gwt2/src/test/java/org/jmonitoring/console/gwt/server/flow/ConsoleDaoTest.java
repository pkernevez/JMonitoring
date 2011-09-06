package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Column;
import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.LongFilter;
import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.StringFilter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;
import org.junit.Test;

public class ConsoleDaoTest extends PersistanceTestCase
{

    @Resource(name = "consoleDao")
    private ConsoleDao dao;

    @SuppressWarnings("rawtypes")
    @Test
    public void testCreateCriteriaWithoutParam()
    {

        Request tRequest = new Request();
        List<Filter> tFilter = new ArrayList<Filter>();
        tRequest.setFilters(tFilter);

        assertEquals(5, dao.createCriteria(tRequest).list().size());
    }

    @Test
    public void testCreateCriteriaWithParam()
    {
        checkFilter(HibernateConstant.THREAD, "Speci", 0);
        checkFilter(HibernateConstant.THREAD, "Speci%", 2);
        checkFilter(HibernateConstant.MIN_DURATION, 200L, 1);
        checkFilter(HibernateConstant.MIN_DURATION, 260L, 0);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass%", 5);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass", 2);
        checkFilter(HibernateConstant.FIRST_MEASURE_CLASS_NAME, "MainClass3", 1);
        checkFilter(HibernateConstant.FIRST_MEASURE_METHOD_NAME, "main2", 1);
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
        assertEquals(3, tResult.get(0).getId());
        assertEquals(4, tResult.get(1).getId());
    }

    @Test
    public void testSearchOrderByDurationDesc()
    {
        Request tRequest = createRequestWithAllRows();
        tRequest.setSortingColumn(HibernateConstant.MIN_DURATION);
        tRequest.setSortType(Column.SORTING_DESC);
        List<FlowExtractDTO> tResult = dao.search(tRequest);
        assertEquals(2, tResult.size());
        assertEquals(2, tResult.get(0).getId());
        assertEquals(1, tResult.get(1).getId());
    }

    @SuppressWarnings("rawtypes")
    private Request createRequestWithAllRows()
    {
        Request tRequest = new Request();
        tRequest.setPageSize(2);
        tRequest.setStartRow(3);
        List<Filter> tFilter = new ArrayList<Filter>();
        StringFilter curFilter = new StringFilter();
        curFilter.setPropertyName(HibernateConstant.FIRST_MEASURE_CLASS_NAME);
        curFilter.setValue("MainClass%");
        tFilter.add(curFilter);
        tRequest.setFilters(tFilter);
        return tRequest;
    }

    @SuppressWarnings("rawtypes")
    private void checkFilter(String pPropertyName, String pValue, int pExceptedLines)
    {
        Request tRequest = new Request();
        tRequest.setPageSize(20);
        List<Filter> tFilter = new ArrayList<Filter>();
        StringFilter curFilter = new StringFilter();
        curFilter.setPropertyName(pPropertyName);
        curFilter.setValue(pValue);
        tFilter.add(curFilter);
        tRequest.setFilters(tFilter);
        assertEquals(pExceptedLines, dao.createCriteria(tRequest).list().size());
    }

    @SuppressWarnings("rawtypes")
    private void checkFilter(String pPropertyName, Long pValue, int pExceptedLines)
    {
        Request tRequest = new Request();
        List<Filter> tFilter = new ArrayList<Filter>();
        LongFilter curFilter = new LongFilter();
        curFilter.setPropertyName(pPropertyName);
        curFilter.setValue(pValue);
        tFilter.add(curFilter);
        tRequest.setFilters(tFilter);
        assertEquals(pExceptedLines, dao.createCriteria(tRequest).list().size());
    }

}
