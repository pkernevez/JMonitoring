package org.jmonitoring.console.methodcall.search;

import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyMap;
import org.jmonitoring.core.dto.MethodCallExtractDTO;

public class MethodCallSearchForm extends ActionForm
{
    private static final long serialVersionUID = 469269872309634958L;

    private MyMap mTreeOfMethodCallExtract;

    private Map<String, MethodCallExtractDTO> mMapOfMethodCallExtractByFullName;

    public MyMap getTreeOfMethodCallExtract()
    {
        return mTreeOfMethodCallExtract;
    }

    public void setTreeOfMethodCallExtract(MyMap pTreeOfMethodCallExtract)
    {
        mTreeOfMethodCallExtract = pTreeOfMethodCallExtract;
    }

    public Map<String, MethodCallExtractDTO> getMapOfMethodCallExtractByFullName()
    {
        return mMapOfMethodCallExtractByFullName;
    }

    public void setMapOfMethodCallExtractByFullName(Map<String, MethodCallExtractDTO> pMapOfMethodCallExtractByFullName)
    {
        mMapOfMethodCallExtractByFullName = pMapOfMethodCallExtractByFullName;
    }

}
