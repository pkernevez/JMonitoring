package org.jmonitoring.console.gwt.server.flow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.Request;

import org.hibernate.Criteria;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.junit.Test;

public class ConsoleDaoTest extends PersistanceTestCase
{

    @Resource(name="consoleDao")
    private ConsoleDao dao;
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testCreateCriteriaWithoutParam()
    {
        Request tRequest = new Request();
        List<Filter> tFilter = new ArrayList<Filter>();
        tRequest.setFilters(tFilter);
        
        Criteria tCriteria = dao.createCriteria(tRequest);
        
//        assertEquals(3, tCriteria.list().size());
    }

}
