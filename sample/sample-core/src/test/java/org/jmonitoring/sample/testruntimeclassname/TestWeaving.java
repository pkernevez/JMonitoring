package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.sample.SamplePersistenceTestcase;

public class TestWeaving extends SamplePersistenceTestcase {

    public void testWeaving() throws InterruptedException {
        StoreManager.changeStoreWriterClass(MemoryWriter.class);

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
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
        assertEquals(1, MemoryWriter.countFlows());
    }

    private void checkWeaving2() {
        ExecutionFlowPO tFlow;
        tFlow = MemoryWriter.getFlow(1);
        assertEquals(AbstractSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodATester", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getRuntimeClassName());
    }

    private void checkWeaving3() {
        ExecutionFlowPO tFlow;
        assertEquals(3, MemoryWriter.countFlows());
        tFlow = MemoryWriter.getFlow(2);
        assertEquals(ChildSample.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertEquals("methodWithOverride", tFlow.getFirstMethodCall().getMethodName());
        assertNull(tFlow.getFirstMethodCall().getRuntimeClassName());
    }

}
