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
    private StringBuilder mCurrentBuffer;

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
        mCurrentBuffer = new StringBuilder();
        mCurrentBuffer.append("<Thread name=\"").append(pExecutionFlow.getThreadName());
        mCurrentBuffer.append("\" server=\"").append(pExecutionFlow).append("\" duration=\"");
        mCurrentBuffer.append(pExecutionFlow.getEndTime() - pExecutionFlow.getBeginTime());
        mCurrentBuffer.append("\" startTime=\"");
        mCurrentBuffer.append(mFormater.formatDateTime(pExecutionFlow.getBeginTime()));
        mCurrentBuffer.append("\" >\n");
        writeMethodCall(pExecutionFlow.getFirstMethodCall());
        mCurrentBuffer.append("</Thread>");
        writeToFile(mCurrentBuffer.toString(), false);
        // free memory
        mCurrentBuffer = null;
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
    private void writeMethodCall(MethodCallPO pCurrentMethodCall)
    {
        mCurrentBuffer.append("<MethodCall ");
        mCurrentBuffer.append("class=\"").append(pCurrentMethodCall.getClassName()).append("\" ");
        mCurrentBuffer.append("method=\"").append(pCurrentMethodCall.getMethodName()).append("\" ");
        mCurrentBuffer.append("duration=\"");
        mCurrentBuffer.append(pCurrentMethodCall.getEndTime() - pCurrentMethodCall.getBeginTime());
        mCurrentBuffer.append("\" ").append("startTime=\"");
        mCurrentBuffer.append(mFormater.formatDateTime(pCurrentMethodCall.getBeginTime()));
        mCurrentBuffer.append("\" ");
        mCurrentBuffer.append("parameter=\"").append(pCurrentMethodCall.getParams()).append("\" ");
        if (pCurrentMethodCall.getThrowableClass() == null)
        { // Le retour de cette m�thode s'est bien pass�
            if (pCurrentMethodCall.getReturnValue() == null)
            { // M�thode de type 'void'
                mCurrentBuffer.append("result=\"void\" ");
            } else
            { // On log le retour
                mCurrentBuffer.append("result=\"").append(pCurrentMethodCall.getReturnValue()).append("\" ");
            }
        } else
        { // On log l'exception
            mCurrentBuffer.append("throwable=\"").append(pCurrentMethodCall.getThrowableClass()).append("\" ");
            mCurrentBuffer.append("throwableMessage=\"").append(pCurrentMethodCall.getThrowableMessage()).append("\" ");
        }
        mCurrentBuffer.append(">\n");

        // On fait le recusrif sur les children
        for (MethodCallPO curChild : pCurrentMethodCall.getChildren())
        {
            writeMethodCall(curChild);
        }

        // On ferme le tag
        mCurrentBuffer.append("</MethodCall>\n");
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
