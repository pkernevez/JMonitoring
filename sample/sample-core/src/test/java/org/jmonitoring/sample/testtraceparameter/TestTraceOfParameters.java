package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.store.StoreFactory;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.server.store.impl.SynchroneJdbcStore;

public class TestTraceOfParameters extends SamplePersistenceTestcase
{

    public void testWithoutAnyTrace()
    {
        ConfigurationHelper.getInstance().setProperty(StoreFactory.STORE_CLASS,SynchroneJdbcStore.class.getName());

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithoutTrace(3, "pString");

        closeAndRestartSession();

        checkWithoutAnyTrace();
    }

    private void checkWithoutAnyTrace()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
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
        ConfigurationHelper.getInstance().setProperty(StoreFactory.STORE_CLASS,SynchroneJdbcStore.class.getName());

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameter(3, "pString");

        closeAndRestartSession();

        checkWithTraceOfParameter();
    }

    private void checkWithTraceOfParameter()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
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
        ConfigurationHelper.getInstance().setProperty(StoreFactory.STORE_CLASS,SynchroneJdbcStore.class.getName());

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameterAndResult(3, "pString");

        closeAndRestartSession();
        checkWithTraceOfParamAndResult();
    }

    private void checkWithTraceOfParamAndResult()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
       ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
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
        ConfigurationHelper.getInstance().setProperty(StoreFactory.STORE_CLASS,SynchroneJdbcStore.class.getName());

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithResult(3, "pString");

        closeAndRestartSession();

        checkWithTraceOfResult();
    }

    private void checkWithTraceOfResult()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);

        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(null, tMethodCall.getParams());
        assertEquals("Toto", tMethodCall.getReturnValue());
        assertNull(tMethodCall.getThrowableClass());
        assertNull(tMethodCall.getThrowableMessage());
    }

    public void testWithDefaultTraceOfException()
    {
        ConfigurationHelper.getInstance().setProperty(StoreFactory.STORE_CLASS,SynchroneJdbcStore.class.getName());

        ClassToBeCall tCallObject = new ClassToBeCall();
        try
        {
            tCallObject.toBeCallWithException(3, "pString");
        } catch (RuntimeException e)
        {
        }
        closeAndRestartSession();
        checkWithDefaultTraceOfException();
    }

    private void checkWithDefaultTraceOfException()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(null, tMethodCall.getParams());
        assertEquals(null, tMethodCall.getReturnValue());
        assertEquals("java.lang.RuntimeException", tMethodCall.getThrowableClass());
        assertEquals("Coucou", tMethodCall.getThrowableMessage());
    }

}
