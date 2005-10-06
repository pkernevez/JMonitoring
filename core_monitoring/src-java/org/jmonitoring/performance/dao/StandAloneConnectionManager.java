package net.kernevez.performance.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.measure.MeasureException;

/**
 * Purchase <code>Connection</code> for database access.
 * 
 * @author pke
 */
public class StandAloneConnectionManager implements IConnectionManager
{
    private boolean isInitialized = false;

    private Configuration mConfiguration;

    /**
     * Default constructor.
     * 
     * @param pConfig The configuration instance to use.
     */
    public StandAloneConnectionManager(Configuration pConfig)
    {
        mConfiguration = pConfig;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.dao.IConnectionManager#getConnection()
     */
    public synchronized Connection getConnection() throws SQLException
    {
        if (!isInitialized)
        {
            initDriver();
        }
        Connection tResult;
        if (mConfiguration.getJdbcUser() != null)
        {
            tResult = DriverManager.getConnection(mConfiguration.getJdbcUrl(), mConfiguration.getJdbcUser(),
                            mConfiguration.getJdbcPassword());

        } else
        {
            tResult = DriverManager.getConnection(mConfiguration.getJdbcUrl());

        }
        tResult.setAutoCommit(false);
        return tResult;
    }

    /**
     * Register the driver.
     */
    private synchronized void initDriver()
    {
        if (!isInitialized)
        {
            try
            {

                Class tDriverClass = Class.forName(mConfiguration.getJdbcDriverClass());
                Driver tDriver = (Driver) tDriverClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                DriverManager.registerDriver(tDriver);
            } catch (SQLException e)
            {
                throw new MeasureException("Unable to register driver.", e);
            } catch (IllegalArgumentException e)
            {
                throw new MeasureException("No constructor without parameter.", e);
            } catch (SecurityException e)
            {
                throw new MeasureException("Unable to access to default constructor.", e);
            } catch (InstantiationException e)
            {
                throw new MeasureException("Unable to create new instance of driver.", e);
            } catch (IllegalAccessException e)
            {
                throw new MeasureException("Unable to access to default constructor.", e);
            } catch (InvocationTargetException e)
            {
                throw new MeasureException("Unable to call default constructor.", e);
            } catch (NoSuchMethodException e)
            {
                throw new MeasureException("No constructor without parameter.", e);
            } catch (ClassNotFoundException e)
            {
                throw new MeasureException("Unable to load the DriverClass", e);
            }
            isInitialized = true;
        }
    }

}
