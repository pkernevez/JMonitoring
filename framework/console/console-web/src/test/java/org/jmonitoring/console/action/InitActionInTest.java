package org.jmonitoring.console.action;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.process.ConsoleManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class InitActionInTest extends JMonitoringMockStrustTestCase
{
    @Resource(name = "hibernateConfiguration")
    private Configuration mConfiguration;

    @Autowired
    ConsoleManager mManager;

    @Test
    public void testInitActionIn() throws SQLException
    {
        dropSchema();
        assertFalse("The databse shouldn't exist...", mManager.doDatabaseExist());
        SpringConfigurationUtil.setContext(getApplicationContext());
        setRequestPathInfo("/Index");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "homepage");

        assertTrue(mManager.doDatabaseExist());

    }

    private void dropSchema() throws SQLException
    {
        Connection tCon = mSessionFactory.getCurrentSession().connection();
        try
        {
            SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
            tDdlexport.drop(true, true);
        } finally
        {
            tCon.close();
        }

    }
}
