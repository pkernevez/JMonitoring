package org.jmonitoring.core.configuration;

import java.awt.Color;
import java.util.Map;
import java.util.StringTokenizer;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public final class ColorManager
{

    private Map<String, String> mGroups;

    private static final int NB_COLOR_BIT = 128;

    /**
     * Generate a <code>Color</code> for a <code>String</code> using hashcode.
     * 
     * @param pGroupName The name of the group.
     * @return The generated <code>Color</code>.
     */
    public Color calculColor(String pGroupName)
    {
        Color tColor;
        if (pGroupName == null || pGroupName.length() == 0)
        {
            tColor = Color.BLACK;
        } else
        {
            int tInt = Math.abs(pGroupName.hashCode());
            int tR, tG, tB;
            tR = tInt % NB_COLOR_BIT;
            tG = (tInt / NB_COLOR_BIT) % NB_COLOR_BIT;
            tB = (tInt / (NB_COLOR_BIT * NB_COLOR_BIT)) % NB_COLOR_BIT;
            tColor = new Color(tR * 2, tG * 2, tB * 2);
        }
        return tColor;
    }

    /**
     * Get the <code>Color</code> of a group as <code>String</code>.
     * 
     * @param pGroupName The group name.
     * @return The RGB format like "#00FF88" of the <code>Color</code>.
     */
    public String getGroupAsColorString(Color pColor)
    {
        String tRed = Integer.toHexString(pColor.getRed());
        if (tRed.length() == 1)
        {
            tRed = "0" + tRed;
        }
        String tGreen = Integer.toHexString(pColor.getGreen());
        if (tGreen.length() == 1)
        {
            tGreen = "0" + tGreen;
        }
        String tBlue = Integer.toHexString(pColor.getBlue());
        if (tBlue.length() == 1)
        {
            tBlue = "0" + tBlue;
        }
        return "#" + tRed + tGreen + tBlue;
    }

    public Color getColor(String pGroupName)
    {
        String tColorAsString = mGroups.get(pGroupName);
        Color tColor;
        if (tColorAsString == null)
        {
            tColor = calculColor(pGroupName);
        } else
        {
            StringTokenizer tToken = new StringTokenizer(tColorAsString, ",");
            assert (tToken.countTokens() == 3);
            int tR = Integer.parseInt(tToken.nextToken());
            int tG = Integer.parseInt(tToken.nextToken());
            int tB = Integer.parseInt(tToken.nextToken());
            tColor = new Color(tR, tG, tB);
        }
        return tColor;
    }

    public String getColorString(String pGroupName)
    {
        String tColorAsString = mGroups.get(pGroupName);
        Color tColor;
        if (tColorAsString == null)
        {
            tColor = calculColor(pGroupName);
        } else
        {
            StringTokenizer tToken = new StringTokenizer(tColorAsString, ",");
            assert (tToken.countTokens() == 3);
            int tR = Integer.parseInt(tToken.nextToken());
            int tG = Integer.parseInt(tToken.nextToken());
            int tB = Integer.parseInt(tToken.nextToken());
            tColor = new Color(tR, tG, tB);
        }
        return getGroupAsColorString(tColor);
    }

    /**
     * @return the mGroups
     */
    public Map<String, String> getGroups()
    {
        return mGroups;
    }

    /**
     * @param pGroups the mGroups to set
     */
    public void setGroups(Map<String, String> pGroups)
    {
        mGroups = pGroups;
    }
}
