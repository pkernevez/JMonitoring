package org.jmonitoring.console.gwt.server.common.converter;

import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;

public class MethodCallLocalToDtoVisitor implements DomainVisitor
{

    private BeanConverterUtil converter;

    MethodCallDTO firstMethod;

    public MethodCallLocalToDtoVisitor(BeanConverterUtil pConverter)
    {
        super();
        converter = pConverter;
    }

    public boolean visit(ExecutionFlowPO pFlow)
    {
        throw new RuntimeException("Not supported");
    }

    public boolean visit(MethodCallPO pMeth)
    {
        if (firstMethod == null)
        {
            firstMethod = converter.convertMethodCallToDto(pMeth);
            return true;
        } else
        {
            MethodCallExtractDTO tExtract = converter.convertToExtractDto(pMeth);
            firstMethod.setChildren(addChild(tExtract, firstMethod.getChildren()));
            return false;
        }
    }

    private MethodCallExtractDTO[] addChild(MethodCallExtractDTO pChildExtract, MethodCallExtractDTO[] pCurrentChildren)
    {
        MethodCallExtractDTO[] tNewChild;
        if (pCurrentChildren == null)
        {
            tNewChild = new MethodCallExtractDTO[] {pChildExtract };
        } else
        {
            tNewChild = new MethodCallExtractDTO[pCurrentChildren.length + 1];
            System.arraycopy(pCurrentChildren, 0, tNewChild, 0, pCurrentChildren.length);
            tNewChild[pCurrentChildren.length] = pChildExtract;
        }
        return tNewChild;
    }

    public MethodCallDTO getConverted(MethodCallPO pMeth)
    {
        pMeth.accept(this);
        return firstMethod;
    }

}
