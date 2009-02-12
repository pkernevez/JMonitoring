package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * General configuration of JMonitoring. This class is supposed to have only one instance in database.
 * 
 * @author pke
 */
public class GeneralConfigurationPO {
    private int mId = ConfigurationDAO.UNIQUE_CONF_ID;

    /**
     * Max number of execution for displaying an ExecutionFlow. log. If there is more MethodCall for this ExecutionFlow,
     * the user had to choose the action: ignore this number or filter the MethodCall list.
     * 
     */
    private int mMaxExecutionDuringFlowEdition = 2000;

    /** Date configuration for user rendered */
    private String mDateFormat = "dd/MM/yy";

    /** Time configuration for user rendered */
    private String mTimeFormat = "HH:mm:ss";

    public String getDateFormat() {
        return mDateFormat;
    }

    public void setDateFormat(String pDateFormat) {
        mDateFormat = pDateFormat;
    }

    public int getMaxExecutionDuringFlowEdition() {
        return mMaxExecutionDuringFlowEdition;
    }

    public void setMaxExecutionDuringFlowEdition(int pMaxExecutionDuringFlowEdition) {
        mMaxExecutionDuringFlowEdition = pMaxExecutionDuringFlowEdition;
    }

    public String getTimeFormat() {
        return mTimeFormat;
    }

    public void setTimeFormat(String pTimeFormat) {
        mTimeFormat = pTimeFormat;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + mId;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GeneralConfigurationPO other = (GeneralConfigurationPO) obj;
        if (mId != other.mId)
            return false;
        return true;
    }

    public int getId() {
        return mId;
    }

    public void setId(int pId) {
        mId = pId;
    }

}
