package org.jmonitoring.console.gwt.server.image;

import javax.annotation.Resource;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.server.flow.FlowBuilderUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowDetailURLGeneratorTest extends PersistanceTestCase
{
    @Resource(name = "color")
    private ColorManager color;

    @Autowired
    private FlowBuilderUtil flowBuilder;

    @Autowired
    private ConsoleDao dao;

    public FlowDetailURLGeneratorTest()
    {
        dataInitialized = true;// Don't insert data before those test
    }

    @Test
    public void testUGRGeneration()
    {
        ExecutionFlowPO tFlow = FlowBuilderUtil.buildNewFullFlow(2);
        int tId = dao.insertFullExecutionFlow(tFlow);
        sessionFactory.getCurrentSession().clear();
        tFlow = dao.loadFullFlow(tId);

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
        assertEquals("javascript:window.methClick(1,3);", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));

    }

}
