package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.IStoreReader;
import org.jmonitoring.core.store.IStoreWriter;

/**
 * @author pke
 * 
 * @todo Refactoring de la classe pour utiliser les mécanismes de batch: soit en permettant de valider les contraintes
 *       d'intégrité relationnelle au COMMIT soit en faisant 2 batchs: un en insert sans clé étrangère et un deuxième en
 *       UPDATE pour créer les liens Templates
 */
public class JdbcLogger implements IStoreReader, IStoreWriter
{
    /**
     * (non-Javadoc)
     * 
     * @see org.jmonitoring.core.log.IStoreWriter#writeExecutionFlow(
     *      ExecutionFlowPO)
     */
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {

//        Connection tConnection = null;
//        try
//        {
//
//            tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
//            new ExecutionFlowDAO(tConnection).insertFullExecutionFlow(pExecutionFlow);
//            tConnection.commit();
//
//        } catch (SQLException e)
//        {
//            throw new MeasureException("Unable to getConnection.", e);
//        } finally
//        {
//            if (tConnection != null)
//            {
//                try
//                {
//                    tConnection.close();
//                } catch (SQLException e2)
//                {
//                    throw new MeasureException("Connection to DataBase lost.", e2);
//                }
//            }
//        }
    }

}
