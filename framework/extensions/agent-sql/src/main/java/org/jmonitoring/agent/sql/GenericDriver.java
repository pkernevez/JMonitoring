package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class GenericDriver implements Driver
{
    private static final String JMONITORING = "jmonitoring";

    private static Logger sLog = LoggerFactory.getLogger(GenericDriver.class);

    private Driver mRealDriver;

    private String mUrl;

    public GenericDriver()
    {
        this("oracle.jdbc.OracleDriver","jdbc:oracle:thin:@localhost:1521:INSAPOR0" );
    }
    
    public GenericDriver(String pDriverClassName, String pUrl)
    {
        super();
        try
        {
            mUrl = pUrl;
            Class<Driver> tClass = (Class<Driver>) Class.forName(pDriverClassName);
            mRealDriver = tClass.newInstance();
            DriverManager.println("Registering the CrossAccess JDBC Driver");
            try
            {
                DriverManager.registerDriver(this);
            } catch (SQLException e)
            {
                sLog.error("Cannot register the driver with the Manager ");
            }
            DriverManager.println("Registered");
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to load real driver class:" + pDriverClassName, e);
        }

    }

    /**
     * @param pUrl
     * @return
     * @throws SQLException
     * @see java.sql.Driver#acceptsURL(java.lang.String)
     */
    public boolean acceptsURL(String pUrl) throws SQLException
    {
        if (JMONITORING.equals(pUrl))
        {
            return mRealDriver.acceptsURL(mUrl);
        } else
        {
            return false;
        }
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
        return (JMONITORING.equals(pUrl) ? new GenericConnection(mRealDriver.connect(mUrl, pInfo)) : null);
    }

    /**
     * @return
     * @see java.sql.Driver#getMajorVersion()
     */
    public int getMajorVersion()
    {
        return mRealDriver.getMajorVersion();
    }

    /**
     * @return
     * @see java.sql.Driver#getMinorVersion()
     */
    public int getMinorVersion()
    {
        return mRealDriver.getMinorVersion();
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
        return mRealDriver.getPropertyInfo(pUrl, pInfo);
    }

    /**
     * @return
     * @see java.sql.Driver#jdbcCompliant()
     */
    public boolean jdbcCompliant()
    {
        return mRealDriver.jdbcCompliant();
    }

}
