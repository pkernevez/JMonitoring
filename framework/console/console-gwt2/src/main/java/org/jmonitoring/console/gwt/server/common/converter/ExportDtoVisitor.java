package org.jmonitoring.console.gwt.server.common.converter;

import java.util.HashMap;
import java.util.Map;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowExportDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExportDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;

public class ExportDtoVisitor implements DomainVisitor
{

    private BeanConverterUtil converter;

    private ExecutionFlowExportDTO flow;

    MethodCallExportDTO firstMethod;

    Map<Integer, MethodCallExportDTO> convertedMethodCall = new HashMap<Integer, MethodCallExportDTO>();

    public ExportDtoVisitor(BeanConverterUtil pConverter)
    {
        super();
        converter = pConverter;
    }

    public boolean visit(ExecutionFlowPO pFlow)
    {
        flow = converter.convertExecFlowToExportDto(pFlow);
        return true;
    }

    public boolean visit(MethodCallPO pMeth)
    {
        MethodCallExportDTO tExport = converter.convertMethodCallToExportDto(pMeth);
        if (firstMethod == null)
        {
            firstMethod = tExport;
        } else
        {
            int tParentPosition = pMeth.getParentMethodCall().getPosition();
            MethodCallExportDTO tParent = convertedMethodCall.get(tParentPosition);
            tParent.setChildren(addChild(tExport, tParent.getChildren()));
        }
        convertedMethodCall.put(pMeth.getPosition(), tExport);
        return true;
    }

    private MethodCallExportDTO[] addChild(MethodCallExportDTO pExport, MethodCallExportDTO[] pMethodCallExportDTOs)
    {
        MethodCallExportDTO[] tNewChild;
        if (pMethodCallExportDTOs == null)
        {
            tNewChild = new MethodCallExportDTO[] {pExport };
        } else
        {
            tNewChild = new MethodCallExportDTO[pMethodCallExportDTOs.length + 1];
            System.arraycopy(pMethodCallExportDTOs, 0, tNewChild, 0, pMethodCallExportDTOs.length);
            tNewChild[pMethodCallExportDTOs.length] = pExport;
        }
        return tNewChild;
    }

    public ExecutionFlowExportDTO getConverted(ExecutionFlowPO pFlowPO)
    {
        pFlowPO.accept(this);
        flow.setFirstMethodCall(firstMethod);
        return flow;
    }

}
