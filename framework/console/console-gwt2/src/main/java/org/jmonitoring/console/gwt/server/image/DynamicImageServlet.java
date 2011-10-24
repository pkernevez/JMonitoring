package org.jmonitoring.console.gwt.server.image;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.console.gwt.server.common.HibernateServlet;
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

    // private ColorManager colorManager;

    /**
     * @see HttpServlet.doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void doGetWithSpring(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException,
        IOException
    {
        String tId = pReq.getParameter("id");
        String tType = pReq.getParameter("type");
        String tSessionId = tType + "&" + tId;
        byte[] tOutput = (byte[]) pReq.getSession().getAttribute(tSessionId);
        if (tOutput == null)
        {
            sLog.warn("Image not found in memory, recreate new one {}", tSessionId);
            if ("DurationInGroups".equals(tType))
            {
                tOutput =
                    flowService.generateDurationInGroupChart(pReq.getSession(), tSessionId, Integer.parseInt(tId));
            } else if ("GroupsCalls".equals(tType))
            {
                tOutput = flowService.generateGroupsCallsChart(pReq.getSession(), tSessionId, Integer.parseInt(tId));
            } else
            {
                sLog.warn("Unable to create Image id={} type={}", tId, tType);
                pResp.sendError(404);
                tOutput = new byte[0];
            }
        }
        pResp.getOutputStream().write(tOutput);
        // Clear image from session to avoid memory leaks
        pReq.getSession().removeAttribute(tSessionId);
    }

    @Override
    protected void init(WebApplicationContext pContext)
    {
        super.init(pContext);
        // colorManager = pContext.getBean(ColorManager.class);
    }

}
