package net.kernevez.sample;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * 
 * @author pke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Item
{

    private static final int TEMPO2 = 4;
    private static final int TEMPO = 3;
    private String mId;

    private float mPrice;

    /**
     * For the Sample
     * @param pId For the Sample
     * @param pPrice For the Sample
     */
    public Item(String pId, float pPrice)
    {
        mId = pId;
        mPrice = pPrice;
        try
        {
            Thread.sleep(TEMPO2);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * For the Sample
     * @return For the Sample
     */
    public String getID()
    {
        return mId;
    }

    /**
     * For the Sample
     * @return For the Sample
     */
    public float getPrice()
    {
        try
        {
            Thread.sleep(2);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return mPrice;
    }

    /**
     * For the Sample
     * @return For the Sample
     */
    public String toString()
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return "Item: " + mId;
    }

}