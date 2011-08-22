package org.jmonitoring.console.gwt.shared.flow;

import java.io.Serializable;

public class FlowSearchRequestDTO implements Serializable
{
    private static final long serialVersionUID = -9173009879031466320L;

    private String mThreadName;

    public String getThreadName()
    {
        return mThreadName;
    }

    public void setThreadName(String threadName)
    {
        mThreadName = threadName;
    }

}
