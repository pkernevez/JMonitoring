package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Extract of a king of measure.
 * 
 */
public class MethodCallExtractDTO
{
    private final String mName;

    private final String mClassName;

    private final String mMethodName;

    private final int mOccurenceNumber;

    private final String mGroupName;

    /**
     * Default constructor.
     * 
     * @param pClassName Full class name
     * @param pMethodName Method name).
     * @param pGroupName The name of the group associated with this measure extract.
     * @param pNb The number of occurrence of this kind of measure.
     */
    public MethodCallExtractDTO(String pClassName, String pMethodName, String pGroupName, int pNb)
    {
        mName = getFullName(pClassName, pMethodName);
        mClassName = pClassName;
        mMethodName = pMethodName;
        mGroupName = getGroupName(pGroupName);
        mOccurenceNumber = pNb;
    }

    protected static String getFullName(String pClassName, String pMethodName)
    {
        String tName = pClassName + "." + pMethodName;
        return tName.replaceAll("\\.++", ".");
    }

    protected static String getGroupName(String pGroupName)
    {
        return pGroupName.replaceAll("\\.++", "");
    }

    /**
     * Get the method name.
     * 
     * @return The name of the method of this extract.
     */
    public String getMethodName()
    {
        return mMethodName;
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
     * @return The number of occurrence of this kind of measure.
     */
    public int getOccurenceNumber()
    {
        return mOccurenceNumber;
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

    public String getClassName()
    {
        return mClassName;
    }

}