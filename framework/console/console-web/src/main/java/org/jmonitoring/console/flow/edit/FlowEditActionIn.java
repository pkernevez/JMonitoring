package org.jmonitoring.console.flow.edit;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.console.flow.jfreechart.ChartManager;
import org.jmonitoring.console.flow.jfreechart.FlowChartBarUtil;
import org.jmonitoring.console.flow.jfreechart.FlowUtil;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.process.ConsoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowEditActionIn extends AbstractSpringAction
{
    private static final String MAX_FLOW_FOR_EDITION = "maxExecutionDuringFlowEdition";

    private static Logger sLog = LoggerFactory.getLogger(FlowEditActionIn.class);

    /**
     * Default constructor.
     */
    public FlowEditActionIn()
    {
    }

    /**
     * Max number of measure to show in one flow. If this number is exceed, then the user is asked to choose an action.
     */
    // TODO Spring : à mettre dans la configuration Spring
    private static int sMaxFlowToShow = 800;

    // ConfigurationHelper.getInt(MAX_FLOW_FOR_EDITION);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        FormaterBean tFormater = (FormaterBean) SpringConfigurationUtil.getBean("formater");

        ColorManager tColor = (ColorManager) SpringConfigurationUtil.getBean("color");

        ConsoleManager tConsoleManager = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");

        SessionFactory tSessionFactory = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        ChartManager tChartManager = (ChartManager) SpringConfigurationUtil.getBean("chartManager");
        ActionForward tForward;
        FlowEditForm tForm = (FlowEditForm) pForm;
        sLog.debug("Read flow from database, Id=[" + tForm.getId() + "]");
        ExecutionFlowDTO tFlow = tConsoleManager.readFullExecutionFlow(tForm.getId());
        sLog.debug("End Read from database of Flow, Id=[" + tForm.getId() + "]");
        int tNbMeasure = tFlow.getMeasureCount();
        tForm.setExecutionFlow(tFlow);
        if ((tNbMeasure > sMaxFlowToShow) && (tForm.getKindOfAction() == FlowEditForm.ACTION_DEFAULT))
        {
            sLog.debug("Need more information to know if we can displayed the next screen...");
            tForward = pMapping.findForward("required_info");
        } else
        {
            MethodCallDTO tFirstMeasure = tFlow.getFirstMethodCall();
            // Creation of the associated images.
            HttpSession tSession = pRequest.getSession();
            sLog.debug("Write PieCharts into HttpSession");
            FlowUtil tUtil = new FlowUtil(tColor, tFormater);
            tUtil.writeImageIntoSession(tSession, tFirstMeasure);
            sLog.debug("Write GantBarChart into HttpSession");
            FlowChartBarUtil.writeImageIntoSession(tFormater, tColor, tSession, tFirstMeasure, tForm, tChartManager);
            if (tForm.getKindOfAction() == FlowEditForm.ACTION_DURATION_FILTER)
            {
                sLog.debug("MethodCallDTO Filtering : duration>" + tForm.getDurationMin());
                limitMeasureWithDuration(tForm.getDurationMin(), tFirstMeasure);
            }
            sLog.debug("Forward success.");
            tForward = pMapping.findForward("success");
        }
        return tForward;
    }

    /**
     * Filter the <code>MethodCallDTO</code> tree using the duration.
     * 
     * @param pDurationMin The minimum duration of the <code>MethodCallDTO</code>.
     * @param pCurrentMeasure The current <code>MethodCallDTO</code> of the tree.
     */
    void limitMeasureWithDuration(int pDurationMin, MethodCallDTO pCurrentMeasure)
    {
        MethodCallDTO curChild;
        for (int i = 0; i < pCurrentMeasure.getChildren().length;)
        {
            curChild = pCurrentMeasure.getChild(i);
            if (curChild.getDuration() < pDurationMin)
            { // We remove this child
                pCurrentMeasure.removeChild(i);
                // We don't increment 'i' because of the removing
            } else
            {
                limitMeasureWithDuration(pDurationMin, curChild);
                i++;
            }
        }

    }

    public static void setMaxFlowToShow(int pMaxFlowToShow)
    {
        sMaxFlowToShow = pMaxFlowToShow;
    }

    public static int getMaxFlowToShow()
    {
        return sMaxFlowToShow;
    }

}
