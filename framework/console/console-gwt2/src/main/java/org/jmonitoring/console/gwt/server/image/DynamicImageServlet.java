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
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String tIdInSession = tType + "&" + tId;
        byte[] tOutput = (byte[]) pReq.getSession().getAttribute(tIdInSession);
        try
        {
            if (tOutput == null)
            {
                sLog.warn("Image not found in memory, recreate new one {}", tIdInSession);
                if ("DurationInGroups".equals(tType))
                {
                    tOutput =
                        flowService.generateDurationInGroupChart(pReq.getSession(), tIdInSession, Integer.parseInt(tId));
                } else if ("GroupsCalls".equals(tType))
                {
                    tOutput =
                        flowService.generateGroupsCallsChart(pReq.getSession(), tIdInSession, Integer.parseInt(tId));
                } else if ("FlowDetail".equals(tType))
                {
                    tOutput =
                        flowService.generateFlowDetailChart(pReq.getSession(), tIdInSession, Integer.parseInt(tId)).image;
                } else if ("Distribution".equals(tType))
                {
                    String[] tParams = tId.split("/");
                    assert tParams.length == 3;
                    flowService.getDistributionAndGenerateImage(pReq.getSession(), tParams[0], tParams[1],
                                                                Long.valueOf(tParams[2]));
                    tOutput = (byte[]) pReq.getSession().getAttribute(tIdInSession);
                } else
                {
                    sLog.warn("Unable to create Image id={} type={}", tId, tType);
                    pResp.sendError(404);
                    tOutput = new byte[0];
                }
            }
            pResp.getOutputStream().write(tOutput);
            // Clear image from session to avoid memory leaks
            pReq.getSession().removeAttribute(tIdInSession);
        } catch (UnknownEntity e)
        {
            pResp.setStatus(500);
        }
    }

}
