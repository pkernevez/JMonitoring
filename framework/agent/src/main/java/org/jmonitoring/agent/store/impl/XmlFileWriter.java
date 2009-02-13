package org.jmonitoring.agent.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allow the logging with an XMLFile. This class is not ThreadSafe when multiple file is used.
 * 
 * @see org.jmonitoring.core.configuration.Configuration.SAME_FILE_FOR_ALL_THREAD
 * 
 * @author pke
 */
public class XmlFileWriter implements IStoreWriter
{
    private FormaterBean mFormater;

    final Writer mLogFile;

    private static Logger sLog = LoggerFactory.getLogger(XmlFileWriter.class);

    public XmlFileWriter(String pFolderName, FormaterBean pFormater)
    {
        mFormater = pFormater;
        File tLogDir = new File(pFolderName);
        if (!tLogDir.exists())
        {
            tLogDir.mkdir();
        }
        if (!tLogDir.isDirectory())
        {
            throw new MeasureException("The log directory isn't valid [" + tLogDir.getAbsolutePath() + "]");
        }
        File tLogFile =
            new File(tLogDir.getAbsolutePath() + File.separatorChar + "Thread." + Thread.currentThread().getName()
                + ".xml");
        if (tLogFile.exists())
        {
            if (!tLogFile.delete())
            {
                throw new MeasureException("Unable to delete file[" + tLogFile.getAbsolutePath() + "]");
            }
        }
        try
        {
            mLogFile = new FileWriter(tLogFile);
        } catch (IOException e)
        {
            sLog.error("Unable to create LogFile for Thread [" + Thread.currentThread().getName() + "] ["
                + tLogFile.getAbsolutePath() + "]");
            throw new MeasureException("Unable to create LogFile for Thread [" + Thread.currentThread().getName()
                + "] [" + tLogFile.getAbsolutePath() + "]");
        }
        writeToFile("<Threads>\n", true);
        Runtime.getRuntime().addShutdownHook(new Thread()
        { // Close the TAG at the end
                                                 @Override
                                                 public void run()
                                                 {
                                                     try
                                                     {
                                                         mLogFile.write("</Threads>");
                                                         mLogFile.flush();
                                                         mLogFile.close();
                                                     } catch (IOException e)
                                                     {
                                                         sLog
                                                             .debug("JVM Shutdown : Unable to close file, probably already closed.");
                                                     }
                                                 }
                                             });
    }

    private void writeToFile(String pMessage, boolean pFlush)
    {
        try
        {
            mLogFile.write(pMessage);
            if (pFlush)
            {
                mLogFile.flush();
            }
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write into LogFile for Thread [" + Thread.currentThread().getName()
                + "]");
        }
    }

    // TODO Seperate and create another specific class XMLSerializer
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        StringBuilder tCurrentBuffer = new StringBuilder();
        tCurrentBuffer.append("<Thread name=\"").append(pExecutionFlow.getThreadName());
        tCurrentBuffer.append("\" server=\"").append(pExecutionFlow).append("\" duration=\"");
        tCurrentBuffer.append(pExecutionFlow.getEndTime() - pExecutionFlow.getBeginTime());
        tCurrentBuffer.append("\" startTime=\"");
        tCurrentBuffer.append(mFormater.formatDateTime(pExecutionFlow.getBeginTime()));
        tCurrentBuffer.append("\" >\n");
        writeMethodCall(tCurrentBuffer, pExecutionFlow.getFirstMethodCall());
        tCurrentBuffer.append("</Thread>");
        writeToFile(tCurrentBuffer.toString(), false);
        // free memory
        tCurrentBuffer = null;
        if (sLog.isDebugEnabled())
        {
            sLog.debug("Write ExecutionFlow to File " + pExecutionFlow);
        }
    }

    /**
     * Ecrit le log dans le Buffer pour �viter les probm�mes de multithreading si on �crit dans le m�me fichier. Cette
     * m�thode est recursive.
     * 
     * @param pExecutionFlow La racine courante de l'arbre � logger.
     */
    private void writeMethodCall(StringBuilder pBuffer, MethodCallPO pCurrentMethodCall)
    {
        pBuffer.append("<MethodCall ");
        pBuffer.append("class=\"").append(pCurrentMethodCall.getClassName()).append("\" ");
        pBuffer.append("method=\"").append(pCurrentMethodCall.getMethodName()).append("\" ");
        pBuffer.append("duration=\"");
        pBuffer.append(pCurrentMethodCall.getEndTime() - pCurrentMethodCall.getBeginTime());
        pBuffer.append("\" ").append("startTime=\"");
        pBuffer.append(mFormater.formatDateTime(pCurrentMethodCall.getBeginTime()));
        pBuffer.append("\" ");
        pBuffer.append("parameter=\"").append(pCurrentMethodCall.getParams()).append("\" ");
        if (pCurrentMethodCall.getThrowableClass() == null)
        { // The call was Ok
            if (pCurrentMethodCall.getReturnValue() == null)
            { // 'void' method return value
                pBuffer.append("result=\"void\" ");
            } else
            { // Have to log the result
                pBuffer.append("result=\"").append(pCurrentMethodCall.getReturnValue()).append("\" ");
            }
        } else
        { // On log l'exception
            pBuffer.append("throwable=\"").append(pCurrentMethodCall.getThrowableClass()).append("\" ");
            pBuffer.append("throwableMessage=\"").append(pCurrentMethodCall.getThrowableMessage()).append("\" ");
        }
        pBuffer.append(">\n");

        // On fait le recusrif sur les children
        for (MethodCallPO curChild : pCurrentMethodCall.getChildren())
        {
            writeMethodCall(pBuffer, curChild);
        }

        // On ferme le tag
        pBuffer.append("</MethodCall>\n");
    }

    /**
     * @return the mFormater
     */
    public FormaterBean getFormater()
    {
        return mFormater;
    }

    /**
     * @param pFormater the mFormater to set
     */
    public void setFormater(FormaterBean pFormater)
    {
        mFormater = pFormater;
    }
}
