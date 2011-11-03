package org.jmonitoring.console.gwt.server.image;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.ExecutionFlowBuilder;
import org.jmonitoring.console.gwt.server.common.MethodCallBuilder;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/all-test.xml" })
public class PieChartGeneratorTest extends JMonitoringTestCase
{

    @Resource(name = "formater")
    private FormaterBean formater;

    @Resource(name = "color")
    private ColorManager color;

    public static final long START_TIME = 1149282668046L;

    private MethodCallPO getSampleMeasurePoint()
    {
        return getSampleMeasurePoint(formater, color, null);
    }

    static MethodCallPO getSampleMeasurePoint(FormaterBean pFormater, ColorManager pColor, ConsoleDao pDao)
    {
        // Fri Jun 02 23:11:08 CEST 2006
        long tRefDate = new Date(START_TIME).getTime();
        ExecutionFlowBuilder tBuilder = ExecutionFlowBuilder.create(tRefDate);
        MethodCallBuilder tParentBuilder =
            tBuilder.createMethodCall(PieChartGeneratorTest.class.getName(), "builNewFullFlow", "GrDefault", 106);
        MethodCallBuilder tChild1 =
            tParentBuilder.addSubMethod(PieChartGeneratorTest.class.getName(), "builNewFullFlow2", "GrChild1", 2, 43);
        tChild1.addSubMethod(PieChartGeneratorTest.class.getName(), "builNewFullFlow4", "GrChild2", 3, 12);

        tChild1.addSubMethod(PieChartGeneratorTest.class.getName(), "builNewFullFlow4", "GrDefault", 21, 4);

        tParentBuilder.addSubMethod(PieChartGeneratorTest.class.getName(), "builNewFullFlow3", "GrChild2", 48, 27);
        try
        {
            return (pDao == null ? tParentBuilder.get() : tParentBuilder.getAndSave(pDao)).getFirstMethodCall();
        } catch (UnknownEntity e)
        {
            throw new RuntimeException(e);
        }
    }

    public static MethodCallPO createSample(MethodCallPO pParent, int pFlowId, int pPosition, String pClassName,
        String pMethodName, String pGroupName, String pParams, long pBegin, long pEnd)
    {
        MethodCallPO tPoint;
        tPoint = new MethodCallPO();
        tPoint.setMethId(new MethodCallPK());
        tPoint.setPosition(pPosition);
        tPoint.setParentMethodCall(pParent);
        tPoint.setClassName(pClassName);
        tPoint.setMethodName(pMethodName);
        tPoint.setGroupName(pGroupName);
        tPoint.setParams(pParams);
        tPoint.setBeginTime(pBegin);
        tPoint.setEndTime(pEnd);
        return tPoint;
    }

    @Test
    public void testAddTimeWith()
    {
        PieChartGenerator tUtil = new PieChartGenerator(color);
        tUtil.addTimeWith(getSampleMeasurePoint());
        Map<String, Integer> tMap = tUtil.listOfGroup;
        assertEquals(40, (int) tMap.get("GrDefault"));
        assertEquals(27, (int) tMap.get("GrChild1"));
        assertEquals(39, (int) tMap.get("GrChild2"));
        assertEquals(106, 40 + 27 + 39);
    }

    @Test
    public void testAddNbCallWith()
    {
        PieChartGenerator tUtil = new PieChartGenerator(color);
        tUtil.addNbCallWith(getSampleMeasurePoint());
        Map<String, Integer> tMap = tUtil.listOfGroup;
        assertEquals(2, (int) tMap.get("GrDefault"));
        assertEquals(1, (int) tMap.get("GrChild1"));
        assertEquals(2, (int) tMap.get("GrChild2"));
    }

}
