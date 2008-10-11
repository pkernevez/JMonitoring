package org.jmonitoring.console.flow.edit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

public class ImportFlowForm extends ActionForm {
    private FormFile mFile;

    private ExecutionFlowDTO mNewExecutionFlow;

    public String getNewExecutionFlowId() {
        return "" + mNewExecutionFlow.getId();
    }

    public void setNewExecutionFlow(ExecutionFlowDTO pNewExecutionFlow) {
        mNewExecutionFlow = pNewExecutionFlow;
    }

    /**
     * @return Returns the theFile.
     */
    public FormFile getTheFile() {
        return mFile;
    }

    /**
     * @param theFile The FormFile to set.
     */
    public void setTheFile(FormFile theFile) {
        this.mFile = theFile;
    }

}
