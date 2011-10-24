package org.jmonitoring.console.gwt.server.image;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

public class MethodCallTask extends Task {
    private static final long serialVersionUID = 7030808811626064063L;

    private int position;

    private int flowId;

    public MethodCallTask(int pFlowId, int pMethodCallId, SimpleTimePeriod tTimePeriod) {
        super("", tTimePeriod);
        flowId = pFlowId;
        position = pMethodCallId;
    }

    public int getMethodCallId() {
        return position;
    }

    public int getFlowId() {
        return flowId;
    }

}
