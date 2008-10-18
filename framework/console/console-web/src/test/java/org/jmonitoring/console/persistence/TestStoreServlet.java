package org.jmonitoring.console.persistence;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.HttpWriter;
import org.jmonitoring.agent.store.impl.MemoryStoreWriter;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestStoreServlet extends MockStrutsTestCase
{

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

    public void testDoPostOk() throws ServletException, IOException
    {
        request.setContentType(HttpWriter.CONTENT_TYPE);
        checkPostStore();
    }

    private void checkPostStore() throws ServletException, IOException
    {
        StoreManager.changeStoreWriterClass(MemoryStoreWriter.class);
        // ExecutionFlowPO tFlow = PersistanceTestCase.buildNewFullFlow();
        // request.(HttpWriter.FLOW_ATTR, tFlow);
        // StoreServlet tServlet = new StoreServlet();
        // tServlet.init();
        // tServlet.doPost(request, response);
        // assertEquals(1, MemoryStoreWriter.countFlows());
        // assertEquals(tFlow, MemoryStoreWriter.getFlow(0));
    }

}
