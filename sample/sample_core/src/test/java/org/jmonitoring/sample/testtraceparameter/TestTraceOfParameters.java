package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;

public class TestTraceOfParameters extends PersistanceTestCase
{

    public void testWithoutAnyTrace()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithoutTrace(3, "pString");

        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(null, tMethodCall.getParams());
        assertNull(tMethodCall.getReturnValue());
        assertNull(tMethodCall.getThrowableClass());
        assertNull(tMethodCall.getThrowableMessage());
    }

    public void testWithTraceOfParameter()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameter(3, "pString");

        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals("[3, pString]", tMethodCall.getParams());
        assertNull(tMethodCall.getReturnValue());
        assertNull(tMethodCall.getThrowableClass());
        assertNull(tMethodCall.getThrowableMessage());
    }

    public void testWithTraceOfParamAndResult()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameterAndResult(3, "pString");

        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals("[3, pString]", tMethodCall.getParams());
        assertEquals("Toto", tMethodCall.getReturnValue());
        assertNull(tMethodCall.getThrowableClass());
        assertNull(tMethodCall.getThrowableMessage());
    }

    public void testWithTraceOfResult()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithResult(3, "pString");

        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(null, tMethodCall.getParams());
        assertEquals("Toto", tMethodCall.getReturnValue());
        assertNull(tMethodCall.getThrowableClass());
        assertNull(tMethodCall.getThrowableMessage());
    }

    // public void testWithCustomTraceOfException()
    // {
    // ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
    // Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);
    //
    // ClassToBeCall tCallObject = new ClassToBeCall();
    // tCallObject.toBeCallWithParameter(3, "pString");
    //
    // getSession().flush();
    // ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
    //
    // assertNotNull(tFlow);
    // MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
    // assertNotNull(tMethodCall);
    // assertEquals("", tMethodCall.getParams());
    // assertEquals("Toto", tMethodCall.getReturnValue());
    // assertNull(tMethodCall.getThrowableClass());
    // assertNull(tMethodCall.getThrowableMessage());
    // }

    public void testWithDefaultTraceOfException()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        try
        {
            tCallObject.toBeCallWithException(3, "pString");
        } catch (RuntimeException e)
        {
        }
        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(null, tMethodCall.getParams());
        assertEquals(null, tMethodCall.getReturnValue());
        assertEquals("java.lang.RuntimeException", tMethodCall.getThrowableClass());
        assertEquals("Coucou", tMethodCall.getThrowableMessage());
    }

}
