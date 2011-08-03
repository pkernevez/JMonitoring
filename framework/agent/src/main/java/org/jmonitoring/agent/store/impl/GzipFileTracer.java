package org.jmonitoring.agent.store.impl;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;

public class GzipFileTracer implements IStoreWriter
{

    File mDir;

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        String tFileName =
            "ExecutionFlow_" + pExecutionFlow.getJvmIdentifier() + "_Id" + pExecutionFlow.getId() + "_"
                + pExecutionFlow.getBeginTime() + ".gzip";
        FileOutputStream tFileOutput;
        try
        {
            tFileOutput = new FileOutputStream(new File(mDir, tFileName));
        } catch (FileNotFoundException e1)
        {
            throw new MeasureException("Unable to create OutputStream in file: " + mDir.getAbsolutePath()
                + File.pathSeparator + tFileName, e1);
        }
        try
        {
            ByteArrayOutputStream tOutput = new ByteArrayOutputStream(10000);
            try
            {
                GZIPOutputStream tZipStream;
                try
                {
                    tZipStream = new GZIPOutputStream(tOutput);
                    XMLEncoder tEncoder = new XMLEncoder(tZipStream);
                    tEncoder.writeObject(pExecutionFlow);
                    tEncoder.close();
                    tFileOutput.write(tOutput.toByteArray());
                } catch (IOException e)
                {
                    throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
                }
            } finally
            {
                try
                {
                    tOutput.close();
                } catch (IOException e)
                {
                    throw new MeasureException("Unable to close Stream", e);
                }
            }
        } finally
        {
            try
            {
                tFileOutput.close();
            } catch (IOException e)
            {
                throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
            }
        }
    }

    public void setDir(String pDir)
    {
        mDir = new File(pDir);
        if (!mDir.exists())
        {
            if (!mDir.mkdirs())
            {
                throw new RuntimeException(
                                           "Unable to create root dir for exporting ExecutionFlow, check your configuration (attribut dir of bean StoreWriter).");
            }
            if (!mDir.isDirectory())
            {
                throw new RuntimeException(
                                           "Check your configuration : attribut dir of bean StoreWriter is not a folder.");
            }
        }
    }

}
