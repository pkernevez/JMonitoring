package org.jmonitoring.agent.sql;

import java.lang.reflect.Method;
import java.sql.Statement;

import org.aspectj.lang.Signature;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SqlSignature implements Signature
{

    private final Method mMethod;

    private final Class<? extends Statement> mClass;

    public SqlSignature(Class<? extends Statement> pClass, Method pMethod)
    {
        super();
        mMethod = pMethod;
        mClass = pClass;
    }

    @SuppressWarnings("unchecked")
    public Class getDeclaringType()
    {
        return mClass;
    }

    public String getDeclaringTypeName()
    {
        return mClass.getName();
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
