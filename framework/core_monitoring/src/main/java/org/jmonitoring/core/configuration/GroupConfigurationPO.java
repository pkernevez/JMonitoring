package org.jmonitoring.core.configuration;

import java.awt.Color;
import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Configuration for a group.
 * 
 * @author pke
 * 
 */
public class GroupConfigurationPO implements Serializable
{
    private static final long serialVersionUID = -8206014416776157737L;

    private String mGroupName;

    private Color mColor;

    /** Hibernate purpose. */
    GroupConfigurationPO()
    {
    }

    public GroupConfigurationPO(String pGroupName)
    {
        mGroupName = pGroupName;
        mColor = Configuration.calculColor(pGroupName);
    }

    public GroupConfigurationPO(String pGroupName, Color pColor)
    {
        mGroupName = pGroupName;
        mColor = pColor;
    }

    public String getColorAsString()
    {
        return "" + mColor.getRed() + ", " + mColor.getGreen() + ", " + mColor.getBlue();
    }

    public void setColorAsString(String pColor)
    {
        StringTokenizer tTok = new StringTokenizer(pColor, ",");
        int tRed = Integer.parseInt(tTok.nextToken().trim());
        int tGreen = Integer.parseInt(tTok.nextToken().trim());
        int tBlue = Integer.parseInt(tTok.nextToken().trim());

        mColor = new Color(tRed, tGreen, tBlue);
    }

    public String getGroupName()
    {
        return mGroupName;
    }

    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    public Color getColor()
    {
        return mColor;
    }

    public void setColor(Color pColor)
    {
        mColor = pColor;
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
        final GroupConfigurationPO other = (GroupConfigurationPO) obj;
        if (mGroupName == null)
        {
            if (other.mGroupName != null)
                return false;
        } else if (!mGroupName.equals(other.mGroupName))
            return false;
        return true;
    }
}
