package org.jmonitoring.console.gwt.shared.method;

import java.io.Serializable;
import java.util.Date;

public class MethodCallSearchCriterion implements Serializable
{
    private static final long serialVersionUID = 7778614314993149539L;

    private String flowThread;

    private String flowServer;

    private Date flowBeginDate;

    private String flowMinDuration;

    private String className;

    private String methodName;

    private String position;

    private String parentPosition;

    private String parameters;

    private String returnValue;

    private String thrownExceptionClass;

    private String thrownExceptionMessage;

    public String getFlowThread()
    {
        return flowThread;
    }

    public void setFlowThread(String pFlowThread)
    {
        flowThread = pFlowThread;
    }

    public String getFlowServer()
    {
        return flowServer;
    }

    public void setFlowServer(String pFlowServer)
    {
        flowServer = pFlowServer;
    }

    public Date getFlowBeginDate()
    {
        return flowBeginDate;
    }

    public void setFlowBeginDate(Date pFlowBeginDate)
    {
        flowBeginDate = pFlowBeginDate;
    }

    public String getFlowMinDuration()
    {
        return flowMinDuration;
    }

    public void setFlowMinDuration(String pFlowMinDuration)
    {
        flowMinDuration = pFlowMinDuration;
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

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String pPosition)
    {
        position = pPosition;
    }

    public String getParentPosition()
    {
        return parentPosition;
    }

    public void setParentPosition(String pParentPosition)
    {
        parentPosition = pParentPosition;
    }

    public String getParameters()
    {
        return parameters;
    }

    public void setParameters(String pParameters)
    {
        parameters = pParameters;
    }

    public String getReturnValue()
    {
        return returnValue;
    }

    public void setReturnValue(String pReturnValue)
    {
        returnValue = pReturnValue;
    }

    public String getThrownExceptionClass()
    {
        return thrownExceptionClass;
    }

    public void setThrownExceptionClass(String pThrownExceptionClass)
    {
        thrownExceptionClass = pThrownExceptionClass;
    }

    public String getThrownExceptionMessage()
    {
        return thrownExceptionMessage;
    }

    public void setThrownExceptionMessage(String pThrownExceptionMessage)
    {
        thrownExceptionMessage = pThrownExceptionMessage;
    }

}
