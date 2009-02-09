package org.jmonitoring.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class PNGMapGenerationTest extends TestCase
{
    @Test
    public void testPNGGen()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "S1", "C1");
        dataset.addValue(4.0, "S1", "C2");
        dataset.addValue(15.0, "S1", "C3");
        dataset.addValue(14.0, "S1", "C4");
        dataset.addValue(-5.0, "S2", "C1");
        dataset.addValue(-7.0, "S2", "C2");
        dataset.addValue(14.0, "S2", "C3");
        dataset.addValue(-3.0, "S2", "C4");
        dataset.addValue(6.0, "S3", "C1");
        dataset.addValue(17.0, "S3", "C2");
        dataset.addValue(-12.0, "S3", "C3");
        dataset.addValue(7.0, "S3", "C4");
        dataset.addValue(7.0, "S4", "C1");
        dataset.addValue(15.0, "S4", "C2");
        dataset.addValue(11.0, "S4", "C3");
        dataset.addValue(0.0, "S4", "C4");
        dataset.addValue(-8.0, "S5", "C1");
        dataset.addValue(-6.0, "S5", "C2");
        dataset.addValue(10.0, "S5", "C3");
        dataset.addValue(-9.0, "S5", "C4");
        dataset.addValue(9.0, "S6", "C1");
        dataset.addValue(8.0, "S6", "C2");
        dataset.addValue(null, "S6", "C3");
        dataset.addValue(6.0, "S6", "C4");
        dataset.addValue(-10.0, "S7", "C1");
        dataset.addValue(9.0, "S7", "C2");
        dataset.addValue(7.0, "S7", "C3");
        dataset.addValue(7.0, "S7", "C4");
        dataset.addValue(11.0, "S8", "C1");
        dataset.addValue(13.0, "S8", "C2");
        dataset.addValue(9.0, "S8", "C3");
        dataset.addValue(9.0, "S8", "C4");
        dataset.addValue(-3.0, "S9", "C1");
        dataset.addValue(7.0, "S9", "C2");
        dataset.addValue(11.0, "S9", "C3");
        dataset.addValue(-10.0, "S9", "C4");
        JFreeChart chart =
            ChartFactory.createBarChart("Bar Chart", "Category", "Value", dataset, PlotOrientation.VERTICAL, true,
                                        true, false);

        try
        {
            File tTmpFile = File.createTempFile("PNG", "png");
            FileOutputStream tStream = new FileOutputStream(tTmpFile);
            ChartRenderingInfo tInfo = new ChartRenderingInfo();

            ChartUtilities.writeChartAsPNG(tStream, chart, 400, 300, tInfo);

            String tMap = ChartUtilities.getImageMap("MyMap", tInfo);
            assertEquals("<MAP NAME=\"MyMap\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"78,86,82,146\" title=\"(S1, C1) = 10\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"85,147,89,177\" title=\"(S2, C1) = -5\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"91,111,95,147\" title=\"(S3, C1) = 6\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"98,104,102,146\" title=\"(S4, C1) = 7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"105,147,109,195\" title=\"(S5, C1) = -8\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"111,92,115,146\" title=\"(S6, C1) = 9\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"118,147,122,207\" title=\"(S7, C1) = -10\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"125,80,129,146\" title=\"(S8, C1) = 11\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"131,147,135,165\" title=\"(S9, C1) = -3\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"158,123,162,147\" title=\"(S1, C2) = 4\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"164,147,168,189\" title=\"(S2, C2) = -7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"171,44,175,147\" title=\"(S3, C2) = 17\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"178,56,182,147\" title=\"(S4, C2) = 15\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"184,147,188,183\" title=\"(S5, C2) = -6\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"191,98,195,146\" title=\"(S6, C2) = 8\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"198,92,202,146\" title=\"(S7, C2) = 9\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"204,68,208,147\" title=\"(S8, C2) = 13\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"211,104,215,146\" title=\"(S9, C2) = 7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"238,56,242,147\" title=\"(S1, C3) = 15\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"244,62,248,147\" title=\"(S2, C3) = 14\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"251,147,255,220\" title=\"(S3, C3) = -12\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"257,80,261,146\" title=\"(S4, C3) = 11\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"264,86,268,146\" title=\"(S5, C3) = 10\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"277,104,281,146\" title=\"(S7, C3) = 7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"284,92,288,146\" title=\"(S8, C3) = 9\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"291,80,295,146\" title=\"(S9, C3) = 11\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"317,62,321,147\" title=\"(S1, C4) = 14\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"324,147,328,165\" title=\"(S2, C4) = -3\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"331,104,335,146\" title=\"(S3, C4) = 7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"337,147,341,148\" title=\"(S4, C4) = 0\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"344,147,348,201\" title=\"(S5, C4) = -9\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"350,111,354,147\" title=\"(S6, C4) = 6\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"357,104,361,146\" title=\"(S7, C4) = 7\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"364,92,368,146\" title=\"(S8, C4) = 9\">\r\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"370,147,374,207\" title=\"(S9, C4) = -10\">\r\n" + "</MAP>", tMap);
        } catch (IOException e)
        {
            fail(e.getMessage());
        }
    }

}
