package org.jmonitoring.console.methodcall.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.MethodCallExtractDTO;

import servletunit.struts.MockStrutsTestCase;

public class TestMethodCallSearchActionIn extends JMonitoringMockStrustTestCase
{

    public void testExecuteActionMappingActionFormHttpServletRequestHttpServletResponse()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(2);

        setRequestPathInfo("/MethodCallSearchIn");
        MethodCallSearchForm tForm = new MethodCallSearchForm();
        setActionForm(tForm);

        actionPerform();
        verifyForward("success");

        Map tTreeOfExtract = ((MethodCallSearchForm) getActionForm()).getTreeOfMethodCallExtract();
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = (Map) tTreeOfExtract.get("org");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = (Map) tTreeOfExtract.get("jmonitoring");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = (Map) tTreeOfExtract.get("console");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = (Map) tTreeOfExtract.get("flow");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = (Map) tTreeOfExtract.get("FlowBuilderUtil");
        assertEquals(2, tTreeOfExtract.size());
        assertEquals(new HashMap(), tTreeOfExtract.get("builNewFullFlowGrDefault"));
        assertEquals(new HashMap(), tTreeOfExtract.get("builNewFullFlow3GrChild2"));

    }

    public void testConvertListAsTree()
    {
        List tList = new ArrayList();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getTotoBis", "Grp1", new Integer(2)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.TotoBis", "getToto", "Grp1", new Integer(3)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Tata", "getTata", "Grp1", new Integer(4)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Toto", "getToto", "Grp1", new Integer(5)));
        tList.add(new MethodCallExtractDTO("com.monitoring.Titi", "getTiti", "Grp2", new Integer(6)));

        Map tFullListOfMap = new HashMap();
        Map tMap = MethodCallSearchActionIn.convertListAsTree(tFullListOfMap, tList);
        assertEquals(2, tMap.size());
        HashMap curMap = (HashMap) tMap.get("org");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("monitoring");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("toto");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("Toto");
        assertEquals(2, curMap.size());
        assertEquals(0, ((HashMap) curMap.get("getTotoGrp1")).size());

        assertEquals(0, ((Map) curMap.get("getTotoBisGrp1")).size());

        curMap = (HashMap) tMap.get("org");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("toto");
        curMap = (HashMap) curMap.get("TotoBis");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTotoGrp1");
        assertEquals(0, curMap.size());

        curMap = (HashMap) tMap.get("org");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("tata");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("Tata");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTataGrp1");
        assertEquals(0, curMap.size());

        curMap = (HashMap) tMap.get("com");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("Titi");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTitiGrp2");
        assertEquals(0, curMap.size());

        assertEquals(6, tFullListOfMap.size());
        assertNotNull(tFullListOfMap.get("org.monitoring.toto.Toto.getTotoGrp1"));
        assertNotNull(tFullListOfMap.get("org.monitoring.toto.Toto.getTotoBisGrp1"));
        assertNotNull(tFullListOfMap.get("org.monitoring.toto.TotoBis.getTotoGrp1"));
        assertNotNull(tFullListOfMap.get("org.monitoring.tata.Tata.getTataGrp1"));
        assertNotNull(tFullListOfMap.get("org.monitoring.tata.Toto.getTotoGrp1"));
        assertNotNull(tFullListOfMap.get("com.monitoring.Titi.getTitiGrp2"));
    }

}
