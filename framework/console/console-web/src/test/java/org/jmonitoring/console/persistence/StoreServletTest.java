package org.jmonitoring.console.persistence;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jmonitoring.agent.store.impl.HttpWriter;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
@ContextConfiguration(locations = "/storeWriter-test.xml")
public class StoreServletTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    MemoryWriter mStoreWriter;

    @Test
    public void testDoPostInvalidContentType() throws ServletException, IOException
    {
        try
        {
            checkPostStore();
        } catch (RuntimeException e)
        {
            assertEquals("Invalid content type=[null]", e.getMessage());
        }
        try
        {
            request.setContentType("Toto");
            checkPostStore();
        } catch (RuntimeException e)
        {
            assertEquals("Invalid content type=[Toto]", e.getMessage());
        }
    }

    @Test
    @Ignore
    /* Find a way for settings an object into the InputStream of the request */
    public void testDoPostOk() throws ServletException, IOException
    {
        request.setContentType(HttpWriter.CONTENT_TYPE);
        checkPostStore();
    }

    private void checkPostStore() throws ServletException, IOException
    {
        ExecutionFlowPO tFlow = PersistanceTestCase.buildNewFullFlow();
        // request.setAttribute(HttpWriter.FLOW_ATTR, tFlow);
        StoreServlet tServlet = new StoreServlet();
        tServlet.init();
        tServlet.doPost(request, response);
        assertEquals(1, mStoreWriter.countFlows());
        assertEquals(tFlow, mStoreWriter.getFlow(0));
    }
}
