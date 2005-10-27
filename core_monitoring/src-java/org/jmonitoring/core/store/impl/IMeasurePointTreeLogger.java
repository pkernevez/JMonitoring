package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.core.dto.ExecutionFlowDTO;

/**
 * Log a LogPointTree in an XML file. This class is not synchronized and need to be use in its own Thread or be
 * externaly synchronised.
 * 
 * @author pke
 * @deprecated
 */
public interface IMeasurePointTreeLogger
{
    /**
     * Write the current MeasurePointTree in an Xml file.
     * 
     * @param pExecutionFlow The measure point to log.
     */
    void logMeasurePointTree(ExecutionFlowDTO pExecutionFlow);

}
