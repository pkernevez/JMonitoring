package org.jmonitoring.console.gwt.server.common.converter;

import static org.mockito.Mockito.*;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.domain.DomainVisitor;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/all-test.xml" })
public class ExecutionFlowOnlyToDtoVisitorTest extends JMonitoringTestCase
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
    @ExpectedException(RuntimeException.class)
    public void testVisitMethodCall() throws Exception
    {

        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        DomainVisitor tVisitor = new ExecutionFlowOnlyToDtoVisitor(converter);
        tVisitor.visit(tFlowPO.getFirstMethodCall());
    }

    @Test
    public void testVisitOnlyExecutionFlow() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        DomainVisitor tVisitor = new ExecutionFlowOnlyToDtoVisitor(converter);
        assertFalse(tVisitor.visit(tFlowPO));
    }

    @Test
    public void testGetConverted() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExecutionFlowOnlyToDtoVisitor tVisitor = new ExecutionFlowOnlyToDtoVisitor(converter);
        when(converter.convertExecFlowToDto(tFlowPO)).thenReturn(new ExecutionFlowDTO());
        // when(converter.convertMethodCallToDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallDTO());
        // when(converter.convertToExtractDto(any(MethodCallPO.class), anyInt())).thenReturn(new
        // MethodCallExtractDTO());

        tVisitor.visit(tFlowPO);

        verify(converter).convertExecFlowToDto(tFlowPO);
        verifyNoMoreInteractions(converter);
    }
}
