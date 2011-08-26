package org.jmonitoring.console.gwt.shared.flow;

import java.io.Serializable;

public class FlowExtractDTO implements Serializable
{
    private static final long serialVersionUID = -8885958767279114532L;

    private String thread;

    public String getThread()
    {
        return thread;
    }

    public void setThread(String thread)
    {
        this.thread = thread;
    }

}
