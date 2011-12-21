package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.jmonitoring.agent.store.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

/**
 * Generic driver for logging sql access. URL are like "jmonitoring:<URL>" Where * DriverName is the real driver class *
 * URL is the real url For exemple with Oracle, URL should be like :
 * "jmonitoring:jdbc:oracle:thin:@localhost:1521:INSAPOR0" This mecanism is quite complexe but the real driver can't be
 * pass by the url, as the URL are not provided during registering and that the same Driver may be use with several
 * database connexion inside the same JVM.
 * 
 * 
 * TODO Document How to do when you want to use specifi driver Aka OracleDriver and not only the jdbc interface (exemple
 * : for using blob)
 * 
 * @author pke
 * 
 */
public abstract class GenericDriver implements Driver
{
    private static final String JMONITORING = "jmonitoring:";

    private static Logger sLog = LoggerFactory.getLogger(GenericDriver.class);

    Driver realDriver;

    private final String groupName;

    private final Filter methodCallFilter;

    /**
     * @param pRealDriver The real DB driver use by this delegating driver.
     * @param pGroupName The group name use to log DB access.
     * @param pFilter The filter that may be applied by <code>MethodCall</code> created by this SLQ interceptor.
     */
    public GenericDriver(Driver pRealDriver, String pGroupName, Filter pFilter)
    {
        realDriver = pRealDriver;
        groupName = pGroupName;
        methodCallFilter = pFilter;
    }

    public GenericDriver(Driver pRealDriver, String pGroupName)
    {
        // if (pRealDriverClass!=null && pRealDriverClass.length()>0){
        // realDriver = Class.forName(pRealDriverClass);
        // } else{
        // sLog.error("Invalid configuration, you should provide the real driver class in you jmonitoring-driver.properties file");
        // }
        // ;
        this(pRealDriver, pGroupName, null);
    }

    public GenericDriver(Driver pRealDriver)
    {
        this(pRealDriver, "Jdbc", null);
    }

    @SuppressWarnings("unchecked")
    private String getRealUrl(String pUrl) throws SQLException
    {
        if (pUrl != null && pUrl.startsWith(JMONITORING))
        {
            return pUrl.substring(JMONITORING.length());
        } else
        {
            throw new RuntimeException("Invalid URL, it must start with [jmonitoring:] but was [" + pUrl + "]");
        }
        // int tUrlStartPosition = pUrl.indexOf(":", tDriverStartPosition);
        // if (0 < tDriverStartPosition && tDriverStartPosition < tUrlStartPosition)
        // {
        // String tRealDriverClassName = pUrl.substring(tDriverStartPosition + 1, tDriverStartPosition);
        // try
        // {
        // Connector tResult = new Connector();
        // tResult.realDriver = tClass.newInstance();
        // DriverManager.registerDriver(this);
        // tResult.url = pUrl.substring(tDriverStartPosition + 1, pUrl.length());
        // sLog.info("The driver [" + tClass + "] has been instanciated and initialized with url ["
        // + tResult.url);
        // return tResult;
        // } catch (ClassNotFoundException e)
        // {
        // sLog.error("Cannot register the driver [" + tRealDriverClassName + "] with the Manager ");
        // throw new RuntimeException("Cannot register the driver [" + tRealDriverClassName
        // + "] with the Manager ", e);
        // } catch (InstantiationException e)
        // {
        // sLog.error("Cannot register the driver [" + tRealDriverClassName + "] with the Manager ");
        // throw new RuntimeException("Cannot register the driver [" + tRealDriverClassName
        // + "] with the Manager ", e);
        // } catch (IllegalAccessException e)
        // {
        // sLog.error("Cannot register the driver [" + tRealDriverClassName + "] with the Manager ");
        // throw new RuntimeException("Cannot register the driver [" + tRealDriverClassName
        // + "] with the Manager ", e);
        // }
        // }
        // }
        // return null;
    }

    /**
     * @param pUrl
     * @return
     * @throws SQLException
     * @see java.sql.Driver#acceptsURL(java.lang.String)
     */
    public boolean acceptsURL(String pUrl) throws SQLException
    {
        return pUrl != null && pUrl.startsWith(JMONITORING) && realDriver.acceptsURL(getRealUrl(pUrl));
    }

    /**
     * @param pUrl
     * @param pInfo
     * @return
     * @throws SQLException
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
     */
    public Connection connect(String pUrl, Properties pInfo) throws SQLException
    {
        if (acceptsURL(pUrl))
        {
            return new GenericConnection(realDriver.connect(getRealUrl(pUrl), pInfo), groupName, methodCallFilter);
        } else
        {
            return null;
        }
    }

    /**
     * @return
     * @see java.sql.Driver#getMajorVersion()
     */
    public int getMajorVersion()
    {
        return realDriver.getMajorVersion();
    }

    /**
     * @return
     * @see java.sql.Driver#getMinorVersion()
     */
    public int getMinorVersion()
    {
        return realDriver.getMinorVersion();
    }

    /**
     * @param pUrl
     * @param pInfo
     * @return
     * @throws SQLException
     * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
     */
    public DriverPropertyInfo[] getPropertyInfo(String pUrl, Properties pInfo) throws SQLException
    {
        return realDriver.getPropertyInfo(pUrl, pInfo);
    }

    /**
     * @return
     * @see java.sql.Driver#jdbcCompliant()
     */
    public boolean jdbcCompliant()
    {
        return realDriver.jdbcCompliant();
    }

    protected static void registerDriver(Driver pDriver)
    {
        try
        {
            DriverManager.registerDriver(pDriver);
        } catch (SQLException sqlexception)
        {
            sLog.error("Unable to register GenericDriver", sqlexception);
            throw new RuntimeException("Unable to register GenericDriver", sqlexception);
        }
    }
}
