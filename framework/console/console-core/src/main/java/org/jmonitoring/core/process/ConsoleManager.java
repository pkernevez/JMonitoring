package org.jmonitoring.core.process;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.SQLGrammarException;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.DtoManager;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;
import org.jmonitoring.core.dto.v2.DtoManagerV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConsoleManager
{
    private static Logger sLog = LoggerFactory.getLogger(ConsoleManager.class);

    @Resource(name = "dtoManager")
    private DtoManager dtoManager;

    @Resource(name = "dtoManagerV2")
    private DtoManagerV2 dtoManagerV2;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    enum Version {
        V2,V3
    }

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

    public List<ExecutionFlowDTO> getListOfExecutionFlowDto(FlowSearchCriterion pCriterion)
    {
        List<ExecutionFlowDTO> tList = new ArrayList<ExecutionFlowDTO>();
        for (ExecutionFlowPO tFlow : mDao.getListOfExecutionFlowPO(pCriterion))
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
        return mDao.getListOfMethodCallExtractOld();
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
     * @param pFlowAsXml The bytes.
     * @return The ExecutionFLow.
     */
    protected ExecutionFlowPO convertFlowFromXml(byte[] pFlowAsXml)
    {
        Version version = Version.V3;
        try {
            String msg = new String(pFlowAsXml, "UTF-8");
            if (msg.contains("<void property=\"beginDateAsString\">")){
                version = Version.V2;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Bad encoding !");
        }

        if (version == Version.V2) { // may be a v2 export
            sLog.info("Try to parse v2 export");
            return dtoManagerV2.loadV2Export(pFlowAsXml);
        } else {
            XMLDecoder tDecoder = new XMLDecoder(new ByteArrayInputStream(pFlowAsXml));
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            if (tResult instanceof ExecutionFlowDTO) {
                ExecutionFlowDTO tResult2 = (ExecutionFlowDTO) tResult;
                ExecutionFlowPO tFlow = dtoManager.getDeepCopy(tResult2);
                tFlow.setEndTime(tFlow.getBeginTime() + tResult2.getDuration());
                return tFlow;
            } else if (tResult instanceof ExecutionFlowPO) {
                ExecutionFlowPO tFlow = (ExecutionFlowPO) tResult;
                tFlow.setId(-1);
                return tFlow;
            } else {
                throw new RuntimeException("invalid class in gzip file: " + tResult.getClass().getName());
            }
        }
    }

    public ExecutionFlowDTO insertFlowFromXml(byte[] pFlowAsXml)
    {
        try {
            byte[] raw = ungzip(pFlowAsXml);

            // V3
            ExecutionFlowPO tFlow = convertFlowFromXml(raw);



            mDao.insertFullExecutionFlow(tFlow);
            return dtoManager.getDeepCopy(tFlow);
        } catch (IOException e) {
            throw new RuntimeException("Unable to unzip stream");
        }


    }

    byte[] ungzip(byte[] pFlowAsXml) throws IOException {
        InputStream tInput = new ByteArrayInputStream(pFlowAsXml);
        GZIPInputStream tZipStream = new GZIPInputStream(tInput);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int nRead;
        byte[] buffer = new byte[1024];
        while ((nRead = tZipStream.read(buffer, 0, buffer.length)) != -1) {
            result.write(buffer, 0, nRead);
        }

        return result.toByteArray();
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
