package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.exception.GenericJDBCException;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;

public class TestConfigurationDAO extends PersistanceTestCase
{

    public void testSaveGeneralConfiguration()
    {
        assertEquals(0, countGeneralConf());

        GeneralConfigurationPO tConf = new GeneralConfigurationPO();
        assertEquals(ConfigurationDAO.UNIQUE_CONF_ID, tConf.getId());

        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();

        assertEquals(ConfigurationDAO.UNIQUE_CONF_ID, tConf.getId());
        assertEquals(1, countGeneralConf());

        tConf = new GeneralConfigurationPO();
        try
        {
            tDao.saveGeneralConfiguration(tConf);
            fail("Can't save 2 instances of General configuration");
        } catch (NonUniqueObjectException e)
        {
            assertTrue(e.getMessage().length() > 0);
        }
        assertEquals(1, countGeneralConf());

        getSession().clear();
        try
        {
            tDao.saveGeneralConfiguration(tConf);
            getSession().flush();
            fail("Can't save 2 instances of General configuration");
        } catch (GenericJDBCException e)
        {
            assertTrue(e.getMessage().length() > 0);
        }
        assertEquals(1, countGeneralConf());

    }

    public void testGetGeneralConfiguration()
    {
        assertEquals(0, countGeneralConf());

        GeneralConfigurationPO tConf = new GeneralConfigurationPO();
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        tConf.setDateFormat("YYY");
        tConf.setTimeFormat("UUIU");
        tConf.setMaxExecutionDuringFlowEdition(3000);
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();
        getSession().clear();

        GeneralConfigurationPO tConf2 = tDao.getGeneralConfiguration();

        assertNotNull(tConf2);
        assertNotSame(tConf, tConf2);
        assertEquals(tConf.getId(), tConf2.getId());
        assertEquals(tConf.getDateFormat(), tConf2.getDateFormat());
        assertEquals(tConf.getMaxExecutionDuringFlowEdition(), tConf2.getMaxExecutionDuringFlowEdition());
        assertEquals(tConf.getTimeFormat(), tConf2.getTimeFormat());
        assertEquals(1, countGeneralConf());

        // Check update
        tConf = tConf2;
        tConf.setDateFormat("IOOPP");
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();
        getSession().clear();
        tConf2 = tDao.getGeneralConfiguration();

        assertNotNull(tConf2);
        assertNotSame(tConf, tConf2);
        assertEquals(tConf.getId(), tConf2.getId());
        assertEquals("IOOPP", tConf2.getDateFormat());
        assertEquals(1, countGeneralConf());

    }

    public void testSaveGroupConfiguration()
    {
        GroupConfigurationPO tConf = new GroupConfigurationPO("groupName", new Color(12, 13, 14));
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        assertEquals(0, tDao.getAllGroupConfigurations().size());
        tDao.saveGroupConfiguration(tConf);
        getSession().flush();
        assertEquals(1, tDao.getAllGroupConfigurations().size());

        tConf.setColor(new Color(15, 16, 17));
        tDao.saveGroupConfiguration(tConf);
    }

    public void testGetGroupConfiguration()
    {
        GroupConfigurationPO tConf = new GroupConfigurationPO("groupName1", new Color(12, 13, 14));
        GroupConfigurationPO tConf2 = new GroupConfigurationPO("groupName2", new Color(15, 16, 17));
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        assertEquals(0, tDao.getAllGroupConfigurations().size());
        tDao.saveGroupConfiguration(tConf);
        tDao.saveGroupConfiguration(tConf2);
        getSession().flush();
        getSession().clear();
        assertEquals(2, tDao.getAllGroupConfigurations().size());

        GroupConfigurationPO tNewConf = tDao.getGroupConfiguration("groupName1");
        assertNotNull(tNewConf);
        assertNotSame(tConf, tNewConf);
        assertEquals(12, tNewConf.getColor().getRed());
        assertEquals("groupName1", tNewConf.getGroupName());

        assertNotNull(tDao.getGroupConfiguration("groupName2"));

        try
        {
            tDao.getGroupConfiguration("groupName3");
            fail("Should not be retreive...");
        } catch (ObjectNotFoundException e)
        {
            assertEquals(
                "No row with the given identifier exists: [org.jmonitoring.core.configuration.GroupConfigurationPO#groupName3]",
                e.getMessage());
        }

    }

    public void testDeleteGroupConfiguration()
    {
        GroupConfigurationPO tConf = new GroupConfigurationPO("groupName1", new Color(12, 13, 14));
        GroupConfigurationPO tConf2 = new GroupConfigurationPO("groupName2", new Color(15, 16, 17));
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        assertEquals(0, tDao.getAllGroupConfigurations().size());
        tDao.saveGroupConfiguration(tConf);
        tDao.saveGroupConfiguration(tConf2);
        getSession().flush();
        getSession().clear();
        assertEquals(2, tDao.getAllGroupConfigurations().size());

        tDao.deleteGroupConfiguration("groupName1");
        getSession().flush();
        assertEquals(1, tDao.getAllGroupConfigurations().size());

        try
        {
            tDao.deleteGroupConfiguration("uyuyy");
            fail("Object shoul not be found");
        } catch (ObjectNotFoundException e)
        {
            assertEquals(GroupConfigurationPO.class.getName(), e.getEntityName());
        }
        getSession().flush();
        assertEquals(1, tDao.getAllGroupConfigurations().size());

        tDao.deleteGroupConfiguration(tConf2.getGroupName());
        getSession().flush();
        assertEquals(0, tDao.getAllGroupConfigurations().size());

    }

    public void testGetAllGroupConfigurations()
    {
        GroupConfigurationPO tConf = new GroupConfigurationPO("groupName1", new Color(12, 13, 14));
        GroupConfigurationPO tConf2 = new GroupConfigurationPO("groupName2", new Color(15, 16, 17));
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        assertEquals(0, tDao.getAllGroupConfigurations().size());
        tDao.saveGroupConfiguration(tConf);
        tDao.saveGroupConfiguration(tConf2);

        TestExecutionFlowDAO.buildAndSaveNewFullFlow(getSession());
        getSession().flush();
        getSession().clear();

        Collection tGroups = tDao.getAllGroupConfigurations();
        assertEquals(5, tGroups.size());
        Iterator tIt = tGroups.iterator();
        assertEquals("groupName2", ((GroupConfigurationPO) tIt.next()).getGroupName());
        assertEquals("GrDefault", ((GroupConfigurationPO) tIt.next()).getGroupName());
        assertEquals("GrChild1", ((GroupConfigurationPO) tIt.next()).getGroupName());
        assertEquals("groupName1", ((GroupConfigurationPO) tIt.next()).getGroupName());
        assertEquals("GrChild2", ((GroupConfigurationPO) tIt.next()).getGroupName());

        assertEquals(2, getStats().getQueries().length);
        assertEquals(0, getStats().getEntityFetchCount());
        assertEquals(2, getStats().getEntityLoadCount());

    }

    public void testGeneralConfigurationCacheLevel2()
    {
        GeneralConfigurationPO tConf = new GeneralConfigurationPO();

        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();
        getSession().clear();
        getSession().getSessionFactory().evict(GeneralConfigurationPO.class);

        GeneralConfigurationPO tNewConf = tDao.getGeneralConfiguration();
        assertNotSame(tConf, tNewConf);
        assertEquals(0, getStats().getQueries().length);
        assertEquals(1, getStats().getEntityLoadCount());
        getSession().clear();

        tConf = tNewConf;
        tNewConf = tDao.getGeneralConfiguration();
        assertNotSame(tConf, tNewConf);
        assertEquals(0, getStats().getQueries().length);
        assertEquals(1, getStats().getEntityLoadCount());

    }

    public void testGroupConfigurationCacheLevel2()
    {
        GroupConfigurationPO tConf = new GroupConfigurationPO("groupName1", new Color(12, 13, 14));
        GroupConfigurationPO tConf2 = new GroupConfigurationPO("groupName2", new Color(15, 16, 17));
        GroupConfigurationPO tConf3 = new GroupConfigurationPO("groupName3", new Color(15, 16, 17));
        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
        tDao.saveGroupConfiguration(tConf);
        tDao.saveGroupConfiguration(tConf2);
        tDao.saveGroupConfiguration(tConf3);
        getSession().flush();
        getSession().clear();
        getSession().getSessionFactory().evict(GroupConfigurationPO.class);

        tConf = tDao.getGroupConfiguration(tConf.getGroupName());
        tConf2 = tDao.getGroupConfiguration(tConf2.getGroupName());
        tConf3 = tDao.getGroupConfiguration(tConf3.getGroupName());

        assertEquals(0, getStats().getQueries().length);
        assertEquals(3, getStats().getEntityLoadCount());
        getSession().clear();
        tDao.getGroupConfiguration(tConf.getGroupName());
        tDao.getGroupConfiguration(tConf2.getGroupName());
        tDao.getGroupConfiguration(tConf3.getGroupName());
        assertEquals(0, getStats().getQueryCacheHitCount());
        assertEquals(1, getStats().getSecondLevelCacheRegionNames().length);
        assertEquals(3, getStats().getSecondLevelCacheStatistics("Conf").getEntries().size());
        assertEquals(3, getStats().getSecondLevelCacheStatistics("Conf").getElementCountInMemory());
        assertEquals(0, getStats().getQueries().length);
        assertEquals(3, getStats().getEntityLoadCount());
        assertEquals(0, getStats().getEntityFetchCount());
        assertEquals(3, getStats().getSecondLevelCacheHitCount());
    }

    private int countGeneralConf()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From CONFIGURATION_GENERAL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }
}
