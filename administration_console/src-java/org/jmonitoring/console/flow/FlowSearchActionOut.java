package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class FlowSearchActionOut extends Action
{

    /**
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        FlowSearchForm tForm = (FlowSearchForm) pForm;

        FlowSearchCriterion tCriterion = copyBeanFormToCriterion(tForm);

        ((FlowSearchForm) tForm).setListOfFlows(tProcess.getListOfExecutionFlowDto(tCriterion));
        return pMapping.findForward("success");
    }

    static FlowSearchCriterion copyBeanFormToCriterion(FlowSearchForm pForm) throws ParseException
    {
        FlowSearchCriterion tResult = new FlowSearchCriterion();
        tResult.setThreadName((pForm.getThreadName() == null ? null : pForm.getThreadName()));
        Date tBeginDate = null;
        if (pForm.getBeginDate() != null && pForm.getBeginDate().length() != 0)
        {
            tBeginDate = new Date(Configuration.getInstance().getDateFormater().parse(pForm.getBeginDate()).getTime());
            tResult.setBeginDate(tBeginDate);
        }
        Date tBeginTime = null;
        if (pForm.getBeginTimeMin() != null && pForm.getBeginTimeMin().length() != 0)
        {
            tBeginTime = new Date(Configuration.getInstance().getTimeFormater().parse(pForm.getBeginTimeMin())
                .getTime());
            tResult.setBeginTimeMin(tBeginTime);
        }
        if (pForm.getDurationMin() != null && pForm.getDurationMin().length() != 0)
        {
            tResult.setDurationMin(new Long(pForm.getDurationMin()));
        }
        tResult.setJVM(pForm.getJVM());
        tResult.setClassName(pForm.getFirstMeasureClassName());
        tResult.setMethodName(pForm.getFirstMeasureMethodName());
        tResult.setGroupName(pForm.getFirstMeasureGroupName());
        return tResult;
    }
}
