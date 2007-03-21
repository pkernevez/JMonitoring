package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.server.store.impl.SynchroneJdbcStore;

public class TestWeaving extends SamplePersistenceTestcase
{

    public void testWeaving() throws InterruptedException
    {
        InsertionDao tDao = new InsertionDao(getSession());
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        ConfigurationHelper.getInstance().setProperty(ConfigurationHelper.STORE_CLASS, SynchroneJdbcStore.class.getName());

        AbstractSample tMother = new AbstractSample();
        tMother.methodATester();
        closeAndRestartSession();
        tDao = new InsertionDao(getSession());
        tConsoleDao = new ConsoleDao(getSession());
        
        checkWeaving1(tDao, tConsoleDao);

        ChildSample tChild = new ChildSample();
        tChild.methodATester();
        closeAndRestartSession();
        tDao = new InsertionDao(getSession());
        tConsoleDao = new ConsoleDao(getSession());
        
        checkWeaving2(tDao, tConsoleDao);

        tChild.methodWithOverride();
        closeAndRestartSession();
        tDao = new InsertionDao(getSession());
        
        tConsoleDao = new ConsoleDao(getSession());
        
        checkWeaving3(tDao, tConsoleDao);
    }

    private void checkWeaving1(InsertionDao pDao, ConsoleDao pConsoleDao)
    {
        assertEquals(1, pDao.countFlows());
        ExecutionFlowPO tFlow = pConsoleDao.readExecutionFlow(1);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
        assertEquals(1, pDao.countFlows());
    }

    private void checkWeaving2(InsertionDao pDao, ConsoleDao pConsoleDao)
    {
        ExecutionFlowPO tFlow;
        tFlow = pConsoleDao.readExecutionFlow(2);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getRuntimeClassName());
    }

    private void checkWeaving3(InsertionDao pDao, ConsoleDao pConsoleDao)
    {
        ExecutionFlowPO tFlow;
        assertEquals(3, pDao.countFlows());
        tFlow = pConsoleDao.readExecutionFlow(3);
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodWithOverride", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
    }

}
