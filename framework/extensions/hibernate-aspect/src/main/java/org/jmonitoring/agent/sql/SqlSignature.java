package org.jmonitoring.agent.sql;

import java.lang.reflect.Method;

import org.aspectj.lang.Signature;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SqlSignature implements Signature
{

    private final Method mMethod;

    public SqlSignature(Method pMethod)
    {
        super();
        mMethod = pMethod;
    }

    @SuppressWarnings("unchecked")
    public Class getDeclaringType()
    {
        return mMethod.getDeclaringClass();
    }

    public String getDeclaringTypeName()
    {
        return mMethod.getDeclaringClass().getName();
    }

    public int getModifiers()
    {
        return mMethod.getModifiers();
    }

    public String getName()
    {
        return mMethod.getName();
    }

    public String toLongString()
    {
        return mMethod.toString();
    }

    public String toShortString()
    {
        return mMethod.getName();
    }

}
