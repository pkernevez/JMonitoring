package org.jmonitoring.console.image;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class DynamicImageServlet extends HttpServlet {
    /** LogClass. */
    private static Log sLog = LogFactory.getLog(DynamicImageServlet.class);

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256725099942916912L;

    /**
     * @see HttpServlet.doGet(HttpServletRequest, HttpServletResponse)
     */
    public void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException {
        String tKey = pReq.getParameter("Id");
        HttpSession tSession = pReq.getSession();
        byte[] tOutput = (byte[]) tSession.getAttribute(tKey);
        if (tOutput == null) {
            sLog.warn("Image not found for Id=" + tKey);
        } else {
            pResp.getOutputStream().write(tOutput);
        }
    }

    /**
     * @see HttpServlet.doPost(HttpServletRequest, HttpServletResponse)
     */
    public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        doGet(pRequest, pResponse);
    }

}
