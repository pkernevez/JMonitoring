package org.jmonitoring.sample.console;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.sample.main.RunSample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class MultipleAlreadyWeavedActionIn extends Action
{
    private static Log sLog = LogFactory.getLog(MultipleAlreadyWeavedActionIn.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        PooledExecutor tExecutor;
        tExecutor = new PooledExecutor(10);

        for (int i = 0; i < 50; i++)
        {
            tExecutor.execute(new MyRun());
        }
//        while (tExecutor.getPoolSize() > 0)
//        {
//            Thread.sleep(1000);
//            sLog.info("Waiting for the end of execution... Always " + tExecutor.getPoolSize() + " in queue.");
//        }
        return pMapping.findForward("success");
    }

    private static class MyRun implements Runnable
    {
        public void run()
        {
            RunSample.main(new String[0]);
        }

    }
}
