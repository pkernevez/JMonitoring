package net.kernevez.performance.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Extract of a king of measure.
 *  
 */
public class MeasureExtract
{
    private String mName;

    private int mOccurenceNumber;

    private String mGroupName;

    /**
     * Default constructor.
     * 
     * @param pName Full name of the measure (class name and method name).
     * @param pGroupName The name of the group associated with this measure extract.
     * @param pNb The number of occurrence of this kind of measure.
     */
    public MeasureExtract(String pName, String pGroupName, int pNb)
    {
        mName = pName;
        mGroupName = pGroupName;
        mOccurenceNumber = pNb;
    }

    /**
     * Get the method name.
     * 
     * @return The name of the method of this extract.
     */
    public String getMethodName()
    {
        return mName.substring(mName.lastIndexOf(".") + 1, mName.length());
    }

    /**
     * Accessor.
     * 
     * @return The name of the measure.
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Accessor.
     * 
     * @param pName The name of the measure.
     */
    public void setName(String pName)
    {
        mName = pName;
    }

    /**
     * Accessor.
     * 
     * @return The number of occurrence of this kind of measure.
     */
    public int getOccurenceNumber()
    {
        return mOccurenceNumber;
    }

    /**
     * Accessor.
     * 
     * @param pOccurenceNumber The number of occurrence of this kind of measure.
     */
    public void setOccurenceNumber(int pOccurenceNumber)
    {
        mOccurenceNumber = pOccurenceNumber;
    }

    /**
     * Accessor.
     * 
     * @return The group name assiociated with this kind of measure.
     */
    public String getGroupName()
    {
        return mGroupName;
    }

    /**
     * Accessor.
     * 
     * @param pGroupName The group name assiociated with this kind of measure.
     */
    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

}