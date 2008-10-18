package org.jmonitoring.core.configuration;

import java.awt.Color;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * @todo URGENT Supprimer l'aspet statique de la configuration.
 */
public final class ColorHelper
{

    private static final int NB_COLOR_BIT = 128;

    /**
     * Generate a <code>Color</code> for a <code>String</code> using hashcode.
     * 
     * @param pGroupName The name of the group.
     * @return The genrated <code>Color</code>.
     */
    public static Color calculColor(String pGroupName)
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
    public static String getGroupAsColorString(Color pColor)
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

    public static String getColor(String pGroupName)
    {
        String tColor = ConfigurationHelper.getString("group.color." + pGroupName);
        if (tColor == null)
        {
            tColor = getGroupAsColorString(calculColor(pGroupName));
        }
        return tColor;

    }

    // /**
    // * Factory's method for the <code>Configuration</code>. This method is not synchrnoized because there isn't any
    // * trouble if we load the configuration twice.
    // *
    // * @return The Syngleton.
    // */
    // public static Configuration getInstance()
    // {
    // if (sConfiguration == null)
    // {
    // sConfiguration = new Configuration();
    // }
    // return sConfiguration;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The default log parameter.
    // */
    // public boolean getLogMethodParameter()
    // {
    // return mLogMethodParameter;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The Asynchrone thread pool size.
    // */
    // public int getAsynchroneStoreThreadPoolSize()
    // {
    // return mAsynchroneStoreThreadPoolSize;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The store class.
    // */
    // public Class getMeasurePointStoreClass()
    // {
    // return mMeasurePointStoreClass;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The boolean that indicates if all the Tread use the same XmlStoreFile.
    // */
    // public boolean getSameFileForAllThread()
    // {
    // return mSameFileForAllThread;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The Xml outputdir.
    // */
    // public String getXmlOutpuDir()
    // {
    // return mXmlOutputDir;
    // }
    //
    // /**
    // * Accessor.
    // *
    // * @return The name of the server that is at the origin of the flow.
    // */
    // public String getServerName()
    // {
    // return mServerName;
    // }
    //
    // public void setLogMethodParameter(boolean pLogParameter)
    // {
    // mLogMethodParameter = pLogParameter;
    // }
    //
    // /**
    // * @param pMeasurePointStoreClass The measurePointStoreClass to set.
    // */
    // public void setMeasurePointStoreClass(Class pMeasurePointStoreClass)
    // {
    // mMeasurePointStoreClass = pMeasurePointStoreClass;
    // }
    //
    // public Class getExecutionFlowDaoClass()
    // {
    // return mExecutionFlowDaoClass;
    // }
    //
    // public void setExecutionFlowDaoClass(Class pExecutionFlowDaoClass)
    // {
    // mExecutionFlowDaoClass = pExecutionFlowDaoClass;
    // }
    //
    // public int getMaxExecutionDuringFlowEdition()
    // {
    // return mMaxExecutionDuringFlowEdition;
    // }
    //
}
