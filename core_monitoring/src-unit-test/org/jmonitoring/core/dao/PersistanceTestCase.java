package org.jmonitoring.core.dao;

import java.util.List;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.persistence.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class  PersistanceTestCase extends DatabaseTestCase
{

    protected Session mManagerForDbUnit;

    protected Session mPersistenceManager;

    protected void setUp() throws Exception
    {
        createSchema();
        mManagerForDbUnit = HibernateManager.getNewSession();

        super.setUp();
        mPersistenceManager = HibernateManager.getNewSession();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        org.hibernate.cfg.Configuration config = HibernateManager.getConfig();

        SchemaExport ddlexport = new SchemaExport(config);

        ddlexport.drop(true, true);
    }

    public void createSchema()
    {

        org.hibernate.cfg.Configuration config = HibernateManager.getConfig();

        SchemaExport ddlexport = new SchemaExport(config);

        ddlexport.create(true, true);
    }

    protected IDatabaseConnection getConnection() throws Exception
    {
        return new DatabaseConnection(mManagerForDbUnit.connection());
    }

    protected IDataSet getDataSet() throws Exception
    {
        XmlDataSet tData =new XmlDataSet(getClass().getResourceAsStream("/dataset.xml"));
        System.out.println(tData.toString());
        return tData;
    }

}
