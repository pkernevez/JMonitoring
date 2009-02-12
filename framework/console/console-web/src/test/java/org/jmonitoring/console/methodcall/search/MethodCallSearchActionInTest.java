package org.jmonitoring.console.methodcall.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyHashMap;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyMap;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MethodCallSearchActionInTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    FlowBuilderUtil mUtil;

    @Test
    public void testExecuteActionMappingActionFormHttpServletRequestHttpServletResponse()
    {
        mUtil.buildAndSaveNewDto(2);

        setRequestPathInfo("/MethodCallSearchIn");
        MethodCallSearchForm tForm = new MethodCallSearchForm();
        setActionForm(tForm);

        actionPerform();
        verifyForward("success");

        MyMap tTreeOfExtract = ((MethodCallSearchForm) getActionForm()).getTreeOfMethodCallExtract();
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = tTreeOfExtract.get("org");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = tTreeOfExtract.get("jmonitoring");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = tTreeOfExtract.get("console");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = tTreeOfExtract.get("flow");
        assertEquals(1, tTreeOfExtract.size());

        tTreeOfExtract = tTreeOfExtract.get("FlowBuilderUtil");
        assertEquals(2, tTreeOfExtract.size());
        assertEquals(new MyHashMap(), tTreeOfExtract.get("builNewFullFlowGrDefault"));
        assertEquals(new MyHashMap(), tTreeOfExtract.get("builNewFullFlow3GrChild2"));

    }

    @Test
    public void testConvertListAsTree()
    {
        List<MethodCallExtractDTO> tList = new ArrayList<MethodCallExtractDTO>();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getTotoBis", "Grp1", new Integer(2)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.TotoBis", "getToto", "Grp1", new Integer(3)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Tata", "getTata", "Grp1", new Integer(4)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Toto", "getToto", "Grp1", new Integer(5)));
        tList.add(new MethodCallExtractDTO("com.monitoring.Titi", "getTiti", "Grp2", new Integer(6)));

        Map<String, MethodCallExtractDTO> tFullListOfMap = new HashMap<String, MethodCallExtractDTO>();
        MyMap tMap = MethodCallSearchActionIn.convertListAsTree(tFullListOfMap, tList);
        assertEquals(2, tMap.size());
        MyMap curMap = tMap.get("org");
        assertEquals(1, curMap.size());
        curMap = curMap.get("monitoring");
        assertEquals(2, curMap.size());
        curMap = curMap.get("toto");
        assertEquals(2, curMap.size());
        curMap = curMap.get("Toto");
        assertEquals(2, curMap.size());
        assertEquals(0, curMap.get("getTotoGrp1").size());

        assertEquals(0, curMap.get("getTotoBisGrp1").size());

        curMap = tMap.get("org");
        curMap = curMap.get("monitoring");
        curMap = curMap.get("toto");
        curMap = curMap.get("TotoBis");
        assertEquals(1, curMap.size());
        curMap = curMap.get("getTotoGrp1");
        assertEquals(0, curMap.size());

        curMap = tMap.get("org");
        curMap = curMap.get("monitoring");
        curMap = curMap.get("tata");
        assertEquals(2, curMap.size());
        curMap = curMap.get("Tata");
        assertEquals(1, curMap.size());
        curMap = curMap.get("getTataGrp1");
        assertEquals(0, curMap.size());

        curMap = tMap.get("com");
        curMap = curMap.get("monitoring");
        curMap = curMap.get("Titi");
        assertEquals(1, curMap.size());
        curMap = curMap.get("getTitiGrp2");
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
