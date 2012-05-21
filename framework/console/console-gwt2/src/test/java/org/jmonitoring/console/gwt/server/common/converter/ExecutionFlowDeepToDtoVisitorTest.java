package org.jmonitoring.console.gwt.server.common.converter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
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
public class ExecutionFlowDeepToDtoVisitorTest extends JMonitoringTestCase
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
        DomainVisitor tVisitor = new ExecutionFlowDeepToDtoVisitor(converter);
        assertTrue("The conversion should go deeply", tVisitor.visit(tFlowPO));
    }

    @Test
    public void testVisitMethodCallShouldGoDeeply() throws Exception
    {

        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExecutionFlowDeepToDtoVisitor tVisitor = new ExecutionFlowDeepToDtoVisitor(converter);
        when(converter.convertMethodCallToDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallDTO());
        boolean tVisitResult = tVisitor.visit(tFlowPO.getFirstMethodCall());

        assertTrue(tVisitResult);
        assertNotNull(tVisitor.firstMethod);
    }

    @Test
    public void testGetConvertedReturnTheConvertedTreeOfDto() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExecutionFlowDeepToDtoVisitor tVisitor = new ExecutionFlowDeepToDtoVisitor(converter);

        when(converter.convertExecFlowToDto(tFlowPO)).thenReturn(new ExecutionFlowDTO());
        when(converter.convertMethodCallToDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallDTO());
        when(converter.convertToExtractDto(any(MethodCallPO.class))).thenReturn(new MethodCallExtractDTO());

        ExecutionFlowDTO tConvertedFlow = tVisitor.getConverted(tFlowPO);
        MethodCallPO tRoot = tFlowPO.getFirstMethodCall();

        assertNotNull("The Dto tree is empty", tConvertedFlow);
        verify(converter).convertExecFlowToDto(tFlowPO);
        verify(converter).convertMethodCallToDto(tRoot);
        verify(converter, times(5)).convertToExtractDto(any(MethodCallPO.class));
        verifyNoMoreInteractions(converter);
    }
}