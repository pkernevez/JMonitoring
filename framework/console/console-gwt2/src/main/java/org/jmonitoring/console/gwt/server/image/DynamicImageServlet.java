package org.jmonitoring.console.gwt.server.image;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.HibernateServlet;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class DynamicImageServlet extends HibernateServlet
{
    /** LogClass. */
    private static Logger sLog = LoggerFactory.getLogger(DynamicImageServlet.class);

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256725099942916912L;

    private ColorManager colorManager;

    /**
     * @see HttpServlet.doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void doGetWithSpring(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException,
        IOException
    {
        String tId = pReq.getParameter("id");
        String tType = pReq.getParameter("type");
        byte[] tOutput;
        if ("DurationInGroups".equals(tType))
        {
            MethodCallDTO tFirstMeasure = flowService.loadFull(Integer.parseInt(tId)).getFirstMethodCall();
            PieChartGenerator tGenerator = new PieChartGenerator(colorManager);
            tOutput = tGenerator.getDurationInGroup(tFirstMeasure);
        } else if ("GroupsCalls".equals(tType))
        {
            MethodCallDTO tFirstMeasure = flowService.loadFull(Integer.parseInt(tId)).getFirstMethodCall();
            PieChartGenerator tGenerator = new PieChartGenerator(colorManager);
            tOutput = tGenerator.getGroupCalls(tFirstMeasure);
        } else
        {
            sLog.warn("Unable to create Image id={} type={}", tId, tType);
            pResp.sendError(404);
            tOutput = new byte[0];
        }
        pResp.getOutputStream().write(tOutput);
    }

    /**
     * @see HttpServlet.doPost(HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException
    {
        doGet(pRequest, pResponse);
    }

    @Override
    public void init(WebApplicationContext pContext)
    {
        super.init(pContext);
        colorManager = pContext.getBean(ColorManager.class);
    }

}
