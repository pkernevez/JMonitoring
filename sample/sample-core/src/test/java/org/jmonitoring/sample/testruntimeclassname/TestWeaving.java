package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.sample.SamplePersistenceTestcase;

public class TestWeaving extends SamplePersistenceTestcase {

    public void testWeaving() throws InterruptedException {
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);

        AbstractSample tMother = new AbstractSample();
        tMother.methodATester();
        closeAndRestartSession();

        checkWeaving1();

        ChildSample tChild = new ChildSample();
        tChild.methodATester();

        checkWeaving2();

        tChild.methodWithOverride();
        closeAndRestartSession();

        checkWeaving3();
    }

    private void checkWeaving1() {
        assertEquals(1, MemoryStoreWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryStoreWriter.getFlow(0);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
        assertEquals(1, MemoryStoreWriter.countFlows());
    }

    private void checkWeaving2() {
        ExecutionFlowPO tFlow;
        tFlow = MemoryStoreWriter.getFlow(1);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getRuntimeClassName());
    }

    private void checkWeaving3() {
        ExecutionFlowPO tFlow;
        assertEquals(3, MemoryStoreWriter.countFlows());
        tFlow = MemoryStoreWriter.getFlow(2);
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodWithOverride", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
    }

}
