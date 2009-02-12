package org.jmonitoring.core.configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ForTestBean
{
    private String mText;

    private ForTestBean mRelation;

    /**
     * @return the relation
     */
    public ForTestBean getRelation()
    {
        return mRelation;
    }

    /**
     * @param pRelation the relation to set
     */
    public void setRelation(ForTestBean pRelation)
    {
        mRelation = pRelation;
    }

    @Override
    public String toString()
    {
        return "\"" + mText + "\"; " + super.toString();
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return mText;
    }

    /**
     * @param pText the text to set
     */
    public void setText(String pText)
    {
        mText = pText;
    }

}
