package org.jmonitoring.console.gwt.server.common;

import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;

public class MethodCallBuilder
{
    private MethodCallPO methodCall;

    private ExecutionFlowBuilder executionFlowBuilder;

    public MethodCallBuilder(ExecutionFlowBuilder pExecutionFlowBuilder, MethodCallPO pMeth)
    {
        executionFlowBuilder = pExecutionFlowBuilder;
        methodCall = pMeth;
    }

    public MethodCallBuilder(ExecutionFlowBuilder pExecutionFlowBuilder, String pClassName, String pMethodName,
        String pGroupName, long pBeginTime, long pDuration)
    {
        executionFlowBuilder = pExecutionFlowBuilder;
        methodCall = new MethodCallPO();
        methodCall.setFlow(pExecutionFlowBuilder.getInternal());
        methodCall.setClassName(pClassName);
        methodCall.setMethodName(pMethodName);
        methodCall.setGroupName(pGroupName);
        methodCall.setBeginTime(pBeginTime);
        methodCall.setEndTime(pBeginTime + pDuration);
        methodCall.setDuration(pDuration);
    }

    public ExecutionFlowPO get()
    {
        return executionFlowBuilder.get();
    }

    public MethodCallPO getInternal()
    {
        return methodCall;
    }

    public MethodCallBuilder addSubMethod(String pClassName, String pMethodName, String pGroupName, long pDuration)
    {
        return addSubMethod(pClassName, pMethodName, pGroupName, 0, pDuration);
    }

    public MethodCallBuilder addSubMethod(String pClassName, String pMethodName, String pGroupName, long pOffSet,
        long pDuration)
    {
        if (methodCall.getChildren().size() > 0)
        {
            MethodCallPO tPrevious = methodCall.getChild(methodCall.getChildren().size() - 1);
            if (tPrevious.getEndTime() > (methodCall.getBeginTime() + pOffSet))
            {
                throw new ExecutionFlowInvalidExeption(
                                                       "The next methodCall could not start before the previous endTime. Previous endTime is "
                                                           + tPrevious.getEndTime() + " and next beginTime is "
                                                           + (methodCall.getBeginTime() + pOffSet));
            }
        }
        if (pOffSet < 0)
        {
            throw new ExecutionFlowInvalidExeption(
                                                   "The child could not start before the parent beginTime. Child beginTime is "
                                                       + (methodCall.getBeginTime() + pOffSet)
                                                       + " and parent beginTime is " + methodCall.getBeginTime());
        }
        if (pOffSet > methodCall.getDuration())
        {
            throw new ExecutionFlowInvalidExeption(
                                                   "The child could not start after the parent ended. Child beginTime is "
                                                       + (methodCall.getBeginTime() + pOffSet)
                                                       + " and parent endTime is " + methodCall.getEndTime());
        }
        if ((pOffSet + pDuration) > methodCall.getDuration())
        {
            throw new ExecutionFlowInvalidExeption("The child could not end after the parent. Child endTime is "
                + (methodCall.getBeginTime() + pDuration + pOffSet) + " and parent is " + methodCall.getEndTime());
        }
        MethodCallBuilder subMethodCallBuilder =
            new MethodCallBuilder(executionFlowBuilder, pClassName, pMethodName, pGroupName, methodCall.getBeginTime()
                + pOffSet, pDuration);
        methodCall.addChildren(subMethodCallBuilder.getInternal());
        return subMethodCallBuilder;
    }

    public MethodCallBuilder goToParent()
    {
        return new MethodCallBuilder(executionFlowBuilder, methodCall.getParentMethodCall());
    }

    public MethodCallBuilder goToLastChildren()
    {
        return new MethodCallBuilder(executionFlowBuilder, methodCall.getChild(methodCall.getChildren().size() - 1));
    }

    public MethodCallBuilder setRuntimeClassName(String pClassName)
    {
        methodCall.setRuntimeClassName(pClassName);
        return this;
    }

    public MethodCallBuilder setThrowable(String pThrowableClass, String pThrowableMessage)
    {
        methodCall.setThrowableClass(pThrowableClass);
        methodCall.setThrowableMessage(pThrowableMessage);
        return this;
    }

}
