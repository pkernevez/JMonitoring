using System;
using System.IO;
using System.Threading;

using log4net;
using DotNetGuru.AspectDNG.Joinpoints;

using Org.NMonitoring.Core.Store;
using Org.NMonitoring.Core.Dao;


namespace Org.NMonitoring.Core.Aspect
{
    public class PerformanceAspect
    {

        [ThreadStatic]
        private static StoreManager mStoreManager = null;

        /** Log instance. */
        private static ILog sLog = LogManager.GetLogger("SynchroneDbWriter");

        /** Allow the parameter log. */
        protected static bool mLogParameter = false;

        /** Nom du group associé au pointcut. */
        protected static String mGroupName = "Default";



        public static object executionToLogInternal(OperationJoinPoint jp)
        {
            object tResult = null;
            StoreManager tManager = getManager();

            Object[] tArgs = null;
            if (mLogParameter)
            {
                // On log les paramètres d'appel, de retour et les exceptions
                tArgs = jp.Arguments;
            }

            try
            {
                if (tManager != null)
                {
                    mStoreManager.logBeginOfMethod(jp, tArgs, mGroupName);
                }
                else
                {
                    sLog.Error("executionToLogInternal Impossible de logger l'entrée de la methode");
                }
            }
            catch (Exception e) // TODO : FCH : MeasureException
            {
                sLog.Error("Unable to log", e);
            }



            // En cas d'exception le code est dans le trigger "after()throwing..."
            try
            {
                tResult = jp.Proceed();

                try
                {
                    if (mStoreManager != null)
                    {
                        if (mLogParameter)
                        {
                            tManager.logEndOfMethodNormal(tResult);
                        }
                        else
                        {
                            tManager.logEndOfMethodNormal(null);
                        }
                    }
                    else
                    {
                        sLog.Error("executionToLogInternal Impossible de logger la sortie de la methode");
                    }
                }
                catch (Exception e) // TODO : FCH : MeasureException
                {
                    sLog.Error("Unable to log", e);
                    //LogFactory.getLog(this.getClass()).error("Unable to log", e); FCH :cf PKE
                }
            }
            catch (Exception e)
            {
                try
                {
                    tManager.logEndOfMethodWithException(e);
                }
                catch (Exception tT)
                {
                    sLog.Error("Unable to log execution Throwable");
                }
                throw (e);
            }


            return tResult;
        }

        /*
            after() throwing (Throwable t): executionToLogInternal() {
                
            }
         * */


        private static StoreManager getManager()
        {
            try
            {
                if (mStoreManager == null)
                {
                    DaoHelper.Initialize(System.Data.SqlClient.SqlClientFactory.Instance, "Data Source=VIRTUALPODE\\JMONITORING;Initial Catalog=jmonitoring;User ID=jmonitoring;Password=jmonitoring");
                    mStoreManager = new StoreManager();
                }

            }
            catch (Exception e)
            {
                // Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
                // de la méthode fonctionelle invoquée.
                sLog.Error("Impossible d'instancier un logger pour tracer les appels", e);
            }

            return mStoreManager;
        }
    }
}
