package org.jmonitoring.console.methodcall.search;

import java.util.Map;

import org.apache.struts.action.ActionForm;

public class MethodCallSearchForm extends ActionForm {
    private static final long serialVersionUID = 469269872309634958L;

    private Map mTreeOfMethodCallExtract;

    private Map mMapOfMethodCallExtractByFullName;

    public Map getTreeOfMethodCallExtract() {
        return mTreeOfMethodCallExtract;
    }

    public void setTreeOfMethodCallExtract(Map pTreeOfMethodCallExtract) {
        mTreeOfMethodCallExtract = pTreeOfMethodCallExtract;
    }

    public Map getMapOfMethodCallExtractByFullName() {
        return mMapOfMethodCallExtractByFullName;
    }

    public void setMapOfMethodCallExtractByFullName(Map pMapOfMethodCallExtractByFullName) {
        mMapOfMethodCallExtractByFullName = pMapOfMethodCallExtractByFullName;
    }

}
