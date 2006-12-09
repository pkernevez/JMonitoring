package org.jmonitoring.core.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
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

    public void deleteGroupConfiguration(String pString)
    {
        GroupConfigurationPO tConf = (GroupConfigurationPO) mSession.load(GroupConfigurationPO.class, new Integer(pString));
        if (tConf != null) {
            mSession.delete(tConf);    
        } else {
            throw new ObjectNotFoundException(new Integer(pString), GroupConfigurationPO.class.getName());
        }
        
    }

    public Collection getAllGroupConfigurations()
    {
        List tListFromMeth =  mSession.createQuery("select distinct m.groupName from MethodCallPO m" ).list();
        List tListFromConf = mSession.createQuery("select g.groupName from GroupConfigurationPO g").list();
        tListFromConf.addAll(tListFromMeth);
        Set tSetOfGroupName=new HashSet(tListFromConf);
        List tResult = new ArrayList(tSetOfGroupName.size());
        Query tQuery = mSession.createQuery("from GroupConfigurationPO where groupName=:pGroupName");
        GroupConfigurationPO curConf;
        String curGroupName;
        for (Iterator tIt  = tSetOfGroupName.iterator(); tIt.hasNext();)
        {
            curGroupName = (String) tIt.next();
            tQuery.setString("pGroupName", curGroupName);
            curConf = (GroupConfigurationPO) tQuery.uniqueResult();
            if (curConf==null)
            {
                curConf = new GroupConfigurationPO(curGroupName);
            }
            tResult.add(curConf);
        }
        return tResult;
    }
}
