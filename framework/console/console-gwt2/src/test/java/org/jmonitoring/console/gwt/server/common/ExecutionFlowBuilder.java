package org.jmonitoring.console.gwt.server.common;

import org.jmonitoring.core.domain.ExecutionFlowPO;

public class ExecutionFlowBuilder
{
    private ExecutionFlowPO executionFlow;

    private MethodCallBuilder curMethodCallBuilder;

    private ExecutionFlowBuilder(long pBeginTime)
    {
        executionFlow = new ExecutionFlowPO();
        executionFlow.setBeginTime(pBeginTime);
        executionFlow.setThreadName("Main");
        executionFlow.setJvmIdentifier("DefaultJvm");
    }

    public static ExecutionFlowBuilder create(long pBeginTime)
    {
        return new ExecutionFlowBuilder(pBeginTime);
    }

    public void setThreadName(String pThreadName)
    {

    }

    public void setJVM(String pJvm)
    {

    }

    public ExecutionFlowPO get()
    {
        if (curMethodCallBuilder == null)
        {
            throw new ExecutionFlowInvalidExeption("You should define at least one sub method call");
        }
        return executionFlow;
    }

    public MethodCallBuilder setMethodCall(String pClassName, String pMethodName, String pGroupName, long pDuration)
    {
        if (curMethodCallBuilder == null)
        {
            executionFlow.setFirstClassName(pClassName);
            executionFlow.setFirstMethodName(pMethodName);
            executionFlow.setDuration(pDuration);
            executionFlow.setEndTime(executionFlow.getBeginTime() + pDuration);
        }
        curMethodCallBuilder =
            new MethodCallBuilder(this, pClassName, pMethodName, pGroupName, executionFlow.getBeginTime(), pDuration);
        executionFlow.setFirstMethodCall(curMethodCallBuilder.getInternal());
        return curMethodCallBuilder;
    }

    public ExecutionFlowBuilder setRuntimeClassName(String pClassName)
    {
        curMethodCallBuilder.setRuntimeClassName(pClassName);
        return this;
    }

    public ExecutionFlowBuilder setThrowable(String pThrowableClass, String pThrowableMessage)
    {
        curMethodCallBuilder.setThrowable(pThrowableClass, pThrowableMessage);
        return this;
    }

    public ExecutionFlowBuilder setJvm(String pJvm)
    {
        executionFlow.setJvmIdentifier(pJvm);
        return this;
    }

    public ExecutionFlowBuilder setThread(String pThread)
    {
        executionFlow.setThreadName(pThread);
        return this;
    }

    ExecutionFlowPO getInternal()
    {
        return executionFlow;
    }

}
