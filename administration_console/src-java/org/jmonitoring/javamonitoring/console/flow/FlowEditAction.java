package net.kernevez.javamonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.kernevez.javamonitoring.console.flow.jfreechart.FlowChartBarUtil;
import net.kernevez.javamonitoring.console.flow.jfreechart.FlowUtil;
import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.dao.ExecutionFlowMySqlDAO;
import net.kernevez.performance.dao.StandAloneConnectionManager;
import net.kernevez.performance.measure.ExecutionFlow;
import net.kernevez.performance.measure.MeasurePoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class FlowEditAction extends Action
{
    private static Log sLog;

    /**
     * Default constructor.
     */
    public FlowEditAction()
    {
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
    }

    /**
     * Max number of measure to show in one flow. If this number is exceed, then the user is asked to choose an action.
     */
    private static final int MAX_FLOW_TO_SHOW = 2000;

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

        Connection tConnection = null;
        ActionForward tForward;
        try
        {
            // List tList = new ArrayList();
            FlowEditForm tForm = (FlowEditForm) pForm;
            sLog.debug("Read flow from database, Id=[" + tForm.getId() + "]");
            tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tConnection);
            ExecutionFlow tFlow = tDao.readFullExecutionFlow(tForm.getId());
            sLog.debug("End Read from database of Flow, Id=[" + tForm.getId() + "]");
            int tNbMeasure = tFlow.getMeasureCount();
            tForm.setExecutionFlow(tFlow);
            if ((tNbMeasure > MAX_FLOW_TO_SHOW) && (tForm.getKindOfAction() == FlowEditForm.ACTION_DEFAULT))
            {
                sLog.debug("Need more information to know if we can displayed the next screen...");
                tForward = pMapping.findForward("required_info");
            } else
            {
                sLog.debug("Need more information to know if we can displayed the next screen...");
                MeasurePoint tFirstMeasure = tFlow.getFirstMeasure();
                //Creation of the associated images.
                HttpSession tSession = pRequest.getSession();
                sLog.debug("Write PieCharts into HttpSession");
                FlowUtil.writeImageIntoSession(tSession, tFirstMeasure);
                sLog.debug("Write GantBarChart into HttpSession");
                FlowChartBarUtil.writeImageIntoSession(tSession, tFirstMeasure);
                if (tForm.getKindOfAction() == FlowEditForm.ACTION_DURATION_FILTER)
                {
                    sLog.debug("MeasurePoint Filtering : duration>" + tForm.getDurationMin());
                    limitMeasureWithDuration(tForm.getDurationMin(), tFirstMeasure);
                }
                sLog.debug("Forward success.");
                tForward = pMapping.findForward("success");
            }
        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            tForward = pMapping.findForward("failure");
        } finally
        {
            if (tConnection != null)
            {
                tConnection.close();
            }
        }
        return tForward;
    }

    /**
     * Filter the <code>MeasurePoint</code> tree using the duration.
     * 
     * @param pDurationMin The minimum duration of the <code>MeasurePoint</code>.
     * @param pCurrentMeasure The current <code>MeasurePoint</code> of the tree.
     */
    void limitMeasureWithDuration(int pDurationMin, MeasurePoint pCurrentMeasure)
    {
        MeasurePoint curChild;
        for (int i = 0; i < pCurrentMeasure.getChildren().size();)
        {
            curChild = (MeasurePoint) pCurrentMeasure.getChildren().get(i);
            if (curChild.getDuration() < pDurationMin)
            { // We remove this child
                pCurrentMeasure.getChildren().remove(i);
                // We don't increment 'i' because of the removing
            } else
            {
                limitMeasureWithDuration(pDurationMin, curChild);
                i++;
            }
        }

    }

}
