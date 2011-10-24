package org.jmonitoring.console.gwt.server.image;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

public class MethodCallTask extends Task
{
    private static final long serialVersionUID = 7030808811626064063L;

    private String flowId;

    private String position;

    public MethodCallTask(String pFlowId, String pMethodCallId, SimpleTimePeriod tTimePeriod)
    {
        super("", tTimePeriod);
        flowId = pFlowId;
        position = pMethodCallId;
    }

    public String getMethodCallId()
    {
        return position;
    }

    public String getFlowId()
    {
        return flowId;
    }

}
