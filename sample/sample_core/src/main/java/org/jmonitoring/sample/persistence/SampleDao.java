package org.jmonitoring.sample.persistence;

import org.hibernate.Session;
import org.jmonitoring.sample.main.ShoppingCartPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SampleDao
{

    private Session mSession;

    public SampleDao(Session pSession)
    {
        mSession = pSession;
    }

    public void save(ShoppingCartPO pShopCart)
    {
        mSession.save(pShopCart);
    }

}
