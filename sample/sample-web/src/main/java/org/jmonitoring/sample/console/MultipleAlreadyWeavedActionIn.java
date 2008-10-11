package org.jmonitoring.sample.console;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.sample.main.RunSample;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class MultipleAlreadyWeavedActionIn extends Action {

    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
            HttpServletResponse pResponse) throws Exception {
        PooledExecutor tExecutor;
        tExecutor = new PooledExecutor(10);

        for (int i = 0; i < 50; i++) {
            tExecutor.execute(new MyRun());
        }
        return pMapping.findForward("success");
    }

    private static class MyRun implements Runnable {
        public void run() {
            RunSample.main(new String[0]);
        }

    }
}
