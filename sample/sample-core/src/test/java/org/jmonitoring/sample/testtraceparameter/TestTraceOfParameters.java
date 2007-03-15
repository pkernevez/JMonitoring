package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.sample.SamplePersistenceTestcase;

public class TestTraceOfParameters extends SamplePersistenceTestcase
{

    public void testWithoutAnyTrace()
    {
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithoutTrace(3, "pString");

        getSession().flush();

        checkWithoutAnyTrace();
    }

    private void checkWithoutAnyTrace()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
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
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameter(3, "pString");

        getSession().flush();

        checkWithTraceOfParameter();
    }

    private void checkWithTraceOfParameter()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
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
         Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithParameterAndResult(3, "pString");

        getSession().flush();
        checkWithTraceOfParamAndResult();
    }

    private void checkWithTraceOfParamAndResult()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
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
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithResult(3, "pString");

        getSession().flush();
        checkWithTraceOfResult();
    }

    private void checkWithTraceOfResult()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);

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
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        try
        {
            tCallObject.toBeCallWithException(3, "pString");
        } catch (RuntimeException e)
        {
        }
        getSession().flush();
        checkWithDefaultTraceOfException();
    }

    private void checkWithDefaultTraceOfException()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
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
