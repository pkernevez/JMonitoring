package org.jmonitoring.console.gwt.server.image;

import javax.annotation.Resource;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowDetailURLGeneratorTest extends PersistanceTestCase
{
    @Resource(name = "color")
    private ColorManager color;

    @Autowired
    private ConsoleDao dao;

    @Test
    public void testUGRGeneration()
    {
        ExecutionFlowPO tFlow = dao.loadFullFlow(1);

        ChartBarGenerator tUtil = new ChartBarGenerator(color, tFlow.getFirstMethodCall());
        tUtil.chainAllMethodCallToMainTaskOfGroup();
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        tUtil.createGanttChart(tIntervalcategorydataset);

        FlowDetailURLGenerator tGenerator = new FlowDetailURLGenerator();
        String tUrl = tGenerator.generateURL(tIntervalcategorydataset, 0, 0);
        assertEquals("javascript:window.methClick(1,1);", tUrl);
        assertEquals("javascript:window.methClick(1,1);", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("javascript:window.methClick(1,1);", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("javascript:window.methClick(1,2);", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));

    }

}
