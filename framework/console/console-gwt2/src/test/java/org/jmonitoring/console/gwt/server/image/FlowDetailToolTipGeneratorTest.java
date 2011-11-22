package org.jmonitoring.console.gwt.server.image;

import javax.annotation.Resource;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowDetailToolTipGeneratorTest extends PersistanceTestCase
{
    @Resource(name = "color")
    private ColorManager color;

    @Autowired
    private ConsoleDao dao;

    @Test
    public void testGenerateToolTip() throws UnknownEntity
    {
        ExecutionFlowPO tFlow = dao.loadFullFlow(1);

        FlowDetailChartBarGenerator tUtil = new FlowDetailChartBarGenerator(color, tFlow.getFirstMethodCall());
        tUtil.chainAllMethodCallToMainTaskOfGroup();
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        tUtil.createGanttChart(tIntervalcategorydataset);

        FlowDetailToolTipGenerator tGenerator = new FlowDetailToolTipGenerator();
        String tUrl = tGenerator.generateToolTip(tIntervalcategorydataset, 0, 0);
        assertEquals("goto (flowId=1, position=1)", tUrl);
        assertEquals("goto (flowId=1, position=1)", tGenerator.generateToolTip(tIntervalcategorydataset, 0, 0));
        assertEquals("goto (flowId=1, position=1)", tGenerator.generateToolTip(tIntervalcategorydataset, 0, 0));
        assertEquals("goto (flowId=1, position=2)", tGenerator.generateToolTip(tIntervalcategorydataset, 0, 1));

    }

}
