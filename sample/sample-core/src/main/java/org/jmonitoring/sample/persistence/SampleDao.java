package org.jmonitoring.sample.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jmonitoring.sample.main.ShoppingCartPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SampleDao {

    private static Log sLog = LogFactory.getLog(SampleDao.class);

    private Session mSession;

    public SampleDao(Session pSession) {
        mSession = pSession;
    }

    public void save(ShoppingCartPO pShopCart) {
        mSession.save(pShopCart);

        Connection tCon = mSession.connection();
        try {
            Statement tStat = tCon.createStatement();
            tStat.execute("Select count(*) from SHOPPING_CART");
            tStat.execute("Select * from SHOPPING_CART");
        } catch (SQLException e) {
            sLog.error(e);
        }
    }
}
