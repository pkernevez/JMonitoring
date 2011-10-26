package org.jmonitoring.console.gwt.server.common;

import org.hibernate.Session;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;

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

    public ExecutionFlowPO get()
    {
        if (curMethodCallBuilder == null)
        {
            throw new ExecutionFlowInvalidExeption("You should define at least one sub method call");
        }
        return executionFlow;
    }

    public ExecutionFlowPO getAndSave(Session pSession)
    {
        pSession.save(executionFlow);
        createPK(executionFlow.getFirstMethodCall(), 0);
        pSession.save(executionFlow.getFirstMethodCall());
        return get();
    }

    private int createPK(MethodCallPO pMethodCall, int pCpt)
    {
        pMethodCall.setMethId(new MethodCallPK(pMethodCall.getFlow(), pCpt));
        for (MethodCallPO tCurMeth : pMethodCall.getChildren())
        {
            pCpt = createPK(tCurMeth, ++pCpt);
        }
        return pCpt;
    }

    public MethodCallBuilder createMethodCall(String pClassName, String pMethodName, String pGroupName, long pDuration)
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
