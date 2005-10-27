package org.jmonitoring.core.log;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;

import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.StandAloneConnectionManager;
import org.jmonitoring.core.measure.ExecutionFlow;

/**
 * @author pke
 * 
 * @todo Refactoring de la classe pour utiliser les mécanismes de batch: soit en permettant de valider les contraintes
 *       d'intégrité relationnelle au COMMIT soit en faisant 2 batchs: un en insert sans clé étrangère et un deuxième en
 *       UPDATE pour créer les liens Templates
 */
public class JdbcLogger implements IMeasurePointTreeLogger
{
    /*
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.log.IMeasurePointTreeLogger#logMeasurePointTree(
     *      org.jmonitoring.core.measure.ExecutionFlow)
     */
    public void logMeasurePointTree(ExecutionFlow pExecutionFlow)
    {

        Connection tConnection = null;
        try
        {

            tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            new ExecutionFlowMySqlDAO(tConnection).insertFullExecutionFlow(pExecutionFlow);
            tConnection.commit();

        } catch (SQLException e)
        {
            throw new MeasureException("Unable to getConnection.", e);
        } finally
        {
            if (tConnection != null)
            {
                try
                {
                    tConnection.close();
                } catch (SQLException e2)
                {
                    throw new MeasureException("Connection to DataBase lost.", e2);
                }
            }
        }
    }

}
