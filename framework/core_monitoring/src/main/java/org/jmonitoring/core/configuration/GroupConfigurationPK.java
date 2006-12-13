package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

public class GroupConfigurationPK implements Serializable
{
    private static final long serialVersionUID = 6714884680643463612L;

    private String mGroupName;

    public GroupConfigurationPK()
    {
    }

    public GroupConfigurationPK(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    public String getGroupName()
    {
        return mGroupName;
    }

    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((mGroupName == null) ? 0 : mGroupName.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GroupConfigurationPK other = (GroupConfigurationPK) obj;
        if (mGroupName == null)
        {
            if (other.mGroupName != null)
                return false;
        } else if (!mGroupName.equals(other.mGroupName))
            return false;
        return true;
    }

}
