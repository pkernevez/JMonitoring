package org.jmonitoring.agent.store;

import org.aspectj.lang.Signature;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/** Mock AspectJ Signature */
public class MockSignature implements Signature
{
    /** No doc for this test. */
    public MockSignature(String pName, Class pDeclaringType)
    {
        mName = pName;
        mDeclaringClass = pDeclaringType;
    }

    private String mName;

    private Class mDeclaringClass;

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public String toShortString()
    {
        return mName;
    }

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public String toLongString()
    {
        return mDeclaringClass.getName() + "." + mName;
    }

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public String getName()
    {
        return mName;
    }

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public int getModifiers()
    {
        return 0;
    }

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public Class getDeclaringType()
    {
        return mDeclaringClass;
    }

    /**
     * No doc for this test.
     * 
     * @return no doc
     */
    public String getDeclaringTypeName()
    {
        return mDeclaringClass.getName();
    }

}