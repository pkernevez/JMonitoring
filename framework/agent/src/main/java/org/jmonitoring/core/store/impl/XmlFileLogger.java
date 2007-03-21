package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.store.IStoreWriter;

/**
 * This class allow the logging with an XMLFile. This class is not ThreadSafe when multiple file is used.
 * 
 * @see org.jmonitoring.core.configuration.Configuration.SAME_FILE_FOR_ALL_THREAD
 * 
 * @author pke
 */
public class XmlFileLogger implements IStoreWriter
{
    private static final String XML_DIR_NAME = "xml.logger.dir";

    private static final String XML_FILE_PER_THREAD = "xml.file.per.thread";

    private StringBuffer mCurrentBuffer;

    private static boolean sIsInitialized = false;

    /** Permet de loguer dans un seul fichier quand il est partagé. */
    private static Writer sCommonFileWriter;

    /** Permet de loguer dans un fichier spécifique à chaque instance. */
    private final Writer mLogFile;

    private static Log sLog = LogFactory.getLog(XmlFileLogger.class);

    private static synchronized void init()
    {
        if (!sIsInitialized)
        {
            String tDirName = ConfigurationHelper.getInstance().getString(XML_DIR_NAME, ".");
            File tLogDir = new File(tDirName);
            // On netoie tous les fichiers du repertoire
            if (!tLogDir.isDirectory())
            { // Repertoire invalide
                throw new MeasureException("The log directory isn't valid [" + tLogDir.getAbsolutePath() + "]");
            }
            File[] tFileList = tLogDir.listFiles();
            for (int i = 0; i < tFileList.length; i++)
            {
                if (!tFileList[i].delete())
                {
                    throw new MeasureException("Unable to delete file[" + tFileList[i].getAbsolutePath() + "]");
                }
            }
            if (!ConfigurationHelper.getInstance().getBoolean(XML_FILE_PER_THREAD))
            { // On initalise le fichier commun
                File tCommonFile = new File(tDirName + "/AllThread.xml");
                try
                {
                    sCommonFileWriter = new FileWriter(tCommonFile);
                    writeToAllThreadFile("<Threads>");
                    Runtime.getRuntime().addShutdownHook(new Thread()
                    { // On
                            // ferme le tag en sortant
                            public void run()
                            {
                                writeToAllThreadFile("</Threads>");
                                try
                                {
                                    sCommonFileWriter.flush();
                                } catch (IOException e)
                                {
                                    throw new MeasureException("Unable to flush stream for logging.", e);
                                }
                            }
                        });
                } catch (IOException e)
                {
                    throw new MeasureException("Unable to open stream for logging.", e);
                }
            }
            sIsInitialized = true;
        }
    }

    /**
     * Default constructor.
     */
    public XmlFileLogger()
    {
        if (!sIsInitialized)
        { // Premier passage
            init();
        }
        if (ConfigurationHelper.getInstance().getBoolean(XML_FILE_PER_THREAD))
        { // On initalise un fichier pour ce Thread
            File tFile = new File(ConfigurationHelper.getInstance().getString(XML_DIR_NAME, ".") + "/Thread."
                + Thread.currentThread().getName() + ".xml");
            try
            {
                mLogFile = new FileWriter(tFile);
                writeToFile("<Threads>");
                Runtime.getRuntime().addShutdownHook(new Thread()
                { // On ferme le tag en sortant
                        public void run()
                        {
                            writeToFile("</Threads>");
                            flush();
                        }
                    });
            } catch (IOException e)
            {
                throw new MeasureException("Unable to create LogFile for Thread [" + Thread.currentThread().getName()
                    + "] [" + tFile.getAbsolutePath() + "]");
            }
        } else
        {
            mLogFile = null;
        }

    }

    private void writeToFile(String pMessage)
    {
        if (!ConfigurationHelper.getInstance().getBoolean(XML_FILE_PER_THREAD))
        {
            writeToAllThreadFile(pMessage);
        } else
        {
            try
            {
                mLogFile.write(pMessage);
            } catch (IOException e)
            {
                throw new MeasureException("Unable to write into LogFile for Thread ["
                    + Thread.currentThread().getName() + "]");
            }
        }
    }

    private void flush()
    {
        try
        {
            mLogFile.flush();
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write into LogFile for Thread [" + Thread.currentThread().getName()
                + "]");
        }

    }

    private static synchronized void writeToAllThreadFile(String pMessage)
    {
        try
        {
            sCommonFileWriter.write(pMessage);
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write into LogFile for Thread [" + Thread.currentThread().getName()
                + "]");
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.log.IStoreWriter#writeExecutionFlow( org.jmonitoring.core.dto.ExecutionFlow)
     */
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        mCurrentBuffer = new StringBuffer();
        mCurrentBuffer.append("<Thread name=\"").append(pExecutionFlow.getThreadName());
        mCurrentBuffer.append("\" server=\"").append(pExecutionFlow).append("\" duration=\"");
        mCurrentBuffer.append(pExecutionFlow.getEndTime() - pExecutionFlow.getBeginTime());
        mCurrentBuffer.append("\" startTime=\"");
        mCurrentBuffer.append(ConfigurationHelper.formatDateTime(pExecutionFlow.getBeginTime()));
        mCurrentBuffer.append("\" >\n");
        fillStringBuffer(pExecutionFlow.getFirstMethodCall());
        mCurrentBuffer.append("</Thread>");
        writeToFile(mCurrentBuffer.toString());
        // On libère la mémoire
        mCurrentBuffer = null;
        sLog.info("Wrtit ExecutionFlow to File " + pExecutionFlow);

    }

    /**
     * Ecrit le log dans le Buffer pour éviter les probmèmes de multithreading si on écrit dans le même fichier. Cette
     * méthode est recursive.
     * 
     * @param pExecutionFlow La racine courante de l'arbre à logger.
     */
    private void fillStringBuffer(MethodCallPO pCurrentMethodCall)
    {
        mCurrentBuffer.append("<MethodCall ");
        mCurrentBuffer.append("class=\"").append(pCurrentMethodCall.getClassName()).append("\" ");
        mCurrentBuffer.append("method=\"").append(pCurrentMethodCall.getMethodName()).append("\" ");
        mCurrentBuffer.append("duration=\"");
        mCurrentBuffer.append(pCurrentMethodCall.getEndTime() - pCurrentMethodCall.getBeginTime());
        mCurrentBuffer.append("\" ").append("startTime=\"");
        mCurrentBuffer.append(ConfigurationHelper.formatDateTime(pCurrentMethodCall.getBeginTime()));
        mCurrentBuffer.append("\" ");
        if (ConfigurationHelper.getInstance().getBoolean("log.parameter.defaultvalue", true))
        { // On log tous les paramètres
            mCurrentBuffer.append("parameter=\"").append(pCurrentMethodCall.getParams()).append("\" ");
            if (pCurrentMethodCall.getThrowableClass() == null)
            { // Le retour de cette méthode s'est bien passé
                if (pCurrentMethodCall.getReturnValue() == null)
                { // Méthode de type 'void'
                    mCurrentBuffer.append("result=\"void\" ");
                } else
                { // On log le retour
                    mCurrentBuffer.append("result=\"").append(pCurrentMethodCall.getReturnValue()).append("\" ");
                }
            } else
            { // On log l'exception
                mCurrentBuffer.append("throwable=\"").append(pCurrentMethodCall.getThrowableClass()).append("\" ");
                mCurrentBuffer.append("throwableMessage=\"").append(pCurrentMethodCall.getThrowableMessage()).append(
                    "\" ");
            }
        } else
        { // On log que le type de retour
            if (pCurrentMethodCall.getThrowableClass() == null)
            { // Le retour de cette méthode s'est bien passé
                mCurrentBuffer.append("returnType=\"Ok\"");
            } else
            {
                mCurrentBuffer.append("returnType=\"Throwable\"");
            }
        }
        mCurrentBuffer.append(">\n");

        // On fait le recusrif sur les children
        MethodCallPO curChild;
        for (Iterator tChildIterator = pCurrentMethodCall.getChildren().iterator(); tChildIterator.hasNext();)
        {
            curChild = (MethodCallPO) tChildIterator.next();
            fillStringBuffer(curChild);
        }

        // On ferme le tag
        mCurrentBuffer.append("</MethodCall>\n");
    }
}
