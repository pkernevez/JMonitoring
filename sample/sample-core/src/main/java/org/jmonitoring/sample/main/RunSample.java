package org.jmonitoring.sample.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.sample.persistence.SampleDao;
import org.jmonitoring.sample.persistence.SpringSampleConfigurationUtil;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class RunSample
{
    private static Log sLog = LogFactory.getLog(RunSample.class);

    private static final int SLEEP_TEST_TIME = 5000;

    private static final int NB3 = 32;

    private static final int NB2 = 31;

    private static final int NB1 = 30;

    private final Session mSession;

    public RunSample()
    {
        SessionFactory tFactory = (SessionFactory) SpringSampleConfigurationUtil.getBean("sessionFactory");
        mSession = tFactory.getCurrentSession();
    }

    /**
     * For the Sample
     * 
     */
    public void run()
    {
        ShoppingCartPO.setCounter(0);
        checkDataBase();
        Inventory inventory = new Inventory();
        ItemPO item1 = new ItemPO(NB1);
        ItemPO item2 = new ItemPO(NB2);
        ItemPO item3 = new ItemPO(NB3);

        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(item3);

        ShoppingCartPO tShopCart = new ShoppingCartPO();
        ShoppingCartOperator.addShoppingCartItem(tShopCart, inventory, item1);
        ShoppingCartOperator.addShoppingCartItem(tShopCart, inventory, item2);
        new SampleDao(mSession).save(tShopCart);
        mSession.flush();
        try
        {
            ShoppingCartOperator.addShoppingCartItem(tShopCart, inventory, item3);
        } catch (RuntimeException e)
        {
            // C'est juste pour tester la remont�e d'exception
            System.out.print("");
        }

    }

    private void checkDataBase()
    {
        try
        {
            sLog.info("Check Schema try to count Items...");
            SQLQuery tQuery = mSession.createSQLQuery("Select Count(*) as myCount From ITEM");
            tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        } catch (SQLGrammarException t)
        {
            sLog.info("Creating new Schema for the DataBase");
            Configuration tConfig = (Configuration) SpringSampleConfigurationUtil.getBean("hibernateConfiguration");
            SchemaExport tDdlexport = new SchemaExport(tConfig);
            tDdlexport.create(true, true);
            sLog.info("End of the Schema creation for the DataBase");
        }

    }
}
