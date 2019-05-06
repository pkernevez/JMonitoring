package org.jmonitoring.core.dto.v2;

import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;

import java.io.*;
import java.util.Date;

import static org.junit.Assert.*;

public class DtoManagerTest {

    private DtoManagerV2 manager = new DtoManagerV2();

    @Test
    public void testLoadV2ExportWithV2ShouldReturnARealFlow() throws IOException {
        // Given
        String srcFile = this.getClass().getClassLoader().getResource("testV2Export.xml").getFile();
        InputStream instream = new FileInputStream(srcFile);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int nRead;
        byte[] buffer = new byte[1024];
        while ((nRead = instream.read(buffer, 0, buffer.length)) != -1) {
            result.write(buffer, 0, nRead);
        }
        byte[] exportBytes = result.toByteArray();

        // When
        ExecutionFlowDTO dto = manager.readDTO(exportBytes);

        // Then
        assertNotNull(dto);
        assertEquals("ServerAPISDEV", dto.getJvmIdentifier());
    }

    @Test
    public void testCopyWithExecutionFlowDtoShouldReturnPO() {
        // Given
        ExecutionFlowDTO dto = new ExecutionFlowDTO();
        dto.setBeginDateAsString("");
        dto.setBeginTime(new Date(1000L));
        dto.setEndTime(new Date(2000L));
        dto.setEndTimeAsString("");
        dto.setFirstMethodCall(null);
        dto.setId(1);
        dto.setJvmIdentifier("Jvm");
        dto.setThreadName("Thread");
        dto.setClassName("cname");
        dto.setMethodName("mname");

        // When
        ExecutionFlowPO po = manager.copy(dto);

        // Then
        assertEquals(1000L, po.getBeginTime());
        assertEquals(new Date(1000L), po.getBeginTimeAsDate());
        assertEquals(2000L, po.getEndTime());
        assertNull(po.getFirstMethodCall());
        assertEquals(1, po.getId());
        assertEquals("Jvm", po.getJvmIdentifier());
        assertEquals("Thread", po.getThreadName());
        assertEquals("cname", po.getFirstClassName());
        assertEquals("mname", po.getFirstMethodName());

    }

    @Test
    public void testCopyWithMethodCallDtoShouldReturnPO() {
        // Given
        MethodCallDTO dto = new MethodCallDTO();
        dto.setBeginTime(new Date(1000L));
        dto.setChildPosition(2);
        dto.setClassName("cname");
        dto.setEndTime(new Date(2000L));
        dto.setFlowId(10);
        dto.setGroupName("gname");
        dto.setMethodName("mname");
        dto.setParams("params");
        dto.setPosition(1);
        dto.setReturnValue("returnvalue");
        dto.setRuntimeClassName("rtclassnamr");
        dto.setThrowableClass("thclass");
        dto.setThrowableMessage("thmsg");
        ExecutionFlowPO flow = new ExecutionFlowPO();

        // When
        MethodCallPO po = manager.copy(dto, flow);

        // Then
        assertEquals(1000L, po.getBeginTime());
        assertEquals("cname", po.getClassName());
        assertEquals(2000L, po.getEndTime());
//        assertEquals(10, po.getFlow());
        assertEquals("gname", po.getGroupName());
        assertEquals("mname", po.getMethodName());
        assertEquals("params", po.getParams());
        assertEquals(1, po.getPosition());
        assertEquals("returnvalue", po.getReturnValue());
        assertEquals("rtclassnamr", po.getRuntimeClassName());
        assertEquals("thclass", po.getThrowableClass());
        assertEquals("thmsg", po.getThrowableMessage());
        assertEquals(flow, po.getFlow());

    }

    @Test
    // TODO Use Option instead of null
    public void testLoadV2ExportWithInvalidDataShouldReturnNull() {
        assertNull(manager.loadV2Export("bad string".getBytes()));
    }


}