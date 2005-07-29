package net.kernevez.javamonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.dao.ExecutionFlowMySqlDAO;
import net.kernevez.performance.dao.FlowSearchCriterion;
import net.kernevez.performance.dao.StandAloneConnectionManager;
import net.kernevez.performance.measure.ExecutionFlow;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class FlowSearchAction extends Action
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
        Connection tConnection = null;
        try
        {
            List tList = new ArrayList();
            FlowSearchForm tForm = (FlowSearchForm) pForm;

            FlowSearchCriterion tCriterion = copyBeanFormToCriterion(tForm);

            tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tConnection);
            ExecutionFlow[] tFlows = tDao.getListOfExecutionFlow(tCriterion);

            tConnection.commit();
            for (int i = 0; i < tFlows.length; i++)
            {
                tList.add(tFlows[i]);
            }

            ((FlowSearchForm) pForm).setListOfFlows(tList);
            return pMapping.findForward("success");
        } finally
        {
            if (tConnection != null)
            {
                tConnection.close();
            }
        }
    }

    static FlowSearchCriterion copyBeanFormToCriterion(FlowSearchForm pForm) throws ParseException
    {
        FlowSearchCriterion tResult = new FlowSearchCriterion();
        tResult.setThreadName((pForm.getThreadName() == null ? null : pForm.getThreadName()));
        Date tBeginDate = null;
        if (pForm.getBeginDate() != null && pForm.getBeginDate().length() != 0)
        {
            tBeginDate = new Date(Configuration.getInstance().getDateFormater().parse(pForm.getBeginDate())
                            .getTime());
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
        tResult.setFirstMeasureClassName(pForm.getFirstMeasureClassName());
        tResult.setFirstMeasureMethodName(pForm.getFirstMeasureMethodName());
        tResult.setFirstMeasureGroupName(pForm.getFirstMeasureGroupName());
        return tResult;
    }
}
