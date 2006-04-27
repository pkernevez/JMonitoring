package org.jmonitoring.core.dao;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.persistence.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class PersistanceTestCase extends TestCase
{

    protected Session mPersistenceManager;

    protected Transaction mTransaction;

    protected void setUp() throws Exception
    {
        super.setUp();

        mPersistenceManager = HibernateManager.getSession();
        mTransaction = mPersistenceManager.beginTransaction();

        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.create(true, true);

    }

    protected void createDataSet(String pDataSetFileName)
    {
        Session tSession = HibernateManager.getSession();
        // try
        // {
        // Transaction tTans = tSession.beginTransaction();
        if (pDataSetFileName != null)
        {
            IDataSet tDataSet;
            try
            {
                tDataSet = new XmlDataSet(getClass().getResourceAsStream(pDataSetFileName));
                DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(tSession.connection()), tDataSet);
                mPersistenceManager.flush();
            } catch (DataSetException e)
            {
                throw new RuntimeException(e);
            } catch (Exception e)
            {
                // @todo Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Transaction.;
        // } catch (Exception e)
        // {
        // tSession.clear();
        // throw new RuntimeException(e);
        // } catch (Error e1)
        // {
        // tSession.clear();
        // throw e1;
        // }
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.drop(true, true);
        mTransaction.rollback();
        mPersistenceManager.close();
    }

    // protected String getDataSetFileName() throws Exception
    // {
    // return null;
    // }

}
