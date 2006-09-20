package org.jmonitoring.console.flow.jfreechart;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

public class MethodCallTask extends Task
{
    private static final long serialVersionUID = 7030808811626064063L;

    private int mMethodCallId;

    public MethodCallTask(int pMethodCallId, SimpleTimePeriod tTimePeriod)
    {
        super("", tTimePeriod);
        mMethodCallId = pMethodCallId;
    }

    public int getMethodCallId()
    {
        return mMethodCallId;
    }

}
