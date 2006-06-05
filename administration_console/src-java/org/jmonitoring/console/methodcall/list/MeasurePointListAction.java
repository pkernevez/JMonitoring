package org.jmonitoring.console.methodcall.list;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.persistence.HibernateManager;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MeasurePointListAction extends Action
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
                    return null;
    }
//        MeasurePointListForm tListForm = (MeasurePointListForm) pForm;
//        RowSetDynaClass resultSet = getMeasure(tListForm);
//        pRequest.setAttribute("results", resultSet);
//
//        return pMapping.findForward("success");
//    }

    /**
     * @todo refactore this code.
     */
//    private RowSetDynaClass getMeasure(MeasurePointListForm pForm) 
//    {
//        Connection tConnection = null;
//        PreparedStatement tStmt = null;
//        ResultSet tRs = null;
//        RowSetDynaClass tDynaResultSet;
//        try
//        {
//            tConnection = HibernateManager.getSession().connection();
//            //tStmt = tConnection.createStatement();
//            tStmt = tConnection.prepareStatement("SELECT M.FLOW_ID, E.THREAD_NAME, E.DURATION"
//                            + " AS FLOW_DURATION, E.BEGIN_TIME_AS_DATE, E.JVM, M.SEQUENCE_ID,"
//                            + " M.DURATION FROM EXECUTION_FLOW E, METHOD_EXECUTION M"
//                            + " WHERE E.ID = M.FLOW_ID And M.FULL_CLASS_NAME=? And M.METHOD_NAME=?"
//                            + " And M.Duration>=?  And M.Duration<?" + " ORDER BY DURATION");
//            tStmt.setString(1, pForm.getClassName());
//            tStmt.setString(2, pForm.getMethodName());
//            tStmt.setInt(3, pForm.getDurationMin());
//            tStmt.setInt(4, pForm.getDurationMax());
//            tRs = tStmt.executeQuery();
//            tDynaResultSet = new RowSetDynaClass(tRs, false);
//        } finally
//        {
//            try
//            {
//                if (tRs != null)
//                {
//                    tRs.close();
//                }
//            } finally
//            {
//                try
//                {
//                    if (tStmt != null)
//                    {
//                        tStmt.close();
//                    }
//                } finally
//                {
//                    if (tConnection != null)
//                    {
//                        tConnection.close();
//
//                    }
//
//                }
//            }
//        }
//
//        return tDynaResultSet;
//    }

}
