package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.jmonitoring.sample.driver.MockConnection;

/**
 * 
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class ShoppingCart
{

    private static final int TEMPO7 = 9;

    private static final int TEMPO6 = 11;

    private static final int TEMPO5 = 17;

    private static final int TEMPO4 = 16;

    private static final int TEMPO3 = 15;

    private static final int TEMPO2 = 13;

    private static final int TEMPO = 7;

    private List mItems = new Vector();

    /**
     * For the Sample
     * 
     * @param pItem For the Sample
     */
    public void addItem(Item pItem)
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        if ("3".equals(pItem.getID()))
        {
            throw new RuntimeException("Pour declancher une exception sur item3");
        }
        mItems.add(pItem);
        Connection tCon = new MockConnection();
        PreparedStatement tState = null;
        try
        {
            tState = tCon.prepareStatement("lk");
            try
            {
                Thread.sleep(TEMPO7);
            } catch (InterruptedException e)
            {
                // @todo Auto-generated catch block
                e.printStackTrace();
            }
            tState.setString(0, "Param0");
            try
            {
                Thread.sleep(TEMPO6);
            } catch (InterruptedException e)
            {
                // @todo Auto-generated catch block
                e.printStackTrace();
            }
            tState.setInt(1, 0);
            tState.executeQuery();
        } catch (SQLException e)
        {
            throw new RuntimeException("Impossible de créer un statement !", e);
        } finally
        {
            if (tState != null)
            {
                try
                {
                    try
                    {
                        Thread.sleep(TEMPO2);
                    } catch (InterruptedException e)
                    {
                        // @todo Auto-generated catch block
                        e.printStackTrace();
                    }
                    tState.close();
                } catch (SQLException e)
                {
                    throw new RuntimeException("Impossible de fermer un statement !", e);

                }
            }
        }

    }

    /**
     * For the Sample
     * 
     * @param pItem For the Sample
     */
    public void removeItem(Item pItem)
    {
        try
        {
            Thread.sleep(TEMPO3);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.remove(pItem);
    }

    /**
     * For the Sample
     * 
     */
    public void empty()
    {
        try
        {
            Thread.sleep(TEMPO4);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.clear();
    }

    /**
     * For the Sample
     * 
     * @return For the Sample
     */
    public float totalValue()
    {
        // unimplemented... free!
        try
        {
            Thread.sleep(TEMPO5);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}