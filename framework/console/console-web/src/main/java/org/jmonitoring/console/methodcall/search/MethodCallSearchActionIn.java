package org.jmonitoring.console.methodcall.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyHashMap;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyMap;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

public class MethodCallSearchActionIn extends Action
{
    @Override
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        List<MethodCallExtractDTO> tListOfAllExtract = tProcess.getListOfMethodCallExtract();
        Map<String, MethodCallExtractDTO> tListOfExtractByFullClassName = new HashMap<String, MethodCallExtractDTO>();
        MyMap tTreeOfExtract = convertListAsTree(tListOfExtractByFullClassName, tListOfAllExtract);

        MethodCallSearchForm tForm = (MethodCallSearchForm) pForm;
        tForm.setTreeOfMethodCallExtract(tTreeOfExtract);
        tForm.setMapOfMethodCallExtractByFullName(tListOfExtractByFullClassName);

        return pMapping.findForward("success");
    }

    /**
     * Convert a list of String containing character <code>.</code> into a Tree. Each branch is composed with the
     * Token.
     * 
     * @param pListOfMeasure The list of <code>String</code>.
     * @return The Tree has a Map.
     */
    static MyMap convertListAsTree(Map<String, MethodCallExtractDTO> pListOfExtractByFullClassName,
                    List<MethodCallExtractDTO> pListOfMeasure)
    {
        MyMap tTree = new MyHashMap();
        for (MethodCallExtractDTO tExtract : pListOfMeasure)
        {
            MyMap curMap = tTree;
            pListOfExtractByFullClassName.put(tExtract.getName() + tExtract.getGroupName(), tExtract);
            for (StringTokenizer tTokenizer = new StringTokenizer(tExtract.getName() + tExtract.getGroupName(), "."); tTokenizer
                            .hasMoreElements();)
            {
                String curString = (String) tTokenizer.nextElement();
                MyMap tCurrentBranch = curMap.get(curString);
                if (tCurrentBranch == null)
                {
                    curMap.put(curString, tCurrentBranch = new MyHashMap());
                }
                curMap = tCurrentBranch;
            }
        }
        return tTree;
    }
}
