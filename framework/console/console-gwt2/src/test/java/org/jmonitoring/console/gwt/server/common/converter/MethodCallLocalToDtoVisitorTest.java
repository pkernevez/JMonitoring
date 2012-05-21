package org.jmonitoring.console.gwt.server.common.converter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
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
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/all-test.xml" })
public class MethodCallLocalToDtoVisitorTest extends JMonitoringTestCase
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
    public void testVisitExecutionFlowNotSupported() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        DomainVisitor tVisitor = new MethodCallLocalToDtoVisitor(converter);
        tVisitor.visit(tFlowPO);
    }

    @Test
    public void testVisitMethodCallShouldGoToLevel1Only() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        MethodCallLocalToDtoVisitor tVisitor = new MethodCallLocalToDtoVisitor(converter);
        when(converter.convertMethodCallToDto(tFlowPO.getFirstMethodCall())).thenReturn(new MethodCallDTO());
        when(converter.convertToExtractDto(tFlowPO.getFirstMethodCall().getChild(0))).thenReturn(new MethodCallExtractDTO());
        when(converter.convertToExtractDto(tFlowPO.getFirstMethodCall().getChild(1))).thenReturn(new MethodCallExtractDTO());

        assertTrue("The root should return true", tVisitor.visit(tFlowPO.getFirstMethodCall()));
        assertFalse("The child should return false", tVisitor.visit(tFlowPO.getFirstMethodCall().getChild(0)));
        assertFalse("The child should return false", tVisitor.visit(tFlowPO.getFirstMethodCall().getChild(1)));
        assertNotNull(tVisitor.firstMethod);
        verify(converter).convertMethodCallToDto(tFlowPO.getFirstMethodCall());
        verify(converter, times(2)).convertToExtractDto(any(MethodCallPO.class));
        verifyNoMoreInteractions(converter);
    }
}
