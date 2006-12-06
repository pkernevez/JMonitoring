package org.jmonitoring.core.configuration;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class ConfigurationDAO
{

    private Session mSession;

    public ConfigurationDAO(Session pSession)
    {
        mSession = pSession;
    }

    static final int UNIQUE_CONF_ID = 1;

    public void saveGeneralConfiguration(GeneralConfigurationPO pConf)
    {
        mSession.save(pConf);
    }

    public GeneralConfigurationPO getGeneralConfiguration()
    {
        return (GeneralConfigurationPO) mSession.load(GeneralConfigurationPO.class, new Integer(UNIQUE_CONF_ID));
    }

    // // public void updateConfiguration(GeneralConfigurationPO pConf)
    // // {
    // //
    // }

    public void insertGroupConfiguration()
    {

    }

    public void updateGroupConfiguration(GroupConfigurationPO pConf)
    {

    }

    public GroupConfigurationPO getGroupConfiguration(String pGroupName)
    {
        return null;
    }

    public void deleteGroupConfiguration(String pGroupName)
    {

    }

    public List getListOfGroupConfiguration()
    {
        return new ArrayList();
    }
}
