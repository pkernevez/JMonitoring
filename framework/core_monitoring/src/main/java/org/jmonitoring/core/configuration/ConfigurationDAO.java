package org.jmonitoring.core.configuration;

import java.util.List;

import org.hibernate.Query;
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

    public void saveGroupConfiguration(GroupConfigurationPO pConf)
    {
        mSession.save(pConf);
    }

    public GroupConfigurationPO getGroupConfiguration(String pGroupName)
    {
        Query tQuery = mSession.createQuery("from GroupConfigurationPO where groupName=:pGroupName");
        tQuery.setString("pGroupName", pGroupName);
        return (GroupConfigurationPO) tQuery.uniqueResult();
    }

    public void deleteGroupConfiguration(int pId)
    {

    }

    public List getListOfGroupConfiguration()
    {
        return mSession.createQuery("from GroupConfigurationPO").list();
    }
}
