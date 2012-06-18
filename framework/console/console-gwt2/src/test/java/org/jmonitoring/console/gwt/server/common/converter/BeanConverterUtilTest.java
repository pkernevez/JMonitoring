package org.jmonitoring.console.gwt.server.common.converter;

import java.io.InputStream;

import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/all-test.xml", "/converter-test.xml" })
public class BeanConverterUtilTest extends JMonitoringTestCase
{
    @Autowired
    private FormaterBean formater;

    @Autowired
    private BeanConverterUtil converter;

    @Test
    public void testConvertExecFlowToDto() throws Exception
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.createMockDBFullFlow();
        ExecutionFlowDTO tFlowDTO = converter.convertExecFlowToDto(tFlowPO);
        assertEquals(tFlowPO.getId(), tFlowDTO.getId());
        assertEquals(tFlowPO.getThreadName(), tFlowDTO.getThreadName());
        assertEquals(tFlowPO.getJvmIdentifier(), tFlowDTO.getJvmIdentifier());
        assertEquals(formater.formatDateTime(tFlowPO.getBeginTimeAsDate()), tFlowDTO.getBeginTime());
        assertEquals(formater.formatDateTime(tFlowPO.getEndTime()), tFlowDTO.getEndTime());
        assertEquals(String.valueOf(tFlowPO.getDuration()), tFlowDTO.getDuration());
        assertEquals(tFlowPO.getFirstMethodCall().getGroupName(), tFlowDTO.getGroupName());
        assertEquals(tFlowPO.getFirstClassName(), tFlowDTO.getClassName());
        assertEquals(tFlowPO.getFirstMethodName(), tFlowDTO.getMethodName());

    }

    @Test
    public void testConvertMethodCallToDto() throws Exception
    {

    }

    @Test
    public void testConvertToExtractDtoForRoot() throws Exception
    {
        MethodCallPO tRoot = PersistanceTestCase.createMockDBFullFlow().getFirstMethodCall();
        MethodCallExtractDTO tExtractDTO = converter.convertToExtractDto(tRoot);
        assertEquals("1", tExtractDTO.getPosition());
        assertEquals(null, tExtractDTO.getParentPosition());
        assertEquals(tRoot.getClassName(), tExtractDTO.getFullClassName());
        assertEquals(tRoot.getClassName() + "." + tRoot.getMethodName() + "()", tExtractDTO.getFullMethodName());
        assertEquals(tRoot.getMethodName(), tExtractDTO.getMethodName());
        assertEquals(tRoot.getGroupName(), tExtractDTO.getGroupName());

        assertEquals("0", tExtractDTO.getTimeFromPrevChild());
    }

    @Test
    public void testConvertToExtractDtoForFirstChild() throws Exception
    {
        MethodCallPO tFirstChild = PersistanceTestCase.createMockDBFullFlow().getFirstMethodCall().getChild(0);
        MethodCallExtractDTO tExtractDTO = converter.convertToExtractDto(tFirstChild);
        assertEquals("2", tExtractDTO.getTimeFromPrevChild());

    }

    @Test
    public void testConvertToExtractDtoForLastChild() throws Exception
    {
        MethodCallPO tFirstChild = PersistanceTestCase.createMockDBFullFlow().getFirstMethodCall().getChild(1);
        MethodCallExtractDTO tExtractDTO = converter.convertToExtractDto(tFirstChild);
        assertEquals("3", tExtractDTO.getTimeFromPrevChild());
    }

    @Test
    public void testReadDtoFromXml() throws Exception
    {
        InputStream tGZip = this.getClass().getResourceAsStream("FlowTest-newformat.gzip");
        ExecutionFlowDTO tFlow = converter.readDtoFromXml(tGZip);
        assertEquals("org.jmonitoring.sample.console.SampleAlreadyWeavedActionIn Modified", tFlow.getClassName());
        assertEquals("2642", tFlow.getDuration());
        assertEquals(5, tFlow.getFirstMethodCall().getChildren().length);
        assertEquals("SomeNonStatics", tFlow.getFirstMethodCall().getChild(4).getGroupName());
        assertEquals("org.jmonitoring.sample.main.RunSample", tFlow.getFirstMethodCall().getChild(4).getFullClassName());
        assertEquals("run", tFlow.getFirstMethodCall().getChild(4).getMethodName());
    }

    @Test
    public void testConvertFlowFromXmlNewFormat() throws Exception
    {

    }

    @Test
    public void testConvertFlowFromXmlOldFormat() throws Exception
    {
        fail("NOT IMPLEMENTED");
        // "FlowTest-oldformat.gzip"

    }

}
