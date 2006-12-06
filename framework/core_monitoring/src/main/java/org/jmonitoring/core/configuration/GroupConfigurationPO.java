package org.jmonitoring.core.configuration;

import java.awt.Color;
import java.util.StringTokenizer;

/**
 * Configuration for a group.
 * 
 * @author pke
 * 
 */
public class GroupConfigurationPO
{
    private int mId = -1;

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
        return ""+mColor.getRed()+", "+mColor.getGreen()+", "+mColor.getBlue();
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

    public int getId()
    {
        return mId;
    }

    public void setId(int pId)
    {
        mId = pId;
    }

    public Color getColor()
    {
        return mColor;
    }

    public void setColor(Color pColor)
    {
        mColor = pColor;
    }
}
