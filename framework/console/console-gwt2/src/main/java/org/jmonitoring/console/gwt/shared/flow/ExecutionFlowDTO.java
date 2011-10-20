package org.jmonitoring.console.gwt.shared.flow;

import java.io.Serializable;

public class ExecutionFlowDTO implements Serializable
{

    private static final long serialVersionUID = -5029485594932621914L;

    /** Technical identifier. */
    private int id;

    /** Thread name. */
    private String threadName;

    /** First Measure. */
    private MethodCallDTO firstMethodCall;

    /** Name of the 'JVM' or server. */
    private String jvmIdentifier;

    /** Begin datetime. */
    private String beginTime;

    /** End datetime. */
    private String endTime;

    /** Name of the class on which is done the first method call. */
    private String className;

    /** Name of the method on which is done the first method call. */
    private String methodName;

    private String duration;

    private String groupName;

    public int getId()
    {
        return id;
    }

    public void setId(int pId)
    {
        id = pId;
    }

    public String getThreadName()
    {
        return threadName;
    }

    public void setThreadName(String pThreadName)
    {
        threadName = pThreadName;
    }

    public MethodCallDTO getFirstMethodCall()
    {
        return firstMethodCall;
    }

    public void setFirstMethodCall(MethodCallDTO pFirstMethodCall)
    {
        firstMethodCall = pFirstMethodCall;
    }

    public String getJvmIdentifier()
    {
        return jvmIdentifier;
    }

    public void setJvmIdentifier(String pJvmIdentifier)
    {
        jvmIdentifier = pJvmIdentifier;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String pBeginTime)
    {
        beginTime = pBeginTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String pEndTime)
    {
        endTime = pEndTime;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String pClassName)
    {
        className = pClassName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String pMethodName)
    {
        methodName = pMethodName;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String pDuration)
    {
        duration = pDuration;
    }

    public void setGroupName(String pGroupName)
    {
        groupName = pGroupName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public String getFirstCall()
    {
        return className + "." + methodName + "( )";
    }

    @Override
    public String toString()
    {
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("FlowId=[").append(id).append("] ");
        if (firstMethodCall != null)
        {
            tBuffer.append("SequenceId=[").append(firstMethodCall.getPosition()).append("] ");
            tBuffer.append("GroupName=[").append(firstMethodCall.getGroupName()).append("] ");
            tBuffer.append("ClassName=[").append(firstMethodCall.getClassName()).append("] ");
            tBuffer.append("MethodName=[").append(firstMethodCall.getMethodName()).append("] ");
        } else
        {
            tBuffer.append("SequenceId=[NULL] ");
        }
        return tBuffer.toString();
    }

}
