package org.jmonitoring.console.gwt.client.dto;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class RootMethodCallDTO extends MethodCallDTO
{
    private static final long serialVersionUID = -3441459916159144366L;

    private String mPrevInGroup;

    private String mNextInGroup;

    private String mPrev;

    private String mNext;

    public String getPrevInGroup()
    {
        return mPrevInGroup;
    }

    public void setPrevInGroup(String pPrevInGroup)
    {
        mPrevInGroup = pPrevInGroup;
    }

    public String getNextInGroup()
    {
        return mNextInGroup;
    }

    public void setNextInGroup(String pNextInGroup)
    {
        mNextInGroup = pNextInGroup;
    }

    public String getPrev()
    {
        return mPrev;
    }

    public void setPrev(String pPrev)
    {
        mPrev = pPrev;
    }

    public String getNext()
    {
        return mNext;
    }

    public void setNext(String pNext)
    {
        mNext = pNext;
    }

}
