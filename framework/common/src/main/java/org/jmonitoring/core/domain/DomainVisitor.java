package org.jmonitoring.core.domain;

public interface DomainVisitor
{
    boolean visit(ExecutionFlowPO pFlow);

    /**
     * Return boolean to avoid the may unecessary load of the full graph. If the visitor only ignore visit() calls, we
     * don't avoid the whole load of children.
     * 
     * @param pMeth
     * @return true to go deeper in the graph
     */
    boolean visit(MethodCallPO pMeth);
}
