package org.jmonitoring.core.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jmonitoring.core.configuration.Configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class ExecutionFlowDaoFactory
{
    private static Constructor sDaoConstructor;

    private static Log sLog = LogFactory.getLog(ExecutionFlowDaoFactory.class);

    private ExecutionFlowDaoFactory()
    {
    }

    /** For testing purpose. */
    public static IExecutionFlowDAO getExecutionFlowDao(Session pSession)
    {
        Constructor tCons = sDaoConstructor;
        IExecutionFlowDAO tResult;
        if (tCons == null)
        {
            tCons = initDaoContructor();
        }
        try
        {
            tResult = (IExecutionFlowDAO) tCons.newInstance(new Object[] {pSession });
        } catch (IllegalArgumentException e)
        {
            sLog.fatal("Unable to invoke the ExecutionFlowDAO constructor with Session parameter");
            throw new RuntimeException(e);
        } catch (InstantiationException e)
        {
            sLog.fatal("Unable to invoke the ExecutionFlowDAO constructor with Session parameter");
            throw new RuntimeException(e);
        } catch (IllegalAccessException e)
        {
            sLog.fatal("Unable to invoke the ExecutionFlowDAO constructor with Session parameter");
            throw new RuntimeException(e);
        } catch (InvocationTargetException e)
        {
            sLog.fatal("Unable to invoke the ExecutionFlowDAO constructor with Session parameter");
            throw new RuntimeException(e);
        }
        return tResult;
    }

    private static Constructor initDaoContructor()
    {
        Class tClass = Configuration.getInstance().getExecutionFlowDaoClass();
        Constructor tCons = null;
        try
        {
            tCons = tClass.getConstructor(new Class[] {Session.class });
        } catch (SecurityException e)
        {
            sLog.error("The specified DaoClass should have a public constructor, using class : ExecutionFlowDAO");
        } catch (NoSuchMethodException e)
        {
            sLog.error("The specified DaoClass should have a constructor with "
                + "Session parameter, using class : ExecutionFlowDAO");
        }
        if (tCons == null)
        {
            try
            {
                tCons = ExecutionFlowDAO.class.getConstructor(new Class[] {Session.class });
            } catch (SecurityException e)
            {
                sLog.fatal("ExecutionFlowDAO should have a constructor with Session parameter");
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e)
            {
                sLog.fatal("ExecutionFlowDAO should have a constructor with Session parameter");
                throw new RuntimeException(e);
            }
        }
        sDaoConstructor = tCons;
        return tCons;
    }
}
