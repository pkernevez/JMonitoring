package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.awt.Color;
import java.util.StringTokenizer;

/**
 * Configuration for a group.
 * 
 * @author pke
 * 
 */
public class GroupConfigurationPO {
    private GroupConfigurationPK mId;

    private Color mColor;

    /** Hibernate purpose. */
    GroupConfigurationPO() {
    }

    public GroupConfigurationPO(String pGroupName) {
        mId = new GroupConfigurationPK(pGroupName);
        mColor = ColorHelper.calculColor(pGroupName);
    }

    public GroupConfigurationPO(String pGroupName, Color pColor) {
        mId = new GroupConfigurationPK(pGroupName);
        mColor = pColor;
    }

    public String getColorAsString() {
        return "" + mColor.getRed() + ", " + mColor.getGreen() + ", " + mColor.getBlue();
    }

    public void setColorAsString(String pColor) {
        StringTokenizer tTok = new StringTokenizer(pColor, ",");
        int tRed = Integer.parseInt(tTok.nextToken().trim());
        int tGreen = Integer.parseInt(tTok.nextToken().trim());
        int tBlue = Integer.parseInt(tTok.nextToken().trim());

        mColor = new Color(tRed, tGreen, tBlue);
    }

    public String getGroupName() {
        return (mId == null ? null : mId.getGroupName());
    }

    public void setGroupName(String pGroupName) {
        mId.setGroupName(pGroupName);
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color pColor) {
        mColor = pColor;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((mId == null) ? 0 : mId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GroupConfigurationPO other = (GroupConfigurationPO) obj;
        if (mId == null) {
            if (other.mId != null)
                return false;
        } else if (!mId.equals(other.mId))
            return false;
        return true;
    }

    public GroupConfigurationPK getId() {
        return mId;
    }

    public void setId(GroupConfigurationPK pId) {
        mId = pId;
    }
}
