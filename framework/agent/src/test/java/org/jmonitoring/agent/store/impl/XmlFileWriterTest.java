package org.jmonitoring.agent.store.impl;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class XmlFileWriterTest extends TestCase
{

    @Test
    public void testWriteExecutionFlow() throws IOException
    {
        ConfigurationHelper.resetConfigFile("JMonitoringXmlWriterApplicationContext.xml");
        ExecutionFlowPO tFlow = HttpWriterTest.buildNewFullFlow();
        XmlFileWriter tWriter =
            (XmlFileWriter) ConfigurationHelper.getContext().getBean(IStoreWriter.STORE_WRITER_NAME);
        long tSize = new File("target/log/Thread." + Thread.currentThread().getName() + ".xml").length();
        assertTrue("The log file hasn't been initialized.", tSize > 5);
        assertTrue("The log file must be empty just after its initialization.", tSize < 20);
        tWriter.writeExecutionFlow(tFlow);
        tWriter.mLogFile.flush();
        tSize = new File("target/log/Thread." + Thread.currentThread().getName() + ".xml").length();
        assertTrue("The execution havn't been written.", tSize > 200);
        tWriter.mLogFile.close();
    }

}
