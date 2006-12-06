package org.jmonitoring.core.process;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringProcess
{
    private static Log sLog = LogFactory.getLog(JMonitoringProcess.class);

    JMonitoringProcess()
    {
    }

    public boolean doDatabaseExist()
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.countFlows();
            return true;
        } catch (SQLGrammarException t)
        {
            return false;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }

    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        Session tSession = null;
        Transaction tTransaction = null;
        try
        {
            tSession = HibernateManager.getSession();
            tTransaction = tSession.beginTransaction();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteFlow(pId);
            tTransaction.commit();

        } catch (RuntimeException t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            if (tTransaction != null)
            {
                tTransaction.rollback();
            }
            throw t;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public void deleteAllFlows()
    {
        Session tSession = null;
        Transaction tTransaction = null;
        try
        {
            tSession = HibernateManager.getSession();
            tTransaction = tSession.beginTransaction();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteAllFlows();
            tTransaction.commit();

        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            if (tTransaction != null)
            {
                tTransaction.rollback();
            }
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public MethodCallDTO readFullMethodCall(int pFlowId, int pId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            sLog.debug("Read method call from database, Id=[" + pId + "]");
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            MethodCallPO tMethodCallPo = tDao.readMethodCall(pFlowId, pId);
            return DtoHelper.getFullMethodCallDto(tMethodCallPo, -1);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public ExecutionFlowDTO readFullExecutionFlow(int pId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            sLog.debug("Read flow from database, Id=[" + pId + "]");
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            ExecutionFlowPO tFlowPo = tDao.readExecutionFlow(pId);
            return DtoHelper.getDeepCopy(tFlowPo);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfExecutionFlowDto(FlowSearchCriterion pCriterion)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            List tList = new ArrayList();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            for (Iterator tIt = tDao.getListOfExecutionFlowPO(pCriterion).iterator(); tIt.hasNext();)
            {
                tList.add(DtoHelper.getSimpleCopy((ExecutionFlowPO) tIt.next()));
            }
            return tList;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public MethodCallDTO readMethodCall(int pFlowId, int pMethodCallId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            MethodCallPO tMethod = tDao.readMethodCall(pFlowId, pMethodCallId);
            return DtoHelper.simpleCopy(tMethod, -1);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }

    }

    public List getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            List tResult = tDao.getListOfMethodCall(pClassName, pMethodName);
            return DtoHelper.simpleCopyListOfMethodPO(tResult);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfMethodCallExtract()
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            return tDao.getListOfMethodCallExtract();
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfMethodCallFullExtract(String pClassName, String pMethodName, long pDurationMin,
                    long pDurationMax)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            List tListOfMethodCall = tDao.getMethodCallList(pClassName, pMethodName, pDurationMin, pDurationMax);
            return DtoHelper.copyListMethodCallFullExtract(tListOfMethodCall);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public void createDataBase()
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.createDataBase();
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    /**
     * Serialize a full ExecutionFlow as Xml/GZip bytes.
     * 
     * @param pFlow The flow to serialize.
     * @return The bytes of a GZip.
     */
    public byte[] getFlowAsXml(ExecutionFlowDTO pFlow)
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
    public ExecutionFlowDTO getFlowFromXml(byte[] pFlowAsXml)
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
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            ExecutionFlowDTO tFlowDto = getFlowFromXml(pFlowAsXml);
            ExecutionFlowPO tFlowPO = DtoHelper.getDeepCopy(tFlowDto);
            tFlowPO.setId(-1);
            tDao.insertFullExecutionFlow(tFlowPO);
            return DtoHelper.getDeepCopy(tFlowPO);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

}
