package org.jmonitoring.console.methodcall.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

public class MethodCallSearchActionIn extends Action {
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
            HttpServletResponse pResponse) throws Exception {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        List tListOfAllExtract = tProcess.getListOfMethodCallExtract();
        Map tListOfExtractByFullClassName = new HashMap();
        Map tTreeOfExtract = convertListAsTree(tListOfExtractByFullClassName, tListOfAllExtract);

        MethodCallSearchForm tForm = (MethodCallSearchForm) pForm;
        tForm.setTreeOfMethodCallExtract(tTreeOfExtract);
        tForm.setMapOfMethodCallExtractByFullName(tListOfExtractByFullClassName);

        return pMapping.findForward("success");
    }

    /**
     * Convert a list of String containing caracter <code>.</code> into a Tree. Each branch is composed with the
     * Token.
     * 
     * @param pListOfMeasure The list of <code>String</code>.
     * @return The Tree has a Map.
     */
    static Map convertListAsTree(Map pListOfExtractByFullClassName, List pListOfMeasure) {
        Map tTree = new HashMap();
        String curString;
        Map curMap;
        MethodCallExtractDTO tExtract;
        for (Iterator tIt = pListOfMeasure.iterator(); tIt.hasNext();) {
            curMap = tTree;
            tExtract = (MethodCallExtractDTO) tIt.next();
            pListOfExtractByFullClassName.put(tExtract.getName() + tExtract.getGroupName(), tExtract);
            for (StringTokenizer tTokenizer = new StringTokenizer(tExtract.getName() + tExtract.getGroupName(), "."); tTokenizer.hasMoreElements();) {
                curString = (String) tTokenizer.nextElement();
                if (curMap.get(curString) == null) {
                    curMap.put(curString, new HashMap());
                }
                curMap = (Map) curMap.get(curString);
            }
        }

        return tTree;
    }
}
