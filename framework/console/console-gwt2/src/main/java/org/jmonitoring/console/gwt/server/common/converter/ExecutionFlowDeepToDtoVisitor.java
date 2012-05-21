package org.jmonitoring.console.gwt.server.common.converter;

import java.util.HashMap;
import java.util.Map;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;

public class ExecutionFlowDeepToDtoVisitor implements DomainVisitor
{

    private BeanConverterUtil converter;

    private ExecutionFlowDTO flow;

    MethodCallDTO firstMethod;

    Map<Integer, MethodCallExtractDTO> convertedMethodCall = new HashMap<Integer, MethodCallExtractDTO>();

    public ExecutionFlowDeepToDtoVisitor(BeanConverterUtil pConverter)
    {
        super();
        converter = pConverter;
    }

    public boolean visit(ExecutionFlowPO pFlow)
    {
        flow = converter.convertExecFlowToDto(pFlow);
        return true;
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
            int tParentPosition = pMeth.getParentMethodCall().getPosition();
            if (tParentPosition == 1)
            {// direct child of the root
                firstMethod.setChildren(addChild(tExtract, firstMethod.getChildren()));
            } else
            { // deeper in the graph
                MethodCallExtractDTO tParent = convertedMethodCall.get(tParentPosition);
                tParent.setChildren(addChild(tExtract, tParent.getChildren()));
            }
            convertedMethodCall.put(pMeth.getPosition(), tExtract);
            return true;
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

    public ExecutionFlowDTO getConverted(ExecutionFlowPO pFlowPO)
    {
        pFlowPO.accept(this);
        flow.setFirstMethodCall(firstMethod);
        return flow;
    }

}
