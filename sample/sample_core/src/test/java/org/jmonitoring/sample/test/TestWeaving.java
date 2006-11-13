package org.jmonitoring.sample.test;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;

public class TestWeaving extends PersistanceTestCase
{

    public void testWeaving() throws InterruptedException
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        AbstractSample tMother = new AbstractSample();
        tMother.methodATester();
        assertEquals(1, tDao.countFlows());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertNull( tFlow.getFirstMethodCall().getRuntimeClassName());
        
        ChildSample tChild = new ChildSample();
        tChild.methodATester();
        assertEquals(2, tDao.countFlows());
        tFlow = tDao.readExecutionFlow(2);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getRuntimeClassName());
        
        tChild.methodWithOverride();
        assertEquals(3, tDao.countFlows());
        tFlow = tDao.readExecutionFlow(3);
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodWithOverride", tFlow.getFirstMethodCall().getMethodName());
        assertNull( tFlow.getFirstMethodCall().getRuntimeClassName());
    }
}
