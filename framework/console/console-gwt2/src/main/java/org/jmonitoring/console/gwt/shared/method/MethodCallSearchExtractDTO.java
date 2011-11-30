package org.jmonitoring.console.gwt.shared.method;

import java.io.Serializable;

public class MethodCallSearchExtractDTO implements Serializable
{
    private static final long serialVersionUID = -846139162154247686L;

    private String flowId;

    private String position;

    private String flowDuration;

    private String flowBeginDate;

    private String flowServer;

    private String flowThread;

    private String duration;

    private String className;

    private String methodName;

    private String hasException;

    private String group;

    public String getFlowId()
    {
        return flowId;
    }

    public void setFlowId(String pFlowId)
    {
        flowId = pFlowId;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String pPosition)
    {
        position = pPosition;
    }

    public String getFlowDuration()
    {
        return flowDuration;
    }

    public void setFlowDuration(String pFlowDuration)
    {
        flowDuration = pFlowDuration;
    }

    public String getFlowBeginDate()
    {
        return flowBeginDate;
    }

    public void setFlowBeginDate(String pFlowBeginDate)
    {
        flowBeginDate = pFlowBeginDate;
    }

    public String getFlowServer()
    {
        return flowServer;
    }

    public void setFlowServer(String pFlowServer)
    {
        flowServer = pFlowServer;
    }

    public String getFlowThread()
    {
        return flowThread;
    }

    public void setFlowThread(String pFlowThread)
    {
        flowThread = pFlowThread;
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

    public String getHasException()
    {
        return hasException;
    }

    public void setHasException(String pHasException)
    {
        hasException = pHasException;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String pDuration)
    {
        duration = pDuration;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String pGroup)
    {
        group = pGroup;
    }

}
