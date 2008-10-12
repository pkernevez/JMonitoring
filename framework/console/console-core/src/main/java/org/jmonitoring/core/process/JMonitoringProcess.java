package org.jmonitoring.core.process;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringProcess
{
    private static Log sLog = LogFactory.getLog(JMonitoringProcess.class);

    JMonitoringProcess()
    {
    }

    /**
     * For testing purpose.
     * 
     * @return A nHibernate Session.
     */
    Session getASession()
    {
        return HibernateManager.getSession();
    }

    public boolean doDatabaseExist()
    {
        try
        {
            ConsoleDao tDao = new ConsoleDao();
            tDao.countFlows();
            return true;
        } catch (SQLGrammarException t)
        {
            return false;
        }

    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        try
        {
            ConsoleDao tDao = new ConsoleDao();
            tDao.deleteFlow(pId);
        } catch (RuntimeException t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            throw t;
        }
    }

    public void deleteAllFlows()
    {
        try
        {
            ConsoleDao tDao = new ConsoleDao();
            tDao.deleteAllFlows();
        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
        }
    }

    public MethodCallDTO readFullMethodCall(int pFlowId, int pId)
    {
        sLog.debug("Read method call from database, Id=[" + pId + "]");
        ConsoleDao tDao = new ConsoleDao();
        MethodCallPO tMethodCallPo = tDao.readMethodCall(pFlowId, pId);
        return DtoHelper.getFullMethodCallDto(tMethodCallPo, -1);
    }

    public ExecutionFlowDTO readFullExecutionFlow(int pId)
    {
        sLog.debug("Read flow from database, Id=[" + pId + "]");
        ConsoleDao tDao = new ConsoleDao();
        ExecutionFlowPO tFlowPo = tDao.readExecutionFlow(pId);
        return DtoHelper.getDeepCopy(tFlowPo);
    }

    public List<ExecutionFlowDTO> getListOfExecutionFlowDto(FlowSearchCriterion pCriterion)
    {
        List<ExecutionFlowDTO> tList = new ArrayList<ExecutionFlowDTO>();
        ConsoleDao tDao = new ConsoleDao();
        for (ExecutionFlowPO tFlow : tDao.getListOfExecutionFlowPO(pCriterion))
        {
            tList.add(DtoHelper.getSimpleCopy(tFlow));
        }
        return tList;
    }

    public MethodCallDTO readMethodCall(int pFlowId, int pMethodCallId)
    {
        ConsoleDao tDao = new ConsoleDao();
        MethodCallPO tMethod = tDao.readMethodCall(pFlowId, pMethodCallId);
        return DtoHelper.simpleCopy(tMethod, -1);
    }

    public List<MethodCallDTO> getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        ConsoleDao tDao = new ConsoleDao();
        List<MethodCallPO> tResult = tDao.getListOfMethodCall(pClassName, pMethodName);
        return DtoHelper.simpleCopyListOfMethodPO(tResult);
    }

    // TODO Remplacer par un bean Spring !
    public List<MethodCallExtractDTO> getListOfMethodCallExtract()
    {
        ConsoleDao tDao = new ConsoleDao();
        return tDao.getListOfMethodCallExtract();
    }

    // TODO Remplacer par un bean Spring !
    public List<MethodCallFullExtractDTO> getListOfMethodCallFullExtract(String pClassName, String pMethodName,
                    long pDurationMin, long pDurationMax)
    {
        ConsoleDao tDao = new ConsoleDao();
        List<MethodCallPO> tListOfMethodCall = tDao.getMethodCallList(pClassName, pMethodName, pDurationMin,
                        pDurationMax);
        return DtoHelper.copyListMethodCallFullExtract(tListOfMethodCall);
    }

    public void createDataBase()
    {
        ConsoleDao tDao = new ConsoleDao();
        tDao.createDataBase();
    }

    /**
     * Serialize a full ExecutionFlow as Xml/GZip bytes.
     * 
     * @param pFlow The flow to serialize.
     * @return The bytes of a GZip.
     */
    public byte[] convertFlowToXml(ExecutionFlowDTO pFlow)
    {
        ByteArrayOutputStream tOutput = new ByteArrayOutputStream(10000);
        GZIPOutputStream tZipStream;
        try
        {
            tZipStream = new GZIPOutputStream(tOutput);
            XMLEncoder tEncoder = new XMLEncoder(tZipStream);
            tEncoder.writeObject(pFlow);
            tEncoder.close();
            return tOutput.toByteArray();
        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

    /**
     * Concert an GZip/Xml serialized ExecutionFlow as an ExecutionFLow Object.
     * 
     * @param pFlowAsXml The GZip bytes.
     * @return The ExecutionFLow.
     */
    public ExecutionFlowDTO convertFlowFromXml(byte[] pFlowAsXml)
    {
        InputStream tInput = new ByteArrayInputStream(pFlowAsXml);
        try
        {
            GZIPInputStream tZipStream = new GZIPInputStream(tInput);
            XMLDecoder tDecoder = new XMLDecoder(tZipStream);
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            return (ExecutionFlowDTO) tResult;
        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

    public ExecutionFlowDTO insertFlowFromXml(byte[] pFlowAsXml)
    {
        ConsoleDao tDao = new ConsoleDao();
        ExecutionFlowDTO tFlowDto = convertFlowFromXml(pFlowAsXml);
        ExecutionFlowPO tFlowPO = DtoHelper.getDeepCopy(tFlowDto);
        tFlowPO.setId(-1);
        tDao.insertFullExecutionFlow(tFlowPO);
        return DtoHelper.getDeepCopy(tFlowPO);
    }
}
