package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

public class ConfigurationDAO
{

    private ColorManager mColorBean;

    private final Session mSession;

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
        return (GeneralConfigurationPO) mSession.get(GeneralConfigurationPO.class, new Integer(UNIQUE_CONF_ID));
    }

    public void saveGroupConfiguration(GroupConfigurationPO pConf)
    {
        mSession.save(pConf);
    }

    public GroupConfigurationPO getGroupConfiguration(String pGroupName)
    {
        GroupConfigurationPO tConf =
            (GroupConfigurationPO) mSession.get(GroupConfigurationPO.class, new GroupConfigurationPK(pGroupName));
        if (tConf != null)
        {
            return tConf;
        } else
        {
            throw new ObjectNotFoundException(pGroupName, GroupConfigurationPO.class.getName());
        }
    }

    public void deleteGroupConfiguration(String pGroupName)
    {
        mSession.delete(getGroupConfiguration(pGroupName));
    }

    @SuppressWarnings("unchecked")
    public Collection<GroupConfigurationPO> getAllGroupConfigurations()
    {
        List<String> tListFromMeth = mSession.createQuery("select distinct m.groupName from MethodCallPO m").list();
        List<String> tListFromConf = mSession.createQuery("select g.id.groupName from GroupConfigurationPO g").list();
        tListFromConf.addAll(tListFromMeth);
        Set<String> tSetOfGroupName = new HashSet<String>(tListFromConf);
        List<GroupConfigurationPO> tResult = new ArrayList<GroupConfigurationPO>(tSetOfGroupName.size());
        GroupConfigurationPO curConf;
        for (String curGroupName : tSetOfGroupName)
        {
            try
            {
                curConf = getGroupConfiguration(curGroupName);
            } catch (ObjectNotFoundException e)
            {
                curConf = new GroupConfigurationPO(curGroupName, mColorBean.getColor(curGroupName));
            }
            tResult.add(curConf);
        }
        return tResult;
    }

    /**
     * @param pColorBean the mColorBean to set
     */
    public void setColorBean(ColorManager pColorBean)
    {
        mColorBean = pColorBean;
    }
}
