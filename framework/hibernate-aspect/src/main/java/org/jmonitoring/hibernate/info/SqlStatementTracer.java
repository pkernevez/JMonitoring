package org.jmonitoring.hibernate.info;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.impl.ToStringParametersTracer;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SqlStatementTracer implements IResultTracer
{
    Log sLog = LogFactory.getLog(SqlStatementTracer.class);

    IParamaterTracer mParamTracer = new ToStringParametersTracer();

    public String convertToString(Object pTarget, Object pResult)
    {
        String tResult;
        if (pTarget == null) 
        {
            tResult = "Unable to log this Statement class= NULL";
            sLog.warn(tResult);
        } else if (pTarget instanceof IProxyStatement)
        {
            tResult = ((IProxyStatement) pTarget).getTrace();
        } else
        {
            tResult = "Unable to log this Statement class=" + pTarget.getClass().getName();
            sLog.warn(tResult);
        }
        return tResult;
    }
}