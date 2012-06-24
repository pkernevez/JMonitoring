package org.jmonitoring.console.gwt.server.common.converter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowExportDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExportDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/all-test.xml" })
public class ExportDtoVisitorTest extends JMonitoringTestCase
{
    @Mock
    private BeanConverterUtil converter;

    @Override
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVisitOnlyExecutionFlowShouldGoDeeply() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        DomainVisitor tVisitor = new ExportDtoVisitor(converter);
        assertTrue("The conversion should go deeply", tVisitor.visit(tFlowPO));
    }

    @Test
    public void testVisitMethodCallShouldGoDeeply() throws Exception
    {

        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExportDtoVisitor tVisitor = new ExportDtoVisitor(converter);
        when(converter.convertMethodCallToExportDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallExportDTO());
        boolean tVisitResult = tVisitor.visit(tFlowPO.getFirstMethodCall());

        assertTrue(tVisitResult);
        assertNotNull(tVisitor.firstMethod);
    }

    @Test
    public void testGetConvertedReturnTheConvertedTreeOfDto() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExportDtoVisitor tVisitor = new ExportDtoVisitor(converter);

        when(converter.convertExecFlowToExportDto(tFlowPO)).thenReturn(new ExecutionFlowExportDTO());
        when(converter.convertMethodCallToExportDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallExportDTO());
        when(converter.convertMethodCallToExportDto(any(MethodCallPO.class))).thenReturn(new MethodCallExportDTO());

        ExecutionFlowExportDTO tConvertedFlow = tVisitor.getConverted(tFlowPO);
        MethodCallPO tRoot = tFlowPO.getFirstMethodCall();

        assertNotNull("The Dto tree is empty", tConvertedFlow);
        verify(converter).convertExecFlowToExportDto(tFlowPO);
        verify(converter, times(6)).convertMethodCallToExportDto(any(MethodCallPO.class));
        verifyNoMoreInteractions(converter);
    }
}