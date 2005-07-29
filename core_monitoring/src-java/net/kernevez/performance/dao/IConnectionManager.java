package net.kernevez.performance.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public interface IConnectionManager
{
    /**
     * Permet d'obtenir une connection de la base de données.
     * 
     * @return A connection to the database.
     * @throws SQLException When can't obtain a connection from the Driver.
     */
    Connection getConnection() throws SQLException;

}
