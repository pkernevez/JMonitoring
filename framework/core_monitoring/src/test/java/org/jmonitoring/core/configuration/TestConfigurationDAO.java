package org.jmonitoring.core.configuration;

import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SQLQuery;
import org.hibernate.exception.GenericJDBCException;
import org.jmonitoring.core.dao.PersistanceTestCase;

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
        tConf.setAsynchroneThreadPoolSize(45);
        tConf.setDateFormat("YYY");
        tConf.setTimeFormat("UUIU");
        tConf.setLoggerClass("RUTUUTU");
        tConf.setMaxExecutionDuringFlowEdition(3000);
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();
        getSession().clear();

        GeneralConfigurationPO tConf2 = tDao.getGeneralConfiguration();

        assertNotNull(tConf2);
        assertNotSame(tConf, tConf2);
        assertEquals(tConf.getId(), tConf2.getId());
        assertEquals(tConf.getAsynchroneThreadPoolSize(), tConf2.getAsynchroneThreadPoolSize());
        assertEquals(tConf.getDateFormat(), tConf2.getDateFormat());
        assertEquals(tConf.getLoggerClass(), tConf2.getLoggerClass());
        assertEquals(tConf.getMaxExecutionDuringFlowEdition(), tConf2.getMaxExecutionDuringFlowEdition());
        assertEquals(tConf.getTimeFormat(), tConf2.getTimeFormat());
        assertEquals(1, countGeneralConf());

        //Check update
        tConf = tConf2;
        tConf.setLoggerClass("TheNewClass");
        tDao.saveGeneralConfiguration(tConf);
        getSession().flush();
        getSession().clear();
        tConf2 = tDao.getGeneralConfiguration();

        assertNotNull(tConf2);
        assertNotSame(tConf, tConf2);
        assertEquals(tConf.getId(), tConf2.getId());
        assertEquals("TheNewClass", tConf2.getLoggerClass());
        assertEquals(1, countGeneralConf());
        
    }

//    public void testInsertGroupConfiguration()
//    {
//        GroupConfigurationPO tConf = new GroupConfigurationPO();
//        ConfigurationDAO tDao = new ConfigurationDAO(getSession());
//        assertEquals(0, tDao.getListOfGroupConfiguration().size());
//
////        GeneralConfigurationPO tConf = new GeneralConfigurationPO();
//     
//        fail("Not yet implemented");
//        
//    }
//
//    public void testUpdateGroupConfiguration()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetGroupConfiguration()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testDeleteGroupConfiguration()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetListOfGroupConfiguration()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGeneralConfigurationCacheLevel2()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGroupConfigurationCacheLevel2()
//    {
//        fail("Not yet implemented");
//    }
//
    private int countGeneralConf()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From CONFIGURATION_GENERAL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }
}
