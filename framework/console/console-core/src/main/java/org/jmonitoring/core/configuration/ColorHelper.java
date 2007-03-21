package org.jmonitoring.core.configuration;

import java.awt.Color;

import org.hibernate.util.GetGeneratedKeysHelper;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/


/**
 * @author pke
 * 
 * @todo URGENT Supprimer l'aspet statique de la configuration.
 */
public final class ColorHelper
{
//    private static Configuration sConfiguration;
//
//    private static Log sLog = LogFactory.getLog(Configuration.class);
//
//    /** Class de Stockage a utiliser. */
//    private Class mMeasurePointStoreClass;
//
//    /** Max number of execution for displaying an ExecutionFlow. */
//    private int mMaxExecutionDuringFlowEdition;
//
//    /** Repertoire de log pour les fichiers. */
//    private String mXmlOutputDir;
//
//    /** Permet de savoir si tous les Threads log dans le m�me fichier. */
//    private boolean mSameFileForAllThread = true;
//
//    /** Default value for allowing <code>Aspect</code> to log the method parameters. */
//    private boolean mLogMethodParameter;
//
//    /** Name of the server. */
//    private String mServerName;
//
//    /** Class of the ExecutionFlowDao class. */
//    private Class mExecutionFlowDaoClass;
//
//    /** Format for date rendering. */
//    private String mDateFormat;
//
//    /** Format for time rendering. */
//    private String mTimeFormat;
//
//    /** Format for date and time format. */
//    private String mDateTimeFormat;
//
//    private ThreadLocal mDateFormater = new ThreadLocal();
//
//    private ThreadLocal mTimeFormater = new ThreadLocal();
//
//    private ThreadLocal mDateTimeFormater = new ThreadLocal();
//
//    private ThreadLocal mDateBaseFormater = new ThreadLocal();
//
//    /** List of group's colors. */
//    private Map mGroupColor;
//
//    /** Nb Thread in asynchrone polling ThreadPool. */
//    private int mAsynchroneStoreThreadPoolSize;
//
//    private Configuration()
//    {
//        try
//        {
//            PropertiesConfiguration tConfig = loadConfig();
//
//            loadConfiguration(tConfig);
//        } catch (Error e)
//        {
//            sLog.error("Error during the loading of the configuration file \"jmonitoring.properties\"", e);
//            throw e;
//        } catch (RuntimeException e2)
//        {
//            sLog.error("Error during the loading of the configuration file " + "\"jmonitoring.properties\"", e2);
//            throw e2;
//        }
//    }
//
//    void loadConfiguration(PropertiesConfiguration tConfig)
//    {
////        loadStoreClass(tConfig);
//        loadExecutionFlowClass(tConfig);
//        mXmlOutputDir = tConfig.getString("xml.logger.dir", "/temp/log");
//
//        mLogMethodParameter = tConfig.getBoolean("log.parameter.defaultvalue", true);
//        mServerName = tConfig.getString("server.name", "Babasse");
//        mDateFormat = tConfig.getString("format.ihm.date", "dd/MM/yy");
//        mTimeFormat = tConfig.getString("format.ihm.time", "HH:mm:ss.SSS");
//        mDateTimeFormat = mDateFormat + " " + mTimeFormat;
//        mMaxExecutionDuringFlowEdition = tConfig.getInt("maxExecutionDuringFlowEdition");
//
//        mAsynchroneStoreThreadPoolSize = tConfig.getInt("asynchronelogger.threadpool.size", 1);
//
//        initColor(tConfig);
//    }
//
////    private void loadStoreClass(PropertiesConfiguration pConfig)
////    {
////        Class tResultClass;
////        try
////        {
////            String tLoggerClassName = pConfig.getString("measurepoint.logger.class", AsynchroneJdbcLogger.class
////                .getName());
////            tResultClass = Class.forName(tLoggerClassName);
////        } catch (ClassNotFoundException e1)
////        {
////            sLog.error("Unable to create LogClass, using default class : AsynchroneJdbcLogger");
////            tResultClass = AsynchroneJdbcLogger.class;
////        } catch (NoClassDefFoundError e2)
////        {
////            sLog.error("Unable to create LogClass, using default class : AsynchroneJdbcLogger");
////            tResultClass = AsynchroneJdbcLogger.class;
////        }
////        mMeasurePointStoreClass = tResultClass;
////    }
//
//    private void loadExecutionFlowClass(PropertiesConfiguration pConfig)
//    {
//        Class tResultClass;
//        try
//        {
//            String tDaoClassName = pConfig.getString("execution.dao.class", ExecutionFlowDAO.class.getName());
//            tResultClass = Class.forName(tDaoClassName);
//        } catch (ClassNotFoundException e1)
//        {
//            sLog.error("Unable to create IExecutionFlowDAO, using default class : ExecutionFlowDAO");
//            tResultClass = ExecutionFlowDAO.class;
//        } catch (NoClassDefFoundError e2)
//        {
//            sLog.error("Unable to create IExecutionFlowDAO, using default class : ExecutionFlowDAO");
//            tResultClass = ExecutionFlowDAO.class;
//        }
//        if (!IExecutionFlowDAO.class.isAssignableFrom(tResultClass))
//        {
//            sLog.error("The specified DaoClass isn't an instance of IExecutionFlowDAO, using default class : ExecutionFlowDAO");
//            tResultClass = ExecutionFlowDAO.class;
//        }
//        mExecutionFlowDaoClass = tResultClass;
//    }
//
//    private PropertiesConfiguration loadConfig()
//    {
//        try
//        {
//            return new PropertiesConfiguration("jmonitoring.properties");
//        } catch (ConfigurationException e)
//        {
//            throw new MeasureException("Unable to initialise Configuration. "
//                + "Check that you have a file \"jmonitoring.properties\" in your CLASSPATH");
//        }
//    }
//
//    private void initColor(PropertiesConfiguration pConfig)
//    {
//        mGroupColor = new Hashtable();
//
//        String curKey, tGroupName;
//        String[] curArray;
//        int tRed, tGreen, tBlue;
//        String tPrefix = "group.color";
//        for (Iterator tIt = pConfig.getKeys(tPrefix); tIt.hasNext();)
//        {
//            curKey = (String) tIt.next();
//            try
//            {
//                curArray = pConfig.getStringArray(curKey);
//                tRed = Integer.parseInt(curArray[0]);
//                tGreen = Integer.parseInt(curArray[1]);
//                tBlue = Integer.parseInt(curArray[2]);
//                tGroupName = curKey.substring(tPrefix.length() + 1);
//                mGroupColor.put(tGroupName, new Color(tRed, tGreen, tBlue));
//            } catch (Throwable t)
//            { // Nothing to do we'll use the calculateColorMethod
//                sLog.warn("Unable to initialise ColorGroup name for Key=[" + curKey + "]");
//            }
//
//        }
//    }
//
//    /**
//     * Synchronized access to the <code>DateFormater</code> for only Date.
//     * 
//     * @return The <code>DateFormat</code> to use in the application.
//     */
//    public SimpleDateFormat getDateFormater()
//    {
//        Object tResult = mDateFormater.get();
//        if (tResult == null)
//        {
//            tResult = new SimpleDateFormat(mDateFormat);
//            mDateFormater.set(tResult);
//        }
//        return (SimpleDateFormat) tResult;
//    }
//
//    /**
//     * Synchronized access to the <code>DateFormater</code> for only time.
//     * 
//     * @return The <code>DateFormat</code> to use in the application.
//     */
//    public SimpleDateFormat getTimeFormater()
//    {
//        Object tResult = mTimeFormater.get();
//        if (tResult == null)
//        {
//            tResult = new SimpleDateFormat(mTimeFormat);
//            mTimeFormater.set(tResult);
//        }
//        return (SimpleDateFormat) tResult;
//    }
//
//    /**
//     * Synchronized access to the <code>DateFormater</code> for date and time.
//     * 
//     * @return The <code>DateFormat</code> to use in the application.
//     */
//    public SimpleDateFormat getDateTimeFormater()
//    {
//        Object tResult = mDateTimeFormater.get();
//        if (tResult == null)
//        {
//            tResult = new SimpleDateFormat(mDateTimeFormat);
//            mDateTimeFormater.set(tResult);
//        }
//        return (SimpleDateFormat) tResult;
//    }
//
//    /**
//     * Synchronized access to the <code>DateFormater</code> for date and time.
//     * 
//     * @return The <code>DateFormat</code> to use in the application.
//     */
//    public SimpleDateFormat getDataBaseDateTimeFormater()
//    {
//        Object tResult = mDateBaseFormater.get();
//        if (tResult == null)
//        {
//            tResult = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS000");
//            mDateBaseFormater.set(tResult);
//        }
//        return (SimpleDateFormat) tResult;
//    }
//
//    /**
//     * Get the associated <code>Color</code> of a group. If the color is not defined in the configuration file it will
//     * be generated.
//     * 
//     * @param pGroupName The name of the the group.
//     * @return The Color.
//     * @see Configuration.calculColor(String)
//     * @deprecated
//     */
//    public Color getColor(String pGroupName)
//    {
//        Color tColor = (Color) mGroupColor.get(pGroupName);
//        if (tColor == null)
//        { // On ajoute une couleur...
//            tColor = calculColor(pGroupName);
//            mGroupColor.put(pGroupName, tColor);
//        }
//        return tColor;
//    }
//
    private static final int NB_COLOR_BIT = 128;

    /**
     * Generate a <code>Color</code> for a <code>String</code> using hashcode.
     * 
     * @param pGroupName The name of the group.
     * @return The genrated <code>Color</code>.
     */
    public static Color calculColor(String pGroupName)
    {
        Color tColor;
        if (pGroupName == null || pGroupName.length() == 0)
        {
            tColor = Color.BLACK;
        } else
        {
            int tInt = Math.abs(pGroupName.hashCode());
            int tR, tG, tB;
            tR = tInt % NB_COLOR_BIT;
            tG = (tInt / NB_COLOR_BIT) % NB_COLOR_BIT;
            tB = (tInt / (NB_COLOR_BIT * NB_COLOR_BIT)) % NB_COLOR_BIT;
            tColor = new Color(tR * 2, tG * 2, tB * 2);
        }
        return tColor;
    }

    /**
     * Get the <code>Color</code> of a group as <code>String</code>.
     * 
     * @param pGroupName The group name.
     * @return The RGB format like "#00FF88" of the <code>Color</code>.
     */
    public static String getGroupAsColorString(Color pColor)
    {
        String tRed = Integer.toHexString(pColor.getRed());
        if (tRed.length() == 1)
        {
            tRed = "0" + tRed;
        }
        String tGreen = Integer.toHexString(pColor.getGreen());
        if (tGreen.length() == 1)
        {
            tGreen = "0" + tGreen;
        }
        String tBlue = Integer.toHexString(pColor.getBlue());
        if (tBlue.length() == 1)
        {
            tBlue = "0" + tBlue;
        }
        return "#" + tRed + tGreen + tBlue;
    }

    public static String getColor(String pGroupName)
    {
        String tColor =ConfigurationHelper.getInstance().getString("group.color." + pGroupName); 
        if (tColor==null)
        {
            tColor =getGroupAsColorString(calculColor(pGroupName)); 
        }
        return tColor;
            
    }

//    /**
//     * Factory's method for the <code>Configuration</code>. This method is not synchrnoized because there isn't any
//     * trouble if we load the configuration twice.
//     * 
//     * @return The Syngleton.
//     */
//    public static Configuration getInstance()
//    {
//        if (sConfiguration == null)
//        {
//            sConfiguration = new Configuration();
//        }
//        return sConfiguration;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The default log parameter.
//     */
//    public boolean getLogMethodParameter()
//    {
//        return mLogMethodParameter;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The Asynchrone thread pool size.
//     */
//    public int getAsynchroneStoreThreadPoolSize()
//    {
//        return mAsynchroneStoreThreadPoolSize;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The store class.
//     */
//    public Class getMeasurePointStoreClass()
//    {
//        return mMeasurePointStoreClass;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The boolean that indicates if all the Tread use the same XmlStoreFile.
//     */
//    public boolean getSameFileForAllThread()
//    {
//        return mSameFileForAllThread;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The Xml outputdir.
//     */
//    public String getXmlOutpuDir()
//    {
//        return mXmlOutputDir;
//    }
//
//    /**
//     * Accessor.
//     * 
//     * @return The name of the server that is at the origin of the flow.
//     */
//    public String getServerName()
//    {
//        return mServerName;
//    }
//
//    public void setLogMethodParameter(boolean pLogParameter)
//    {
//        mLogMethodParameter = pLogParameter;
//    }
//
//    /**
//     * @param pMeasurePointStoreClass The measurePointStoreClass to set.
//     */
//    public void setMeasurePointStoreClass(Class pMeasurePointStoreClass)
//    {
//        mMeasurePointStoreClass = pMeasurePointStoreClass;
//    }
//
//    public Class getExecutionFlowDaoClass()
//    {
//        return mExecutionFlowDaoClass;
//    }
//
//    public void setExecutionFlowDaoClass(Class pExecutionFlowDaoClass)
//    {
//        mExecutionFlowDaoClass = pExecutionFlowDaoClass;
//    }
//
//    public int getMaxExecutionDuringFlowEdition()
//    {
//        return mMaxExecutionDuringFlowEdition;
//    }
//
}
