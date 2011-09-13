package org.jmonitoring.agent.store;

import org.jmonitoring.agent.store.impl.OutOfContextFilter;
import org.jmonitoring.agent.store.impl.TimeFilter;
import org.jmonitoring.core.domain.MethodCallPO;

public interface Filter
{

    /**
     * Decide if a method call need to be kept.
     * It may be use to remove an uninteresting measure, for example to remove short call and decrease noise.
     * Another usage is to and information when a measure have been done without any context. 
     * If a <code>MethodCallPO</code> id remove, its children are removed too, independently of their own filtering.
     * 
     * @param pParent The parent of this MethodCall. Before the calling the pCurrent is not yet a child of the parent.
     * @param pCurrent The current measure that may be add to the parent.
     * @return True if we want to kept this measure and add pCurent to the children of the parent.
     * @see TimeFilter
     * @see OutOfContextFilter
     */
    boolean keep(MethodCallPO pCurrent);
    
}
