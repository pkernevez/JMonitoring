package org.jmonitoring.console.flow.edit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

import servletunit.struts.MockStrutsTestCase;

public class TestFlowExportServlet extends JMonitoringMockStrustTestCase {

    public void testDoGet() throws ServletException, IOException {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(5);

        int tFlowId = tFirstDto.getId();
        request.addParameter("id", "" + tFlowId);
        FlowExportServlet tServlet = new FlowExportServlet();
        ByteArrayOutputStream tOut = new ByteArrayOutputStream();
        response.setOutputStream(tOut);
        tServlet.doGet(request, response);
        assertEquals("application/x-zip", response.getContentType());
        assertTrue(response.getHeader("Content-Disposition").startsWith("attachment; filename="));
        assertTrue(
                "No response in servet. (length=" + response.getContentLength() + ")",
                response.getContentLength() > 100);
        assertTrue(tOut.size() > 100);
    }

}
