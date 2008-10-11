package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * 
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class ItemPO {

    private static final int TEMPO2 = 4;

    private static final int TEMPO = 3;

    private int mId = -1;

    private float mPrice;

    /**
     * For the Sample
     * 
     * @param pId For the Sample
     * @param pPrice For the Sample
     */
    public ItemPO(float pPrice) {
        mPrice = pPrice;
        try {
            Thread.sleep(TEMPO2);
        } catch (InterruptedException e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * For the Sample
     * 
     * @return For the Sample
     */
    public float getPrice() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return mPrice;
    }

    /**
     * For the Sample
     * 
     * @return For the Sample
     */
    public String toString() {
        try {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return "Item: " + mId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int pId) {
        mId = pId;
    }

    public void setPrice(float pPrice) {
        mPrice = pPrice;
    }

}