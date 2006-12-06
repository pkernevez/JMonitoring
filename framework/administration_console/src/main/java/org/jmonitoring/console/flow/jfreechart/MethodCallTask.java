package org.jmonitoring.console.flow.jfreechart;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

public class MethodCallTask extends Task
{
    private static final long serialVersionUID = 7030808811626064063L;

    private int mPosition;

    private int mFlowId;

    public MethodCallTask(int pFlowId, int pMethodCallId, SimpleTimePeriod tTimePeriod)
    {
        super("", tTimePeriod);
        mFlowId = pFlowId;
        mPosition = pMethodCallId;
    }

    public int getMethodCallId()
    {
        return mPosition;
    }

    public int getFlowId()
    {
        return mFlowId;
    }

}
