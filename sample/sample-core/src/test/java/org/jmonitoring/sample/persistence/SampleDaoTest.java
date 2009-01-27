package org.jmonitoring.sample.persistence;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.sample.main.ItemPO;
import org.jmonitoring.sample.main.ShoppingCartPO;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SampleDaoTest extends SamplePersistenceTestcase
{

    @Test
    public void testSave()
    {
        ShoppingCartPO tSc = new ShoppingCartPO();
        tSc.addItem(new ItemPO((float) 0.5));
        new SampleDao(getSampleSession()).save(tSc);
        getSampleSession().flush();
        getSampleSession().clear();

        checkCountShoppingCart(1);
        tSc = new ShoppingCartPO();
        tSc.addItem(new ItemPO((float) 0.5));
        new SampleDao(getSampleSession()).save(tSc);
        getSampleSession().flush();
        getSampleSession().clear();

        checkCountShoppingCart(2);

    }

    private void checkCountShoppingCart(int pCount)
    {
        SQLQuery tQuery = getSampleSession().createSQLQuery("Select Count(*) as myCount From SHOPPING_CART");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        assertEquals(pCount, ((Integer) tResult).intValue());
    }

}
