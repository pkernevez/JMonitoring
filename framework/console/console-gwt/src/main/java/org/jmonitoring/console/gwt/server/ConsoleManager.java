package org.jmonitoring.console.gwt.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.SQLGrammarException;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallExtractDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallFullExtractDTO;
import org.jmonitoring.console.gwt.client.executionflow.SearchCriteria;
import org.jmonitoring.console.gwt.server.dto.DtoManager;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConsoleManager
{
    private static Logger sLog = LoggerFactory.getLogger(ConsoleManager.class);

    @Resource(name = "dtoManager")
    private DtoManager dtoManager;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Resource(name = "formater")
    private FormaterBean mFormater;

    ConsoleManager()
    {
    }

    public boolean doDatabaseExist()
    {
        try
        {
            mDao.countFlows();
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
            mDao.deleteFlow(pId);
        } catch (RuntimeException t)
        {
            sLog.error("Unable to Execute Action" + t);
            throw t;
        }
    }

    public void deleteAllFlows()
    {
        try
        {
            mDao.deleteAllFlows();
        } catch (Throwable t)
        {
            sLog.error("Unable to Execute Action" + t);
        }
    }

    public MethodCallDTO readFullMethodCall(int pFlowId, int pId)
    {
        sLog.debug("Read method call from database, Id=[" + pId + "]");
        MethodCallPO tMethodCallPo = mDao.readMethodCall(pFlowId, pId);
        return dtoManager.getFullMethodCallDto(tMethodCallPo, -1);
    }

    public ExecutionFlowDTO readFullExecutionFlow(int pId)
    {
        sLog.debug("Read flow from database, Id=[" + pId + "]");
        ExecutionFlowPO tFlowPo = mDao.readExecutionFlow(pId);
        return dtoManager.getDeepCopy(tFlowPo);
    }

    public List<ExecutionFlowDTO> getListOfExecutionFlowDto(SearchCriteria pCriterion)
    {
        FlowSearchCriterion tCrit = new FlowSearchCriterion();
        tCrit.setBeginDate(mFormater.parseDate(pCriterion.getBeginDate()));
        tCrit.setClassName(pCriterion.getClassName());
        String tMinimumDuration = pCriterion.getMinimumDuration();
        if (tMinimumDuration != null && tMinimumDuration.length() > 0)
        {
            tCrit.setDurationMin(Long.parseLong(tMinimumDuration));
        }
        tCrit.setGroupName(pCriterion.getGroupName());
        tCrit.setMethodName(pCriterion.getMethodName());
        tCrit.setJVM(pCriterion.getServer());
        tCrit.setThreadName(pCriterion.getThreadName());
        List<ExecutionFlowDTO> tList = new ArrayList<ExecutionFlowDTO>();
        for (ExecutionFlowPO tFlow : mDao.getListOfExecutionFlowPO(tCrit))
        {
            tList.add(dtoManager.getSimpleCopy(tFlow));
        }
        return tList;
    }

    public MethodCallDTO readMethodCall(int pFlowId, int pMethodCallId)
    {
        MethodCallPO tMethod = mDao.readMethodCall(pFlowId, pMethodCallId);
        return dtoManager.simpleCopy(tMethod, -1);
    }

    public List<MethodCallDTO> getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        List<MethodCallPO> tResult = mDao.getListOfMethodCall(pClassName, pMethodName);
        return dtoManager.simpleCopyListOfMethodPO(tResult);
    }

    public List<MethodCallExtractDTO> getListOfMethodCallExtract()
    {
        List<MethodCallExtractDTO> tResult = new ArrayList<MethodCallExtractDTO>();
        for (Object[] tExtract : mDao.getListOfMethodCallExtract())
        {
            tResult.add(new MethodCallExtractDTO((String) tExtract[ConsoleDao.EXTRACT_CLASSNAME_POS],
                                                 (String) tExtract[ConsoleDao.EXTRACT_METHODNAME_POS],
                                                 (String) tExtract[ConsoleDao.EXTRACT_GROUPNAME_POS],
                                                 ((Long) tExtract[ConsoleDao.EXTRACT_NB_POS]).intValue()));
        }
        return tResult;
    }

    public Map<Integer, MethodCallDTO> getListOfMethodCall(int pFlowId, List<Integer> pIds)
    {
        Map<Integer, MethodCallDTO> tResult = new HashMap<Integer, MethodCallDTO>();
        for (MethodCallPO tMeth : mDao.getMethodCallList(pFlowId, pIds))
        {
            tResult.put(tMeth.getPosition(), dtoManager.getMethodCallDto(tMeth, 2));
        }
        return tResult;
    }

    public List<MethodCallDTO> getListOfMethodCall(int pFlowId)
    {
        return dtoManager.copyListOfMethodPO(mDao.readExecutionFlow(pFlowId).getFirstMethodCall().getChildren(), 2);
    }

    public List<MethodCallFullExtractDTO> getListOfMethodCallFullExtract(String pClassName, String pMethodName,
        long pDurationMin, long pDurationMax)
    {
        List<MethodCallPO> tListOfMethodCall =
            mDao.getMethodCallList(pClassName, pMethodName, pDurationMin, pDurationMax);
        return dtoManager.copyListMethodCallFullExtract(tListOfMethodCall);
    }

    public void createDataBase()
    {
        mDao.createDataBase();
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
        ExecutionFlowDTO tFlowDto = convertFlowFromXml(pFlowAsXml);
        ExecutionFlowPO tFlowPO = dtoManager.getDeepCopy(tFlowDto);
        tFlowPO.setId(-1);
        mDao.insertFullExecutionFlow(tFlowPO);
        return dtoManager.getDeepCopy(tFlowPO);
    }

    public MethodCallDTO readPrevMethodCall(int pFlowId, int pPosition, String pGroupName)
    {
        MethodCallPO tMethPo = mDao.getPrevInGroup(pFlowId, pPosition, pGroupName);
        if (tMethPo == null)
        {
            throw new ObjectNotFoundException(pFlowId, MethodCallPO.class.getName());
        }
        return dtoManager.getFullMethodCallDto(tMethPo, -1);
    }

    public MethodCallDTO readNextMethodCall(int pFlowId, int pPosition, String pGroupName)
    {
        MethodCallPO tMethPo = mDao.getNextInGroup(pFlowId, pPosition, pGroupName);
        if (tMethPo == null)
        {
            throw new ObjectNotFoundException(pFlowId, MethodCallPO.class.getName());
        }
        return dtoManager.getFullMethodCallDto(tMethPo, -1);
    }

}
