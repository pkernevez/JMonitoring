package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.sample.SamplePersistenceTestcase;

public class TestWeaving extends SamplePersistenceTestcase
{

    public void testWeaving() throws InterruptedException
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        AbstractSample tMother = new AbstractSample();
        tMother.methodATester();
        getSession().flush();
        
        checkWeaving1(tDao);
        
        ChildSample tChild = new ChildSample();
        tChild.methodATester();

        checkWeaving2(tDao);

        tChild.methodWithOverride();
        checkWeaving3(tDao);
    }

    private void checkWeaving3(IExecutionFlowDAO tDao)
    {
        ExecutionFlowPO tFlow;
        assertEquals(3, tDao.countFlows());
        tFlow = tDao.readExecutionFlow(3);
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodWithOverride", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
    }

    private void checkWeaving2(IExecutionFlowDAO tDao)
    {
        ExecutionFlowPO tFlow;
        tFlow = tDao.readExecutionFlow(2);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getRuntimeClassName());
    }

    private void checkWeaving1(IExecutionFlowDAO tDao)
    {
        assertEquals(1, tDao.countFlows());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
        assertEquals(1, tDao.countFlows());
    }
}
