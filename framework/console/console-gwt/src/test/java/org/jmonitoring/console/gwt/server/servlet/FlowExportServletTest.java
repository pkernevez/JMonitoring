package org.jmonitoring.console.gwt.server.servlet;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.server.dto.DtoManager;
import org.jmonitoring.console.gwt.server.executionflow.ExecutionFlowServiceImpl;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/dao-test.xml", "/persistence-test.xml", "/formater-test.xml", "/color-test.xml",
    "/chart-manager-test.xml" })
public class FlowExportServletTest
{
    private ServletUnitClient mClient;

    @Resource(name = "dtoManager")
    private DtoManager mDtoMgr;

    @Resource(name = "sessionFactory")
    protected SessionFactory mSessionFactory;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Test
    public void testDoGet() throws ServletException, IOException, SAXException, UnknownFlowException
    {
        ExecutionFlowServiceImpl.before(mSessionFactory);
        ExecutionFlowDTO tFirstDto = buildAndSaveNewDto(5);
        ExecutionFlowServiceImpl.after(mSessionFactory);

        try
        {
            int tFlowId = tFirstDto.getId();
            WebResponse tResponse = mClient.getResponse("http://localhost/ExportXml?id=" + tFlowId);
            assertEquals("application/x-zip", tResponse.getContentType());
            assertTrue(tResponse.getHeaderField("Content-Disposition").startsWith("attachment; filename="));
            assertTrue("No response in servet. (length=" + tResponse.getContentLength() + ")",
                       tResponse.getContentLength() > 100);
        } finally
        {
            ExecutionFlowServiceImpl.before(mSessionFactory);
            mDao.deleteFlow(tFirstDto.getId());
            ExecutionFlowServiceImpl.after(mSessionFactory);
        }
    }

    public ExecutionFlowDTO buildAndSaveNewDto(int pNbMethods)
    {
        ExecutionFlowPO tExecPO = buildNewFullFlow(pNbMethods);

        mDao.insertFullExecutionFlow(tExecPO);
        return mDtoMgr.getLimitedCopy(tExecPO);
    }

    public ExecutionFlowPO buildNewFullFlow(int pNbMethods)
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long currentTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, FlowExportServlet.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(currentTime);

        for (int i = 0; i < pNbMethods; i++)
        {
            tSubPoint =
                new MethodCallPO(tPoint, FlowExportServlet.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
            tSubPoint.setBeginTime(currentTime + 1);
            currentTime = currentTime + 5;
            tSubPoint.setEndTime(currentTime);
        }
        tPoint.setEndTime(currentTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    @Before
    public void initClient() throws IOException, SAXException
    {
        ServletRunner tSr = new ServletRunner(getWebFile());
        mClient = tSr.newClient();
    }

    private File getWebFile()
    {
        return new File("src/main/webapp/WEB-INF/web.xml");
    }
}
