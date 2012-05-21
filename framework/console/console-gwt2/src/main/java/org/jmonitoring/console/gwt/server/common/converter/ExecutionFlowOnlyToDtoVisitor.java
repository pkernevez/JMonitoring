package org.jmonitoring.console.gwt.server.common.converter;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;

public class ExecutionFlowOnlyToDtoVisitor implements DomainVisitor
{
    private BeanConverterUtil beanConverter;

    private ExecutionFlowDTO currentFlow;

    public ExecutionFlowOnlyToDtoVisitor(BeanConverterUtil pBeanConverter)
    {
        beanConverter = pBeanConverter;
    }

    public boolean visit(ExecutionFlowPO pFlow)
    {
        currentFlow = beanConverter.convertExecFlowToDto(pFlow);
        return false;
    }

    public boolean visit(MethodCallPO pMeth)
    {
        throw new RuntimeException("Not supported");
    }

    public ExecutionFlowDTO getConverted(ExecutionFlowPO pFlowPO)
    {
        pFlowPO.accept(this);
        return currentFlow;
    }

}
