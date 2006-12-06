package org.jmonitoring.core.configuration;

/**
 * General configuration of JMonitoring. This class is supposed to have only one instance in database.
 * 
 * @author pke
 */
public class GeneralConfigurationPO
{
    private int mId = ConfigurationDAO.UNIQUE_CONF_ID;

    /**
     * ThreadPool Size for AsynchroneLogger
     */
    private int mAsynchroneThreadPoolSize = 1;

    /**
     * Max number of execution for displaying an ExecutionFlow. 
     * log. If there is more MethodCall for this ExecutionFlow, the user had to choose the action: ignore this number or
     * filter the MethodCall list.
     * 
     */
    private int mMaxExecutionDuringFlowEdition = 2000;

    /** Date configuration for user rendered */
    private String mDateFormat = "dd/MM/yy";

    /** Time configuration for user rendered */
    private String mTimeFormat = "HH:mm:ss";

    /**
     * Class to use for logging MeasurePoint
     */
    private String mLoggerClass = org.jmonitoring.core.store.impl.AsynchroneJdbcLogger.class.getName();

    public int getAsynchroneThreadPoolSize()
    {
        return mAsynchroneThreadPoolSize;
    }

    public void setAsynchroneThreadPoolSize(int pAsynchroneThreadPoolSize)
    {
        mAsynchroneThreadPoolSize = pAsynchroneThreadPoolSize;
    }

    public String getDateFormat()
    {
        return mDateFormat;
    }

    public void setDateFormat(String pDateFormat)
    {
        mDateFormat = pDateFormat;
    }

    public int getMaxExecutionDuringFlowEdition()
    {
        return mMaxExecutionDuringFlowEdition;
    }

    public void setMaxExecutionDuringFlowEdition(int pMaxExecutionDuringFlowEdition)
    {
        mMaxExecutionDuringFlowEdition = pMaxExecutionDuringFlowEdition;
    }

    public String getTimeFormat()
    {
        return mTimeFormat;
    }

    public void setTimeFormat(String pTimeFormat)
    {
        mTimeFormat = pTimeFormat;
    }

    public String getLoggerClass()
    {
        return mLoggerClass;
    }

    public void setLoggerClass(String pLoggerClass)
    {
        mLoggerClass = pLoggerClass;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int pId)
    {
        mId = pId;
    }

}
