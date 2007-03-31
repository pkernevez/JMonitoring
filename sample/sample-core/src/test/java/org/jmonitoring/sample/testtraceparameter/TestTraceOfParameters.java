package org.jmonitoring.sample.testtraceparameter;

import org.hibernate.Session;
import org.jmonitoring.agent.store.StoreFactory;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.hibernate.dao.InsertionHibernateDAO;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.server.store.impl.SynchroneJdbcStore;

public class TestTraceOfParameters extends SamplePersistenceTestcase
{

    public void testWithoutAnyTraceAndHibernate()
    {
        ConfigurationHelper.getInstance().setProperty(ConfigurationHelper.STORE_CLASS,
            InsertionHibernateDAO.class.getName());
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallOther(3, "AB");

        closeAndRestartSession();

        InsertionHibernateDAO tDao = new InsertionHibernateDAO(getSession());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        MethodCallPO tMethodCall = tFlow.getFirstMethodCall();
        assertNotNull(tMethodCall);
        assertEquals(ClassToBeCall.class.getName(), tMethodCall.getClassName());
        assertEquals("toBeCallOther", tMethodCall.getMethodName());
        assertEquals("Other", tMethodCall.getGroupName());
        assertEquals("[3, AB]", tMethodCall.getParams());
        assertNotNull(tMethodCall.getFlow());

        assertEquals(2, tMethodCall.getChildren().size());
        tMethodCall = tMethodCall.getChild(0);
        assertNotNull(tMethodCall);
        assertEquals(ClassToBeCall.class.getName(), tMethodCall.getClassName());
        assertEquals("toBeCallWithoutTrace", tMethodCall.getMethodName());
        assertEquals("WithoutTrace", tMethodCall.getGroupName());
        assertEquals(null, tMethodCall.getParams());
        assertNotNull(tMethodCall.getFlow());
        assertEquals(0, tMethodCall.getChildren().size());

        tMethodCall = tMethodCall.getParentMethodCall().getChild(1);
        assertNotNull(tMethodCall);
        assertEquals(Session.class.getName(), tMethodCall.getClassName());
        assertEquals("save", tMethodCall.getMethodName());
        assertEquals("Hibernate", tMethodCall.getGroupName());
        assertTrue(tMethodCall.getParams().startsWith("[" + ClassToBeCall.class.getName()));
        assertNotNull(tMethodCall.getFlow());
        assertEquals(0, tMethodCall.getChildren().size());

    }

    public void testWithoutAnyTrace()
    {
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

        ClassToBeCall tCallObject = new ClassToBeCall();
        tCallObject.toBeCallWithoutTrace();

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
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

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
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

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
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

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
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);

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
